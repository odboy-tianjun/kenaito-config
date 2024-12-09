package cn.odboy.service;

import cn.odboy.base.model.SelectOption;
import cn.odboy.domain.ConfigAppEnv;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 配置环境 服务类
 * </p>
 *
 * @author odboy
 * @since 2024-12-09
 */
public interface ConfigAppEnvService extends IService<ConfigAppEnv> {
    void create(ConfigAppEnv args);

    List<SelectOption> queryList(ConfigAppEnv args);

    void remove(ConfigAppEnv args);
}
