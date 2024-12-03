package cn.odboy.modules.system.domain;

import cn.odboy.base.MyEntity;
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

/**
 * 字典
 */
@Getter
@Setter
@TableName("system_dict")
public class Dict extends MyEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long id;

    @TableField(exist = false)
    private List<DictDetail> dictDetails;
    /**
     * 名称
     */
    @NotBlank
    private String name;
    /**
     * 描述
     */
    private String description;
}