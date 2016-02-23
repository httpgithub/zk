package com.may;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

public class Master implements Watcher {
	private ZooKeeper zk = null;
	private String hostPort;
	 private Random random = new Random(this.hashCode());
	public Master(String hostPort) {
		this.hostPort = hostPort;
	}
	private String serverId = Integer.toHexString(random.nextInt());
	public static boolean isLeader = false;

	public void startZK() throws IOException {
		zk = new ZooKeeper(hostPort, 15000, this);
	}

	public void stopZK() throws InterruptedException {
		zk.close();
	}

	// returns true if there is a master
	 boolean checkMaster() {
		while (true) {
			try {
				Stat stat = new Stat();
				byte data[] = zk.getData("/master", false, stat);
				isLeader = new String(data).equals(serverId);
				return true;
			} catch (KeeperException.NoNodeException e) {
				// no master, so try create again
				return false;
			} catch (KeeperException.ConnectionLossException e) {
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (KeeperException e) {
				e.printStackTrace();
			}
		}
	}
	 AsyncCallback.StringCallback masterCreateCallback = new AsyncCallback.StringCallback() {
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			System.out.print("dddddd"+rc);
			switch (KeeperException.Code.get(rc)) {
				case CONNECTIONLOSS:
					checkMaster();
					break;
				case OK:
					isLeader = true;
					break;
				case NODEEXISTS:
					checkMaster();
					break;
				default:
					isLeader = false;
			}
		}
	};

	@Override
	public void process(WatchedEvent arg0) {
		System.out.println("ppppppppppppppp"+arg0);
	}
	
	public void runForMaster(){
		while(true){
			zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,masterCreateCallback,null);
			isLeader = true;
			break;
		}

	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		Master m = new Master("127.0.0.1:2181");
		m.startZK();
		m.runForMaster();
		if (isLeader) {
			System.out.println("I'm the leader");
			// wait for a bit
			Thread.sleep(60000);
		} else {
			System.out.println("Someone else is the leader");
		}
		m.stopZK();
	}
}
