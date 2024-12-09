package cn.odboy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 配置应用与用户关联关系
 *
 * @author odboy
 * @since 2024-12-06
 */
@Getter
@Setter
@TableName("config_user")
public class ConfigUser {
  /** 应用id */
  @TableField("app_id")
  private Long appId;

  /** 环境编码 */
  @TableField("env_code")
  private String envCode;

  /** 用户id */
  @TableField("user_id")
  private Long userId;
}
