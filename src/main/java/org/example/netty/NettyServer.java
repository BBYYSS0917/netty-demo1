package org.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author BaiJY
 * @date 2022/10/18
 **/
public class NettyServer {
    public static void main(String[] args) {
        //这里我们使用NioEventLoopGroup实现类即可，创建BossGroup和WorkerGroup
        //当然还有EpollEventLoopGroup，但是仅支持Linux，这是Netty基于Linux底层Epoll单独编写的一套本地实现，没有使用NIO那套
        EventLoopGroup bossGroup = new NioEventLoopGroup(), workerGroup = new NioEventLoopGroup();

        //创建服务端启动引导类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //可链式，就很棒
        bootstrap
                .group(bossGroup, workerGroup)   //指定事件循环组
                .channel(NioServerSocketChannel.class)   //指定为NIO的ServerSocketChannel
                .childHandler(new ChannelInitializer<SocketChannel>() {   //注意，这里的SocketChannel不是我们NIO里面的，是Netty的
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline()   //直接获取pipeline，然后添加两个Handler
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println("1接收到客户端发送的数据："+buf.toString(StandardCharsets.UTF_8));
                                        ctx.fireChannelRead(msg);
                                    }
                                })
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println("2接收到客户端发送的数据："+buf.toString(StandardCharsets.UTF_8));
                                        ctx.channel().writeAndFlush("伞兵一号卢本伟");  //这里我们使用channel的write
                                    }
                                })
                                .addLast(new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("1号出站："+msg);
                                    }
                                })
                                .addLast(new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("2号出站："+msg);
                                        ctx.write(msg);  //继续write给其他的出站Handler，不然到这里就断了
                                    }
                                });
                    }
                });
        //最后绑定端口，启动
        bootstrap.bind(8080);
    }
}
