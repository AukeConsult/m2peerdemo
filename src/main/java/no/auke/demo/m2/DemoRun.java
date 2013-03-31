package no.auke.demo.m2;

import java.util.UUID;

import no.auke.p2p.m2.InitVar;

public class DemoRun {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		String namespace = InitParam.NAMESPACE;
		String dir = InitParam.USERDIR;
		
		String userid = UUID.randomUUID().toString().substring(1,6);
		String useridRemote = "";
		
		boolean dosend = true;
		
		int port = InitParam.PORT;
		int trialsize = 100000;
		int trialfrequency = 15;
		
		String trial = "sendreplylocal";
		String server = "";
		
		if(args.length >=1 && !args[0].startsWith("-")) {
			 server = args[0];
		}
		 
        int pos = 0;
        while (pos < args.length) {
            
        	if (args[pos].startsWith("-")) {
            
            	if ((args[pos].equals("-t") || args[pos].equals("-run")) && args.length > pos) {
            		trial = args[pos + 1];
                
            	} else if (args[pos].equals("-port") && args.length > pos) {
            		port = Integer.parseInt(args[pos + 1]);
                
                } else if ((args[pos].equals("-namespace") || args[pos].equals("-ns")) && args.length > pos) {
            		namespace = args[pos + 1];
                
            	} else if ((args[pos].equals("-userid") || args[pos].equals("-u")) && args.length > pos) {
            		userid = args[pos + 1];
                
            	} else if ((args[pos].equals("-useridremote") || args[pos].equals("-ur")) && args.length > pos) {
            		useridRemote = args[pos + 1];
                
            	} else if (args[pos].equals("-dir") && args.length > pos) {
            		dir = args[pos + 1];
                
            	} else if ((args[pos].equals("-nosend") || args[pos].equals("-r")) && args.length > pos) {
            		dosend=false;
                
            	} else if ((args[pos].equals("-trialsize") || args[pos].equals("-ts")) && args.length > pos) {
            		trialsize  = Integer.parseInt(args[pos + 1]);

            	} else if ((args[pos].equals("-trialfrequecy") || args[pos].equals("-tf")) && args.length > pos) {
            		trialfrequency  = Integer.parseInt(args[pos + 1]);
                
            	} else if (args[pos].equals("-usemm") && args.length > pos) {
            		InitVar.SEND_MIDDLEMAN_REQURIED = true;
                }
                
            }
            pos++;
        }
        
    	System.out.println("starting " + trial);
        
        if(trial.isEmpty()){
        	
        	SimpleSetup s = new SimpleSetup();
        	s.run(namespace, dir, userid);
        	
        } else if(trial.equals("sendreplylocal")){
        	
        	SendReplyLocal s = new SendReplyLocal();
        	s.run(namespace, dir, port, userid,trialsize,trialfrequency);
        	
        } else if(trial.equals("sendreply3services")){
        	
        	SendReply3Services s = new SendReply3Services();
        	s.run(namespace, dir, userid, useridRemote, port, dosend, trialsize, trialfrequency);       	

        } else if(trial.equals("sendreply")){
        	
        	SendReply s = new SendReply();
        	s.run(namespace, dir, userid, useridRemote, port, dosend, trialsize, trialfrequency);

        } else if(trial.equals("echo")){
        	
        	EchoService s = new EchoService();
        	s.run(userid);
        	
        } else {
        	
        	System.out.println("wrong startup " + trial);
        	
        }

	}

}
