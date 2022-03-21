package com.bianjiahao.config;

import lombok.Data;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import java.util.concurrent.CountDownLatch;

/**
 * @author admin
 */
@Data
public class WatcherCallBack implements Watcher, AsyncCallback.DataCallback, AsyncCallback.StatCallback {

    private ZooKeeper zooKeeper;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private MyConfig config = new MyConfig();

    public void await() {
        zooKeeper.exists("/AppConfig",this,this,"await");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if (bytes != null) {
            config.setConfig(new String(bytes));
            countDownLatch.countDown();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case NodeCreated:
                System.out.println("节点创建了");
                zooKeeper.getData("/AppConfig",this,this,"get");
                break;
            case NodeDeleted:
                config.setConfig("");
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zooKeeper.getData("/AppConfig",this,this,"get");
                break;
            default:
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat != null) {
            System.out.println(stat + "存在进行回调");
            zooKeeper.getData("/AppConfig",this,this,"get");
        }
    }
}
