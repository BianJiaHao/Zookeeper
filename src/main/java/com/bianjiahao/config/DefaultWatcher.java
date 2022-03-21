package com.bianjiahao.config;

import lombok.Data;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Obito
 */
@Data
public class DefaultWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(Watcher.class);

    CountDownLatch init;

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            init.countDown();
            logger.info("连接成功！");
        }
    }
}
