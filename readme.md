# java并发编程

## 辅助资料

### 1.1 java 天然的多线程

在一个进程里可以创建多个线程，这些线程都拥有各自的计数器、堆栈和局部变量等属性，并且能够访问共享的内存变量。处理器在这些线程上高速切换，让使用者感觉到这些线程在同时执行。

[1]main  
主线程

[2]Reference Handler

[3]Finalizer  
JVM垃圾回收相关的内容，此处仅作简单的介绍

1. 只有当开启一轮垃圾收集的时候，才会开启调用finalize方法
2. 他是一个高优先级的守护线程。
3. jvm在垃圾收集的时候，会将失去引用的对象封装到我们的finalizer对象（reference），放入我们的F-queue 队列中，由Finalizer线程执行Finalizer方法

[4]Signal Dispatcher  
信号分发器。 我们通过cmd 发送jstack，传到了jvm进程，这时候信号分发器就要发挥作用了。
[5]Attach Listener  
附加监听器。 简单来说，他是jdk里边一个工具类提供的jvm 进程之间通信的工具。 cmd -- java -version; jvm -- jstack、jmap、dump） 进程间的通信。

开启我们这个线程的两个方式：

- 通过jvm参数开启 -XX:startAttachListener
- 延迟开启:  cmd --> java -version --> JVM适时开启AL线程

[20]Common-Cleaner

[21]Monitor Ctrl-Break  
跟JVM关系不大，idea通过反射的方式，开启一个随着我们运行的jvm线程开启与关闭的一个监听线程

### 1.2 线程的优先级和守护线程

在Java线程中，通过一个整型成员变量priority来控制优先级，优先级的范围从1~10，在线程构建的时候可以通过setPriority(int)方法来修改优先级，默认优先级是5，优先级高的线程分配CPU时间片的数量要多于优先级低的线程。

setPriority 这个方法，他是jvm提供的一个方法，并且能够调用 本地方法 setPriority0，我们发现优先级貌似没有起作用，为什么？

1. 我们现在的计算机都是多核的，t1.t2 哪个cpu处理不好说，由不同的cpu同时提供资源执行
2. 优先级不代表先后顺序，哪怕你的优先级低，也是有可能先拿到我们的cpu时间片的，只不过这个时间片比高优先级的线程的时间片短。优先级针对的是cpu时间片的长短问题
3. 目前工作中，实际项目中，不必要使用 setPriority ,我们现在都是用 hystrix ，sential
   也好，一些开源的信号量控制工具，都能够实现线程资源的合理调度，这个setPriority方法，很难控制，实际的运行环境太复杂。

#### 守护线程

Daemon线程是一种支持型线程，因为它主要被用作程序中后台调度以及支持性工作。这 意味着，当一个Java虚拟机中不存在非Daemon线程的时候，Java虚拟机将会退出。可以通过调 用Thread.setDaemon(true)
将线程设置为Daemon线程。

```java
public final void setDaemon(boolean on){
        checkAccess();
        if(isAlive()){
        //告诉我们 必须要先设置线程是否为守护线程，然后再调用start方法，如果你先调用start方法 isAlive = true
        throw new IllegalThreadStateException();
        }
        daemon=on;
        }
```

### 1.3 线程状态转化 - stack log 解读