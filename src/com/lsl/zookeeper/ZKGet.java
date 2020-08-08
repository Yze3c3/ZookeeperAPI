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

public class ZKGet {
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
    public void get1()throws Exception{
        Stat stat =new Stat();
        byte[] zooKeeperData = zooKeeper.getData("/liushengli/node", false, stat);
        System.out.println(new String(zooKeeperData));
    }
    @Test
    public void get2()throws Exception{

        zooKeeper.getData("/liushengli/node", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println(i);
                System.out.println(s);
                System.out.println(o);
                System.out.println(new String(bytes));
            }
        },"I am Context");
      Thread.sleep(10000);
        System.out.println("结束");
    }
}
