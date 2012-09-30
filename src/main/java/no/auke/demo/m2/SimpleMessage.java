package no.auke.demo.m2;

import java.util.List;

import no.auke.util.ByteUtil;
import no.auke.util.StringConv;

public class SimpleMessage {
	
	String userid = "";
	long num = 0;
	long timesent = 0;
	byte[] message=new byte[0];
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public long getTimesent() {
		return timesent;
	}
	public void setTimesent(long timesent) {
		this.timesent = timesent;
	}
	public byte[] getMessage() {
		return message;
	}
	public void setMessage(byte[] message) {
		this.message = message;
	}
	
	public SimpleMessage(String userid, long num, byte[] message) {
		
		this.userid=userid;
		this.num=num;
		this.timesent=System.currentTimeMillis();
		this.message=message;
		
	}
	
	public SimpleMessage(byte[] data) {
		
		try {
			
			if(data!=null && data.length>0){
				
		     	List<byte[]> subs = ByteUtil.splitDynamicBytes(data);
		     	if(subs.size()>3){
		  
		     		userid = StringConv.UTF8(subs.get(0));
		         	num = ByteUtil.getLong(subs.get(1));
		         	timesent = ByteUtil.getLong(subs.get(2));
		         	message = subs.get(3)==null?new byte[0]:subs.get(3);
		     		
		     		
		     	}
				
			}
			
		} catch (Exception ex) {
			
			// some error in data sent
		
		}      			
        
	}	
	
    public byte[] getBytes()
    {
        return  ByteUtil.mergeDynamicBytesWithLength
                (
                	userid.isEmpty()?new byte[0]:StringConv.getBytes(userid),
                    ByteUtil.getBytes(num, 8),
                    ByteUtil.getBytes(timesent, 8),
                    message==null?new byte[0]:message
                );
    }	
	

}
