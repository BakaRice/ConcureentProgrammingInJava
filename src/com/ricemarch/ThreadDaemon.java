package com.ricemarch;

public class ThreadDaemon {

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonThread(), "Daemon Thread!");
        thread.setDaemon(true); // 设置为守护线程
        thread.start();
        // main 线程退出了
    }

    static class DaemonThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            } finally { // finally 不能保障我们守护线程的最终执行
                System.out.println("FINISH");
            }
        }
    }
}
