package no.auke.demo.m2;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import no.auke.p2p.m2.InitVar;
import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.sockets.ISocketPortListen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MMLocalTest {
	
	public String address="";
	
	Object waitfor=new Object();
	public void run(final String namespace, final String dir, final int port, final String userid) {
		
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				 MMTest sender = new MMTest();
				 sender.run(namespace, dir, userid+"A", userid+"B", port, true);
				 

				
			}});
		
		
		// wait a little while before start next
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				 MMTest sender = new MMTest();
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
