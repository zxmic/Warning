package all.netty;

import all.util.RedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS;
import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_LENGTH;

/*
说明：
1、SimpleChannelInboundHandler是ChannelInboundHandlerAdapter
2、HttpObject 客户端和服务器端相互通讯的数据被封装成HttpObject
 */
public class ServerHandler extends SimpleChannelInboundHandler {
    //private Logger logger = Logger.getLogger(ServerHandler.class);
    private  static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
//
//        System.out.println("收到消息  ：   "+msg.text());
//        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间   ：  "+LocalDateTime.now()));
//
//    }

    //服务器数据处理

//    //channelRead0读取客户端数据
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//
//        System.out.println(ctx.channel().remoteAddress()+" input : "+msg);
//
//
//        String json = "";
//        //logger.info("===================进入netty channelRead了=======================");
//
//        //判断
////        if(msg instanceof HttpRequest){
////            HttpRequest httpRequest=(HttpRequest)msg;
////            System.out.println("请求方法名：" + httpRequest.method().name());
////            URI uri=new URI(httpRequest.uri());
////            if("/favicon.ico".equals(uri.getPath())){
////                System.out.println("请求favicon.ico");
////                return;
////            }
////            ByteBuf content=Unpooled.copiedBuffer("hello a",CharsetUtil.UTF_8);
////            FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
////                    HttpResponseStatus.OK,content);
////            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
////            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
////            ctx.writeAndFlush(response);
////        }
//
//
////        //判断msg是不是httprequest请求
////        if(msg instanceof FullHttpRequest){
////            logger.info("进入http请求了");
////            System.out.println("msg类型="+msg.getClass());
////            System.out.println("客户端地址"+ctx.channel().remoteAddress());
////
////            json = handleHttpRequest(ctx, (FullHttpRequest) msg);
////
////            //回复消息给浏览器http
////            //构造一个http的回应，即httpresponse
////            FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8)));
////            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
////            response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept");
////            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
//////            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
//////            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,context.readableBytes());
////            //将创建好的response返回
////            ctx.writeAndFlush(response);
////
////        }
////        logger.info("最后返回的json数据" + json);
//
//        Channel channel=ctx.channel();
//        channelGroup.forEach(ch ->{
//            if(!channel.equals(ch)){
//                ch.writeAndFlush(channel.remoteAddress()+" 发送消息 ： "+msg);
//
//            }else {
//                ch.writeAndFlush("【自己】 ： "+msg);
//
//            }
//        });
//
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        System.out.println("read0--------------------------");
        String json = "";
        //logger.info("===================进入netty channelRead了=======================");
        if (o instanceof FullHttpRequest) {
            //logger.info("进入http请求了");
            System.out.println("FullHttpRequest");
            json = handleHttpRequest(ctx, (FullHttpRequest) o);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8)));
            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response);
        }
        System.out.println("最后返回数据  ：  " + json);
        //logger.info("最后返回数据" + json);
    }

    /**
     * 处理业务流程
     */
    private String handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest fuHr) {
        // Token 身份验证 从http头中获取 在redis中存token 格式是 key: token_userid值 value: token值
        //String token=fuHr.headers().get("token");
        //String userid=fuHr.headers().get("userid");
        //String type=fuHr.headers().get("type");
        //logger.info("userid:"+userid);
        //Jedis jedis= RedisUtil.getJedis();
//        if (type.equals("t2")) {
//            String restoken = jedis.get("token_" + userid);
//            if (restoken == null || !restoken.equals(token)) {
//                return "{\"message\":\"refuse\"}";
//            }
//        }
        String url = fuHr.uri();
        System.out.println("method  :  " + fuHr.method());
        ByteBuf byteBuf = fuHr.content();
        String data = byteBuf.toString(Charset.forName("utf-8"));
        String json = "";
        String resurl[]=url.split("/");
        System.out.println("url : "+url);
        //logger.info("data:"+data+",URL: "+url+",URLsplit: [1]:" + resurl[1]+",[2]:"+resurl[2]+",[3]:"+resurl[3]);
        try {
            Class<?> clazz=Class.forName("all."+resurl[1]+"."+resurl[2]);
            Object object = clazz.newInstance();
            //System.out.println("clazzname  :  "+clazz.getName());
            Method method=clazz.getMethod(resurl[3],String.class);
            //System.out.println("method  :  "+method);
            json= (String) method.invoke(object,data);
            //System.out.println("json"+json);
            return json;
        } catch (Exception e) {
            System.out.println("全局异常错误!!!");
            //logger.error("===========全局异常错误!!!===========");
            e.printStackTrace();
            return "{\"message\":\"Bad\"}";
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
        //关闭连接
        System.out.println("异常发生");
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        //System.out.println("channel active");
//        Channel channel=ctx.channel();
//        System.out.println(channel.remoteAddress()+" 上线");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelRegistered");
//        super.channelRegistered(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded  :  "+ctx.channel().id().asLongText());
//        Channel channel=ctx.channel();
//
//        channelGroup.writeAndFlush("[服务器]-"+channel.remoteAddress()+"加入\n");
//
//
//        channelGroup.add(channel);
//        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved  :  "+ctx.channel().id().asLongText());
//        Channel channel=ctx.channel();
//        channelGroup.writeAndFlush("[服务器]-"+channel.remoteAddress()+"离开\n");
//
//        //写不写都一样，会自动断掉
//        //channelGroup.remove(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelInactive");
//        Channel channel=ctx.channel();
//        System.out.println(channel.remoteAddress()+" 下线");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelUnregistered");
//        super.channelUnregistered(ctx);
    }



}
