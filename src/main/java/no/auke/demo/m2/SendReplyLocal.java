package no.auke.demo.m2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendReplyLocal {

	Object waitfor=new Object();
	public void run(final String namespace, final String dir, final int port, final String userid) {
		
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				 SendReply sender = new SendReply();
				 sender.run(namespace, dir, userid+"A", userid+"B", port, true);
				 

				
			}});
		
		
		// wait a little while before start next
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				 SendReply sender = new SendReply();
				 sender.run(namespace, dir, userid+"B", userid+"A", (port>0?port + 1:0), false);
				
			}});
		
		synchronized(waitfor){

			try {
				waitfor.wait();
			} catch (InterruptedException e) {
			}
			
		}
		
		executor.shutdownNow();
				
	}
	
}
