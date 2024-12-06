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
@TableName("config_app_user")
public class ConfigAppUser {
  /** 应用名称 */
  @TableField("app_id")
  private Long appId;

  /** 应用名称 */
  @TableField("user_id")
  private Long userId;
}
