package com.example.wws.rxjava.lib;

import io.reactivex.Flowable;

public class RxDemo1 {


    public static void main(String[] args){
        Flowable.just("Hello world!").subscribe(System.out::println);
    }

}
