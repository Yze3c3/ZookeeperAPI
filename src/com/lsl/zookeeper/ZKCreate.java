package com.lsl.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZKCreate {
    private  static final String IP="123.57.219.175:2181";
    private ZooKeeper zooKeeper;
    @Before
    public void before() throws  Exception{
        CountDownLatch countDownLatch=new CountDownLatch(1);
       zooKeeper= new ZooKeeper(IP, 5000, new Watcher() {
           @Override
           public void process(WatchedEvent watchedEvent) {
               if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
                   System.out.println("连接创建成功");
               countDownLatch.countDown();
           }
       }
        });
        countDownLatch.await();
        System.out.println(zooKeeper.getSessionId());
    }

   @After
   public void after() throws Exception {
       zooKeeper.close();
   }

           @Test
           public void create1() throws Exception {
               //arg1:节点的路径
             //arg2:节点的数据
            //arg3:权限列表 world:anyone:cdrwa
           // arg4:节点的类型 持久化节点
        zooKeeper.create("/create/node1","node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
           }


    @Test
    public void create2() throws Exception {
        //arg1:节点的路径
        //arg2:节点的数据
        //arg3:权限列表 world:anyone:cdrwa
        // arg4:节点的类型 持久化节点
zooKeeper.create("/create/node2","node2".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
       }
    @Test
    public void create3() throws Exception {
        //world授权模式
        //权限列表
        List<ACL> acls=new ArrayList<ACL>();
        //授权模式和授权对象
        Id id = new Id("world","anyone");
        //权限设置
        acls.add(new ACL(ZooDefs.Perms.READ,id));
        acls.add(new ACL(ZooDefs.Perms.WRITE,id));
        zooKeeper.create("/create/node3","node3".getBytes(),acls,CreateMode.PERSISTENT);
      }

    @Test
    public void create4() throws Exception {
        //ip授权模式
        //权限列表
        List<ACL> acls=new ArrayList<ACL>();
        //授权模式和授权对象
        Id id = new Id("ip","123.57.219.75");
        //权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL,id));
        zooKeeper.create("/create/node4","node4".getBytes(),acls,CreateMode.PERSISTENT);
     }

    @Test
    public void create5() throws Exception {
        //auth授权模式
        //添加授权用户
zooKeeper.addAuthInfo("digest","liushengligo:12345678".getBytes());
zooKeeper.create("/create/node6","node6".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,CreateMode.PERSISTENT);
      }

    @Test
    public void create6() throws Exception {
        //auth授权模式
        //添加授权用户
        zooKeeper.addAuthInfo("digest","liushengli:12345678".getBytes());
        //权限列表
        List<ACL> acls=new ArrayList<ACL>();
        //授权模式和授权对象
        Id id = new Id("auth","liushengli");
        //权限设置
        acls.add(new ACL(ZooDefs.Perms.READ,id));
        zooKeeper.create("/create/node7","node7".getBytes(),acls,CreateMode.PERSISTENT);
     }

    @Test
    public void create7() throws Exception {
        //digest授权模式
        //权限列表
        List<ACL> acls=new ArrayList<ACL>();
        //授权模式和授权对象
        Id id = new Id("digest","atguigu:qlzQzCLKhBROghkooLvb+Mlwv4A=");
        //权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL,id));
        zooKeeper.create("/create/node8","node8".getBytes(),acls,CreateMode.PERSISTENT);
      }

    @Test
    public void create8() throws Exception {
        // arg4:节点的类型 临时节点
        //Ids.OPEN_ACL_UNSAFE  world:anyone:cdrwa
        String result = zooKeeper.create("/create/node9", "node9".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(result);
      }

    @Test
    public void create9() throws Exception {
        // arg4:节点的类型 持久化顺序节点
        //Ids.OPEN_ACL_UNSAFE  world:anyone:cdrwa
        String result = zooKeeper.create("/create/node10", "node10".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(result);
     }
    @Test
    public void create10() throws Exception {
        // arg4:节点的类型 临时顺序节点
        //Ids.OPEN_ACL_UNSAFE  world:anyone:cdrwa
        String result = zooKeeper.create("/create/node10", "node10".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(result);
      }

    @Test
    public void create11() throws Exception {
        // 异步方式创造节点
        zooKeeper.create("/create/node11", "node11".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT, new AsyncCallback.Create2Callback() {
            @Override
            public void processResult(int i, String path, Object ctx, String name, Stat stat) {
                //0 代表创建成功
                System.out.println(i);
                //节点的路径
                System.out.println(path);
                //节点的路径
                System.out.println(name);
                //上下文参数
                System.out.println(ctx);
            }
        },"I am context");
        Thread.sleep(10000);
        System.out.println("结束");
       }
    }
