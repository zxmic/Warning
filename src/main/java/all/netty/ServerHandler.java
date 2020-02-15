package all.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/*
说明：
1、SimpleChannelInboundHandler是ChannelInboundHandlerAdapter
2、HttpObject 客户端和服务器端相互通讯的数据被封装成HttpObject
 */
public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //服务器数据处理
   
    //channelRead0读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是httprequest请求
        if(msg instanceof HttpRequest){
            System.out.println("msg类型="+msg.getClass());
            System.out.println("客户端地址"+ctx.channel().remoteAddress());
            //回复消息给浏览器http
            ByteBuf context = Unpooled.copiedBuffer("hello,我是服务器！", CharsetUtil.UTF_8);

            //构造一个http的回应，即httpresponse
            FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,HttpResponseStatus.OK,context);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,context.readableBytes());
            //将创建好的response返回
            ctx.writeAndFlush(response);

        }
    }


}
