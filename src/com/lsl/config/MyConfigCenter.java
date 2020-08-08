package com.lsl.config;

import com.lsl.zookeeper.ZKConnectionWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class MyConfigCenter implements Watcher {
    private   String IP="123.57.219.175:2181";

    //计数器对象
    CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper=null;

    //用于本地化存储配置信息
    private String url;

    private String username;

    private String password;


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

                        //当配置信息发生变化时
                    }else if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
                        initValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

         //构造方法
          public MyConfigCenter(){
             //调用initValue()方法可以将从zookeeper上获取的数据初始化到成员变量中去
            initValue();
          }

        //连接zookeeper服务器，读取配置信息
         public void initValue(){
           try {
               //创建连接对象
               //当电脑反应过慢时，需要增加seeionTimeout时间，否则就会报KeeperErrorCode = ConnectionLoss for /config/url 错误
            zooKeeper=new ZooKeeper(IP,100000,this);

            //阻塞线程，等待连接的创建成功
            countDownLatch.await();
            //读取配置信息
            this.url=new String(zooKeeper.getData("/config/url",true,null));
            this.username=new String(zooKeeper.getData("/config/username",true,null));
            this.password=new String(zooKeeper.getData("/config/password",true,null));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            try {
                MyConfigCenter myConfigCenter = new MyConfigCenter();
                for (int i = 0; i <20 ; i++) {
                    Thread.sleep(5000);
                    System.out.println("url: "+myConfigCenter.getUrl());
                    System.out.println("username: "+myConfigCenter.getUsername());
                    System.out.println("password: "+myConfigCenter.getPassword());
                    System.out.println("#########################################");
                }
            } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

