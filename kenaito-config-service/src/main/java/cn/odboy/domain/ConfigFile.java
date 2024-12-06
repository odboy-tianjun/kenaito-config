package cn.odboy.domain;

import cn.odboy.base.MyNormalEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.mail.Multipart;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

  /** configAppId */
  @TableField("app_id")
  private Long appId;

  /** 环境编码 */
  @TableField("env")
  private String env;

  /** 例如: application-daily.properties */
  @TableField("file_name")
  private String fileName;

  /** 当前配置内容版本 */
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
  public class RemoveArgs {
    @NotNull(message = "必填")
    private Long id;
  }
}
