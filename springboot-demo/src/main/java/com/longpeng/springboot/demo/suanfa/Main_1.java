package com.longpeng.springboot.demo.suanfa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main_1 {

    public static void main(String[] args) {
        String str = "5 green red blue red red 3 pink orange pink 0";
        String[] s = str.split(" ");
        //储存颜色组数
        List<List<String>> colorList = new ArrayList<>();
        for (int i = 0; i <s.length ; i++) {
            //判断当前字符是否数字
            if(Character.isDigit(Character.valueOf(s[i].charAt(0)))){
                int index =  Integer.valueOf(s[i]);
                if(0==index) break;
                ArrayList<String> color = new ArrayList<>();
                //获取当组颜色存如list
                for (int j = i+1; j <= i+index; j++) {
                    color.add(s[j]);
                }
                colorList.add(color);
                //调整下标位置
                i+=index;
            }
        }
        for (int i = 0; i < colorList.size(); i++) {
            List<String> strings = colorList.get(i);
            Collections.sort(strings);
            String s1 = strings.get(strings.size() - 1);
            System.out.println(s1);
        }
    }
}
