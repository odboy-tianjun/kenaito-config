package cn.odboy.domain;

import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 配置应用
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
@Getter
@Setter
@TableName("config_app")
public class ConfigApp extends MyObject {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;
    /**
     * 应用说明
     */
    @TableField("description")
    private String description;
}
