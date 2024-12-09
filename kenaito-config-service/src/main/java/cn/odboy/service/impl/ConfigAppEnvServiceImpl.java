package cn.odboy.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.odboy.base.model.SelectOption;
import cn.odboy.domain.ConfigAppEnv;
import cn.odboy.domain.ConfigFile;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.mapper.ConfigAppEnvMapper;
import cn.odboy.service.ConfigAppEnvService;
import cn.odboy.service.ConfigFileService;
import cn.odboy.service.ConfigVersionService;
import cn.odboy.util.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 配置环境 服务实现类
 *
 * @author odboy
 * @since 2024-12-09
 */
@Service
@RequiredArgsConstructor
public class ConfigAppEnvServiceImpl extends ServiceImpl<ConfigAppEnvMapper, ConfigAppEnv>
    implements ConfigAppEnvService {
  private final ConfigFileService configFileService;
  private final ConfigVersionService configVersionService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void create(ConfigAppEnv args) {
    try {
      save(args);
    } catch (Exception e) {
      log.error("新增环境失败", e);
      throw new RuntimeException("环境已存在, 请确认后再试");
    }
  }

  @Override
  public List<SelectOption> queryList(ConfigAppEnv args) {
    return list(new LambdaQueryWrapper<ConfigAppEnv>().eq(ConfigAppEnv::getAppId, args.getAppId()))
        .stream()
        .map(m -> SelectOption.builder().label(m.getEnvCode()).value(m.getEnvCode()).build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void remove(ConfigAppEnv args) {
    if (args.getAppId() == null) {
      throw new BadRequestException("appId必填");
    }
    if (StrUtil.isBlank(args.getEnvCode())) {
      throw new BadRequestException("envCode必填");
    }
    // delete from config_app_env
    remove(
        new LambdaQueryWrapper<ConfigAppEnv>()
            .eq(ConfigAppEnv::getAppId, args.getAppId())
            .eq(ConfigAppEnv::getEnvCode, args.getEnvCode()));
    List<ConfigFile> configFiles =
        configFileService.getByAppIdEnvCode(args.getAppId(), args.getEnvCode());
    if (CollUtil.isNotEmpty(configFiles)) {
      // delete from config_file
      List<Long> configFileId =
          configFiles.stream().map(ConfigFile::getId).collect(Collectors.toList());
      configFileService.removeBatchByIds(configFileId);
      Long fileId = configFileId.stream().findFirst().orElse(null);
      if (fileId != null) {
        // delete from config_version
        configVersionService.removeBatchByFileId(fileId);
      }
    }
  }
}
