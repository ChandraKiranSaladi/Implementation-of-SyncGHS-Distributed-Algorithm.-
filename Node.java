import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Node {
	int UID=-1;
	String hostName="";
	int portNumber=-1;
	int totalNumberOfNodes=0;
	int leaderUID;
	int phase;
	int predecessorUID;
	MinWeightOGE minWeightOGE;
	HashMap<Integer, NeighborNode> nbs = new HashMap<Integer, NeighborNode>();		//nbs is the HashMap that stores the UID of neighbor as key 
																					//and details( Host Name, port number and weight) of the neighbors for this node
	BlockingQueue<Message> msgList = new PriorityBlockingQueue<Message>();
	//List<Message> msgList = Collections.synchronizedList(new ArrayList<Message>());
	
	HashMap<Integer,ClientRequestHandler> connectedClients = new HashMap<Integer,ClientRequestHandler>();// all the connected clients
	
	HashMap<Integer,ClientRequestHandler> inTreeClients=new HashMap<Integer,ClientRequestHandler>();// found as intree clients
	HashMap<Integer,ClientRequestHandler> nonTreeClients=new HashMap<Integer,ClientRequestHandler>();// found as nontree clients
	

	public void addClient(int clientUID,ClientRequestHandler requestHandler)
	{
		this.connectedClients.put(clientUID,requestHandler);
		System.out.println("Client added: "+clientUID);
	}
	
	public void addMsg(Message msg)
	{
		this.msgList.add(msg);
	}
	public Message getHeadMessageFromQueue() {
//		if(this.msgList.peek() != null)
//		{
//			System.out.println("Message phase:"+this.msgList.peek().phase);
//			System.out.println("My phase:"+this.phase);
//		}
//		System.out.println("My phase:"+this.phase);
		if (this.msgList.peek() != null && this.msgList.peek().phase==this.phase) {
			Message msg = this.msgList.peek();
			System.out.println("TOP most in the queue is: from:"+msg.senderUID+" at phase "+msg.phase+" with message "+msg.type);
			this.msgList.remove();
			return msg;
		}
		else if(this.msgList.peek() != null && this.msgList.peek().phase==this.phase-1)
		{
			this.msgList.remove();
		}
		return null;
	}
}
