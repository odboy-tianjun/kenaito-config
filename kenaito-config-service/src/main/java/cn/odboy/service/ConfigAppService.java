package cn.odboy.service;

import cn.odboy.domain.ConfigApp;
import cn.odboy.domain.ConfigFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 配置应用 服务类
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
public interface ConfigAppService extends IService<ConfigApp> {

    void create(ConfigApp.CreateArgs args);

    void remove(ConfigApp.RemoveArgs args);

    void modifyDescription(ConfigApp.ModifyDescriptionArgs args);
}
