package com.lsl.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZKWatcherGetData {
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
    public void watcherGetData() throws KeeperException, InterruptedException {
         //arg1:节点的路径
        //arg2：使用连接对象中的watcher
        zooKeeper.getData("/watcher2",true,null);
       Thread.sleep(50000);
        System.out.println("结束");
    }

    @Test
    public void watcherGetData2() throws KeeperException, InterruptedException {
        //arg1:节点的路径
        //arg2：自定义watcher对象
        zooKeeper.getData("/watcher2", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher");
                System.out.println("path="+watchedEvent.getPath());
                System.out.println("eventType="+watchedEvent.getType());
            }
        },null);
        Thread.sleep(50000);
        System.out.println("结束");
    }


    @Test
    public void watcherGetData3() throws KeeperException, InterruptedException {
         //一次性
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println("自定义watcher");
                    System.out.println("path="+watchedEvent.getPath());
                    System.out.println("eventType="+watchedEvent.getType());

                    if(watchedEvent.getType()==Event.EventType.NodeChildrenChanged) {
                        zooKeeper.getData("/watcher", this, null);
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        zooKeeper.getData("/watcher", watcher,null);
        Thread.sleep(50000);
        System.out.println("结束");
    }



    @Test
    public void watcherGetData4() throws KeeperException, InterruptedException {
        //注册多个监听器对象
        zooKeeper.getData("/watcher", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
             try {
                System.out.println("1");
                System.out.println("path="+watchedEvent.getPath());
                System.out.println("eventType="+watchedEvent.getType());

                if(watchedEvent.getType()==Event.EventType.NodeChildrenChanged) {
                    zooKeeper.getData("/watcher", this, null);
                }
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    },null);

        zooKeeper.getData("/watcher", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println("2");
                    System.out.println("path="+watchedEvent.getPath());
                    System.out.println("eventType="+watchedEvent.getType());

                    if(watchedEvent.getType()==Event.EventType.NodeChildrenChanged) {
                        zooKeeper.getData("/watcher", this, null);
                    }
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },null);
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
