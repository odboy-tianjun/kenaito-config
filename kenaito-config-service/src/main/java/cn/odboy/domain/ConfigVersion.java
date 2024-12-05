package cn.odboy.domain;

import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 配置内容版本
 * </p>
 *
 * @author odboy
 * @since 2024-12-05
 */
@Getter
@Setter
@TableName("config_version")
public class ConfigVersion extends MyObject {
    @TableField("file_id")
    private Long fileId;

    @TableField("file_content")
    private byte[] fileContent;
}
