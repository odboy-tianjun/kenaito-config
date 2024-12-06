package cn.odboy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.domain.ConfigApp;
import cn.odboy.domain.ConfigFile;
import cn.odboy.domain.ConfigVersion;
import cn.odboy.mapper.ConfigAppMapper;
import cn.odboy.service.ConfigAppService;
import cn.odboy.service.ConfigFileService;
import cn.odboy.service.ConfigVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 配置应用 服务实现类
 *
 * @author odboy
 * @since 2024-12-05
 */
@Service
@RequiredArgsConstructor
public class ConfigAppServiceImpl extends ServiceImpl<ConfigAppMapper, ConfigApp>
    implements ConfigAppService {
  private final ConfigFileService configFileService;
  private final ConfigVersionService configVersionService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void create(ConfigApp.CreateArgs args) {
    save(BeanUtil.copyProperties(args, ConfigApp.class));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void remove(ConfigApp.RemoveArgs args) {
    removeById(args.getId());
    List<ConfigFile> configFileList = configFileService.queryInAppId(args.getId());
    if (configFileList.isEmpty()) {
      return;
    }
    List<Long> configFileIds =
        configFileList.stream().map(ConfigFile::getId).distinct().collect(Collectors.toList());
    configFileService.removeBatchByIds(configFileIds);
    List<ConfigVersion> configVersionList = configVersionService.queryInFileId(configFileIds);
    if (configVersionList.isEmpty()) {
      return;
    }
    List<Long> configVersionIds =
        configVersionList.stream()
            .map(ConfigVersion::getFileId)
            .distinct()
            .collect(Collectors.toList());
    configVersionService.removeBatchByIds(configVersionIds);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void modifyDescription(ConfigApp.ModifyDescriptionArgs args) {
    ConfigApp configApp = BeanUtil.copyProperties(args, ConfigApp.class);
    updateById(configApp);
  }
}
