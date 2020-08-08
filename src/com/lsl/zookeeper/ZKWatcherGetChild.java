package com.lsl.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class  ZKWatcherGetChild {
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
    public void watcherGetChild1() throws KeeperException, InterruptedException {
        zooKeeper.getChildren("/watcher",true);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    @Test
    public void watcherGetChild2() throws KeeperException, InterruptedException {
        zooKeeper.getChildren("/watcher", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                    try {
                        System.out.println("自定义watcher");
                        System.out.println("path="+watchedEvent.getPath());
                        System.out.println("eventType="+watchedEvent.getType());
                        if(watchedEvent.getType()==Event.EventType.NodeChildrenChanged) {
                            zooKeeper.getChildren("watcher", this);
                        }
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        });
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
