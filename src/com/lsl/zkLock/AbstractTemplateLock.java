package com.lsl.zkLock;

import org.apache.zookeeper.KeeperException;

abstract class AbstractTemplateLock implements Lock {

    @Override
    public void getLock() throws Exception {
       if(tryLock()){
           System.out.println(">>>"  + Thread.currentThread().getName()+",获取锁成功");
       }else {
           waitLock();
           getLock();
       }
    }


    protected abstract boolean tryLock() throws KeeperException, InterruptedException, Exception;


    protected abstract void waitLock();


    protected abstract void unImpLock();




    @Override
    public void unlock() {
        unImpLock();
    }
}
