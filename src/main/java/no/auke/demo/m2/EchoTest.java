package no.auke.demo.m2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketListener;
import no.auke.p2p.m2.sockets.messages.MsgSimple;

public class EchoTest {
	
    private static final Logger logger = LoggerFactory.getLogger(EchoTest.class);
    	
    Socket socket=null;
    
	public Socket getSocket() {
		return socket;
	}
	
	public void run(final String userid, String useridRemote, int trialsize, int trialFrequence) {
		
		//initialize a peerA
		
		logger.info("start echo test for " + userid +  " send message to echo server" + useridRemote);
		
		final PeerServer peer = new PeerServer("m2echo", "echoapp", InitParam.DEVICEID, ".", "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peer.start(userid);
		
		final Socket socket = peer.open(1, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

				System.out.println("reply from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(message.getMessage().length));
				
			}			

		}); 
		
		Random rnd = new Random();
		int cnt=0;
		while(true){
			
			// fill some random data
			byte[] message = new byte[trialsize + rnd.nextInt(trialsize)];
			rnd.nextBytes(message);
							
			if(getSocket().send(useridRemote, 1, new MsgSimple(userid,cnt,message).getBytes())){

				cnt++;
				System.out.println("sent to " + useridRemote + " size : " + String.valueOf(message.length));
				
			} else {
				
				System.out.println("can not send to " + useridRemote + " error " + getSocket().getLastMessage());
				socket.close(useridRemote);
				
			}
			
			try {
				
				Thread.sleep(trialFrequence*1000);
			
			} catch (InterruptedException e) {
			}
			
		}
			
	}

}
