package com.wyg.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABADemo {
    static AtomicReference<Integer> atomicReference=new AtomicReference<>(100);
    //带版本号（类似时间戳） 防止ABA问题 结果一样 但是中间有操作
    static AtomicStampedReference<Integer> ar=new AtomicStampedReference<>(100,1);
    public static void main(String[] args) {
        new Thread(()->{
            atomicReference.compareAndSet(100,101);
            atomicReference.compareAndSet(101,100);
        },"t1").start();
        new Thread(()->{
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(atomicReference.compareAndSet(100,2019)+"\t"+atomicReference.get());
        },"t2").start();
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("========ABA问题处理：版本号=======");
        new Thread(()->{
            int stamp=ar.getStamp();
            System.out.println(Thread.currentThread().getName()+"第一次版本号是"+stamp);
            try { TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) { e.printStackTrace(); }
            //第三个参数用 ar.getStamp()  其实是不妥的 因为这肯定和当前的相等 这块只是为了演示ABA问题才这样写
            ar.compareAndSet(100,101,ar.getStamp(),ar.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"第二次版本号是"+ar.getStamp());
            ar.compareAndSet(101,100,ar.getStamp(),ar.getStamp()+1);
            System.out.println(Thread.currentThread().getName()+"第三次版本号是"+ar.getStamp());
        },"t3").start();
        new Thread(()->{
            int stamp=ar.getStamp();
            System.out.println(Thread.currentThread().getName()+"第一次版本号是"+stamp);
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            //要对比 你每次改前都需要重新拿版本号的  第三个参数是是否和自己修改前拿到的版本号一样
             Boolean result=ar.compareAndSet(100,2019,stamp,stamp+1);
            System.out.println(ar.getStamp()+" 是否修改成功"+result+"  "+Thread.currentThread().getName()+"实际最新值是"+ar.getReference());
        },"t4").start();


















    }
}
