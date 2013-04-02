package no.auke.demo.m2;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketListener;
import no.auke.p2p.m2.sockets.messages.MsgSimple;

public class EchoMessage {
	
	private ConcurrentHashMap<Long, MsgSimple> sendmessages = new ConcurrentHashMap<Long, MsgSimple>();
	
    Socket socket=null;
	public Socket getSocket() {
		return socket;
	}
	
	public void run(String userid, String useridRemote, int trialsize, int trialFrequence) {

		useridRemote = useridRemote.isEmpty()?EchoService.DEFAULT_USER:useridRemote;
		userid = userid.isEmpty()?UUID.randomUUID().toString().substring(1,5):userid;

		System.out.println("start echo message test for " + userid +  " send message to echo server" + useridRemote);
		
		final PeerServer peer = new PeerServer(EchoService.DEFAULT_NAMESPACE, "echoapp", InitParam.DEVICEID, ".", "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peer.start(userid);
	    
		socket = peer.open(EchoService.MESSAGE_PORT, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				MsgSimple message = new MsgSimple(buffer);

				long time = 0;
				if(sendmessages.containsKey(message.getId())) {
					
					time=System.currentTimeMillis() - message.getTimesent();
				}
				
				System.out.println("reply from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(message.getMessage().length) + 
						" time : " + String.valueOf(time));
				
			}			

		}); 
		
		Random rnd = new Random();
		long cnt=100;
		while(true){
			
			// fill some random data
			byte[] message = new byte[trialsize + rnd.nextInt(trialsize)];
			rnd.nextBytes(message);
			
			MsgSimple msg = new MsgSimple(userid,cnt,message);
			
			sendmessages.put(msg.getId(), msg);
			
			if(getSocket().send(useridRemote, EchoService.MESSAGE_PORT, msg.getBytes())){

				cnt++;
				System.out.println("sent to " + useridRemote + " size : " + String.valueOf(message.length));
				
			} else {

				sendmessages.remove(cnt);

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
