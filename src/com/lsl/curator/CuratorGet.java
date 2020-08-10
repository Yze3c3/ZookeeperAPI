package com.lsl.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorGet {
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
                .sessionTimeoutMs(5000)
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
    public void get1() throws Exception {
        //读取节点数据
        byte[] bytes = client.getData()
                //节点的路径
                .forPath("/node1");
        System.out.println(new String(bytes));
    }

    @Test
    public void get2() throws Exception {
        //读取数据时读取节点的属性
        Stat stat = new Stat();
        byte[] bytes = client.getData()
                //读取属性
                .storingStatIn(stat)
                .forPath("/node1");
        System.out.println(new String(bytes));
        System.out.println(stat.getVersion());
    }

    @Test
    public void get3() throws Exception {
        //异步方式读取节点的数据
        client.getData()
                //读取属性
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        //节点的路径
                        System.out.println(curatorEvent.getPath());
                        //事件类型
                        System.out.println(curatorEvent.getType());
                        //数据
                        System.out.println(new String(curatorEvent.getData()));
                    }
                })
                .forPath("/node1");
        Thread.sleep(5000);
        System.out.println("结束");
    }


    @Test
    public void create1() throws Exception{
        //新增节点
        client.create()
                //节点的类型
                .withMode(CreateMode.EPHEMERAL)
                //节点的权限列表 world:anyone:cdrwa
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                //arg1:节点的路径
                //arg2:节点的数据
                .forPath("/curator","curatorCreate".getBytes());
        System.out.println("结束");
    }
}










