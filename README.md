# Network-Intrusion-Detection-System
1. Snids.java 
* Contains main function.  
* Initializes the objects of the classes SnidsListener, RuleParser.  
* RecievePacket function is called each time a new packet is encountered.  
* The packets are read from the file using loopPacket method with special  argument “-1”.  
* Rulefile is passed as the argument to the class RuleParser and the  values are extracted from the file so that they can be compared to the  values in the IP Packets. 

2. SnidsListener.java  
* This class is used to compare the data from the rule file to the IP Packets.  
* The objects of Rule, Index and Flag classes are taken as the input arguments.  
* Comparison Steps  
o First the packet is checked if it is TCP or UDP Packet. Based on 
this, separate functions are called. o The source and destination address along with the port numbers 
are compared to the data obtained from the rule file. o If all the above criteria match, then the data from the packet is 
checked with the message in the rule file. o If the properties match, the name of the rule is printed. 


3. Rule.java 
* This class has the attributes required for comparison of the data from  the rule file with the data from the IP Packets.  
* The SubRules were implemented as a separate class.  


4. RuleParser.java  
* This class is used for the extraction of data from the rule file.  
* Simple String functions are used for this purpose.  
* The objects of the Rule and SubRule classes are instantiated.  
* The Flags are also appropriately stored.  

5. Index.java  
* Used for the implementation of the TCP Stream Hash Map  
* The HashMap that is used to represent the TCPStream consists of four  attribute values (Source IP, Destination IP, Source Port and Destination Port) grouped together as one group to form the KEY.  
* The VALUE field of the HashMap is an TreeMap consisting of the sequence number and the data of the IP Packet.  
* TCPStream can be easily reconstructed because the TreeMap sorts the entries according to the key(sequence number).  
* If the Packet is a FIN Packet, the data from all the packets belonging to the same record are taken.  
* The message is reconstructed by appending the data obtained from the packets having the same (Source IP, Destination IP, Source Port and Destination Port) and checked against the data from the rule file.  


6. Flag.java  
• This is just an enumeration to represent the flags. 
7. SubRule.java 
* This class is used in the case of protocol rule.  
* This consists three attributes 
o ArrayList of Strings Data/Message 
o ArrayList of Boolean values whether the particular message is 
sent or received. 
o ArrayList of Flags associated with each message. 
