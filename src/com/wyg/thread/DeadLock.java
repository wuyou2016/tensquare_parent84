package com.wyg.thread;

import java.util.concurrent.TimeUnit;

class HoldLockThread  implements Runnable {
     private  String lockA;
     private  String lockB;
    public HoldLockThread(String lockA, String lockB) {
        this.lockA=lockA;
        this.lockB=lockB;
    }
    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+"自己持有"+lockA+"尝试获取锁"+lockB);
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }//保证先拿到A锁
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+"自己持有"+lockB+"尝试获取锁"+lockA);
            }
        }

    }
}

/**
 * 死锁（有几个充分条件（唯一 互斥等） 破坏其中一个就好）：两个或两个以上的线程在执行过程中
 * 因争夺资源而造成的相互等待的现象  若无外力干涉无法推进下去
 * 如何定位：工具
 * 命令 jps -l  查看进程
 *      jstack 进程号  查看此进程情况  堆栈信息
 */
public class DeadLock {
    public static void main(String[] args) {
        String lockA="lockA";
        String lockB="lockB";
        new Thread(new HoldLockThread(lockA,lockB),"threadA").start();
        new Thread(new HoldLockThread(lockB,lockA),"threadB").start();
    }


}
