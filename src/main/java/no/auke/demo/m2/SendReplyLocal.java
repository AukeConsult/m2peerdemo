package no.auke.demo.m2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendReplyLocal {

	public void run(final String namespace, final String dir, final String userid) {
		
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				 SendReply sender = new SendReply();
				 sender.run(namespace, dir, userid+"A", userid+"B");
				
			}});

		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				 SendReply sender = new SendReply();
				 sender.run(namespace, dir, userid+"B", userid+"A");
				
			}});
		
		
		try {
			this.wait();
		} catch (InterruptedException e) {
		}
		
		executor.shutdownNow();
				
	}
	
}
