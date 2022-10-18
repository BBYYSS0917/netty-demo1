package org.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author BaiJY
 * @date 2022/10/18
 **/
public class NettyDemo {
    public static void main(String[] args) {
        allocate();
    }

    public static void test() {
        //创建一个初始容量为10的ByteBuf缓冲区，这里的Unpooled是用于快速生成ByteBuf的工具类
        //至于为啥叫Unpooled是池化的意思，ByteBuf有池化和非池化两种，区别在于对内存的复用，我们之后再讨论
        ByteBuf buf = Unpooled.buffer(10);
        System.out.println("初始状态：" + Arrays.toString(buf.array()));
        buf.writeInt(-888888888);   //写入一个Int数据
        System.out.println("写入Int后：" + Arrays.toString(buf.array()));
        buf.readShort();   //无需翻转，直接读取一个short数据出来
        System.out.println("读取Short后：" + Arrays.toString(buf.array()));
        buf.discardReadBytes();   //丢弃操作，会将当前的可读部分内容丢到最前面，并且读写指针向前移动丢弃的距离
        System.out.println("丢弃之后：" + Arrays.toString(buf.array()));
        buf.clear();    //清空操作，清空之后读写指针都归零
        System.out.println("清空之后：" + Arrays.toString(buf.array()));
    }

    public static void allocate() {
        //我们也可以将一个byte[]直接包装进缓冲区（和NIO是一样的）不过写指针的值一开始就跑到最后去了，但是这玩意是不是只读的
        ByteBuf buf = Unpooled.wrappedBuffer("abcdefg".getBytes());
        System.out.println(Arrays.toString(buf.array()));
        //除了包装，也可以复制数据，copiedBuffer()会完完整整将数据拷贝到一个新的缓冲区中
        buf.readByte();   //读取一个字节
        ByteBuf slice = buf.slice();   //现在读指针位于1，然后进行划分

        System.out.println(slice.arrayOffset());   //得到划分出来的ByteBuf的偏移地址
        System.out.println(Arrays.toString(slice.array()));
    }

    public static void expansionCapacity() {
        ByteBuf buf = Unpooled.buffer(10);    //容量只有10字节
        System.out.println(buf.capacity());
        //直接写一个字符串
        buf.writeCharSequence("卢本伟牛逼！", StandardCharsets.UTF_8);   //很明显这么多字已经超过10字节了
        System.out.println(buf.capacity());
    }

    public static void directMemBuf() {
        ByteBuf buf = Unpooled.directBuffer(10);
        System.out.println(Arrays.toString(buf.array()));
    }

    public static void advanceUsage() {
        // 复用内存空间
        ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        ByteBuf buf = allocator.directBuffer(10);   //申请一个容量为10的直接缓冲区
        buf.writeChar('T');    //随便操作操作
        System.out.println(buf.readChar());
        buf.release();    //释放此缓冲区

        ByteBuf buf2 = allocator.directBuffer(10);   //重新再申请一个同样大小的直接缓冲区
        System.out.println(buf2 == buf);
    }
}
