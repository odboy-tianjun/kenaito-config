package cn.odboy.infra.netty;

import cn.hutool.core.thread.ThreadUtil;
import cn.odboy.service.ConfigFileService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置中心服务
 *
 * @author odboy
 * @date 2024-12-06
 */
@Slf4j
@Component
public class ConfigNettyServer implements InitializingBean {
  @Value("${kenaito.config-center.port}")
  private Integer configCenterPort;

  @Autowired private ConfigFileService configFileService;

  @Override
  public void afterPropertiesSet() throws Exception {
    ThreadUtil.execAsync(
        () -> {
          try {
            start();
          } catch (InterruptedException e) {
            log.error("Netty服务端启动失败", e);
            throw new RuntimeException(e);
          }
        });
  }

  public void start() throws InterruptedException {
    // 这里为什么是16捏，好问题，我之前搞dingtalk二开的时候，看到有个地方的线程默认值就是16。
    // 写配置项也可以，爱怎么改怎么改好吧，但是请记得MIT协议内容~
    EventLoopGroup bossGroup = new NioEventLoopGroup(16);
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap
          .group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(new ConfigServerHandler(configFileService));
                }
              });
      log.info("Netty服务端启动中, 监听端口={}", configCenterPort);
      ChannelFuture channelFuture = serverBootstrap.bind(configCenterPort).sync();
      channelFuture.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
