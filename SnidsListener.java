//package snids;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;
import java.nio.charset.Charset;
/**
 *
 * @author keerthanagunasekaran
 */
public class SnidsListener implements PacketReceiver {
 RuleParser rp;
    //String packetData;
    HashMap<Rule, List<Pattern>> ruleMap;
    TreeMap<Integer,String> mvalue;
    HashMap<Index,TreeMap<Integer,String> > tcpStreams; // <local_port, packet_content>
    @Override
    public void receivePacket(Packet packet) {
//             System.out.println();
//             System.out.println();
//             System.out.println();
//             System.out.println();
           
    //    packetData="";
            // System.out.println("PAcket Recieved");
     /*   try {
            //     System.out.println(new String(packet.data,"ISO-8859-1"));
            if(new String(packet.data,"ISO-8859-1").isEmpty())
                packetData="09443696610keerthana";
            else
                packetData=new String(packet.data,"ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
             
            Logger.getLogger(IDSListener.class.getName()).log(Level.SEVERE, null, ex);
        }
          //  if(packetData.isEmpty())
       */ 
        if(packet != null)    
        if(packet instanceof IPPacket){
             if(packet instanceof TCPPacket){
                
                 try {
                     handleTCPPackets(packet);
                 } catch (Exception ex) {
                   //  Logger.getLogger(SnidsListener.class.getName()).log(Level.SEVERE, null, ex);
                 }
               
		}
             else if(packet instanceof UDPPacket){
                 
                 try {
                     handleUDPPackets(packet);
                 } catch (Exception ex) {
              //       Logger.getLogger(SnidsListener.class.getName()).log(Level.SEVERE, null, ex);
                 }
                
		}
             else 
                return;
        }
        return;
    }
    
   
	
    public SnidsListener(String fileName) throws FileNotFoundException
    {
        rp = new RuleParser(fileName);
    } // end of IDSListener(String fileName)
    
    
private void handleTCPPackets(Packet p) {
		/* Process TCP packets */
		TCPPacket tcp = (TCPPacket) p;
		boolean match;
		for(Rule r: rp.rules){
         /*           
                  System.out.println("ruleip  "+r.ip);
                    System.out.println("rule host  "+rp.host);
                    System.out.println("src ip  "+tcp.src_ip.getHostAddress());
                    System.out.println("dst ip  "+tcp.dst_ip.getHostAddress());
                    System.out.println("r match  "+r.ismatch);
                    System.out.println("local port  "+r.local_port);
                    System.out.println("remote port  "+r.remote_port);
                    System.out.println("src port  "+tcp.src_port);
                    System.out.println("dst port  "+tcp.dst_port);
                    System.out.println("recieve  "+r.receive);
                    System.out.println("send  "+r.send);
                    System.out.println("mesage  "+ r.subRules.recv_send);
           */     
                    if(r.ismatch)
                        continue;
                    if(r.type.equalsIgnoreCase("stream"))
                        handleTCPStream(p);
                    if(!r.proto.equalsIgnoreCase("tcp"))
                    {
             //           System.out.println("tcp not match");
                        continue;
                    }
                        
                        if(r.receive)
                        {
                            if(!r.ip.equalsIgnoreCase("any"))
                                if(!r.ip.equals(tcp.src_ip.getHostAddress()))
                    {
               //        System.out.println("host not match");
                        continue;
                    }
                            if(!rp.host.equalsIgnoreCase("any"))
                            if(!rp.host.equals(tcp.dst_ip.getHostAddress()))
                               
                    {
                 //      System.out.println("ip not match");
                        continue;
                    }
                            if(!r.local_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.local_port)!=(tcp.dst_port))
                    {
                   //    System.out.println("local port not match");
                        continue;
                    }
                            }
                            if(!r.remote_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.remote_port)!=(tcp.src_port))
                                    
