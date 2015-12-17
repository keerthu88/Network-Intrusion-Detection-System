//package snids;
import jpcap.packet.TCPPacket;

/**
 *
 * @author keerthanagunasekaran
 */
public class Index {
    public int srcport;
    public int dstport;
    public String srcip;
    public String dstip;
    public int length;
    public long seq;
    
    public Index(TCPPacket p)
    {
        srcport=p.src_port;
        srcip=p.src_ip.getHostAddress();
        dstip=p.dst_ip.getHostAddress();
        dstport=p.dst_port;           
    }
    
    @Override
  public boolean equals (final Object O) {
    if (!(O instanceof Index)) return false;
    if (((Index) O).srcport != srcport) return false;
    if (((Index) O).dstport != dstport) return false;
    if (((Index) O).srcip != srcip) return false;
    if (((Index) O).dstip != dstip) return false;
    return true;
  }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.srcport;
        hash = 37 * hash + this.dstport;
        return hash;
    }

}
