package com.lsl.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZKGetChild {
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
    public void get1() throws Exception{
        List<String> list = zooKeeper.getChildren("/get", false);
        for (String str:list) {
            System.out.println(str);
        }
    }
    @Test
    public void get2() throws Exception{
        zooKeeper.getChildren("/get", false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int i, String s, Object o, List<String> list) {
                System.out.println(i);
                System.out.println(s);
                System.out.println(o);
                for (String str:list) {
                    System.out.println(str);
                }
            }
        },"I am Context");
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
