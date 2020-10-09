package com.longpeng.springboot.demo.suanfa;

import java.util.Scanner;

public class Main {

    static int[][] arr;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        //坐标位置
        int x = 0;
        int y = 0;
        //最大行数
        int lines = in.nextInt();
        //当前值
        int count = 1;
        //用于存储对应位置的数据
        arr = new int[lines][lines];
        //计算位置并赋值
        solve(x,y,count,lines);

        //输出
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j <=i; j++) {
                System.out.print(arr[j][i]+" ");
            }
            System.out.println();
        }
    }
    private static void solve(int x, int y, int count, int lines) {
        //向下走 y轴++
        for (int i = 0; i < lines-1; i++) {
            //给对应数组位置赋值
           printThis(x,y++,count++);
        }
        //向左走x轴++
        for (int i = 0; i < lines-1; i++) {
            printThis(x++,y,count++);
        }
        //斜边高度减少
        lines--;
        for (int i = 0; i < lines; i++) {
            printThis(x--,y--,count++);
        }
        //外层结束高度调整
        lines-=2;
        if(lines>0){
            //重置坐标位置
            x++;
            y+=2;
            //递归
            solve(x,y,count,lines);
        }
    }

    private static void printThis(int x, int y, int count) {
        arr[x][y] = count;
    }
}
