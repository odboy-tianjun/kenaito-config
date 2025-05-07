package cn.odboy.modules.system.domain;

import cn.odboy.base.MyNormalEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * 用户
 */
@Getter
@Setter
@TableName("system_user")
public class User extends MyNormalEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户角色
     */
    @TableField(exist = false)
    private Set<Role> roles;
    /**
     * 用户岗位
     */
    @TableField(exist = false)
    private Set<Job> jobs;

    @TableField(value = "dept_id")
    private Long deptId;
    /**
     * 用户部门
     */
    @TableField(exist = false)
    private Dept dept;
    /**
     * 用户名称
     */
    @NotBlank
    private String username;
    /**
     * 用户昵称
     */
    @NotBlank
    private String nickName;
    /**
     * 邮箱
     */
    @Email
    @NotBlank
    private String email;
    /**
     * 电话号码
     */
    @NotBlank
    private String phone;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 头像真实名称
     */
    private String avatarName;
    /**
     * 头像存储的路径
     */
    private String avatarPath;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否启用
     */
    @NotNull
    private Boolean enabled;
    /**
     * 是否为admin账号
     */
    private Boolean isAdmin = false;
    /**
     * 最后修改密码的时间
     */
    private Date pwdResetTime;
    /**
     * 钉钉Id
     */
    private String dingtalkId;
    /**
     * 钉钉UnionId
     */
    private String dingtalkUnionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(dingtalkId, user.dingtalkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, dingtalkId);
    }
}