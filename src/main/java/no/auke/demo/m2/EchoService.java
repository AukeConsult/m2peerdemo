package no.auke.demo.m2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketListener;
import no.auke.p2p.m2.sockets.messages.MsgSimple;

public class EchoService {
	
    private static final Logger logger = LoggerFactory.getLogger(EchoService.class);
    
    Socket socket=null;
    
	public Socket getSocket() {
		return socket;
	}

	public void run(final String userid) {
		
		//initialize a peerA
		
		logger.info("start echo for " + userid);
		
		final PeerServer peer = new PeerServer("m2echo", "echoapp", InitParam.DEVICEID, ".", "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peer.start(userid);
		
		socket = peer.open(1, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

				System.out.println("message from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(message.getMessage().length));

				if(getSocket().send(message.getUserid(), 1, new MsgSimple(userid,message.getId(),message.getMessage()).getBytes())){

					System.out.println("SUCCESS: sent reply to " + message.getUserid() + " size : " + String.valueOf(message.getMessage().length));
					
				} else {
					
					System.out.println("ERROR: can not send reply to " + message.getUserid() + " error " + getSocket().getLastMessage());
					
				}
				
			}			

		}); 
		
	}

}
