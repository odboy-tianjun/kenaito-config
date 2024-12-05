package cn.odboy.service.impl;

import cn.odboy.domain.ConfigApp;
import cn.odboy.domain.ConfigFile;
import cn.odboy.mapper.ConfigAppMapper;
import cn.odboy.mapper.ConfigFileMapper;
import cn.odboy.service.ConfigAppService;
import cn.odboy.service.ConfigFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配置应用 服务实现类
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
@Service
public class ConfigAppServiceImpl extends ServiceImpl<ConfigAppMapper, ConfigApp> implements ConfigAppService {

}
