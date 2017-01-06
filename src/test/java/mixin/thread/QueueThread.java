package mixin.thread;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class QueueThread extends Thread{
	private static Queue<Msg> msgQueue;
	private static QueueThread instance;
	
	private QueueThread(){
		msgQueue = new LinkedList<>();
	}
	public static QueueThread newInstace(){
		if(instance==null){
			synchronized (QueueThread.class) {
				if(instance==null){
					instance = new QueueThread();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void run() {
		while(true){
			if(msgQueue.size()<=0){
				try {
					System.out.println("sleep:"+new Date().toString());
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				Msg msg = msgQueue.poll();
				try {
					Long delay = msg.getExpired()-(System.currentTimeMillis()-msg.getCreated());
					System.out.println("睡眠时长:"+delay+"---------current:"+new Date().toString()+"-----delay:"+delay+"------create:"+new Date(msg.getCreated()));
					Thread.sleep(delay);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				System.out.println(msg.toString());
			}
		}
	}
	
	public void add(Msg msg){
		synchronized (instance) {
			msgQueue.add(msg);
		}
	}
}

class Msg{
	private String name;
	private Long created;
	private Long expired;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCreated() {
		return created;
	}
	public void setCreated(Long created) {
		this.created = created;
	}
	public Long getExpired() {
		return expired;
	}
	public void setExpired(Long expired) {
		this.expired = expired;
	}
	@Override
	public String toString() {
		return "Msg [name=" + name + ", created=" + new Date(created).toString() + ", expired=" + expired + "]";
	}
}

