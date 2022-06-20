package com.ricemarch;

public class ReadStackLog {
    public static void main(String[] args) {
        new Thread(new TimeWaiting(), "TimeWaitingThread").start();
        new Thread(new Waiting(), "WaitingThread").start();
        // 使用两个Blocked线程，一个获取锁成功，另一个被阻塞
        new Thread(new Blocked(), "BlockedThread-1").start();
        new Thread(new Blocked(), "BlockedThread-2").start();
    }
}

// 该线程不断地进行睡眠
class TimeWaiting implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Waiting implements Runnable {

    @Override
    public void run() {
        while (true) {
            synchronized (Waiting.class) {
                try {
                    // object 的 wait 方法
                    Waiting.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// 该线程在Blocked.class 实例上加锁后 不会释放该锁
class Blocked implements Runnable {
    @Override
    public void run() {
        synchronized (Blocked.class) {
            while (true) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
