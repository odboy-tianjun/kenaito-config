package cn.odboy.service;

import cn.odboy.base.model.SelectOption;
import cn.odboy.domain.ConfigApp;
import cn.odboy.domain.ConfigFile;
import cn.odboy.infra.response.PageArgs;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    IPage<ConfigApp> queryPage(PageArgs<ConfigApp> args);

}
