package com.lsl.zookeeper;

import org.apache.zookeeper.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKDelete {
    private   String IP="123.57.219.175:2181";
    private ZooKeeper zooKeeper;

    @Before
    public void before()throws Exception{
        CountDownLatch countDownLatch =new CountDownLatch(1);

        zooKeeper=new ZooKeeper(IP, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState()==Event.KeeperState.SyncConnected)
                    System.out.println("连接创建对象");
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }
    @After
    public void after()throws Exception{
        zooKeeper.close();
    }
    @Test
    public void delete1() throws KeeperException, InterruptedException {
        zooKeeper.delete("/delete/node1",-1);
    }

    @Test
    public void delete2() throws KeeperException, InterruptedException {
        zooKeeper.delete("/delete/node2", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int i, String s, Object o) {
                System.out.println(i);
                System.out.println(s);
                System.out.println(o);
            }
        },"I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
