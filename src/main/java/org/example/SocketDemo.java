package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author BaiJY
 * @date 2022/09/28
 **/
public class SocketDemo {
    public static void oldServer() {
        try (ServerSocket server = new ServerSocket(8080)) {    //将服务端创建在端口8080上
            System.out.println("正在等待客户端连接...");
            Socket socket = server.accept();
            System.out.println("客户端已连接，IP地址为：" + socket.getInetAddress().getHostAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  //通过
            System.out.print("接收到客户端数据：");
            System.out.println(reader.readLine());
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write("已收到！");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void oldClient() {
        try (Socket socket = new Socket("localhost", 8080);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("已连接到服务端！");
            OutputStream stream = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(stream);  //通过转换流来帮助我们快速写入内容
            System.out.println("请输入要发送给服务端的内容：");
            String text = scanner.nextLine();
            writer.write(text + '\n');   //因为对方是readLine()这里加个换行符
            writer.flush();
            System.out.println("数据已发送：" + text);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("收到服务器返回：" + reader.readLine());
        } catch (IOException e) {
            System.out.println("服务端连接失败！");
            e.printStackTrace();
        } finally {
            System.out.println("客户端断开连接！");
        }
    }
}