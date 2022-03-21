package com.bianjiahao.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @Author Obito
 * @Date 2022/3/19 15:56
 */
public class Demo {
    public static void main(String[] args) throws Exception{



        CountDownLatch countDownLatch = new CountDownLatch(1);

        ZooKeeper zooKeeper = new ZooKeeper("1.117.150.217:2181/testConfig", 3000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state = watchedEvent.getState();
                Event.EventType type = watchedEvent.getType();
                String path = watchedEvent.getPath();

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        countDownLatch.countDown();
                        //System.out.println("连接了");
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                    case Closed:
                        break;
                    default:
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                    case DataWatchRemoved:
                        break;
                    case ChildWatchRemoved:
                        break;
                    case PersistentWatchRemoved:
                        break;
                    default:
                        break;
                }
            }

        });

        countDownLatch.await();

        ZooKeeper.States state = zooKeeper.getState();

        switch (state) {
            case CONNECTING:
                System.out.println("连接中");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("连接了");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        //String path = zooKeeper.create("/obito", "obito".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        byte[] data = zooKeeper.getData("/AppConfig", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("get data watch" + watchedEvent.toString());
                try {
                    zooKeeper.getData("/AppConfig",this,new Stat());
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());

        System.out.println(new String(data) + "----------");


       // Stat stat = zooKeeper.setData("/obito", "OrerereO".getBytes(), 0);

        //zooKeeper.setData("/obito","OrerereO".getBytes(),stat.getVersion());

        System.out.println("异步回调开始");
        zooKeeper.getData("/AppConfig", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println("异步回调数据到达");
                System.out.println(o.toString());
                System.out.println(new String(bytes));
            }
        },"abc");
        System.out.println("异步回调结束");


        Thread.sleep(2222222);


    }
}
