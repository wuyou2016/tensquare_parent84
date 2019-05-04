package com.wyg.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class  Data{
         volatile int  number=0;//保证其他线程对此变量可见
         public  void add(){
             this.number=10;
         }
         //synchronized
    AtomicInteger aaa=new AtomicInteger();


         public  void add2(){
             aaa.getAndIncrement();
            this.number++;//这不是原子性操作
        }
        }
public class Volatitle {
    public static void main(String[] args) {
    }
        //可见性
        public static  void my(){
            Data data = new Data();
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "come in");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data.add();
                System.out.println(Thread.currentThread().getName() + "数据改成了" + data.number);
            }, "线程1").start();
            while (data.number == 0) {
            }
            System.out.println("我是主线程");
        }

}
