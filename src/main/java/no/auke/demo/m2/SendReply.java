package no.auke.demo.m2;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketListener;
import no.auke.p2p.m2.SocketRetStatus;
import no.auke.p2p.m2.sockets.messages.MsgSimple;

public class SendReply {
	
    private static final Logger logger = LoggerFactory.getLogger(Socket.class);
    	
	public void run(String namespace, String dir, final String userid, String useridRemote, int port, boolean send, int trialsize, int trialFrequence) {
		
		//initialize a peerA
		
		logger.info("start for " + userid);
		
		final PeerServer peer = new PeerServer(namespace, InitParam.APPID, InitParam.DEVICEID, dir, "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peer.start("",port,userid);

		final Socket socket_reply = peer.openSocket(2001, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

				System.out.println("reply message from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(message.getMessage().length));

			}			

		});
		
		final Socket socket = peer.openSocket(2000, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

				System.out.println("message from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(message.getMessage().length));

				SocketRetStatus ret = socket_reply.send(message.getUserid(), 2001, new MsgSimple(userid,message.getId(),message.getMessage()).getBytes());
				if(ret.isOk()){

					System.out.println("sent reply to " + message.getUserid() + " size : " + String.valueOf(message.getMessage().length));
					
				} else {
					
					System.out.println("can not send reply to " + message.getUserid() + " error " + ret.getLastMessage());
					
				}
				
			}			

		}); 
		
		
		
		if(send) {
			
			Random rnd = new Random();
			int cnt=0;
			while(true){
				
				// fill som random data
				byte[] message = new byte[trialsize + rnd.nextInt(trialsize)];
				rnd.nextBytes(message);
						
				SocketRetStatus ret = socket.send(useridRemote, 2000, new MsgSimple(userid,cnt,message).getBytes());
				if(ret.isOk()){

					cnt++;
					System.out.println("sent to " + useridRemote + " size : " + String.valueOf(message.length));
					
				} else {
					
					System.out.println("can not send to " + useridRemote + " error " + ret.getLastMessage());
					socket.close(useridRemote);
					
				}
				
				try {
					
					Thread.sleep(trialFrequence*1000);
				
				} catch (InterruptedException e) {
				}
				
			}
			
		}
	}

}
