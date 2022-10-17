package org.example;

import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * reference: https://www.yuque.com/qingkongxiaguang/javase/kl68ty
 *
 * @author BaiJY
 * @date 2022/09/27
 **/
public class BufferWriteDemo {

    public static void main(String[] args) {
        // 创建
        IntBuffer buffer = IntBuffer.allocate(10);

        int[] arr = new int[]{1, 2, 3, 4, 5, 6};
        IntBuffer buffer1 = IntBuffer.wrap(arr);

        // 写操作
        buffer.put(1).put(2).put(3);
        System.out.println(buffer);

        buffer.put(arr, 1, 2);
        System.out.println(Arrays.toString(buffer.array()));


        IntBuffer src = IntBuffer.wrap(new int[]{1, 2, 3, 4, 5});

        IntBuffer target = IntBuffer.allocate(10);
        target.put(src);
        System.out.println(Arrays.toString(target.array()));


        // 错误实现：position的位置随着put向后移动
//        IntBuffer src = IntBuffer.allocate(5);
//        for (int i = 0; i < 5; i++) src.put(i);   //手动插入数据
//        IntBuffer buffer = IntBuffer.allocate(10);
//        buffer.put(src);
//        System.out.println(Arrays.toString(buffer.array()));


    }

}
