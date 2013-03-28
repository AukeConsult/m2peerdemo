package no.auke.demo.m2;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketListener;

public class SimpleSetup {
	
	public void run(String namespace, String dir, String userid) {
		
		//initialize a peerA
		final PeerServer peerA = new PeerServer(namespace, InitParam.APPID, InitParam.DEVICEID+"A", dir+"/A", "", new SimpleListener(InitParam.DEBUGLEVEL));
	    peerA.start(userid + "A");
	    
		final Socket socketA = peerA.open(2000, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				System.out.println(new String(buffer));
				
			}			

		}); 
	    
	    //initialize a peerB
	    final PeerServer peerB = new PeerServer(namespace, InitParam.APPID, InitParam.DEVICEID+"B", dir+"/B", "", new SimpleListener(InitParam.DEBUGLEVEL));
		peerB.start(userid + "B");
		
		final Socket socketB = peerB.open(2000, new SocketListener(){

			@Override
			public void onIncomming(byte[] buffer) {
				
				System.out.println(new String(buffer));
				
			}

		}); 
        
		
		for(int i=0;i<10;i++){
			
	        //peer A send a greeting to peer B
	        socketA.send(userid + "B", 2000, ("hello, mr B " + String.valueOf(i)).getBytes());
	        //peer A send a greeting to peer B
	        socketB.send(userid + "A", 2000, ("hello, mr A "  + String.valueOf(i)).getBytes());			
		}

        
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
		}
        //HUYDO: please have a look, hang on ComChanel.Read (line 975)
        //stop servers
       
        peerA.stopServer();
        peerB.stopServer();
        
	}

}
