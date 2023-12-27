package com.lizk.fun.netty.lession2.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.util.UUID;


public class RPCByMyself {


}

class ProxyUtil {
    public static <T> T get(Class<T> inter) {
        return (T) Proxy.newProxyInstance(
                inter.getClassLoader(),
                new Class<?>[]{inter},
                (proxy, method, args) -> {


                    ByteBuf buffer = Unpooled.buffer();
                    buffer.readerIndex();


                    new ByteArrayOutputStream();

                    return null;
                });
    }
}


interface C {

}
