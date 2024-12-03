package cn.odboy.modules.system.domain;

import cn.odboy.base.MyEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 菜单
 */
@Getter
@Setter
@TableName("system_menu")
public class Menu extends MyEntity implements Serializable {

    @NotNull(groups = {Update.class})
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long id;
    /**
     * 菜单角色
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    private Set<Role> roles;

    @TableField(exist = false)
    private List<Menu> children;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 菜单组件名称
     */
    @TableField(value = "name")
    private String componentName;
    /**
     * 排序
     */
    private Integer menuSort = 999;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 菜单类型，目录、菜单、按钮
     */
    private Integer type;
    /**
     * 权限标识
     */
    private String permission;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 缓存
     */
    private Boolean cache;
    /**
     * 是否隐藏
     */
    private Boolean hidden;
    /**
     * 上级菜单
     */
    @TableField(value = "pid",updateStrategy = FieldStrategy.ALWAYS)
    private Long pid;
    /**
     * 子节点数目
     */
    private Integer subCount = 0;
    /**
     * 外链菜单
     */
    private Boolean iFrame;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return title;
    }
}
