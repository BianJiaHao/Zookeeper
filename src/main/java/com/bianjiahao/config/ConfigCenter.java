package com.bianjiahao.config;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Obito
 */
public class ConfigCenter {

    private static final Logger logger = LoggerFactory.getLogger(ConfigCenter.class);

    private ZooKeeper zk;

    @Before
    public void  getZooKeeper() {
        zk = ZooKeeperUtils.getZookeeperClient();
    }


    @Test
    public void testConfig() {
        WatcherCallBack watcherCallBack = new WatcherCallBack();
        MyConfig myConfig = new MyConfig();
        watcherCallBack.setConfig(myConfig);
        watcherCallBack.setZooKeeper(zk);

        watcherCallBack.await();

        while (true) {
            if ("".equals(myConfig.getConfig())) {
                System.out.println("conf diu le ......");
                watcherCallBack.await();
            }else {
                System.out.println(myConfig.getConfig());
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
