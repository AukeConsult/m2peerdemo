package no.auke.demo.m2;

import java.util.Random;

import no.auke.p2p.m2.InitVar;
import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.sockets.ISocketPortListen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MMTest {
	
	public String boostrap="89.221.242.80:8434";
	public String address="";
	
	private static final Logger logger = LoggerFactory.getLogger(Socket.class);
	
	public void run(String namespace, String dir, String userid, String useridRemote, int port, boolean send) {
		
		//prepare
		InitVar.SEND_MIDDLEMAN_REQURIED = true;
		
		logger.info("start for " + userid);
		
		final PeerServer peer = new PeerServer(namespace, InitParam.APPID, InitParam.DEVICEID, dir, boostrap, new SimpleListener(InitParam.DEBUGLEVEL));
	    peer.start(address,port,userid);

		final Socket socket = peer.open(2000, new ISocketPortListen(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				SimpleMessage message = new SimpleMessage(buffer);
				
				System.out.println("message from " + 
									message.getUserid() + 
									" num " + String.valueOf(message.getNum()) + 
									" size : " + String.valueOf(message.getMessage().length));
				
			}			

		}); 
		
		if(send) {
			 
			Random rnd = new Random();
			int cnt=0;
			while(true){
				
				// fill som random data
				byte[] message = new byte[10000];
				rnd.nextBytes(message);
				
				
				if(socket.send(useridRemote, 2000, new SimpleMessage(userid,cnt,message).getBytes())){

					cnt++;
					System.out.println("SUCCESS: sent to " + useridRemote + " size : " + String.valueOf(message.length));
					
				} else {
					
					System.out.println("ERROR: can not send to " + useridRemote + " error " + socket.getLastMessage());
					socket.close(useridRemote);
					
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				
			}
			
			
		}
	    
	}

}
