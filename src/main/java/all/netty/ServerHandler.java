package all.netty;

import all.util.RedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private Logger logger = Logger.getLogger(ServerHandler.class);

    //服务器数据处理

    //channelRead0读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        String json = "";
        logger.info("===================进入netty channelRead了=======================");
        //判断msg是不是httprequest请求
        if(msg instanceof FullHttpRequest){
            logger.info("进入http请求了");
            System.out.println("msg类型="+msg.getClass());
            System.out.println("客户端地址"+ctx.channel().remoteAddress());

            json = handleHttpRequest(ctx, (FullHttpRequest) msg);

            //回复消息给浏览器http
            //构造一个http的回应，即httpresponse
            FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8)));
            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
//            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,context.readableBytes());
            //将创建好的response返回
            ctx.writeAndFlush(response);

        }
        logger.info("最后返回的json数据" + json);

    }

    /**
     * 处理业务流程
     */
    private String handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest fuHr) {
        // Token 身份验证 从http头中获取 在redis中存token 格式是 key: token_userid值 value: token值
        //String token=fuHr.headers().get("token");
        String userid=fuHr.headers().get("userid");
        String type=fuHr.headers().get("type");
        logger.info("userid:"+userid);
        Jedis jedis= RedisUtil.getJedis();
//        if (type.equals("t2")) {
//            String restoken = jedis.get("token_" + userid);
//            if (restoken == null || !restoken.equals(token)) {
//                return "{\"message\":\"refuse\"}";
//            }
//        }
        String url = fuHr.uri();
        ByteBuf byteBuf = fuHr.content();
        String data = byteBuf.toString(Charset.forName("utf-8"));
        String json = "";
        String resurl[]=url.split("/");
        logger.info("data:"+data+",URL: "+url+",URLsplit: [1]:" + resurl[1]+",[2]:"+resurl[2]+",[3]:"+resurl[3]);
        try {
            Class<?> clazz=Class.forName("all."+resurl[1]+"."+resurl[2]);
            System.out.println(clazz.getName());
            Method method=clazz.getMethod(resurl[3],String.class);
            json= (String) method.invoke(clazz.newInstance(),data);
            return json;
        } catch (Exception e) {
            logger.error("===========全局异常错误!!!===========");
            e.printStackTrace();
            return "{\"message\":\"Bad\"}";
        }
    }


}
