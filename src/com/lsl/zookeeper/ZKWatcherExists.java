package com.lsl.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKWatcherExists {
    private   String IP="123.57.219.175:2181";
    private ZooKeeper zooKeeper=null;

    @Before
    public void before()throws Exception{
        CountDownLatch countDownLatch =new CountDownLatch(1);

        zooKeeper=new ZooKeeper(IP, 6000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("连接对象的参数");
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
                System.out.println("path="+watchedEvent.getPath());
                System.out.println("eventType="+watchedEvent.getType());
            }
        });
        countDownLatch.await();
    }
    @After
    public void after()throws Exception{
        zooKeeper.close();
    }


    @Test
    public void watcherExists1() throws KeeperException, InterruptedException {
        //args1:节点的路径
        //args2:使用连接对象中的watcher
        zooKeeper.exists("/watcher",true);
        Thread.sleep(200000);
        System.out.println("结束");
    }

    @Test
    public void watcherExists2() throws KeeperException, InterruptedException {
        //args1:节点的路径
        //args2:使用连接对象中的watcher
        zooKeeper.exists("/watcher2", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher2");
                System.out.println("path="+watchedEvent.getPath());
                System.out.println("eventType="+watchedEvent.getType());
            }
        });
                 Thread.sleep(100000);
                 System.out.println("结束");
    }


    @Test
    public void watcherExists3() throws KeeperException, InterruptedException {
         //watcher一次性
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher");
                System.out.println("path="+watchedEvent.getPath());
                System.out.println("eventType="+watchedEvent.getType());
                try {
                    zooKeeper.exists("/watcher3",this);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        zooKeeper.exists("/watcher3",watcher);
        Thread.sleep(80000);
        System.out.println("结束");
    }

    @Test
    public void watcherExists4() throws KeeperException, InterruptedException {
                    //注册多个监听器对象
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("1");
                System.out.println("path="+watchedEvent.getPath());
                System.out.println("eventType="+watchedEvent.getType());
            }
        });
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("2");
                System.out.println("path="+watchedEvent.getPath());
                System.out.println("eventType="+watchedEvent.getType());
            }
        });
        Thread.sleep(80000);
        System.out.println("结束");
    }
}
