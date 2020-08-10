package com.lsl.zkLock;

public class OrderService implements  Runnable{

    private OrderNumberGenerator numberGenetor=new OrderNumberGenerator();

    private Lock  lock =new ZkTemplateImplLock();
    @Override
    public void run() {
      getNumber();
    }

    public void getNumber(){
        try {
            lock.getLock();
            System.out.println(Thread.currentThread().getName()+",获取的number:"+numberGenetor.getNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
