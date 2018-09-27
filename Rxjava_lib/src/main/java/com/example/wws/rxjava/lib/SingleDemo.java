package com.example.wws.rxjava.lib;

class SingleDemo {
    private static final SingleDemo ourInstance = new SingleDemo();

    static SingleDemo getInstance() {
        return ourInstance;
    }

    private SingleDemo() {
    }
}
