package com.lsl.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorExists {
    String  IP="123.57.219.175:2181,123.57.219.175:2182,123.57.219.175:2183";
    CuratorFramework client;
    @Before
    public void before(){
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //创建连接对象
        client = CuratorFrameworkFactory.builder()
                //IP地址端口号
                .connectString(IP)
                //会话超时时间
                .sessionTimeoutMs(1000000)
                //重连机制
                .retryPolicy(retryPolicy)
                //构建连接对象
                .build();
        client.start();
    }
    @After
    public void after(){
        client.close();
    }

    @Test
    public void exists1() throws Exception{
        //判断节点是否存在
        Stat stat = client.checkExists()
                .forPath("/get");
        System.out.println(stat);
    }
    @Test
    public void exists2() throws Exception{
        //异步方式判断节点是否存在
        Stat stat = client.checkExists()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getPath());
                        System.out.println(curatorEvent.getType());
                        System.out.println(curatorEvent.getStat().getVersion());
                    }
                })
                .forPath("/node1");
        Thread.sleep(5000);
        System.out.println("结束");
    }
}
