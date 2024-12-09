package cn.odboy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 配置应用与环境关联关系
 *
 * @author odboy
 * @since 2024-12-09
 */
@Getter
@Setter
@TableName("config_app_env")
public class ConfigAppEnv {
    /**
     * 应用id
     */
    @TableField("app_id")
    private Long appId;

    /**
     * 环境编码
     */
    @TableField("env_code")
    private String envCode;

    @Data
    public static class QueryList {
        @NotNull(message = "必填")
        private Long appId;
        @NotBlank(message = "必填")
        private String envCode;
    }
}
