package cn.odboy.service;

import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.domain.ConfigFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 配置文件 服务类
 *
 * @author odboy
 * @since 2024-12-05
 */
public interface ConfigFileService extends IService<ConfigFile> {
  /**
   * @param env 环境编码
   * @param dataId 数据ID，这里是应用名称
   * @return /
   */
  List<ConfigFileInfo> getFileList(String env, String dataId);
  List<ConfigFile> queryInAppId(Long appId);
  void create(ConfigFile.CreateArgs args) throws Exception;
  void remove(ConfigFile.RemoveArgs args);
  void modifyFileContent(ConfigFile.ModifyFileContentArgs args);
}
