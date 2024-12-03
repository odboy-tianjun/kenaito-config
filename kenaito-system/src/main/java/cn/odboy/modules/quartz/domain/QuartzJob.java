package cn.odboy.modules.quartz.domain;

import cn.odboy.base.MyEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@TableName("system_quartz_job")
public class QuartzJob extends MyEntity {
    public static final String JOB_KEY = "QUARTZ_JOB_KEY";

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {Update.class})
    private Long id;
    /**
     * 所属模块
     */
    private String moduleName;
    /**
     * 用于子任务唯一标识
     */
    @TableField(exist = false)
    private String uuid;
    /**
     * 定时器名称
     */
    private String jobName;
    /**
     * Bean名称
     */
    @NotBlank
    private String beanName;
    /**
     * 方法名称
     */
    @NotBlank
    private String methodName;
    /**
     * 参数
     */
    private String params;
    /**
     * cron表达式
     */
    @NotBlank
    private String cronExpression;
    /**
     * 状态，暂时或启动
     */
    private Boolean isPause = false;
    /**
     * 负责人
     */
    private String personInCharge;
    /**
     * 报警邮箱
     */
    private String email;
    /**
     * 子任务
     */
    private String subTask;
    /**
     * 失败后暂停
     */
    private Boolean pauseAfterFailure;
    /**
     * 备注
     */
    @NotBlank
    private String description;

    @Data
    public static class QueryArgs {
        private String jobName;
        private String moduleName;
        private Boolean isSuccess;
        private List<Timestamp> createTime;
    }
}
