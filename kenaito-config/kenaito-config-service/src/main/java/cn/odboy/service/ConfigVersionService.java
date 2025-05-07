package cn.odboy.service;

import cn.odboy.domain.ConfigVersion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 配置版本 服务类
 *
 * @author odboy
 * @since 2024-12-05
 */
public interface ConfigVersionService extends IService<ConfigVersion> {
    List<ConfigVersion> queryInFileId(List<Long> fileIds);
    void removeBatchByFileId(Long fileId);
}
