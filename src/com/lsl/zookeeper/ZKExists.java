package com.lsl.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKExists {
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
    public void exist1()throws Exception{
        Stat stat = zooKeeper.exists("/exists1", false);
        System.out.println(stat);
    }

    @Test
    public void exist2()throws Exception{
        zooKeeper.exists("/get", false, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int i, String s, Object o, Stat stat) {
                System.out.println(i);
                System.out.println(s);
                System.out.println(o);
                System.out.println(stat);
            }
        },"I am Context");
       Thread.sleep(50000);
        System.out.println("结束");
    }
}
