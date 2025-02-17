package cn.odboy.config.model.msgtype;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户端 信息
 *
 * @author odboy
 * @date 2024-12-09
 */
@Data
public class ClientInfo implements Serializable {
    /**
     * 服务器地址
     * 用于指定服务的主机名或IP地址
     */
    private String server;

    /**
     * 端口号
     * 用于指定服务的通信端口
     */
    private Integer port;

    /**
     * 环境标识
     * 用于标识当前配置所属的运行环境，例如开发、测试或生产环境
     */
    private String env;

    /**
     * 数据ID
     * 用于唯一标识配置数据，在分布式系统中起到关键作用
     */
    private String dataId;

    /**
     * 缓存目录
     * 用于存储缓存数据的目录路径
     */
    private String cacheDir;

}
