//package snids;

/**
 *
 * @author keerthanagunasekaran
 */
public class Rule {
    
	public String name;
	public String type; //Either tcp_stream_rule or protocol_rule
	public String local_port;
	public String remote_port;
	public String ip;
	public boolean receive; //True if it's recv=<regexp>, false otherwise
	public String recv, send; //Specific for tcp_stream_rule
	public String proto; //Specific for protocol_rule
	public SubRule subRules; //Specific for protocol_rule
        public boolean ismatch;

	public Rule (String name, String type, String local_port, String remote_port, String ip,
								String recv_send, boolean receive, String proto){
		this.name = name;
		this.type = type;
		this.local_port = local_port;
		this.remote_port = remote_port;
		this.ip = ip;
		if (receive){
			recv = recv_send; //it's a recv pattern
		}
		else {
			send = recv_send; //it's a send pattern
		}
		this.receive = receive;
		this.proto = proto; //Note, leave blank if not protocol_rule
		this.ismatch=false;
        }

	public void setSubRules(SubRule subRules){
		if (type.equals("protocol")){
			this.subRules = subRules;
		}
		else{
			this.subRules = null;
		}

	}
}

