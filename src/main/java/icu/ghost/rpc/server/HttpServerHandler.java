package icu.ghost.rpc.server;

import icu.ghost.rpc.RpcApplication;
import icu.ghost.rpc.model.RpcRequest;
import icu.ghost.rpc.model.RpcResponse;
import icu.ghost.rpc.registry.LocalRegistry;
import icu.ghost.rpc.serializer.JDKSerializer;
import icu.ghost.rpc.serializer.Serializer;
import icu.ghost.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Ghost
 * @version 1.0
 * @className HttpServerHandler
 * @date 2024/07/28 18:28
 * @since 1.0
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        // JDKSerializer serializer = new JDKSerializer();
        String serializerKey = RpcApplication.getRpcConfig().getSerializerKey();
        Serializer serializer = SerializerFactory.getInstance(serializerKey);
        System.out.println("Received request,request method:" + request.method() + ",request uri" + request.uri());

        // 异步处理HTTP请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            // 反序列化
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                // 响应
                doResponse(request, rpcResponse, serializer);
            }

            try {
                // 获取要调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                // 返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 响应
            doResponse(request,rpcResponse,serializer);
        });


    }

    /**
     * 响应
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");

        // 序列化
        byte[] bytes = null;
        try {
            bytes = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
            httpServerResponse.end(Buffer.buffer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
