package cn.odboy.modules.system.domain;

import cn.odboy.base.MyEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 职位
 */
@Getter
@Setter
@TableName("system_job")
public class Job extends MyEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long id;
    /**
     * 岗位名称
     */
    @NotBlank
    private String name;
    /**
     * 岗位排序
     */
    @NotNull
    private Long jobSort;
    /**
     * 是否启用
     */
    @NotNull
    private Boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}