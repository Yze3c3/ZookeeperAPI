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

public class ZKSet {
    private   String IP="123.57.219.175:2181";
    private  ZooKeeper zooKeeper;

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
    public void set1() throws  Exception{
        //arg1:节点路径
        //arg2:修改的数据
        //arg3:数据版本号 -1代表版本号不参与更新
        Stat stat = zooKeeper.setData("/set/node1", "node11".getBytes(), -1);

        //打印版本信息
        System.out.println(stat.getVersion());

    }

    @Test
    public void set2() throws  Exception{
      zooKeeper.setData("/set/node1", "node13".getBytes(), -1, new AsyncCallback.StatCallback() {
          @Override
          public void processResult(int i, String path, Object ctx,Stat stat) {
              //0 代表修改成功
              System.out.println(i);
              //节点的路径
              System.out.println(path);
              //上下文参数对象
              System.out.println(ctx);
          }
      },"I am Context");
      Thread.sleep(10000);
        System.out.println("结束");
    }
}
