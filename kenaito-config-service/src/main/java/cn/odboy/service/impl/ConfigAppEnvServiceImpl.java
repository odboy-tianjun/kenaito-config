package cn.odboy.service.impl;

import cn.odboy.base.model.SelectOption;
import cn.odboy.domain.ConfigAppEnv;
import cn.odboy.mapper.ConfigAppEnvMapper;
import cn.odboy.service.ConfigAppEnvService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        return list(new LambdaQueryWrapper<ConfigAppEnv>()
                .eq(ConfigAppEnv::getAppId, args.getAppId())
        ).stream()
                .map(m -> SelectOption.builder()
                        .label(m.getEnvCode())
                        .value(m.getEnvCode())
                        .build())
                .collect(Collectors.toList());
    }
}
