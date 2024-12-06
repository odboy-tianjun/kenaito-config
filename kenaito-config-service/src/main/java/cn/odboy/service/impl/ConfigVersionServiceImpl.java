package cn.odboy.service.impl;

import cn.odboy.domain.ConfigVersion;
import cn.odboy.mapper.ConfigVersionMapper;
import cn.odboy.service.ConfigVersionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 配置版本 服务实现类
 *
 * @author odboy
 * @since 2024-12-05
 */
@Service
public class ConfigVersionServiceImpl extends ServiceImpl<ConfigVersionMapper, ConfigVersion>
    implements ConfigVersionService {
  @Override
  public List<ConfigVersion> queryInFileId(List<Long> fileIds) {
    return list(new LambdaQueryWrapper<ConfigVersion>().in(ConfigVersion::getFileId, fileIds));
  }

  @Override
  public void removeBatchByFileId(Long fileId) {
    remove(new LambdaQueryWrapper<ConfigVersion>()
            .eq(ConfigVersion::getFileId, fileId)
    );
  }
}
