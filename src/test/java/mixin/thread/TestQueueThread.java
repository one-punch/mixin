package mixin.thread;

public class TestQueueThread {
	
	public static void main(String[] args) {
		QueueThread qt = QueueThread.newInstace();
		qt.start();
		for(int i=0;i<10;i++){
			Msg msg = new Msg();
			msg.setCreated(System.currentTimeMillis()+i*888);
			msg.setExpired(20000L);
			msg.setName("msgQueue"+i);
			qt.add(msg);
		} 
	}	
}
