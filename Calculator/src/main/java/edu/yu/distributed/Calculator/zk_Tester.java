package edu.yu.distributed.Calculator;

import java.io.IOException;
import java.util.Scanner;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class zk_Tester implements Watcher, Runnable{
	
	private String filepath;
	private Boolean dead = false;
	ZooKeeper zk = null;
	
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		String hostport, path;
		System.out.println("Please enter the host and port as in the following manner: +"
				+ "/'Host:Port/'");
		hostport = sc.nextLine();
		System.out.println("Enter path of znode to be edited:");
		path = sc.nextLine();
		try {
			zk_Tester tester = new zk_Tester(path, hostport);
			tester.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close();
	}
	
	public zk_Tester(String filepath, String hostport) throws IOException{
		this.filepath = filepath;
		zk = new ZooKeeper(hostport, 3500, this);
		try {
			zk.getData(filepath, this, null);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public zk_Tester(String filepath, String hostport, ZooKeeper zk) throws IOException{
		this.filepath = filepath;
		this.zk = zk;
	}

	public void run() {
		try {
			synchronized(this){
				while (!dead) {
					wait();
				}
			}
		} catch(Exception e) {
			
		}
	}

	public void process(WatchedEvent event) {
		Watcher.Event.KeeperState state = event.getState();
		Watcher.Event.EventType type = event.getType();
		String path = event.getPath();
		if(state == Watcher.Event.KeeperState.Disconnected ||
		   type == Watcher.Event.EventType.NodeDeleted) {//end program
			dead = true;
		}
		else if(type == Watcher.Event.EventType.NodeDataChanged) {
			try {
				byte[] result = zk.getData(path, this, null);
				String stringResult = new String(result);
				System.out.println("File Data Changed to: " + stringResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
