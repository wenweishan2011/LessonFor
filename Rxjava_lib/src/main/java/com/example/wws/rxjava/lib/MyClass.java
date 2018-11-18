package com.example.wws.rxjava.lib;

public class MyClass<T> {


    public static void main(String[] args){
        new MyClass().function(new B());
    }

    public static <T> void function(T param){
        System.out.println(param.toString());
    }

    static class B<T>{

    }

    interface A{

    }
}
