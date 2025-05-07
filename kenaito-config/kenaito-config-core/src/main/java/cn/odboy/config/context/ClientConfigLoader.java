package cn.odboy.config.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.config.constant.ClientConfigConsts;
import cn.odboy.config.constant.ClientConfigVars;
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
              if (!ClientConfigVars.originConfigs.isEmpty()) {
                try {
                  // 降低频率，减小对三方应用本身的影响
                  Thread.sleep(30 * 1000);
                } catch (Exception e) {
                  // 忽略
                }
                for (Map.Entry<String, String> kve : ClientConfigVars.originConfigs.entrySet()) {
                  try {
                    FileUtil.writeString(
                        kve.getValue(),
                        FileUtil.file(ClientConfigConsts.clientInfo.getCacheDir(), kve.getKey()),
                        StandardCharsets.UTF_8);
                  } catch (Exception e) {
                    logger.error(
                        "应用 {},环境 {},缓存配置文件 {} 失败",
                        ClientConfigConsts.clientInfo.getDataId(),
                        ClientConfigConsts.clientInfo.getEnv(),
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
        logger.info("客户端属性: {}", ClientConfigConsts.clientInfo);
        validateCacheDirPath(defaultCacheDir, ClientConfigConsts.clientInfo.getCacheDir());
        createCacheDir(ClientConfigConsts.clientInfo.getCacheDir());
        fixedTimeFlushConfigFileThread.start();
        ThreadUtil.execAsync(
            () -> {
              try {
                ConfigClient.getInstance()
                    .start(
                        ClientConfigConsts.clientInfo.getServer(),
                        ClientConfigConsts.clientInfo.getPort());
              } catch (InterruptedException e) {
                logger.error("Netty客户端启动失败", e);
                throw new RuntimeException(e);
              }
            });
        // 这里加个同步锁，等客户端准备就绪后，拉取配置完成时
        synchronized (ClientConfigConsts.clientInfo) {
          while (!ClientConfigVars.isConfigLoaded) {
            try {
              // 等待配置加载完成
              ClientConfigConsts.clientInfo.wait();
            } catch (InterruptedException e) {
              Thread currentThread = Thread.currentThread();
              currentThread.interrupt();
            }
          }
          // 判断配置中心服务是否处于离线状态
          if (ClientConfigVars.isServerOffline) {
            logger.info("配置中心离线，尝试从本地缓存加载配置文件");
            String cacheDir = ClientConfigConsts.clientInfo.getCacheDir();
            FileUtil.walkFiles(
                FileUtil.file(cacheDir),
                (file -> {
                  try {
                    String fileName = file.getName();
                    ClientConfigVars.originConfigs.clear();
                    // 配置原生内容
                    ClientConfigVars.originConfigs.put(
                        fileName, FileUtil.readString(file, StandardCharsets.UTF_8));
                    // 转换为应用能识别的配置项
                    ClientConfigVars.lastConfigs.clear();
                    String suffix = FileUtil.getSuffix(file);
                    boolean isYml = "yml".equals(suffix) || "yaml".equals(suffix);
                    if (isYml) {
                      Yaml yaml = new Yaml();
                      ClientConfigVars.lastConfigs.put(
                          fileName, yaml.load(ClientConfigVars.originConfigs.get(fileName)));
                    } else {
                      Properties properties = new Properties();
                      properties.load(
                          StrUtil.getReader(ClientConfigVars.originConfigs.get(fileName)));
                      Map<String, Object> tempMap = new HashMap<>(1);
                      for (Map.Entry<Object, Object> kv : properties.entrySet()) {
                        String key = (String) kv.getKey();
                        tempMap.put(key, kv.getValue());
                      }
                      ClientConfigVars.lastConfigs.put(fileName, tempMap);
                    }
                  } catch (Exception e) {
                    logger.info("配置文件转map失败", e);
                  }
                }));
          }
          // 合并配置项
          ClientConfigVars.cacheConfigs.clear();
          Set<Map.Entry<String, Map<String, Object>>> filename2ConfigMap =
              ClientConfigVars.lastConfigs.entrySet();
          for (Map.Entry<String, Map<String, Object>> filename2Config : filename2ConfigMap) {
            ClientConfigVars.cacheConfigs.putAll(filename2Config.getValue());
          }
          MapPropertySource propertySource =
              new MapPropertySource(
                  ClientConfigConsts.PROPERTY_SOURCE_NAME, ClientConfigVars.cacheConfigs);
          environment.getPropertySources().addFirst(propertySource);
        }
      }
    };
  }

  /**
   * 初始化客户端信息
   *
   * @param defaultCacheDir 默认缓存目录如果环境变量中未指定缓存目录，则使用此默认值
   * @param environment 应用程序环境变量用于从中获取配置信息
   */
  private static void initClientInfo(String defaultCacheDir, ConfigurableEnvironment environment) {
    // 设置服务器地址
    ClientConfigConsts.clientInfo.setServer(
        environment.getProperty(
            ClientConfigConsts.DEFAULT_CONFIG_NAME_SERVER,
            String.class,
            ClientConfigConsts.DEFAULT_CONFIG_SERVER));
    // 设置端口
    ClientConfigConsts.clientInfo.setPort(
        environment.getProperty(
            ClientConfigConsts.DEFAULT_CONFIG_NAME_PORT,
            Integer.class,
            ClientConfigConsts.DEFAULT_CONFIG_PORT));
    // 设置环境
    ClientConfigConsts.clientInfo.setEnv(
        environment.getProperty(
            ClientConfigConsts.DEFAULT_CONFIG_NAME_ENV,
            String.class,
            ClientConfigConsts.DEFAULT_CONFIG_ENV));
    // 设置数据ID
    ClientConfigConsts.clientInfo.setDataId(
        environment.getProperty(
            ClientConfigConsts.DEFAULT_CONFIG_NAME_DATA_ID,
            String.class,
            ClientConfigConsts.DEFAULT_CONFIG_DATA_ID));
    // 设置缓存目录
    ClientConfigConsts.clientInfo.setCacheDir(
        environment.getProperty(
            ClientConfigConsts.DEFAULT_CONFIG_NAME_CACHE_DIR, String.class, defaultCacheDir));
  }

  /**
   * 获取默认的缓存目录路径 根据操作系统类型返回对应的缓存目录路径
   *
   * @return 默认的缓存目录路径
   */
  private static String getDefaultCacheDir() {
    String defaultCacheDir;
    String os = System.getProperty("os.name");
    if (os.toLowerCase().startsWith(ClientConfigConsts.OS_TYPE_WIN)) {
      defaultCacheDir = ClientConfigConsts.DEFAULT_PATH_WIN;
    } else if (os.toLowerCase().startsWith(ClientConfigConsts.OS_TYPE_MAC)) {
      defaultCacheDir = ClientConfigConsts.DEFAULT_PATH_MAC;
    } else {
      // 对于未知操作系统，默认使用Mac操作系统的缓存路径
      defaultCacheDir = ClientConfigConsts.DEFAULT_PATH_MAC;
    }
    return defaultCacheDir;
  }

  /**
   * 验证缓存目录路径的合法性 确保提供的缓存路径与默认路径格式相符，防止路径配置错误
   *
   * @param defaultCacheDir 默认的缓存目录路径
   * @param cacheDir 用户配置的缓存目录路径
   */
  private static void validateCacheDirPath(String defaultCacheDir, String cacheDir) {
    // 检查是否为Windows系统默认路径格式，且用户配置的路径是否符合该格式
    if (defaultCacheDir.contains(ClientConfigConsts.DEFAULT_PATH_WIN_SEP)
        && !cacheDir.contains(ClientConfigConsts.DEFAULT_PATH_WIN_SEP)) {
      throw new RuntimeException(ClientConfigConsts.DEFAULT_CONFIG_NAME_CACHE_DIR + " 配置的路径不正确");
    }
    // 检查用户配置的路径是否包含Windows系统路径分隔符，且是否正确使用
    if (cacheDir.contains(ClientConfigConsts.DEFAULT_PATH_WIN_SEP)
        && !cacheDir.contains(ClientConfigConsts.DEFAULT_PATH_SEP_WIN)) {
      throw new RuntimeException(
          ClientConfigConsts.DEFAULT_CONFIG_NAME_CACHE_DIR
              + " 配置的路径不正确, 正确的路径示范, "
              + ClientConfigConsts.DEFAULT_PATH_WIN);
    }
  }

  /**
   * 创建缓存目录 如果目录不存在，将尝试创建它并检查写权限
   *
   * @param cacheDir 缓存目录的路径
   * @throws RuntimeException 如果目录创建失败或没有写权限
   */
  private static void createCacheDir(String cacheDir) {
    // 获取缓存目录的路径对象
    Path path = Paths.get(cacheDir);
    // 检查缓存目录是否存在，如果不存在则尝试创建
    if (!Files.exists(path)) {
      // 使用FileUtil工具类创建目录
      File mkdir = FileUtil.mkdir(cacheDir);
      // 检查创建后的目录是否可写，如果不可写则抛出异常
      if (!mkdir.canWrite()) {
        throw new RuntimeException("缓存文件夹创建失败, 无读写权限");
      }
    }
  }
}
