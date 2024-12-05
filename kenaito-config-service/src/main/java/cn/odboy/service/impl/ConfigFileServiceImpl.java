package cn.odboy.service.impl;

import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.mapper.ConfigFileMapper;
import cn.odboy.service.ConfigFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 配置文件 服务实现类
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
@Service
@RequiredArgsConstructor
public class ConfigFileServiceImpl implements ConfigFileService {
    private final ConfigFileMapper configFileMapper;

    @Override
    public List<ConfigFileInfo> getFileList(String env, String dataId) {
        return configFileMapper.selectByEnvAndAppName(env, dataId);
    }
}
