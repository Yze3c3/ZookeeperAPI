package com.lsl.zkLock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import java.util.concurrent.CountDownLatch;

public class ZkTemplateImplLock extends  AbstractTemplateLock {

    private static final String ipAddress = "123.57.219.175:2181";

    private static final int timeOut = 50000;

    private  CountDownLatch countDownLatch = null;


    private static final String localPath = "/localPath001";


    ZkClient zkClient= new ZkClient(ipAddress,timeOut);

    @Override
    protected boolean tryLock() throws Exception {
        //新增节点
        try {
            zkClient.createEphemeral(localPath);
            return true;
        } catch (Exception e) {
            return  false;
        }
    }



    @Override
    protected void waitLock() {
        try {
            zkClient.subscribeDataChanges(localPath, new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {

                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    if (countDownLatch != null) {
                        countDownLatch.countDown();
                    }
                }
            });


            if (countDownLatch == null) {
                countDownLatch = new CountDownLatch(1);
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finally {

        }
    }

    @Override
    protected void unImpLock() {
        if(zkClient!=null) {
            try {
                zkClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
