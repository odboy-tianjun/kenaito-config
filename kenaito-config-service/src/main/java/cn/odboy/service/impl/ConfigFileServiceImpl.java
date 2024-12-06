package cn.odboy.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.domain.ConfigFile;
import cn.odboy.domain.ConfigVersion;
import cn.odboy.mapper.ConfigFileMapper;
import cn.odboy.service.ConfigFileService;
import cn.odboy.service.ConfigVersionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 配置文件 服务实现类
 *
 * @author odboy
 * @since 2024-12-05
 */
@Service
@RequiredArgsConstructor
public class ConfigFileServiceImpl extends ServiceImpl<ConfigFileMapper, ConfigFile>
    implements ConfigFileService {
  private final ConfigVersionService configVersionService;

  @Override
  public List<ConfigFileInfo> getFileList(String env, String dataId) {
    return getBaseMapper().selectByEnvAndAppName(env, dataId);
  }

  @Override
  public List<ConfigFile> queryInAppId(Long appId) {
    return list(new LambdaQueryWrapper<ConfigFile>().in(ConfigFile::getAppId, appId));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void create(ConfigFile.CreateArgs args) throws Exception {
    MultipartFile file = args.getFile();
    Long version = 1L;
    ConfigFile configFile = new ConfigFile();
    configFile.setEnv(args.getEnv());
    configFile.setAppId(args.getAppId());
    configFile.setFileName(file.getOriginalFilename());
    configFile.setVersion(version);
    save(configFile);
    ConfigVersion configVersion = new ConfigVersion();
    configVersion.setFileId(configFile.getId());
    configVersion.setFileContent(StrUtil.str(file.getBytes(), StandardCharsets.UTF_8));
    configVersion.setVersion(version);
    configVersionService.save(configVersion);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void remove(ConfigFile.RemoveArgs args) {
    removeById(args.getId());
    configVersionService.removeBatchByFileId(args.getId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void modifyFileContent(ConfigFile.ModifyFileContentArgs args) {
    ConfigFile originConfigFile = getById(args.getId());
    long newVersion = originConfigFile.getVersion() + 1;
    ConfigFile updVersion = new ConfigFile();
    updVersion.setId(originConfigFile.getId());
    updVersion.setVersion(newVersion);
    updateById(updVersion);
    ConfigVersion lastConfigVersion = new ConfigVersion();
    lastConfigVersion.setFileId(originConfigFile.getId());
    lastConfigVersion.setFileContent(args.getFileContent());
    lastConfigVersion.setVersion(newVersion);
    configVersionService.save(lastConfigVersion);
  }
}
