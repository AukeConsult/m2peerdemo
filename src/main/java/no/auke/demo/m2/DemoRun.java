package no.auke.demo.m2;

public class DemoRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String namespace = InitParam.NAMESPACE;
		String dir = InitParam.USERDIR;
		
		String userid = "TestUser";
		String trial = "";
		
        int pos = 0;
        while (pos < args.length) {
            
        	if (args[pos].startsWith("-")) {
            
            	if (args[pos].equals("-t") && args.length > pos) {
            		trial = args[pos + 1];
                }
        		
            	if (args[pos].equals("-ns") && args.length > pos) {
            		namespace = args[pos + 1];
                }
                
            	if (args[pos].equals("-u") && args.length > pos) {
            		userid = args[pos + 1];
                }
                
            	if (args[pos].equals("-dir") && args.length > pos) {
            		dir = args[pos + 1];
                }
                
            }
            pos++;
        }
        
        if(trial.isEmpty()){
        	
        	SimpleSetup s = new SimpleSetup();
        	s.run(namespace, dir, userid);
        	
        }
		

	}

}
