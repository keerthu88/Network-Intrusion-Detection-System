//package snids;

import java.io.IOException;
import jpcap.JpcapCaptor;
import jpcap.packet.Packet;

/**
 *
 * @author keerthanagunasekaran
 */
public class Snids {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
     //   System.out.println("Check");
       Snids obj= new Snids(args[0],args[1]);
      // Rule r= new Rule();
       
    }
    public Snids(String ruleFile, String pcapFile) throws IOException
    {
         SnidsListener obj = new SnidsListener(ruleFile);
        try
        {
        JpcapCaptor pc = JpcapCaptor.openFile(pcapFile);
       
        int p = pc.loopPacket(-1, obj);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    } 
}




