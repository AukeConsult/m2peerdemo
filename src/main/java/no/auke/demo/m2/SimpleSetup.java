package no.auke.demo.m2;

import no.auke.p2p.m2.PeerServer;
import no.auke.p2p.m2.Socket;
import no.auke.p2p.m2.SocketBuffer;


public class SimpleSetup {
	
	public static void main(String[] args) {
		
		//initialize a peerA
		final PeerServer peerA = new PeerServer("NTM", "test", "pwdleif", ".", "", new SimpleListener());
	    peerA.start("PeerA");
	    
	    //initialize a peerB
	    final PeerServer peerB = new PeerServer("NTM", "test", "pwdleif", ".", "", new SimpleListener());
		peerB.start("PeerB");
		
		//now peer A and peer B must open same port 2000 to take to each other
		final Socket socketA = openSocketAndListen(peerA);
		
        final Socket socketB = openSocketAndListen(peerB);
        
        //peer A send a greeting to peer B
        socketA.send("PeerB", 2000, "hello, mr B");
        //peer A send a greeting to peer B
        socketB.send("PeerA", 2000, "hello, mr A");
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			
		}
        //HUYDO: please have a look, hang on ComChanel.Read (line 975)
        //stop servers
       
        peerA.stopServer();
      
        peerB.stopServer();
        
		
		
	}

	private static Socket openSocketAndListen(final PeerServer peer) {
		final Socket socket = peer.open(2000); 
		//HUYDO: too complicated, make interface, open and listen; example peerA.open(2000, IPortListen)
		//peerA open listening thread
		Thread Alisten =  new Thread(new Runnable() {			
			public void run() {
		        while(peer.isRunning())
		        {
		        	SocketBuffer data = socket.ReadBuffer(1000);
		        	if(data !=null)
		        	{
		        		System.out.println(new String(data.getBuffer()));
		        	}
		        }
			}
		});
		Alisten.setDaemon(true);
		Alisten.start();
		return socket;
	}
	

	

}
