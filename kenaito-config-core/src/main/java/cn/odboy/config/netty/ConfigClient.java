package cn.odboy.config.netty;

import cn.odboy.config.context.ClientConfigLoader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class ConfigClient {
    /**
     * 客户端
     */
    private EventLoopGroup eventLoopGroup;
    /**
     * 存放客户端bootstrap对象
     */
    private Bootstrap bootstrap;
    /**
     * 存放客户端channel对象
     */
    private Channel channel;
    /**
     * 重连间隔，单位秒
     */
    private Integer delaySeconds = 5;
    /**
     * 连接属性
     */
    private String connectServerIp;
    private Integer connectServerPort;

    public void start(String server, Integer port) throws InterruptedException {
        this.connectServerIp = server;
        this.connectServerPort = port;
        try {
            eventLoopGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 设置接收缓冲区大小为10MB
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024 * 1024 * 10))
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ConfigClientHandler());
                        }
                    });
            doConnect(server, port);
        } finally {
//            eventLoopGroup.shutdownGracefully();
        }
    }

    private void doConnect(String server, Integer port) throws InterruptedException {
        System.err.println("ConfigClient -> Start");
        if (channel != null && channel.isActive()) {
            return;
        }
        bootstrap.connect(server, port)
                .addListener(new ConfigClientChannelListener())
                .sync().channel();
//        channel.closeFuture().sync();
    }

    private class ConfigClientChannelListener implements ChannelFutureListener {
        /**
         * 该方法会在channelActive之前执行，去判断客户端连接是否成功，并做失败重连的操作
         */
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            // 连接成功后保存Channel
            if (channelFuture.isSuccess()) {
                channel = channelFuture.channel();
//                SocketAddress sa = channel.remoteAddress();
//                connectedServerAddr = ((InetSocketAddress) sa).getAddress().getHostAddress();
                System.err.println("ConfigClient -> Connect success " + connectServerIp + ":" + connectServerPort);
            } else {
                // 失败后delaySecond秒（默认是5秒）重连，周期性delaySecond秒的重连；
                channelFuture.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        // 进行重连
                        reConnect();
                    }
                }, delaySeconds, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 重新连接
     */
    protected void reConnect() {
        try {
            System.err.println("ConfigClient -> Start reconnect to server." + this.connectServerIp + ":" + this.connectServerPort);
            ClientConfigLoader.isConfigLoaded = false;
            if (channel != null && channel.isOpen()) {
                System.err.println("ConfigClient -> Server [" + this.connectServerIp + "] channel is active, close it and reconnect");
                channel.close();
            }
            bootstrap.connect(this.connectServerIp, this.connectServerPort)
                    .addListener(new ConfigClientChannelListener())
                    .sync().channel();
        } catch (Exception e) {
            System.err.println("ConfigClient -> ReConnect to server failure. server=" + this.connectServerIp + ":" + this.connectServerPort + ":" + e.getMessage());
        }
    }
}
