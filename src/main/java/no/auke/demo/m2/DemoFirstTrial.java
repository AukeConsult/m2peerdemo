package no.auke.demo.m2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketListener;
import no.auke.p2p.m2.sockets.messages.MsgSimple;

public class DemoFirstTrial {
	
    private static final Logger logger = LoggerFactory.getLogger(Socket.class);
    
	private static final String APPID = "demo";
	private static final String DEVICEID = "my device";

	private static final int SOFTPORT = 2000;
	private static final String NAMESPACE = "testnamespace";
	private static final String WORKDIR = ".";
	private static final String BOOTSTRAP = "";

	private static final int UDP_PORT2 = 0;
	private static final int UDP_PORT1 = 0;

	private static final int DEBUG_LEVEL = 2; // 0-4, 0=trace
	
	public static void main(String[] args) {
		
		DemoFirstTrial demo = new DemoFirstTrial();
		
		demo.runReply("userIDremote");
		demo.runSend("userID","userIDremote",10000,1);		
		
	}

	public void runSend(final String userid, String useridRemote, int trialsize, int trialFrequence) {
		
		//initialize a peerA
		
		logger.info("start for " + userid);
		
		final PeerServer peer = new PeerServer(NAMESPACE, APPID, DEVICEID, WORKDIR, BOOTSTRAP, new SimpleListener(DEBUG_LEVEL));
	    peer.start("",UDP_PORT1,userid);
		
		final Socket socket = peer.openSocket(SOFTPORT, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

				System.out.println("REPLY message from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(message.getMessage().length));
				
			}			

		}); 
		
		Random rnd = new Random();
		int cnt=0;
		while(true){
			
			// fill som random data
			byte[] message = new byte[trialsize + rnd.nextInt(trialsize)];
			rnd.nextBytes(message);
							
			if(socket.send(useridRemote, socket.getPort(), new MsgSimple(userid,cnt,message).getBytes())){

				cnt++;
				System.out.println("sent to " + useridRemote + " size : " + String.valueOf(message.length));
				
			} else {
				
				System.out.println("can not send to " + useridRemote + " error " + socket.getLastMessage());
				socket.close(useridRemote);
				
			}
			
			try {
				
				Thread.sleep(trialFrequence*1000);
			
			} catch (InterruptedException e) {
			}
			
		}
			
	}
	
	public void runReply(final String useridRemote) {
		
		//initialize a peerA
		
		logger.info("start for " + useridRemote);
		
		final PeerServer peer = new PeerServer(NAMESPACE, APPID, DEVICEID, WORKDIR, BOOTSTRAP, new SimpleListener(2));
	    peer.start("",UDP_PORT2,useridRemote);

		peer.openSocket(SOFTPORT, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

				System.out.println("message from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(message.getMessage().length));

				if(this.getSocket().send(message.getUserid(), this.getSocket().getPort(), new MsgSimple(useridRemote,message.getId(),message.getMessage()).getBytes())){

					System.out.println("sent reply to " + message.getUserid() + " size : " + String.valueOf(message.getMessage().length));
					
				} else {
					
					System.out.println("can not send reply to " + message.getUserid() + " error " + getSocket().getLastMessage());
					
				}
				
			}			

		}); 

	}	

}
