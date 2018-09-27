package com.example.wws.javase.lesson01;

public class LessonDemo1 {

    public static void main(String args[]){
        new B();                //验证 类初始化顺序
        new B(1);         //初始化时，子类构造函数默认调用父类无参构造函数
    }

}

class A{

    static{
        System.out.println("Load A");
    }

    public A(){
        System.out.println("Create A");
    }

    public A(int arg){
        System.out.println("Create A + arg");
    }

    static{
        System.out.println("Load A");
    }
}

class B extends A{
    static {
        System.out.println("Load B");
    }

    public B(){
        System.out.println("Create B");
    }

    public B(int arg){
        System.out.println("Create B + arg");
    }
}
