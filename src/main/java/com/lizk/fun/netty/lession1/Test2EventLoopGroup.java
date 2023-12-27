package com.lizk.fun.netty.lession1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class Test2EventLoopGroup {
    /**
     * 测试 NioEventLoopGroup 的线程池执行器的基本使用
     * 这个 NioEventLoopGroup 可以当做一个执行器使用，它实现了ExecutorService接口，也就是有类似线程池ScheduledThreadPoolExecutor的功能
     */
    @Test
    public void loopExecutor() {
//      NioEventLoopGroup selector = new NioEventLoopGroup(1);
        NioEventLoopGroup selector = new NioEventLoopGroup(2);
        selector.execute(() -> {
            try {
                while (true) {
                    System.out.println(Thread.currentThread() + "xxxxxxx1");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        selector.execute(() -> {
            try {
                while (true) {
                    System.out.println(Thread.currentThread() + "xxxxxxx2");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 不是用 Bootstrap 利用netty实现网络通讯
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void clientMode() throws InterruptedException, IOException {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);
        NioSocketChannel client = new NioSocketChannel();
        selector.register(client);
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(ClientChannelInboundHandler.client);

        ChannelFuture channelFuture = client.connect(new InetSocketAddress("127.0.0.1", 9090));

        ByteBuf bf = Unpooled.copiedBuffer("hhhhsdfasd".getBytes());
        ChannelFuture future = client.writeAndFlush(bf);
        future.sync();


        channelFuture.channel().closeFuture().sync();
        System.in.read();
        System.out.println("client over");
    }


    /**
     * 不使用Bootstrap，直接使用 NioEventLoopGroup，NioServerSocketChannel等实现服务端通讯
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void serverMode() throws InterruptedException, IOException {

        NioEventLoopGroup loop = new NioEventLoopGroup(1);
        NioServerSocketChannel server = new NioServerSocketChannel();
        loop.register(server);

        server.pipeline().addLast(new ServerChannelInboundHandler());

        ChannelFuture channelFuture = server.bind(new InetSocketAddress("127.0.0.1", 9090));
        channelFuture.sync().channel().closeFuture().sync();
        System.in.read();
    }

    static class ServerChannelInboundHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            SocketChannel socketChannel = (SocketChannel) msg;
            socketChannel.pipeline().addLast(ClientChannelInboundHandler.client);
            ctx.channel().eventLoop().register(socketChannel);
        }
    }

    /**
     * 如果同一个handler实例被重复使用，那么必须添加 @ChannelHandler.Sharable 注解
     */
    @ChannelHandler.Sharable
    static class ClientChannelInboundHandler extends ChannelInboundHandlerAdapter {
        static ClientChannelInboundHandler client = new ClientChannelInboundHandler();

        private ClientChannelInboundHandler() {
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelRegistered");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("channelRead");
            ByteBuf buf = (ByteBuf) msg;
            CharSequence msgStr = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset());
            System.out.println(msgStr);

            ((ByteBuf) msg).resetReaderIndex();
            ctx.channel().writeAndFlush(msg).sync();
        }
    }
}
