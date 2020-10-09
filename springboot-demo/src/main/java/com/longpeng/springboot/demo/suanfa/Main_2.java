package com.longpeng.springboot.demo.suanfa;

import java.util.Scanner;
import java.util.Stack;

public class Main_2 {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();
        int count = 0;
        Stack<String> stack = new Stack<>();
        Stack<String> stack1 = new Stack<>();
        Stack<String> stack2 = new Stack<>();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String s = String.valueOf(chars[i]);
            if("<".equals(s) || ">".equals(s)){stack.push(s);}
            if("{".equals(s) || "}".equals(s)){stack1.push(s);}
            if("[".equals(s) || "]".equals(s)){stack2.push(s);}
        }
        if(stack.size()>1){
            do{
                if(stack.pop().equals(">") && stack.peek().equals("<")){count++;}
            } while (stack.size()>1);
        }

        if(stack1.size()>1) {
            do {
                if (stack1.pop().equals("}") && stack1.peek().equals("{")) {
                    count++;
                }
            } while (stack1.size() > 1);
        }

        if(stack2.size()>1) {
            do {
                if (stack2.pop().equals("]") && stack2.peek().equals("[")) {
                    count++;
                }
            } while (stack2.size() > 1);
        }
        System.out.println(count);
    }
}
