//package snids;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author keerthanagunasekaran
 */
public class RuleParser {
    public String fileName; //Rule file to be parsed
	public String host; //The host in the rule file
	public ArrayList<Rule> rules = new ArrayList<Rule>(); 
	
	public RuleParser(String fileName) throws FileNotFoundException{
		this.fileName = fileName;
		parseFile();
	}

	public void parseFile() throws FileNotFoundException{
		try{
				FileInputStream fstream = new FileInputStream(fileName);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				int count = 0; //No. of Rules

				//The following are temp variables to form Rule objects with
				String name = "";
				String type = "";
				String local_port = "";
				String remote_port = "";
				String ip = "";
				String proto = "";
				boolean receive = false; 
				String recv_send = "";
				ArrayList<Boolean> subrule_receive = new ArrayList<Boolean>(); 
				ArrayList<String> receive_send = new ArrayList<String>(); 
				ArrayList<Flag> flags = new ArrayList<Flag>();
                                while ((strLine = br.readLine()) != null){
					strLine = strLine.trim(); 
						if ((strLine.length() >= 4) && strLine.substring(0, 4).equals("host")){
							 host = extractValueOf(strLine);
						}
						else if ((strLine.length() >=4) && strLine.substring(0, 4).equals("name")){
							 if (count == 0){ 
						 		name = extractValueOf(strLine);
								count++;
							 }
							 else {
								if (type.equals("stream")){
									rules.add(new Rule(name, type, local_port, remote_port, ip, recv_send, receive, proto));
								}
								else {
									Rule proto_rule = new Rule(name, type, local_port, remote_port, ip, recv_send, receive, proto);
									SubRule r = new SubRule(subrule_receive, receive_send);
									r.setFlags(flags);
									proto_rule.setSubRules(r);
									rules.add(proto_rule); 
									
								}
								name = extractValueOf(strLine);
								
								type = "";
								local_port = "";
								remote_port = "";
								ip = "";
								proto = "";
								receive = false; 
								recv_send = "";
								subrule_receive = new ArrayList<Boolean>(); 
								receive_send = new ArrayList<String>(); 
								flags = new ArrayList<Flag>();
								
							 }
						}
						else if ((strLine.length() >=4) && strLine.substring(0, 4).equals("type")){
							type = extractValueOf(strLine);
						}
						else if ((strLine.length() >=10) && strLine.substring(0, 10).equals("local_port")){
							 local_port = extractValueOf(strLine);
						}
						else if ((strLine.length() >=11) && strLine.substring(0, 11).equals("remote_port")){
							 remote_port = extractValueOf(strLine);
						}
						else if ((strLine.length() >=2) && strLine.substring(0, 2).equals("ip")){
							 ip = extractValueOf(strLine);
						}
						else if ((strLine.length() >=5) && strLine.substring(0, 5).equals("proto")){
							 proto = extractValueOf(strLine);
						}

						else if ((strLine.length() >=4) && strLine.substring(0, 4).equals("recv")
											&& type.equals("stream")){
							 recv_send = extractValueOf(strLine);
							 receive = true;
						}
						else if ((strLine.length() >=4) && strLine.substring(0, 4).equals("send")
										  && type.equals("stream")){
							 recv_send = extractValueOf(strLine);
							 receive = false;
						}

						else if ((strLine.length() >=4) && strLine.substring(0, 4).equals("recv")
										&& type.equals("protocol")){
							if (strLine.contains("with")){
								String temp[] = strLine.split("with");
								recv_send = extractValueOf(temp[0]);
								String Flags = extractValueOf(temp[1]);
								for (int i =0 ; i< Flags.length(); i++){
									char f = Flags.charAt(i);
									flags.add(getFlagRepresentation(f));
							receive=true;	
								}
								 receive=true;
							}
							else {
								recv_send = extractValueOf(strLine);
							}
							subrule_receive.add(true);
							receive_send.add(recv_send);
						}
						
						else if ((strLine.length() >=4) && strLine.substring(0, 4).equals("send")
									&& type.equals("protocol")){
							if (strLine.contains("with")){
								String temp[] = strLine.split("with");
								recv_send = extractValueOf(temp[0]);
								String Flags = extractValueOf(temp[1]);
								for (int i =0 ; i< Flags.length(); i++){
									char f = Flags.charAt(i);
									flags.add(getFlagRepresentation(f));
							
								}
							}
							else {
								recv_send = extractValueOf(strLine);
							}
							subrule_receive.add(false);
							receive_send.add(recv_send);
						}

				}
				
				if (count == 1){
					if (type.equals("stream")){
						rules.add(new Rule(name, type, local_port, remote_port, ip, recv_send, receive, proto));
					}
					else {
						Rule proto_rule = new Rule(name, type, local_port, remote_port, ip, recv_send, receive, proto);
						SubRule r = new SubRule(subrule_receive, receive_send);
						r.setFlags(flags);
						proto_rule.setSubRules(r);
						rules.add(proto_rule); 
						
					}
					count = 0; 
				}

				in.close();

		} catch (Exception e){
					System.err.println("Error: " + e.getMessage());
		}
	}

	
	public String extractValueOf(String eq){
		if (eq.charAt(eq.length() - 1) == '='){
			return "";
		}
		else{
			String temp[] = eq.split("=");
			return (temp[1]).trim();
		}
	}
	
	public Flag getFlagRepresentation(char f){
		if (f == 'S'){
			return Flag.S;
		}
		else if (f == 'A'){
			return Flag.A;
		}
		else if (f == 'F'){
			return Flag.F;
		}
		else if (f == 'R'){
			return Flag.R;
		}
		else if (f == 'P'){
			return Flag.P;
		}
		else if (f == 'U'){
			return Flag.U;
		}
		else{
			return null;
		}
	}
}