                    {
                     //  System.out.println("remote port not match");
                        continue;
                    }
                            }
                                    
                        }
                        else
                        {
                            
                            if(!r.ip.equalsIgnoreCase("any"))
                                if(!r.ip.equals(tcp.dst_ip.getHostAddress()))
                                    
                    {
                //        System.out.println("sen ip not match");
                        continue;
                    }      if(!rp.host.equalsIgnoreCase("any"))
                            if(!rp.host.equals(tcp.src_ip.getHostAddress()))
                                
                    {
                  //     System.out.println("sen host not match");
                        continue;
                    }
                             if(!r.local_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.local_port)!=(tcp.src_port))
                                    
                    {
              //          System.out.println("s local not match");
                        continue;
                    }
                                
                            }
                            if(!r.remote_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.remote_port)!=(tcp.dst_port))
                                    
                    {
                //        System.out.println("s remote not match");
                        continue;
                    }
                            }
                        }
                        try
                        {
                        System.out.println(r.name);
                        r.ismatch=true;
                  	match=TCPRegexMatch(p, r);
			if(match&&!r.ismatch)
                        {
                            System.out.println(r.name);
                            r.ismatch=match;
                        }
                        }
                        catch(Exception e)
                        {
                        	
                        }
		} // end of for rp.rules loop
}
	
		private boolean TCPRegexMatch(Packet p, Rule r) {
		Boolean isMatch=false;
		try{
                System.out.println("daTA111 :"+new String(p.data,"ISO-8859-1"+":"));
		}
		catch(Exception e)
		{
			//System.out.println("cut message1111:"+r.subRules.recv_send+":");
		}
			
            for(String pat : r.subRules.recv_send){
                
		try{
                        pat = pat.substring(1,pat.length()-1);
                        if(pat.equals(new String(p.data,"ISO-8859-1")))
                        {
                            isMatch=true;
                           // System.out.println("MATCH !!!!!");
                            return isMatch;
                        }
                   //     System.out.println("daTA :"+new String(p.data,"ISO-8859-1"+":"));
                   //    System.out.println("cut message:"+pat+":");
			Pattern p1 = Pattern.compile(pat);
			Matcher m = p1.matcher(new String(p.data,"ISO-8859-1"));
                        if(m.matches())
                        {
                            isMatch=true;
                        }
		}catch(Exception e){
		//	System.out.println("Invalid regex syntax!");
		}
				
			}
        return isMatch;
		
                
        }
	private void handleUDPPackets(Packet p) {
		/* Process UDP packets */
            UDPPacket udp = (UDPPacket)p;
            boolean match;
            
            for(Rule r: rp.rules){
			// Match rules with the packet info
                    if(!r.proto.equalsIgnoreCase("udp"))
                        continue;
                     if(r.ismatch)
                        continue;
                        if(r.receive)
                        {
                            if(!r.ip.equalsIgnoreCase("any"))
                                if(!r.ip.equals(udp.src_ip.getHostAddress()))
                                    continue;
                            if(!rp.host.equalsIgnoreCase("any"))
                            if(!rp.host.equals(udp.dst_ip.getHostAddress()))
                                continue;
                            if(!r.local_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.local_port)!=(udp.dst_port))
                                    continue;
                            }
                            if(!r.remote_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.remote_port)!=(udp.src_port))
                                    continue;
                            }
                                    
                        }
                        else
                        {
                            if(!rp.host.equals(udp.src_ip))
                                continue;
                             if(!r.local_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.local_port)!=(udp.src_port))
                                    continue;
                                
                            }
                            if(!r.remote_port.equalsIgnoreCase("any"))
                            {
                                if(Integer.parseInt(r.remote_port)!=(udp.dst_port))
                                    continue;
                            }
                        }
                        System.out.println(r.name);
                        r.ismatch=true;
                        try
                        {
                        match=TCPRegexMatch(p, r);
			if(match)
                        {
                            System.out.println(r.name);
                           r.ismatch=match;
                        }
                        }
                        catch(Exception e)
                        {
                        	
                        }
                }   		
        }
        
        public void handleTCPStream(Packet p) 
        {
            TCPPacket tcp=(TCPPacket)p;
           
          //  StringBuilder sb = new StringBuilder(new String (p.data));
            Index i = new Index(tcp);
            mvalue.clear();
            if(tcpStreams.containsKey(i))
            {
            	try
            	{
                tcpStreams.get(i).put((int)tcp.sequence, new String(tcp.data,"ISO-8859-1"));
            	}
            	catch(Exception e)
            	{
            		//mvalue=tcpStreams.replace(i, mvalue);
            	}
                //tcpStreams.remove(i);
                //tcpStreams.put(i, mvalue)
                //mvalue=tcpStreams.r
            }
          try
          {
            mvalue.put((int)tcp.sequence,new String(tcp.data,"ISO-8859-1"));
          }
          catch(Exception e)
          {
        	  
          }
            tcpStreams.put(new Index(tcp),mvalue);
            if (tcp.fin)
            {
                StringBuilder temp=new StringBuilder("");
                TreeMap<Integer,String> map= tcpStreams.get(i);
                 Set keys = map.keySet();
                for (Iterator ii = keys.iterator(); ii.hasNext();) {
                      Integer key = (Integer) ii.next();
                      String value = (String) map.get(key);
                      temp.append(value);
              }
                String message=temp.toString();
                for(Rule r: rp.rules){
                        if(r.receive)
                        {
                            if(!rp.host.equalsIgnoreCase("any"))
                            if(!rp.host.equals(tcp.dst_ip))
                                continue;
                            if(!r.local_port.equalsIgnoreCase("any"))
                            {
                                if(!r.local_port.equals(tcp.dst_port))
                                    continue;
                            }
                            if(!r.remote_port.equalsIgnoreCase("any"))
                            {
                                if(!r.remote_port.equals(tcp.src_port))
                                    continue;
                            }
                            System.out.println(r.name);
                            r.ismatch=true;
                            Pattern pat = Pattern.compile(r.recv);
                            Matcher matchstream = pat.matcher(message);
                            if(!matchstream.matches())
                                continue;
                            System.out.println(r.name);
                        }
                        else
                        {
                           if(!r.ip.equalsIgnoreCase("any"))
                                if(!r.ip.equals(tcp.dst_ip.getHostAddress()))
                                    continue;
                            if(!rp.host.equalsIgnoreCase("any"))
                            if(!rp.host.equals(tcp.src_ip.getHostAddress()))
                                continue;
                             if(!r.local_port.equalsIgnoreCase("any"))
                            {
                                if(!r.local_port.equals(tcp.src_port))
                                    continue;
                                
                            }
                            if(!r.remote_port.equalsIgnoreCase("any"))
                            {
                                if(!r.remote_port.equals(tcp.dst_port))
                                    continue;
                            }
                            System.out.println(r.name);
                           // r.ismatch=true;
                            Pattern pat = Pattern.compile(r.send);
                            Matcher matchstream = pat.matcher(message);
                            if(!matchstream.matches())
                                continue;
                            System.out.println(r.name);
                        }				
		
            }
        }
}
}


