package cn.odboy.config.netty;

import cn.odboy.config.context.ClientConfigLoader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置中心客户端
 *
 * @author odboy
 * @date 2024-12-06
 */
@Data
public class ConfigClient {
  private static final Logger logger = LoggerFactory.getLogger(ConfigClient.class);

  /** 客户端 */
  private EventLoopGroup eventLoopGroup;

  /** 存放客户端bootstrap对象 */
  private Bootstrap bootstrap;

  /** 存放客户端channel对象 */
  private Channel channel;

  /** 重连间隔，单位秒 */
  private Integer delaySeconds = 5;

  /** 连接属性：服务ip */
  private String serverIp;

  /** 连接属性：服务端口 */
  private Integer serverPort;

  /** 私有静态实例 */
  private static volatile ConfigClient instance;

  /** 最大重试次数 */
  private static final int MAX_RETRY_COUNT = 2;

  /** 当前重试次数 */
  private static int retryCount = 0;

  /** 私有化构造函数 */
  private ConfigClient() {}

  /** 获取 */
  public static ConfigClient getInstance() {
    if (instance == null) {
      synchronized (ConfigClient.class) {
        if (instance == null) {
          instance = new ConfigClient();
        }
      }
    }
    return instance;
  }

  public void start(String server, Integer port) throws InterruptedException {
    this.serverIp = server;
    this.serverPort = port;
    try {
      this.eventLoopGroup = new NioEventLoopGroup();
      this.bootstrap = new Bootstrap();
      this.bootstrap
          .group(this.eventLoopGroup)
          .option(ChannelOption.SO_KEEPALIVE, true)
          // 设置接收缓冲区大小为10MB
          .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024 * 1024 * 10))
          .channel(NioSocketChannel.class)
          .handler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(new ConfigClientHandler());
                }
              });
      this.doConnect(server, port);
    } finally {
      //            eventLoopGroup.shutdownGracefully();
    }
  }

  private void doConnect(String server, Integer port) throws InterruptedException {
    logger.info("ConfigClient -> Start");
    if (channel != null && channel.isActive()) {
      return;
    }
    bootstrap.connect(server, port).addListener(new ConfigClientChannelListener()).sync().channel();
    /// channel.closeFuture().sync();
  }

  private class ConfigClientChannelListener implements ChannelFutureListener {
    /** 该方法会在channelActive之前执行，去判断客户端连接是否成功，并做失败重连的操作 */
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
      // 连接成功后保存Channel
      if (channelFuture.isSuccess()) {
        channel = channelFuture.channel();
        // 重置重试次数
        retryCount = 0;
        logger.info("ConfigClient -> Connect success {}:{}", serverIp, serverPort);
      } else {
        // 失败后delaySecond秒（默认是5秒）重连，周期性delaySecond秒的重连
        retryCount++;
        logger.info("ConfigClient -> 当前重试次数: {}", retryCount);
        if (retryCount >= MAX_RETRY_COUNT) {
          ClientConfigLoader.isConfigLoaded = true;
          ClientConfigLoader.isServerOffline = true;
          synchronized (ClientConfigLoader.clientInfo) {
            // 通知等待的线程
            ClientConfigLoader.clientInfo.notifyAll();
          }
        } else {
          // 进行重连
          channelFuture
              .channel()
              .eventLoop()
              .schedule(ConfigClient.this::reConnect, delaySeconds, TimeUnit.SECONDS);
        }
      }
    }
  }

  /** 重新连接 */
  protected void reConnect() {
    try {
      logger.info(
          "ConfigClient -> Start reconnect to server.{}:{}", this.serverIp, this.serverPort);
      ClientConfigLoader.isConfigLoaded = false;
      ClientConfigLoader.isServerOffline = false;
      if (channel != null && channel.isOpen()) {
        logger.info(
            "ConfigClient -> Server [{}] channel is active, close it and reconnect", this.serverIp);
        channel.close();
      }
      bootstrap
          .connect(this.serverIp, this.serverPort)
          .addListener(new ConfigClientChannelListener())
          .sync()
          .channel();
    } catch (Exception e) {
      logger.error(
          "ConfigClient -> ReConnect to server failure. server={}:{}:{}",
          this.serverIp,
          this.serverPort,
          e.getMessage());
    }
  }
}
