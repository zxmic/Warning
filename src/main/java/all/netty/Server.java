package all.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class Server {
    private void start() {
        //创建BossGroup和workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//接收组，处理来访问服务器的客户的连接请求
        EventLoopGroup workGroup = new NioEventLoopGroup();//工作组，实现数据的读写

        try {
            ServerBootstrap bootstrap=new ServerBootstrap();//服务端来设置通道参数的工具
            bootstrap.group(bossGroup, workGroup)//将两个工作线程与通道绑定
                    .channel(NioServerSocketChannel.class)//指定NIO模式
                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .option(ChannelOption.SO_BACKLOG,128)//设置TCP缓冲区
//                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置长连接
                    .childHandler(new  ServerChannelInitializer());

            /*
            new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new HttpResponseEncoder());//server端发送的是httpResponse,要进行编码
                    socketChannel.pipeline().addLast(new HttpRequestDecoder());//server端接收的是httpRequest,要进行解码

                    //等待解码后的报文头和报文体一起扔给下一层
                    socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                    socketChannel.pipeline().addLast(new ServerHandler());
                }
            }
            */

            System.out.println("服务器 is ready。。。");
            ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080)).sync();//绑定端口
            //给future注册监听器，监控关心的事件
            future.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听端口 8080 成功");
                    }else {
                        System.out.println("监听端口 8080 失败");
                    }
                }
            });
            //阻止程序关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception{
        new Thread(){
            @Override
            public void run() {
                Server server=new Server();
                server.start();
            }
        }.start();
    }
}
