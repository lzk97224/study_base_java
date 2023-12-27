package com.lizk.fun.netty.lession1;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.nio.charset.Charset;

public class Test3Bootstrap {
    /**
     * 使用Bootstrap实现客户端功能
     *
     * @throws InterruptedException
     */
    @Test
    public void testNettyClient() throws InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connectFuture = bs.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(Test2EventLoopGroup.ClientChannelInboundHandler.client);
                    }
                })
                .connect("127.0.0.1", 9090);
        Channel client = connectFuture.sync().channel();

        ByteBuf msg = Unpooled.copiedBuffer("就是肯德基", Charset.defaultCharset());

        client.writeAndFlush(msg).sync();


        client.closeFuture().sync();
    }

    /**
     * 使用Bootstrap实现服务端功能
     *
     * @throws InterruptedException
     */
    @Test
    public void testNttyServer() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap sb = new ServerBootstrap();
        ChannelFuture bindFuture = sb.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(Test2EventLoopGroup.ClientChannelInboundHandler.client);
                    }
                }).bind("127.0.0.1", 9090);

        bindFuture.sync().channel().closeFuture().sync();
    }
}
