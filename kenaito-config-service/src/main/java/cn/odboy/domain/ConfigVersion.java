package cn.odboy.domain;

import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 配置内容版本
 *
 * @author odboy
 * @since 2024-12-05
 */
@Getter
@Setter
@TableName("config_version")
public class ConfigVersion extends MyObject {
  /** 文件id */
  @TableField("file_id")
  private Long fileId;

  /** 配置文件内容 */
  @TableField("file_content")
  private String fileContent;

  /** 配置文件类型 */
  @TableField("file_type")
  private String fileType;

  /** 配置文件内容版本 */
  @TableField("version")
  private Long version;
}
