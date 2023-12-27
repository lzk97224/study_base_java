package com.lizk.fun.netty.lession1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

public class Test4TwoBsUseSameDF {

    @Test
    public void testTwoBsUseSameDF() throws InterruptedException {
        ServerBootstrap sbs1 = new ServerBootstrap();
        ServerBootstrap sbs2 = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup(2);
        //NioEventLoopGroup中的一个NioEventLoop相当于是一个select的包装，参数传入几，就会建立几个selector
        //ServerBootstrap调用bind方法以后，相当于创建了一个listen文件描述符fd，并且吧fd绑定到了selector上，并且把处理接收数据的handler和文件描述符绑定，这里服务端的handler是框架默认的实现，实现的内容是把建立连接后的文件描述符绑定到selector，并且把配置的childHandler等信息与文件描述符关联
        //当有客户端连接的时候，listen fd 状态变化，selector返回待处理的文件描述符，框架找到对应的handler处理。


        ChannelFuture s1Future = sbs1.group(boss, boss)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(Thread.currentThread().getName() + "8888");
                            }
                        });
                    }
                })
                .bind(8888);

        sbs2.group(boss, boss)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(Thread.currentThread().getName() + "8889");
                            }
                        });
                    }
                })
                .bind(8889);

        System.out.println("启动服务");
        s1Future.sync().channel().closeFuture().sync();
    }


}
