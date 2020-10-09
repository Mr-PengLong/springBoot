package com.longpeng.springboot.demo.suanfa;

public class Main_3 {

    public static void main(String[] args) {
        Main_3 main_3 = new Main_3();
        main_3.aVoid(3);
    }

    public void  aVoid(int n){
        digui(0,2*n,"");
    }

    private void digui(int i, int i1, String s) {
        if(i>=i1){
            System.out.println(s);
            return;
        }

        digui(i+1,i1,s+"(");
        digui(i+1,i1,s+")");
    }
}
