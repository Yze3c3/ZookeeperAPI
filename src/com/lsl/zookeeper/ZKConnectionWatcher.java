package com.lsl.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKConnectionWatcher implements Watcher {
    static ZooKeeper zooKeeper;
    static CountDownLatch countDownLatch =new CountDownLatch(1);
    @Override
    public void process(WatchedEvent watchedEvent) {
     if(watchedEvent.getType()==Event.EventType.None){
         if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
             countDownLatch.countDown();
             System.out.println("连接创建成功！");
         }else if(watchedEvent.getState()==Event.KeeperState.Disconnected){
             System.out.println("断开连接");
         }else if(watchedEvent.getState()==Event.KeeperState.Expired){
             System.out.println("会话超时");
         }else if(watchedEvent.getState()==Event.KeeperState.AuthFailed){
             System.out.println("身份认证失败");
         }
       }
    }

    public static void main(String[] args) {
        try {
            zooKeeper=new ZooKeeper("123.57.219.175:2181",5000,new ZKConnectionWatcher());
            countDownLatch.await();
            System.out.println(zooKeeper.getSessionId());
            zooKeeper.close();
            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
