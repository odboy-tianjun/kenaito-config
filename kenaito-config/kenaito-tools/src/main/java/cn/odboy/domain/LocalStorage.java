package cn.odboy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.odboy.base.MyNormalEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * 本地存储
 *
 * @author odboy
 * @date 2024-09-06
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("system_tool_local_storage")
public class LocalStorage extends MyNormalEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 真实文件名
     */
    private String realName;

    /**
     * 文件名
     */
    private String name;
    /**
     * 后缀
     */
    private String suffix;

    /**
     * 路径
     */
    private String path;

    /**
     * 类型
     */
    private String type;

    /**
     * 大小
     */
    private String size;

    public LocalStorage(String realName, String name, String suffix, String path, String type, String size) {
        this.realName = realName;
        this.name = name;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.size = size;
    }

    public void copy(LocalStorage source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

    @Data
    public static class QueryArgs {
        private String blurry;
        private List<Timestamp> createTime;
    }
}
