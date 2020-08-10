package com.lsl.zkLock;

public interface Lock {

    public void getLock() throws Exception;

    public  void unlock();
}
