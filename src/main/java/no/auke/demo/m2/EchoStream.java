package no.auke.demo.m2;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.SocketRetStatus;
import no.auke.p2p.m2.StreamSocket;
import no.auke.p2p.m2.StreamSocket.StreamPacket;
import no.auke.p2p.m2.StreamSocketListener;
import no.auke.p2p.m2.sockets.messages.MsgSimple;
import no.auke.util.ByteUtil;

public class EchoStream {
	
	ConcurrentHashMap<Long, MsgSimple> sendmessages = new ConcurrentHashMap<Long, MsgSimple>();
	
	StreamSocket socket=null;
	public StreamSocket getSocket() {
		return socket;
	}
	
	public void run(String userid, String useridRemote, int trialsize, int trialFrequence) {

		useridRemote = useridRemote.isEmpty()?EchoService.DEFAULT_USER:useridRemote;
		userid = userid.isEmpty()?UUID.randomUUID().toString().substring(1,10):userid;

		System.out.println("start echo stream test for " + userid +  " send message to echo server" + useridRemote);
		
		final PeerServer peer = new PeerServer(EchoService.DEFAULT_NAMESPACE, "echoapp", InitParam.DEVICEID, ".", "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peer.start(userid);
	    
		socket = peer.openStream(EchoService.STREAM_PORT, new StreamSocketListener(){

			@Override
			public void onIncomming(StreamPacket buffer) {
				
				MsgSimple message = new MsgSimple(buffer.getData());

				long time = 0;
				if(sendmessages.containsKey(message.getId())) {
					
					time=System.currentTimeMillis() - message.getTimesent();
				}
				
				System.out.println("reply from " + 
						message.getUserid() + 
						" id " + String.valueOf(message.getId()) + 
						" size : " + String.valueOf(buffer.getData().length) + 
						" time : " + String.valueOf(time));

				
			}			

		}); 
		
		Random rnd = new Random();
		int cnt=0;
		while(true){
			
			// fill some random data
			byte[] message = new byte[trialsize + rnd.nextInt(trialsize)];
			rnd.nextBytes(message);
			
			List<byte[]> submessages = ByteUtil.splitBytesWithFixedLength(message, 512);
			for(byte[] submessage:submessages) {
				
				MsgSimple msg = new MsgSimple(userid,cnt,submessage);
				
				sendmessages.put(msg.getId(), msg);
				
				byte[] msgbytes = msg.getBytes();
				
				SocketRetStatus ret = getSocket().send(new SocketRetStatus(), useridRemote, EchoService.STREAM_PORT, msgbytes); 
				
				if(ret.isOk()){

					cnt++;
					System.out.println("sent stream to " + useridRemote + " size : " + String.valueOf(msgbytes.length));
					
				} else {

					sendmessages.remove(cnt);
					System.out.println("can not send stream to " + useridRemote + " size : " + String.valueOf(msgbytes.length) + " error " + ret.getLastMessage());
					
				}
				
				cnt++;
				
			}
			
			
			try {
				
				Thread.sleep(trialFrequence*1000);
			
			} catch (InterruptedException e) {
			}
			
		}
			
	}

}
