package org.example.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;

/**
 * @author BaiJY
 * @date 2022/09/28
 **/
public class FileLockDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建RandomAccessFile对象，并拿到Channel
        RandomAccessFile f = new RandomAccessFile("test.txt", "rw");
        FileChannel channel = f.getChannel();
        System.out.println(new Date() + " 正在尝试获取文件锁...");
        //接着我们直接使用lock方法进行加锁操作（如果其他进程已经加锁，那么会一直阻塞在这里）
        //加锁操作支持对文件的某一段进行加锁，比如这里就是从0开始后的6个字节加锁，false代表这是一把独占锁
        //范围锁甚至可以提前加到一个还未写入的位置上
        FileLock lock = channel.lock(0, 6, false);
        System.out.println(new Date() + " 已获取到文件锁！");
        Thread.sleep(5000);   //假设要处理5秒钟
        System.out.println(new Date() + " 操作完毕，释放文件锁！");

        //操作完成之后使用release方法进行锁释放
        lock.release();
    }
}
