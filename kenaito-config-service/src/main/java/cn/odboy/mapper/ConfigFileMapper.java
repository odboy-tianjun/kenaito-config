package cn.odboy.mapper;

import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.domain.ConfigFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配置文件 Mapper 接口
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
@Mapper
public interface ConfigFileMapper extends BaseMapper<ConfigFile> {

    List<ConfigFileInfo> selectInfoByEnvAndAppName(@Param("envCode") String envCode, @Param("dataId") String dataId);

    List<ConfigFile.QueryList> selectDetailByEnvAndAppId(@Param("envCode") String envCode, @Param("appId") Long appId);

    String selectContentByFileId(@Param("id") Long id);
}
