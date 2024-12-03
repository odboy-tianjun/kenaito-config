package cn.odboy.modules.system.domain;

import cn.odboy.base.MyEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 部门
 */
@Getter
@Setter
@TableName("system_dept")
public class Dept extends MyEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    private Set<Role> roles;

    @TableField(exist = false)
    private List<Dept> children;
    /**
     * 排序
     */
    private Integer deptSort;
    /**
     * 部门名称
     */
    @NotBlank
    private String name;
    /**
     * 是否启用
     */
    @NotNull
    private Boolean enabled;
    /**
     * 上级部门
     */
    private Long pid;
    /**
     * 子节点数目
     */
    private Integer subCount = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dept dept = (Dept) o;
        return Objects.equals(id, dept.id) &&
                Objects.equals(name, dept.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
    }
}