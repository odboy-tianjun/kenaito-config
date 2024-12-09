package cn.odboy.domain;

import cn.odboy.base.MyNormalEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 配置文件
 *
 * @author odboy
 * @since 2024-12-05
 */
@Getter
@Setter
@TableName("config_file")
public class ConfigFile extends MyNormalEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * configAppId
     */
    @TableField("app_id")
    private Long appId;

    /**
     * 环境编码
     */
    @TableField("env_code")
    private String envCode;

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

    @Data
    public static class CreateArgs {
        @NotNull(message = "必填")
        private Long appId;

        @NotBlank(message = "必填")
        private String env;

        @NotNull(message = "必填")
        private MultipartFile file;
    }

    @Data
    public static class ModifyFileContentArgs {
        @NotNull(message = "必填")
        private Long id;

        @NotBlank(message = "必填")
        private String fileContent;
    }

    @Data
    public static class RemoveArgs {
        @NotNull(message = "必填")
        private Long id;
    }

    @Data
    public static class QueryList {
        private Long id;
        @NotNull(message = "必填")
        private Long appId;
        @NotBlank(message = "必填")
        private String envCode;
        private String fileName;
        private String fileType;
        private Long version;
        @TableField(value = "create_by", fill = FieldFill.INSERT)
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String createBy;
        @TableField(value = "create_time", fill = FieldFill.INSERT)
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    }
}
