package com.bianjiahao.lock;

import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Data
public class WatchCallBackForLock implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback, AsyncCallback.Create2Callback, AsyncCallback.Children2Callback {

    public ZooKeeper zk;
    public String threadName;
    public CountDownLatch countDownLatch = new CountDownLatch(1);
    public String pathName;


    public void tryLock() {
        zk.create("/lock",threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL, this,"create");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock() {
        try {
            zk.delete(pathName,-1);
            System.out.println(threadName + " over work....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {

    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {

    }

    @Override
    public void process(WatchedEvent event) {
        //如果第一个哥们，那个锁释放了，其实只有第二个收到了回调事件！！
        //如果，不是第一个哥们，某一个，挂了，也能造成他后边的收到这个通知，从而让他后边那个跟去watch挂掉这个哥们前边的。。。
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this ,"sdf");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }


    // 创建回调
    @Override
    public void processResult(int i, String s, Object o, String name, Stat stat) {
        if (name != null) {
            System.out.println("线程：" + threadName + "创建了节点" + name);
            pathName = name;
            zk.getChildren("/",false, this,"Children");
        }
    }

    // 获取孩子回调
    @Override
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        System.out.println(threadName+"look locks.....");
        for (String child : list) {
            System.out.println(child);
        }

        Collections.sort(list);
        int index = list.indexOf(pathName.substring(1));
        if (index == 0) {
            System.out.println("我是第一个");
            try {
                zk.setData("/",threadName.getBytes(),-1);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }else {
            zk.exists("/" + list.get(index - 1),this,this,"sss");
        }
    }
}
