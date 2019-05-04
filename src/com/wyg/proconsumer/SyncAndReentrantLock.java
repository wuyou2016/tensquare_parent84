package com.wyg.proconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class  ShareResource{
    private  int number=1;//a 1 b 2 c 3    a  b  c三线程标志字段
    private Lock lock=new ReentrantLock();
    private Condition c1=lock.newCondition();//一把锁 多个条件 做到精确唤醒
    private Condition c2=lock.newCondition();
    //判断
    public  void aprint(){
        lock.lock();
        try {
            //1判断 多线程最重要的是判断
            while (number!=1){
                c1.await();
            }
            //标志字段更改  这是等待还是唤醒的条件
            number=2;
            //指定唤醒
            c2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public  void bprint(){
        lock.lock();
        try {
            //1判断 多线程最重要的是判断
            while (number!=2){
                c2.await();
            }
            //标志字段更改
            number=1;
            //指定唤醒  形成闭环
            c1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
public class SyncAndReentrantLock {
}
