package com.wyg.atomic;

import java.util.concurrent.atomic.AtomicReference;

class  User{
    String userName;
    int age;
    public User(String name, int age) {
        this.userName=name;
        this.age=age;
    }
}
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User z3=new User("z3",20);
        User l4=new User("l4",30);

        AtomicReference<User> atomicReference=new AtomicReference<>();
        atomicReference.set(z3);
        atomicReference.compareAndSet(z3,l4);
        atomicReference.get().toString();
    }
}
