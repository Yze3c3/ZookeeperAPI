package com.lsl.zkLock;

public class testZkLock{
    public static void main(String[] args) {
        for (int i = 0; i <100; i++) {
            new Thread(new OrderService()).start();
        }
    }
}
