package cn.odboy.domain;

import cn.odboy.base.MyNormalEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 配置应用
 *
 * @author odboy
 * @since 2024-12-05
 */
@Getter
@Setter
@TableName("config_app")
public class ConfigApp extends MyNormalEntity {

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

    @Data
    public static class CreateArgs {
        @NotBlank(message = "必填")
        private String appName;

        @NotBlank(message = "必填")
        private String description;
    }

    @Data
    public static class ModifyDescriptionArgs {
        @NotNull(message = "必填")
        private Long id;

        @NotBlank(message = "必填")
        private String description;
    }

    @Data
    public static class RemoveArgs {
        @NotNull(message = "必填")
        private Long id;
    }

    @Data
    public static class QueryClientArgs {
        @NotBlank(message = "必填")
        private String env;
        @NotBlank(message = "必填")
        private String dataId;
    }

    @Data
    public static class ClientInfo {
        private String ip;
        private Boolean isActive = false;
    }
}
