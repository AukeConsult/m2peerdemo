package no.auke.demo.m2;

import no.auke.p2p.m2.agent.NetAddress;
import no.auke.p2p.m2.general.IListener;
import no.auke.p2p.m2.general.LicenseReasons;

public class SimpleListener extends IListener {

	public SimpleListener(int Loglevel){
		super(Loglevel);
	}
	
	@Override
	public void printLog(String message) {
		System.out.println(message);
	}

	@Override
	public void onServiceStarted(String message) {
		message(message);
	}

	@Override
	public void onServiceStopped(String message) {
		message(message);
	}

	@Override
	public void onServiceConnected(NetAddress publicAddress,
			NetAddress kaServerAddress) {
		debug("service is connected " + publicAddress.getAddressPort());
	}

	@Override
	public void onServiceDisconnected(NetAddress kaServerAddress) {
		debug("service is disconnected");
	}

	@Override
	public void connectionRejected(NetAddress kaServerAddress, String msg) {
		message("connection rejected " + kaServerAddress.getAddressPort() +  " " + msg);
	}

	@Override
	public void onPeerConnected(NetAddress peerAddress) {
		debug("peer is connected " + peerAddress.getAddressPort());
	}

	@Override
	public void onPeerDisconnected(NetAddress peerAddress) {
		debug("peer is disconnected " + peerAddress.getAddressPort());
	}

	@Override
	public void onMessageSend(NetAddress peerAddress, int socketPort,
			int messageId, int size) {
		trace("message sent" + peerAddress.getAddressPort() + " port " + String.valueOf(socketPort) + " size " + String.valueOf(size) + " messageid " + String.valueOf(messageId));
	}

	@Override
	public void onMessageRecieved(NetAddress peerAddress, int socketPort,
			int messageId, int size) {
		trace("message recieved" + peerAddress.getAddressPort() + " port " + String.valueOf(socketPort) + " size " + String.valueOf(size) + " messageid " + String.valueOf(messageId));
	}

	@Override
	public void onMessageDisplay(String message) {}

	@Override
	public void onMessageConfirmed(NetAddress peerAddress, int messageId) {
		trace("message confirmed " + peerAddress.getAddressPort() + " messageid " + String.valueOf(messageId));
	}

	@Override
	public void onTraffic(float bytes_in_sec, float bytes_out_sec,
			long bytes_total_in, long bytes_total_out) {

		debug("traffic " + 
						   " byte in pr second " + String.valueOf(bytes_in_sec) + 
						   " byte out pr second " + String.valueOf(bytes_out_sec) + 
						   " byte in total " + String.valueOf(bytes_total_in) + 
						   " byte out total " + String.valueOf(bytes_total_out));
		
	}

	@Override
	public void onLicenseError(LicenseReasons reason, String licenseKey) {}
	
	
}