package com.lsl.zkLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;


import java.util.concurrent.CountDownLatch;

public class curatorTemPlateLock extends  AbstractTemplateLock {

    private static final String ipAddress = "123.57.219.175:2181";

    private static final int timeOut = 5000;

    private  CountDownLatch countDownLatch = null;


    private static final String localPath = "/localPath001";

    private Boolean flag = true;
    //创建连接对象
    CuratorFramework client=CuratorFrameworkFactory.builder()
            //IP地址端口号
            .connectString(ipAddress)
            //会话超时时间
            .sessionTimeoutMs(5000)
            //重连机制
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            //构建连接对象
            .build();

    @Override
    protected boolean tryLock() throws Exception {
        //新增节点
        try {

            client.start();
            client.create()
                    //节点的类型
                    .withMode(CreateMode.EPHEMERAL)
                    //节点的权限列表 world:anyone:cdrwa
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    //arg1:节点的路径
                    //arg2:节点的数据
                    .forPath(localPath, ("localPath"+Thread.currentThread().getName()).getBytes());
        } catch (Exception e) {
            flag = false;
            return flag;
        }

        return flag;
    }



    @Override
    protected void waitLock() {
        try {
            client.getData().usingWatcher(new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getType()== Event.EventType.NodeDeleted){
                        if(countDownLatch!=null) {
                            countDownLatch.countDown();
                        }
                    }
                }
            }).forPath(localPath);
        } catch (Exception e) {
            e.printStackTrace();
        }



        if(countDownLatch==null){
            countDownLatch=new CountDownLatch(1);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void unImpLock() {
        if(client!=null) {
            try {
                client.delete().guaranteed().forPath(localPath);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
