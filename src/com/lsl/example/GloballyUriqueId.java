package com.lsl.example;

import com.lsl.zookeeper.ZKConnectionWatcher;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class GloballyUriqueId implements Watcher {
    private   String IP="123.57.219.175:2181";

    //计数器对象
    CountDownLatch countDownLatch = new CountDownLatch(1);

    //用户生成序号的节点
    String defaultPath="/uniqueId";

    private ZooKeeper zooKeeper=null;
    @Override
    public void process(WatchedEvent watchedEvent) {
        //捕获事件动态
        try {
            if(watchedEvent.getType()==Event.EventType.None) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                    System.out.println("连接创建成功！");
                } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                    System.out.println("断开连接");
                } else if (watchedEvent.getState() == Event.KeeperState.Expired) {
                    System.out.println("会话超时");
                    //超时后服务器已经将连接释放，需要重新连接服务器
                    zooKeeper = new ZooKeeper(IP, 6000, new ZKConnectionWatcher());
                } else if (watchedEvent.getState() == Event.KeeperState.AuthFailed) {
                    System.out.println("验证失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //构造方法
    public  GloballyUriqueId(){
        try {
            //打开连接对象
            zooKeeper = new ZooKeeper(IP,50000,this);
            //阻塞线程，等待连接的创建成功
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //生成id的方法
    public  String getUniqueId(){
        String ids="";
        try {
            //创建临时有序节点
            ids = zooKeeper.create(defaultPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ids.substring(9);
    }

    public static void main(String[] args) {
        GloballyUriqueId uriqueId = new GloballyUriqueId();
        for (int i = 0; i <= 5 ; i++) {
            String id = uriqueId.getUniqueId();
            System.out.println(id);
        }
    }
}
