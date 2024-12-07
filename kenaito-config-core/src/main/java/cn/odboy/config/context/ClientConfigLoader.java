package cn.odboy.config.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.config.model.msgtype.ClientInfo;
import cn.odboy.config.netty.ConfigClient;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.yaml.snakeyaml.Yaml;

/**
 * 配置加载器
 *
 * @author odboy
 * @date 2024-12-03
 */
@Configuration
public class ClientConfigLoader {
  private static final Logger logger = LoggerFactory.getLogger(ClientConfigLoader.class);

  /** 默认的配置值 */
  private static final String OS_TYPE_WIN = "win";

  private static final String OS_TYPE_MAC = "mac";
  private static final String DEFAULT_PATH_WIN = "c:\\data";
  private static final String DEFAULT_PATH_MAC = "/home/admin/data";
  private static final String DEFAULT_CONFIG_SERVER = "127.0.0.1";
  private static final Integer DEFAULT_CONFIG_PORT = 28010;
  private static final String DEFAULT_CONFIG_ENV = "default";
  private static final String DEFAULT_CONFIG_DATA_ID = "default";
  private static final String DEFAULT_PATH_WIN_SEP = ":";

  /** 默认配置项：配置中心服务ip */
  private static final String DEFAULT_CONFIG_NAME_SERVER = "kenaito.config-center.server";

  /** 默认配置项：配置中心服务端口 */
  private static final String DEFAULT_CONFIG_NAME_PORT = "kenaito.config-center.port";

  /** 默认配置项：将拉取的配置环境 */
  private static final String DEFAULT_CONFIG_NAME_ENV = "kenaito.config-center.env";

  /** 默认配置项：将拉取配置的应用的名称 */
  private static final String DEFAULT_CONFIG_NAME_DATA_ID = "kenaito.config-center.data-id";

  /** 默认配置项：配置缓存目录 */
  private static final String DEFAULT_CONFIG_NAME_CACHE_DIR = "kenaito.config-center.cache-dir";

  /** Win路径分割符 */
  private static final String DEFAULT_PATH_SEP_WIN = "\\";

  /** Mac路径分割符 */
  private static final String DEFAULT_PATH_SEP_MAC = "/";

  /** 配置源名称 */
  public static final String PROPERTY_SOURCE_NAME = "kenaito-dynamic-config";

  /** 当前客户端配置 */
  public static final ClientInfo clientInfo = new ClientInfo();

  /** 配置是否加载完毕 */
  public static boolean isConfigLoaded = false;

  /** 服务器是否离线 */
  public static boolean isServerOffline = false;

  /** 原有的配置信息：filename -> file content */
  public static Map<String, String> originConfigs = new HashMap<>();

  /** 转换后的配置信息：filename -> {configKey: configValue} */
  public static Map<String, Map<String, Object>> lastConfigs = new HashMap<>();

  /** 所有自定义配置项缓存 */
  public static Map<String, Object> cacheConfigs = new HashMap<>();

  /** 定时将配置写盘，缓存配置信息 */
  private final Thread fixedTimeFlushConfigFileThread =
      ThreadUtil.newThread(
          () -> {
            while (true) {
              // 原来是所有的配置写到了一个文件中
              //              try {
              //                if (!lastConfigs.isEmpty()) {
              //                  List<String> fileContent = new ArrayList<>();
              //                  for (Map.Entry<String, Object> kve : lastConfigs.entrySet()) {
              //                    fileContent.add(kve.getKey() + "=" + kve.getValue());
              //                  }
              //                  FileUtil.writeLines(
              //                      fileContent,
              //                      FileUtil.file(clientInfo.getCacheDir(), "config"),
              //                      StandardCharsets.UTF_8);
              //                }
              //                Thread.sleep(5 * 1000);
              //              } catch (Exception e) {
              //                // 忽略
              //              }
              // 改为每个配置文件写到单独的文件中
              if (!originConfigs.isEmpty()) {
                try {
                  // 降低频率，减小对三方应用本身的影响
                  Thread.sleep(30 * 1000);
                } catch (Exception e) {
                  // 忽略
                }
                for (Map.Entry<String, String> kve : originConfigs.entrySet()) {
                  try {
                    FileUtil.writeString(
                        kve.getValue(),
                        FileUtil.file(clientInfo.getCacheDir(), kve.getKey()),
                        StandardCharsets.UTF_8);
                  } catch (Exception e) {
                    logger.error(
                        "应用 {},环境 {},缓存配置文件 {} 失败",
                        clientInfo.getDataId(),
                        clientInfo.getEnv(),
                        kve.getKey(),
                        e);
                  }
                }
              }
            }
          },
          "fixedTimeFlushConfigFileThread");

