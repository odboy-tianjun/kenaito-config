package cn.odboy.domain;

import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 配置文件
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
@Getter
@Setter
@TableName("config_file")
public class ConfigFile extends MyObject {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("app_name")
    private String appName;

    @TableField("env")
    private String env;

    /**
     * 例如: application-daily.properties
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 当前配置内容版本
     */
    @TableField("version")
    private Long version;
}
