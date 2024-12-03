package cn.odboy.modules.quartz.domain;

import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("system_quartz_log")
public class QuartzLog extends MyObject {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * bean名称
     */
    private String beanName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数
     */
    private String params;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 状态
     */
    private Boolean isSuccess;
    /**
     * 异常详情
     */
    private String exceptionDetail;
    /**
     * 执行耗时
     */
    private Long time;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;
}
