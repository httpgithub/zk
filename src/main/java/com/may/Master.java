package com.may;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class Master implements Watcher {
	private ZooKeeper zk = null;
	private String hostPort;
	 private Random random = new Random(this.hashCode());
	public Master(String hostPort) {
		this.hostPort = hostPort;
	}

	public void startZK() throws IOException {
		zk = new ZooKeeper(hostPort, 15000, this);
	}

	public void stopZK() throws InterruptedException {
		zk.close();
	}

	@Override
	public void process(WatchedEvent arg0) {
		System.out.println(arg0);
	}
	
	public void runForMaster() throws KeeperException, InterruptedException{
		String serverId = Integer.toHexString(random.nextInt());
		zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		Master m = new Master("127.0.0.1:2181");
		m.startZK();
		// wait for a bit
		Thread.sleep(60000);
		m.stopZK();
	}
}
