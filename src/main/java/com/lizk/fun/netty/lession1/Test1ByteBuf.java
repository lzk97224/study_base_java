package com.lizk.fun.netty.lession1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.Test;

/**
 * ByteBuf 是对 ByteBuffer 的包装，提供了更方便易用的api
 */
public class Test1ByteBuf {

    /**
     * 测试 readerIndex(int)方法的使用，方法可以移动读数据的索引
     */
    @Test
    public void testByteBufReaderIndex() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(new byte[]{2, 3, 4, 5});
        System.out.println(buffer.readableBytes());
        buffer.readerIndex(2);
        System.out.println(buffer.readableBytes());
        byte[] tmp = new byte[buffer.readableBytes()];
        buffer.readBytes(tmp);
        for (byte b : tmp) {
            System.out.println(b);
        }
    }

    /**
     * 这里演示了想ByteBuf中写入数据时，内部状态的变化
     */
    @Test
    public void test1() {
        ByteBufAllocator bufAllocator = null;
        //bufAllocator = ByteBufAllocator.DEFAULT;
        bufAllocator = UnpooledByteBufAllocator.DEFAULT;

        ByteBuf buffer = bufAllocator.buffer(8, 20);

        printByteBuf(0, buffer);

        for (int i = 0; i < 6; i++) {
            buffer.writeBytes(new byte[]{1, 2, 3, 4});
            printByteBuf(i + 1, buffer);
        }
    }

    public void printByteBuf(int index, ByteBuf buf) {
        System.out.println("-----------------" + index + "---------------");
        System.out.println("buf.isReadable()                :" + buf.isReadable());
        System.out.println("buf.readerIndex()               :" + buf.readerIndex());
        System.out.println("buf.readableBytes()             :" + buf.readableBytes());
        System.out.println("buf.maxFastWritableBytes()      :" + buf.maxFastWritableBytes());
        System.out.println("buf.maxWritableBytes()          :" + buf.maxWritableBytes());
        System.out.println("buf.isWritable()                :" + buf.isWritable());
        System.out.println("buf.isWritable(4)               :" + buf.isWritable(4));
        System.out.println("buf.isReadOnly()                :" + buf.isReadOnly());
        System.out.println("buf.writerIndex()               :" + buf.writerIndex());
        System.out.println("buf.writableBytes()             :" + buf.writableBytes());
        System.out.println("buf.capacity()                  :" + buf.capacity());
        System.out.println("buf.maxCapacity()               :" + buf.maxCapacity());
        System.out.println("buf.isDirect()                  :" + buf.isDirect());
    }
}
