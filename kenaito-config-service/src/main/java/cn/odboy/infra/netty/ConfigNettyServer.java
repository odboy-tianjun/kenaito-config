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

@Slf4j
@Component
public class ConfigNettyServer implements InitializingBean {
    @Value("${kenaito.config-center.port}")
    private Integer configCenterPort;
    @Autowired
    private ConfigFileService configFileService;

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadUtil.execAsync(() -> {
            try {
                start();
            } catch (InterruptedException e) {
                log.error("Netty Server Start Error", e);
                throw new RuntimeException(e);
            }
        });
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ConfigServerHandler(configFileService));
                        }
                    });
            log.info("Netty Server Start...");
            ChannelFuture channelFuture = serverBootstrap.bind(configCenterPort).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
