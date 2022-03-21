package com.bianjiahao.config;

import org.apache.zookeeper.ZooKeeper;
import java.util.concurrent.CountDownLatch;

/**
 * 创建ZooKeeper客户端
 * @author Obito
 */
public class ZooKeeperUtils {

    private  static ZooKeeper zk;

    private static final String ADDRESS = "1.117.150.217:2181/testConfig";

    private static DefaultWatcher watch = new DefaultWatcher();

    private static CountDownLatch init  =  new CountDownLatch(1);

    public static ZooKeeper getZookeeperClient(){

        try {
            zk = new ZooKeeper(ADDRESS,1000,watch);
            watch.setInit(init);
            init.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return zk;
    }
}
