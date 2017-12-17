package edu.yu.distributed.Calculator;

import java.io.IOException;
import java.util.Scanner;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Tester_UI {
	
	
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		String input;
		ZooKeeper zk = null;
		String path;
		String hostport;
		
		System.out.println("Please enter the host and port as in the following manner:"
				+ "'Host:Port'\n");
		hostport = sc.nextLine();
		System.out.println("Enter path of znode to be edited:");
		path = sc.nextLine();
		try {
			zk = new ZooKeeper(hostport, 3500, null);
			createNode(zk, path);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Enter Zookeeper request");
		while(sc.hasNextLine()){
			input = sc.nextLine();
			if(input.toLowerCase().equals("close")||
			   input.toLowerCase().equals("exit")) {
				break;
			}
			if (input.toLowerCase().substring(0,4).equals("set ")) {
				setData(zk, path, input.substring(4,input.length()));
				System.out.println("UI: Data Changed!");
				}
			else if(input.toLowerCase().substring(0, 7).equals("create ")) {
				createNode(zk, input.substring(7, input.length()));
			}
			
			else {
				System.out.print("Syntax error, please try again.");
					}
			System.out.println("Enter Zookeeper request");
			}
			sc.close();
		}
	
	private static void createNode(ZooKeeper zk, String path) {
		try {
			if (zk.exists(path, false) == null) {
				zk.create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			}
			else {
				setData(zk, path, "");
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void setData(ZooKeeper zk, String path, String data) {
		try {
			Stat stat = zk.exists(path, false);
			zk.setData(path, data.getBytes(), stat.getVersion());
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
