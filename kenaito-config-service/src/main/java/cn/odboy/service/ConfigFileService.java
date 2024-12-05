package cn.odboy.service;

import cn.odboy.config.model.msgtype.ConfigFileInfo;

import java.util.List;

/**
 * <p>
 * 配置文件 服务类
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
public interface ConfigFileService {
    /**
     * @param env    环境编码
     * @param dataId 数据ID，这里是应用名称
     * @return /
     */
    List<ConfigFileInfo> getFileList(String env, String dataId);
}
