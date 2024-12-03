package cn.odboy.modules.system.domain;

import cn.odboy.base.MyEntity;
import cn.odboy.constant.DataScopeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * 角色
 */
@Getter
@Setter
@TableName("system_role")
public class Role extends MyEntity implements Serializable {

    @NotNull(groups = {Update.class})
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户
     */
    @TableField(exist = false)
    private Set<User> users;
    /**
     * 菜单
     */
    @TableField(exist = false)
    private Set<Menu> menus;
    /**
     * 部门
     */
    @TableField(exist = false)
    private Set<Dept> depts;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 数据权限，全部 、 本级 、 自定义
     */
    private String dataScope = DataScopeEnum.THIS_LEVEL.getValue();
    /**
     * 级别，数值越小，级别越大
     */
    private Integer level = 3;
    /**
     * 描述
     */
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
