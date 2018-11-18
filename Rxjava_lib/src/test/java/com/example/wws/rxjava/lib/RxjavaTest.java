package com.example.wws.rxjava.lib;

import org.junit.Test;
import org.reactivestreams.Publisher;

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
        String[] words = {"Hello", "world"};
//        Observable.fromArray(words)
//                .map(s -> s + "123")
//                .subscribe(System.out::println);

        Disposable disposable = Observable.fromArray(words)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + "123";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        System.out.println(Thread.currentThread().getName());
                    }
                });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disposable.dispose();
    }

    @Test
    public void testflatMap(){
        Observable.just("Hello")
//                .flatMap(toString().toCharArray().forEach((p) -> System.out.println(p))) dev dev
                .flatMap(s -> Observable.fromArray(s))
                .subscribe(s -> System.out.println("result: " + s));
    }

    @Test
    public void testFlowable(){
        Flowable.range(1, 10)
                .observeOn(Schedulers.computation())
                .map(v -> {
                    System.out.println(""+ Thread.currentThread().getName() + System.currentTimeMillis());
                    Thread.sleep(3000);
                    return v * v;
                })
                .blockingSubscribe(v -> {
                    System.out.println(""+ Thread.currentThread().getName() + "  v = " + v + "time:" + System.currentTimeMillis());
                    return;
                });
    }

    @Test
    public void testParallelProcess(){
        Flowable.range(1, 10)
                .flatMap(v -> Flowable.just(v)
                .subscribeOn(Schedulers.computation())
                .map(w -> w * w))
                .blockingSubscribe(System.out::println);
    }
    @Test
    public void testParallelProcess2(){
        Flowable.range(1, 10)
//                .parallel()
//                .runOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation())
                .concatMapEager( v -> Flowable.just(v * v))
//                .sequential()
                .blockingSubscribe(System.out::println);
    }

    @Test
    public void testInterval(){
        Disposable disposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("do on next");
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Long aLong) throws Exception {
                        System.out.println("flat map");
                        return null;
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                    }
                });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disposable.dispose();


    }

}