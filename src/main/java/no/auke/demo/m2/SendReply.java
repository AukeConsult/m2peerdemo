package no.auke.demo.m2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.sockets.ISocketPortListen;

public class SendReply {
	
    private static final Logger logger = LoggerFactory.getLogger(Socket.class);
	
	public void run(String namespace, String dir, String userid, String useridRemote, boolean send) {
		
		//initialize a peerA
		
		logger.info("start for " + userid);
		
		final PeerServer peer = new PeerServer(namespace, InitParam.APPID, InitParam.DEVICEID, dir, "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peer.start(userid);

	    System.out.println("started for " + userid);
	    
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
				byte[] message = new byte[rnd.nextInt(1000000)];
				rnd.nextBytes(message);
				
				
				if(socket.send(useridRemote, 2000, new SimpleMessage(userid,cnt,message).getBytes())){

					cnt++;
					System.out.println("sent to " + useridRemote + " size : " + String.valueOf(message.length));
					
				} else {
					
					System.out.println("can not send to " + useridRemote + " error " + socket.getLastMessage());
					
				}
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				
			}
			
			
		}
	    
	}


}
