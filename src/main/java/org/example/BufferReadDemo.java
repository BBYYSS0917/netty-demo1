package org.example;

import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * @author BaiJY
 * @date 2022/09/28
 **/
public class BufferReadDemo {
    public static void main(String[] args) {

//        IntBuffer buffer = IntBuffer.wrap(new int[]{1, 2, 3, 4, 5});
//        int[] arr = new int[10];
//        buffer.get(arr, 2, 5);
//
//        System.out.println(Arrays.toString(arr));
//
//
//        IntBuffer buffer1 = IntBuffer.wrap(new int[]{1, 2, 3, 4, 5});
//        int[] arr1 = buffer1.array();
//        arr1[0] = 9999;
//        System.out.println(buffer1.get());


        // test mark
        IntBuffer buffer = IntBuffer.wrap(new int[]{1, 2, 3, 4, 5});
        buffer.get();
        buffer.mark();
        buffer.get();
        buffer.reset();
        System.out.println(buffer.get());

    }
}
