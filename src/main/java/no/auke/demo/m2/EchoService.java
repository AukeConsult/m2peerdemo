package no.auke.demo.m2;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketListener;
import no.auke.p2p.m2.SocketRetStatus;
import no.auke.p2p.m2.StreamSocket;
import no.auke.p2p.m2.StreamSocket.StreamPacket;
import no.auke.p2p.m2.StreamSocketListener;
import no.auke.p2p.m2.sockets.messages.MsgSimple;

public class EchoService {
	
	public static final int MESSAGE_PORT = 1;
	public static final int STREAM_PORT = 2;
	public static final String DEFAULT_NAMESPACE = "echoservice";
	public static final String DEFAULT_USER = "echo1";
    
    PeerServer peerserver;
    Socket socket=null;
    
	public Socket getSocket() {
		return socket;
	}

    StreamSocket streamsocket=null;
    
	public StreamSocket getStreamSocket() {
		return streamsocket;
	}
	

	public void run(String userid) {
		
		userid = userid.isEmpty()?DEFAULT_USER:userid;		
		
		System.out.println("start echo for " + userid);
		
		peerserver = new PeerServer(DEFAULT_NAMESPACE, "echoapp", "deviceid_" + userid, "", "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peerserver.start(userid);
		
		socket = peerserver.openSocket(MESSAGE_PORT, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

//				System.out.println("message " + message.getUserid() + 
//						" id " + String.valueOf(message.getId()) + 
//						" size : " + String.valueOf(message.getMessage().length));

				SocketRetStatus ret = getSocket().send(message.getUserid(), MESSAGE_PORT, new MsgSimple(peerserver.getClientid(),message.getId(),message.getTimesent(),message.getMessage()).getBytes());
				
				if(!ret.isOk()){

					System.out.println("ERROR: can not send message reply to " + message.getUserid() + " error " + ret.getLastMessage());
					
				} else {
					
					System.out.println("Reply message to " + message.getUserid() + " id " + String.valueOf(message.getId() + " size " + String.valueOf(buffer.length)));
					
				}
				
			}			

		}); 
		
		streamsocket = peerserver.openStream(STREAM_PORT, new StreamSocketListener() {

			@Override
			public void onIncomming(StreamPacket buffer) {

				MsgSimple message = new MsgSimple(buffer.getData());

//				System.out.println("stream " + 
//						message.getUserid() + 
//						" id " + String.valueOf(message.getId()) + 
//						" size : " + String.valueOf(message.getMessage().length));

				SocketRetStatus ret = getStreamSocket().send(new SocketRetStatus(),message.getUserid(), STREAM_PORT, new MsgSimple(peerserver.getClientid(),message.getId(),message.getTimesent(),message.getMessage()).getBytes());
				if(!ret.isOk()){

					System.out.println("ERROR: can not send stream reply to " + message.getUserid() + " error " + ret.getLastMessage());
					
				} else { 
					
					System.out.println("Reply stream to " + message.getUserid() + " id " + String.valueOf(message.getId() + " size " + String.valueOf(buffer.getData().length)));
					
				}
				
			}});		
		
		
		
	}

}
