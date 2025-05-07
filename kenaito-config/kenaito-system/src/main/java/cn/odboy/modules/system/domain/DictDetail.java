package cn.odboy.modules.system.domain;

import cn.odboy.base.MyNormalEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字典详情
 */
@Getter
@Setter
@TableName("system_dict_detail")
public class DictDetail extends MyNormalEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "dict_id")
    private Long dictId;

    @TableField(exist = false)
    private Dict dict;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典值
     */
    private String value;
    /**
     * 排序
     */
    private Integer dictSort = 999;
}