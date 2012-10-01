package no.auke.demo.m2;

import java.util.UUID;

public class InitParam {

	public static final String APPID = "m2demo";
	public static final String NAMESPACE = "m2demo" + UUID.randomUUID().toString().substring(0,5);
	public static final String DEVICEID = "mydevice";
	public static final String USERDIR = ".";
	public static final int DEBUGLEVEL = 2; // 0=trace , 1=debug, 2=info, 3=error, 4=fatal error
	public static final int PORT = 0;
	
	
}
