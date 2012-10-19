package no.auke.demo.m2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendReply3Services {

	Object waitfor=new Object();
	public void run(final String namespace, final String dir, final String userid, final String useridRemote, int port, final boolean isreading, final int trialsize) {
		
		
		ExecutorService executor = Executors.newCachedThreadPool();
		for(int x=1;x<=3;x++){

			final int i=x;
			final int port_start = port + (x-1);
			executor.execute(new Runnable(){

				@Override
				public void run() {
					
					 SendReply sender = new SendReply();
					 sender.run(namespace, dir, userid+String.valueOf(i), useridRemote+String.valueOf(i), port_start, isreading, trialsize);
					 

					
				}});
			
			// wait a little while before start next
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			
		}
		
		
		synchronized(waitfor){

			try {
				waitfor.wait();
			} catch (InterruptedException e) {
			}
			
		}
		
		executor.shutdownNow();
				
	}
	
}
