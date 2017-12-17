package edu.yu.distributed.Calculator;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;

public class WorkerEventHandler implements Runnable{

	
	String childPath;
	WorkerData data;
	
	public WorkerEventHandler(WorkerData data, String childPath) {
		this.childPath = childPath;
		this.data = data;
	}
	public void run() {
		//EVENT HANDLER LOGIC
		
	}

}
