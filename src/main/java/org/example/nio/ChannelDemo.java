package org.example.nio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author BaiJY
 * @date 2022/09/28
 **/
public class ChannelDemo {
    public static void main(String[] args) throws IOException {

    }

    public static void oldIO() throws IOException {
        //数组创建好，一会用来存放从流中读取到的数据
        byte[] data = new byte[10];
        //直接使用输入流
        InputStream in = System.in;
        while (true) {
            int len;
            while ((len = in.read(data)) >= 0) {  //将输入流中的数据一次性读取到数组中
                System.out.print("读取到一批数据：" + new String(data, 0, len));  //读取了多少打印多少
            }
        }
    }

    public static void newIO() throws IOException {
        //缓冲区创建好，一会就靠它来传输数据
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //将System.in作为输入源，一会Channel就可以从这里读取数据，然后通过缓冲区装载一次性传递数据
        ReadableByteChannel readChannel = Channels.newChannel(System.in);
        while (true) {
            //将通道中的数据写到缓冲区中，缓冲区最多一次装10个
            readChannel.read(buffer);
            //写入操作结束之后，需要进行翻转，以便接下来的读取操作
            buffer.flip();
            //最后转换成String打印出来康康
            System.out.println("读取到一批数据："+new String(buffer.array(), 0, buffer.remaining()));
            //回到最开始的状态
            buffer.clear();
        }
    }
}
