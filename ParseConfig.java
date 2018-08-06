import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
class NodeDetails{
	String hostName;
	int portNumber;
	NodeDetails(String hostName, int portNumber)
	{
		this.hostName= hostName;
		this.portNumber= portNumber;
	}
}
public class ParseConfig {
	ParseConfig(Node current, String path,String args)
	{
		
		
		
		String thisMachine="";		// Stores the host name for this machine
		try {
			thisMachine = InetAddress.getLocalHost().getHostName();	
			// System.out.println(thisMachine);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		HashMap<Integer, NodeDetails> temp_hm = new HashMap<Integer, NodeDetails>();// This TEMP hashmap stores the details of all nodes while reading the file
		
		//Start reading the config file and extracting information from it
		try {
				Scanner sc  = new Scanner(new FileReader(path));
				String readLine;
				while (sc.hasNext()) {
					//System.out.println("I am here");
					readLine = sc.nextLine().trim();
					//System.out.println("Current Line"+readLine);
					if (readLine.isEmpty() || readLine.charAt(0) == '#')
						continue;
				    
					//Storing the Node details
					String[] s = readLine.split("\\s+");
					if(s.length==3)
					{
						temp_hm.put(Integer.parseInt(s[0]), new NodeDetails(s[1],Integer.parseInt(s[2])));
						if(args.equals(s[0]))
						{
							// System.out.println("HERE");
							current.UID = Integer.parseInt(s[0]);
							current.hostName=s[1];
							current.portNumber=Integer.parseInt(s[2]);
							// System.out.println(current.portNumber);
							current.leaderUID = current.UID;
							current.minWeightOGE= new MinWeightOGE();
						}
					}
					else if(s.length==1)
					{
						current.totalNumberOfNodes=Integer.parseInt(s[0]);
					}
					else if(s.length==2)
					{
						s[0]=s[0].replaceAll("\\(", "").replaceAll("\\)","");
						String[] neighbors = s[0].split(",");
						int a=Integer.parseInt(neighbors[0]);
						int b = Integer.parseInt(neighbors[1]);
						int n=-1;
						if(a==current.UID)
						{
							n=b;
						}
						else if(b==current.UID)
						{
							n=a;
						}
						if(n!=-1)
						{
							NodeDetails nbsDetails=temp_hm.get(n);
							int weight = Integer.parseInt(s[1]);
							NeighborNode nbsNode = new NeighborNode(n,nbsDetails.hostName,nbsDetails.portNumber,weight);
							current.nbs.put(n, nbsNode);
						}
						
					}
							    
				} 
				//Closing the file read
				sc.close();
				// System.out.println("This Node details 	"+current.UID+"	"+current.hostName+"	"+current.portNumber);
		}
			catch (Exception e) {
			e.printStackTrace();
		}
	}
}
