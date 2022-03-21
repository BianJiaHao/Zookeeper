package com.bianjiahao.lock;


import com.bianjiahao.config.ZooKeeperUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Obito
 */
public class LockTest {

    public static ZooKeeper zk;

    @Before
    public void getZooKeeperClient() {
        zk = ZooKeeperUtils.getZookeeperClient();
    }

    @Test
    public void testLock() {
        for (int i = 0; i < 10; i++) {
            new Thread(()-> {
                WatchCallBackForLock watchCallBackForLock = new WatchCallBackForLock();
                String threadName = Thread.currentThread().getName();
                watchCallBackForLock.setThreadName(threadName);
                watchCallBackForLock.setZk(zk);

                watchCallBackForLock.tryLock();
                System.out.println(threadName+" working...");
                watchCallBackForLock.unLock();

            }).start();
        }

        while (true) {

        }
    }
}
