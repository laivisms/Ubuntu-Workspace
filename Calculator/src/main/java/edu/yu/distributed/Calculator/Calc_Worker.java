package edu.yu.distributed.Calculator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Calc_Worker implements Watcher, Runnable{
	
	private static final int ZK_TIMEOUT = 3500;
	private static final int POOL_SIZE = 10;
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(POOL_SIZE);
	private ZooKeeper zk;
	private String zkHost, znodePath;
	private Boolean dead = false;
	private WorkerData data = new WorkerData();

	public Calc_Worker(String zkHost, String znodePath) {
		this.zkHost = zkHost;
		this.znodePath = znodePath;
		try {
			zk = new ZooKeeper(zkHost, ZK_TIMEOUT, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		try {
			processList(zk.getChildren(znodePath, true));
			synchronized(this){
				while (!dead) {
					wait();
				}
			}
		} catch(Exception e) {
			
		}
		
	}

	public void process(WatchedEvent event) {
		if(event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
			try {
				processList(zk.getChildren(znodePath, true));
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void processList(List<String> list) {
		for(String path : list) {
			WorkerEventHandler temp = new WorkerEventHandler(data, path);
			scheduler.execute(temp);
		}
		
	}
	
}
