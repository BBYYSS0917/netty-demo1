package org.example;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author BaiJY
 * @date 2022/09/28
 **/
public class FileChannelDemo {
    public static void main(String[] args) {

    }

    public static void oldFileIO() {
        try(FileOutputStream out = new FileOutputStream("test.txt");
            FileInputStream in = new FileInputStream("test.txt")){
            String data = "伞兵一号卢本伟准备就绪！";
            out.write(data.getBytes());   //向文件的输出流中写入数据，也就是把数据写到文件中
            out.flush();

            byte[] bytes = new byte[in.available()];
            in.read(bytes);    //从文件的输入流中读取文件的信息
            System.out.println(new String(bytes));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFile() throws IOException {
        //1. 直接通过输入或输出流获取对应的通道
        FileInputStream in = new FileInputStream("test.txt");
        //但是这里的通道只支持读取或是写入操作
        FileChannel channel = in.getChannel();
        //创建一个容量为128的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(128);
        //从通道中将数据读取到缓冲区中
        channel.read(buffer);
        //翻转一下，接下来要读取了
        buffer.flip();

        System.out.println(new String(buffer.array(), 0, buffer.remaining()));
    }

    public static void readRandomAccessFile() {
        /*
      通过RandomAccessFile进行创建，注意后面的mode有几种：
      r        以只读的方式使用
      rw   读操作和写操作都可以
      rws  每当进行写操作，同步的刷新到磁盘，刷新内容和元数据
      rwd  每当进行写操作，同步的刷新到磁盘，刷新内容
     */
        try(RandomAccessFile f = new RandomAccessFile("test.txt", "rw");  //这里设定为支持读写，这样创建的通道才能具有这些功能
            FileChannel channel = f.getChannel()){   //通过RandomAccessFile创建一个通道
            channel.write(ByteBuffer.wrap("伞兵二号马飞飞准备就绪！".getBytes()));

            System.out.println("写操作完成之后文件访问位置："+channel.position());  //注意读取也是从现在的位置开始
            channel.position(0);  //需要将位置变回到最前面，这样下面才能从文件的最开始进行读取

            ByteBuffer buffer = ByteBuffer.allocate(128);
            channel.read(buffer);
            buffer.flip();

            System.out.println(new String(buffer.array(), 0, buffer.remaining()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
