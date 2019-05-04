package com.wyg.proconsumer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData{
    private  int number=0;
    private Lock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();
    public  void increment()throws  Exception{
        lock.lock();//哪些步骤需要同步
        //lock.tryLock(2, TimeUnit.SECONDS);
        try {
            //1判断 多线程最重要的是判断 有商品 消费后再生产
            while(number!=0){//换成IF多线程就出虚假唤醒
                //等待 不能生产
                condition.await();
            }
            //干活  等于0才能加
            number++;
            System.out.println(Thread.currentThread().getName()+" "+number);
            //通知唤醒
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public  void decrement()throws  Exception{
        lock.lock();
        try {
            //1判断 多线程最重要的是判断 等于0 表示没商品了 只能等
            while (number==0){
                //等待 不能生产
                condition.await();
            }
            //干活  不能等于0才能减
            number--;
            System.out.println(Thread.currentThread().getName()+" "+number);
            //通知唤醒
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * li:一个初始值为0的变量 两个线程对其交互操作，一个加1 一个减1 来5轮
 * 1 线程  操作（方法）   资源
 * 2 判断  业务           通知唤醒
 * 3 放虚假唤醒  用while(循环) 不要用if（一个消费者线程没事 多个就出问题）
 * 如何保证同步:锁 自动的synchronized  手动的lock
 */
public class ProdConsumer {
    public static void main(String[] args) {
         ShareData shareData=new ShareData();
        new Thread(()->{
            for (int i = 1; i <=5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        },"生产者").start();
        new Thread(()->{
            for (int i = 1; i <=5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"消费者").start();
        new Thread(()->{
            for (int i = 1; i <=5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        },"生产者22").start();
        new Thread(()->{
            for (int i = 1; i <=5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"消费者33").start();


    }
}
