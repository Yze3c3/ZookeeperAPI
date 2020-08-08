package com.lsl.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZookeeperConnection {
    public static void main(String[] args) {
       try {
           CountDownLatch countDownLatch=new CountDownLatch(1);
           ZooKeeper zooKeeper= new ZooKeeper("123.57.219.175:2181",
                   5000, new Watcher() {
               @Override
               public void process(WatchedEvent watchedEvent) {
              if(watchedEvent.getState()==Event.KeeperState.SyncConnected)
                  System.out.println("连接创建成功");
                  countDownLatch.countDown();
               }
           });
           countDownLatch.await();
           System.out.println(zooKeeper.getSessionId());
           zooKeeper.close();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
