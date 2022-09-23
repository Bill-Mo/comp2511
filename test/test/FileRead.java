package test;

import java.io.*;
 
public class FileRead {
    public static void main(String args[]) throws IOException {
        // 创建 FileReader 对象
        FileReader fr = new FileReader("main/Hello1.txt");
        char[] a = new char[50];
        fr.read(a); // 读取数组中的内容
        for (char c : a)
            System.out.print(c); // 一个一个打印字符
        fr.close();
    }
}