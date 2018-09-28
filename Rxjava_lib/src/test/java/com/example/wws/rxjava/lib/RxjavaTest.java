package com.example.wws.rxjava.lib;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RxjavaTest {

    @Test
    public void testFlowableJustMethod(){
        Flowable flowable = Flowable.just("Hello world!");
        flowable.subscribe(new Consumer<String>(){

            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    @Test
    public void testObservableJustMethod(){
        Observable observable = Observable.just("Hello world !");
        observable.subscribe(System.out::println);
    }

    @Test
    public void testObservableFromMethod(){
        Observable observable = Observable.fromArray("Hello world !");
        observable.subscribe(System.out::println);
    }

    @Test
    public void testSingle() throws InterruptedException {
        Disposable disposable = Single.just("Hello world!")
                .delay(1, TimeUnit.SECONDS,Schedulers.single())
                .subscribeWith(new DisposableSingleObserver<String>(){

                    @Override
                    public void onSuccess(String s) {
                        System.out.println("success: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    protected void onStart() {
                        super.onStart();
                        System.out.println("onStart: ");
                    }

        });

        Thread.sleep(3000);
        disposable.dispose();
    }

    @Test
    public void testMap(){
        Observable.just("Hello", "world")
                .map(s -> s + "123")
                .subscribe(System.out::println);
    }

    @Test
    public void testflatMap(){
        Observable.just("Hello")
//                .flatMap(toString().toCharArray().forEach((p) -> System.out.println(p)))
                .flatMap(s -> Observable.fromArray(s))
                .subscribe(s -> System.out.println("result: " + s));
    }

}