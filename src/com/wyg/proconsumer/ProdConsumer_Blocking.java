package com.wyg.proconsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Resource{
    private volatile boolean flag=true;//线程间要可见  默认开启 进行生产消费
    private AtomicInteger atomicInteger=new AtomicInteger();//原子性
    BlockingQueue<String> blockingQueue=null;//数据存哪 注意是接口
    public  Resource(BlockingQueue<String> blockingQueue){//构造注入
        this.blockingQueue=blockingQueue;
        System.out.println(blockingQueue.getClass().getName());//落地要具体
    }

    public void prod() throws  Exception {
        String data=null;
        Boolean retValue;
        while (flag){
            data=atomicInteger.incrementAndGet()+"";
            retValue=blockingQueue.offer(data,2L, TimeUnit.SECONDS);
            if(retValue){
                System.out.println(Thread.currentThread().getName()+" 插入队列"+data+"成功");
            }else{
                System.out.println(Thread.currentThread().getName()+" 插入队列"+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"flag=false,停止生产");
    }
    //队列有东西会自己去取 注意业务是在循环里的  消费从队列取就行
    public void consumer() throws  Exception {
        String result=null;
        while (flag){
            result=blockingQueue.poll(2L, TimeUnit.SECONDS);
            if(null==result||result.equalsIgnoreCase("")){
                flag=false;
                System.out.println(Thread.currentThread().getName()+"超过2秒未得到消费的资源 退出");
                return;
            }
            System.out.println(Thread.currentThread().getName()+"  消费队列"+result+"成功");
        }
    }
    public  void stop()throws  Exception{
        this.flag=false;
    }
}
/**
 * volatile cas atomicInteger BlockingQueue 线程交互 原子引用（某个类变成原子类）
 * 各知识点整合  阻塞队列  生消
 * 多线程下不要i++等非原子性操作
 * 用接口 可多实现
 * 如何注入：get  构造方法 传接口
 * 队列版生消：不用关心什么时候需要阻塞线程 什么时候唤醒 这些blockingqueue处理  MQ基本就是这样的思想
 */
public class ProdConsumer_Blocking {
    public static void main(String[] args) throws Exception {
        //资源  多生产 多消费呢  池
        Resource resource=new Resource(new ArrayBlockingQueue<>(4));
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"生产线程启动");
            try {
                resource.prod();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"生产").start();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"消费线程启动");
            try {
                resource.consumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"消费").start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resource.stop();
    }

}