  @Bean
  public BeanFactoryPostProcessor configLoader(ConfigurableEnvironment environment) {
    return new BeanFactoryPostProcessor() {
      @Override
      public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
          throws BeansException {
        String defaultCacheDir = getDefaultCacheDir();
        initClientInfo(defaultCacheDir, environment);
        logger.info("客户端属性: {}", clientInfo);
        validateCacheDirPath(defaultCacheDir, clientInfo.getCacheDir());
        createCacheDir(clientInfo.getCacheDir());
        fixedTimeFlushConfigFileThread.start();
        ThreadUtil.execAsync(
            () -> {
              try {
                ConfigClient.getInstance().start(clientInfo.getServer(), clientInfo.getPort());
              } catch (InterruptedException e) {
                logger.error("Netty客户端启动失败", e);
                throw new RuntimeException(e);
              }
            });
        // 这里加个同步锁，等客户端准备就绪后，拉取配置完成时
        synchronized (clientInfo) {
          while (!isConfigLoaded) {
            try {
              // 等待配置加载完成
              clientInfo.wait();
            } catch (InterruptedException e) {
              Thread currentThread = Thread.currentThread();
              String currentThreadName = currentThread.getName();
              currentThread.interrupt();
              logger.error("中断线程: {}", currentThreadName, e);
            }
          }
          // 判断配置中心服务是否处于离线状态
          if (isServerOffline) {
            logger.info("配置中心离线，尝试从本地缓存加载配置文件");
            String cacheDir = clientInfo.getCacheDir();
            FileUtil.walkFiles(
                FileUtil.file(cacheDir),
                (file -> {
                  try {
                    String fileName = file.getName();
                    originConfigs.clear();
                    // 配置原生内容
                    originConfigs.put(fileName, FileUtil.readString(file, StandardCharsets.UTF_8));
                    // 转换为应用能识别的配置项
                    lastConfigs.clear();
                    String suffix = FileUtil.getSuffix(file);
                    boolean isYml = "yml".equals(suffix) || "yaml".equals(suffix);
                    if (isYml) {
                      Yaml yaml = new Yaml();
                      lastConfigs.put(fileName, yaml.load(originConfigs.get(fileName)));
                    } else {
                      Properties properties = new Properties();
                      properties.load(StrUtil.getReader(originConfigs.get(fileName)));
                      Map<String, Object> tempMap = new HashMap<>(1);
                      for (Map.Entry<Object, Object> kv : properties.entrySet()) {
                        String key = (String) kv.getKey();
                        tempMap.put(key, kv.getValue());
                      }
                      lastConfigs.put(fileName, tempMap);
                    }
                  } catch (Exception e) {
                    logger.info("配置文件转map失败", e);
                  }
                }));
          }
          // 合并配置项
          cacheConfigs.clear();
          Set<Map.Entry<String, Map<String, Object>>> filename2ConfigMap = lastConfigs.entrySet();
          for (Map.Entry<String, Map<String, Object>> filename2Config : filename2ConfigMap) {
            cacheConfigs.putAll(filename2Config.getValue());
          }
          MapPropertySource propertySource =
              new MapPropertySource(PROPERTY_SOURCE_NAME, cacheConfigs);
          environment.getPropertySources().addFirst(propertySource);
        }
      }
    };
  }

  private static void initClientInfo(String defaultCacheDir, ConfigurableEnvironment environment) {
    clientInfo.setServer(
        environment.getProperty(DEFAULT_CONFIG_NAME_SERVER, String.class, DEFAULT_CONFIG_SERVER));
    clientInfo.setPort(
        environment.getProperty(DEFAULT_CONFIG_NAME_PORT, Integer.class, DEFAULT_CONFIG_PORT));
    clientInfo.setEnv(
        environment.getProperty(DEFAULT_CONFIG_NAME_ENV, String.class, DEFAULT_CONFIG_ENV));
    clientInfo.setDataId(
        environment.getProperty(DEFAULT_CONFIG_NAME_DATA_ID, String.class, DEFAULT_CONFIG_DATA_ID));
    clientInfo.setCacheDir(
        environment.getProperty(DEFAULT_CONFIG_NAME_CACHE_DIR, String.class, defaultCacheDir));
  }

  private static String getDefaultCacheDir() {
    String defaultCacheDir;
    String os = System.getProperty("os.name");
    if (os.toLowerCase().startsWith(OS_TYPE_WIN)) {
      defaultCacheDir = DEFAULT_PATH_WIN;
    } else if (os.toLowerCase().startsWith(OS_TYPE_MAC)) {
      defaultCacheDir = DEFAULT_PATH_MAC;
    } else {
      defaultCacheDir = DEFAULT_PATH_MAC;
    }
    return defaultCacheDir;
  }

  private static void validateCacheDirPath(String defaultCacheDir, String cacheDir) {
    if (defaultCacheDir.contains(DEFAULT_PATH_WIN_SEP)
        && !cacheDir.contains(DEFAULT_PATH_WIN_SEP)) {
      throw new RuntimeException(DEFAULT_CONFIG_NAME_CACHE_DIR + " 配置的路径不正确");
    }
    if (cacheDir.contains(DEFAULT_PATH_WIN_SEP) && !cacheDir.contains(DEFAULT_PATH_SEP_WIN)) {
      throw new RuntimeException(
          DEFAULT_CONFIG_NAME_CACHE_DIR + " 配置的路径不正确, 正确的路径示范, " + DEFAULT_PATH_WIN);
    }
  }

  /** 创建缓存文件夹 */
  private static void createCacheDir(String cacheDir) {
    Path path = Paths.get(cacheDir);
    if (!Files.exists(path)) {
      File mkdir = FileUtil.mkdir(cacheDir);
      if (!mkdir.canWrite()) {
        throw new RuntimeException("缓存文件夹创建失败, 无读写权限");
      }
    }
  }
}
