import java.io.IOException;
import java.util.*;

public class SyncGHS {
	Node current;
	boolean MWOE=true;
	boolean safe,canMove;
	int count;
	List<Message> tempMsgList= new ArrayList<Message>();
	List<Integer> successorUID= new ArrayList<Integer>();
	SyncGHS(Node current)
	{
		
		current.phase=0;
		count=current.connectedClients.size();
		System.out.println("Starting SyncGHS...");
		this.current = current;
		//At the starting of the SyncGHS, all the clients are nonTreeClients
		current.nonTreeClients.putAll(current.connectedClients);
		//System.out.println("nonTreeClients:	");
		MWOE=false;
		resetMWOE();
		System.out.println("Starting FindMWOE...");
		if(current.minWeightOGE!=null)
		{
			System.out.println("MWOE: "+current.minWeightOGE);
		}
		System.out.println("Current phase"+current.phase);
		sendFindMWOE();
		current.phase++;
		if(current.minWeightOGE!=null)
		{
			System.out.println("MWOE: "+current.minWeightOGE);
		}
		//System.out.println("Ended FindMWOE...");
		//System.out.println("\n\n\n");
		//System.out.println("Starting sendTestMessage...");
		sendTestMessage();
		current.phase++;
		if(current.minWeightOGE!=null)
		{
			System.out.println("MWOE: "+current.minWeightOGE);
		}
		// System.out.println("Ended sendTestMessage...");
		// System.out.println("\n\n\n");
		// System.out.println("Sending Ack...");
		sendAck();
		System.out.println("Ack sent...");
		if(current.minWeightOGE!=null)
		{
			System.out.println("MWOE: "+current.minWeightOGE);
		}
		
		processAck();
		System.out.println("Ack proccessed..");
		current.phase++;
		if(current.minWeightOGE!=null)
		{
			System.out.println("MWOE: "+current.minWeightOGE);
		}
		// System.out.println("In loop: "+i);
					sendMinWeightOGEToPredecessor();
					current.phase++;
					System.out.println("Min Outgoing edge to the predecessor is sent..");
					if(current.minWeightOGE!=null)
					{
						System.out.println("MWOE: "+current.minWeightOGE);
					}
					broadCastMWOE();
					current.phase++;
					System.out.println("Broadcast MWOE..");
					if(current.minWeightOGE!=null)
					{
						System.out.println("MWOE: "+current.minWeightOGE);
					}
		int i=0;
		do
		{
			merge();
			current.phase++;
			System.out.println("Merge completed..");
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			current.leaderUID=0;
			checkMerge();
			System.out.println("Merge checked..");
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			for(Map.Entry<Integer, ClientRequestHandler> x:current.inTreeClients.entrySet())
			{
				System.out.println("..........Intree"+x.getKey());
			}
			successorUID.clear();
			broadCastLeader();
			System.out.println("broadcast leader..");
			System.out.println("....Leader is...."+current.leaderUID);
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			current.msgList.clear();
			//leaderReceived();
			// System.out.println("clear message..");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Starting next phase!!!!!!");
			MWOE=false;
			resetMWOE();
			System.out.println("Starting FindMWOE...");
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			System.out.println("Current phase"+current.phase);
			sendFindMWOE();
			current.phase++;
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			//System.out.println("Ended FindMWOE...");
			//System.out.println("\n\n\n");
			//System.out.println("Starting sendTestMessage...");
			sendTestMessage();
			current.phase++;
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			// System.out.println("Ended sendTestMessage...");
			// System.out.println("\n\n\n");
			// System.out.println("Sending Ack...");
			sendAck();
			System.out.println("Ack sent...");
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			
			processAck();
			System.out.println("Ack proccessed..");
			current.phase++;
			if(current.minWeightOGE!=null)
			{
				System.out.println("MWOE: "+current.minWeightOGE);
			}
			// System.out.println("In loop: "+i);
						sendMinWeightOGEToPredecessor();
						current.phase++;
						System.out.println("Min Outgoing edge to the predecessor is sent..");
						if(current.minWeightOGE!=null)
						{
							System.out.println("MWOE: "+current.minWeightOGE);
						}
						broadCastMWOE();
						current.phase++;
						System.out.println("Broadcast MWOE..");
						if(current.minWeightOGE!=null)
						{
							System.out.println("MWOE: "+current.minWeightOGE.weight);
						}
						if(current.minWeightOGE!=null)
						if(current.minWeightOGE.weight!=Integer.MAX_VALUE)
							MWOE=true;
		}while( MWOE );
		System.out.println("This is node: "+current.UID);
		System.out.println("Tree edges are: ");
		for(Map.Entry<Integer, ClientRequestHandler> inTree: current.inTreeClients.entrySet())
		{
			System.out.println("("+current.UID+","+inTree.getKey()+")");
		}
	}
	public void resetMWOE()
	{
		if(current.minWeightOGE!=null)
		{
			current.minWeightOGE.treeNodeUID =0;
			current.minWeightOGE.nonTreeNodeUID=0;
			current.minWeightOGE.weight=Integer.MAX_VALUE;
		}
		else
		{
			current.minWeightOGE=new MinWeightOGE();
			current.minWeightOGE.treeNodeUID =0;
			current.minWeightOGE.nonTreeNodeUID=0;
			current.minWeightOGE.weight=Integer.MAX_VALUE;
		}

	}
	public void sendFindMWOE() 
	{
		broadCast(MessageType.FindMWOE,current.inTreeClients,current.UID==current.leaderUID,null);
		System.out.println("BroadCast FindMWOE completed");
	}
	
	public void sendTestMessage()
	{
		broadCast(MessageType.Test,current.nonTreeClients,true,null);
		System.out.println("sendTestMessage completed");
	}
	public void sendAck()
	{
		List<Integer> tempSentMsg = new ArrayList<Integer>();
		tempMsgList.forEach(m->{
			if(m.type==MessageType.Test)
			{
				tempSentMsg.add(m.senderUID);
				ClientRequestHandler crh=current.connectedClients.get(m.senderUID);
				if(m.leaderUID==current.leaderUID)
				{
					try {
						// System.out.println("Sending reject to:	"+m.senderUID);
						crh.out.writeObject(new Message(current.UID,current.leaderUID,MessageType.Reject, current.phase));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						// System.out.println("My leader"+current.leaderUID+"	Sender's leader"+m.leaderUID);
						// System.out.println("Sending accept to:	"+m.senderUID);
						crh.out.writeObject(new Message(current.UID,current.leaderUID,MessageType.Accept, current.phase));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			});
		tempMsgList.clear();
		for(Map.Entry<Integer, ClientRequestHandler> x: current.connectedClients.entrySet())
		{
			if(!tempSentMsg.contains(x.getKey()) || x.getKey()==current.predecessorUID)
			{
				System.out.println("Sending NULL to "+x.getKey());
				try {
					x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,MessageType.NULL,current.minWeightOGE ,current.phase));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		int received=0;
		while(received!=count)
		{
			Message msg=current.getHeadMessageFromQueue();
			if(msg!=null)
			{
				if(msg.type==MessageType.NULL)
				{
					System.out.println(msg.type+" received from "+msg.senderUID);
					received++;
				}
				else if(msg.type==MessageType.Accept || msg.type==MessageType.Reject)
				{
					tempMsgList.add(msg);
					received++;
				}
			}
		}
	}
	public void processAck()
	{
		for(Message msg: tempMsgList)
		{
			if(msg.type==MessageType.Accept)
			{
				MWOE=true;
				int neighborUID=msg.senderUID;
				NeighborNode n= current.nbs.get(neighborUID);
				if(current.minWeightOGE!=null)
				{
					int a=current.minWeightOGE.nonTreeNodeUID;
					int b=current.minWeightOGE.treeNodeUID;
					int UID1=Math.min(a, b);
					int UID2 = Math.max(a, b);
					a=current.UID;
					b=n.UID;
					int UID3=Math.min(a, b);
					int UID4 = Math.max(a, b);
					boolean flag=false;
					if(current.minWeightOGE.weight>n.weight)
						flag=true;
					else if(current.minWeightOGE.weight==n.weight)
					{
						if(UID1<UID3)
							flag=true;
						else if(UID1==UID3)
							if(UID2<UID4)
								flag=true;
					}
					if( flag)
					{
						current.minWeightOGE.weight=n.weight;
						current.minWeightOGE.nonTreeNodeUID=neighborUID;
						current.minWeightOGE.treeNodeUID=current.UID;
					}
				}
				
			}
			else if(msg.type==MessageType.Reject)
			{
				int clientUID=msg.senderUID;
				current.nonTreeClients.remove(clientUID);
			}
		}
	}
	public void sendMinWeightOGEToPredecessor()
	{
		int received=0;
		int count=0;
		successorUID.clear();
		if(current.UID==current.leaderUID)
		{
			count=current.inTreeClients.size();
		}
		else
		{
			count=current.inTreeClients.size()-1;
		}
		while(received!=count)
		{
			for(Message msg:current.msgList)
			{
				if(msg.type==MessageType.MWOEFromSuccessor)
				{
					successorUID.add(msg.senderUID);
					received++;
					msg.processed=true;
					if(msg.minWeightOGE!=null && msg.minWeightOGE.weight<current.minWeightOGE.weight)
					{
						current.minWeightOGE=msg.minWeightOGE;
					}
				}
			}
		}
		if(current.UID!=current.leaderUID)
			try {
				current.connectedClients.get(current.predecessorUID).out.writeObject(new Message(current.UID,current.leaderUID,MessageType.MWOEFromSuccessor,current.minWeightOGE,current.phase ));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void broadCastMWOE()
	{
		for(Map.Entry<Integer, ClientRequestHandler> x: current.inTreeClients.entrySet())
		{System.out.println("InTREE "+x.getKey());
		}
		if(current.UID==current.leaderUID)
			broadCastM(MessageType.ToMerge,current.inTreeClients,true,current.minWeightOGE);
		else
		{
			broadCastM(MessageType.ToMerge,current.inTreeClients,false,current.minWeightOGE);
		}
	}
	
	
	public void broadCastM(MessageType msgType,HashMap<Integer,ClientRequestHandler> clients,boolean check,MinWeightOGE min )
	{
		try {
		while(!check)
		{
			Message msg=current.getHeadMessageFromQueue();
			if(msg!=null)
			{
				if(msg.type==msgType)
				{
					System.out.println(msg.type+" received from "+msg.senderUID);
					current.predecessorUID= msg.senderUID;
					min=msg.minWeightOGE;
					current.minWeightOGE=min;
					check=true;
					if(msgType==MessageType.BroadCastLeader)
					current.leaderUID=msg.leaderUID;
				}
			}
		}
		if(check)
		{
				for(Map.Entry<Integer, ClientRequestHandler> x: clients.entrySet())
				{
					if(x.getValue().clientUID!=current.predecessorUID)
					{
						x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,msgType,null ,current.phase));
					}
				}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	
	
	
	public void broadCast(MessageType msgType,HashMap<Integer,ClientRequestHandler> clients,boolean check,MinWeightOGE min )
	{
		try {
			System.out.println("Checking broad cast for "+msgType);
			safe=false;
			canMove=false;
			int received=0;
			count = current.connectedClients.size();
			while(received!=count)
			{
				while(!check || (msgType!=MessageType.FindMWOE && msgType!=MessageType.Test && msgType!=MessageType.BroadCastLeader && received!=count))
				{
					Message msg=current.getHeadMessageFromQueue();
					if(msg!=null)
					{
						if(msg.type==msgType)
						{
							System.out.println(msg.type+" received from "+msg.senderUID);
							current.predecessorUID= msg.senderUID;
							min=msg.minWeightOGE;
							current.minWeightOGE=min;
							check=true;
							if(msgType==MessageType.BroadCastLeader)
							current.leaderUID=msg.leaderUID;
							received++;
						}
						else if(msg.type==MessageType.NULL)
						{
							System.out.println(msg.type+" received from "+msg.senderUID);
							received++;
						}
					}
				}
				if(!safe)
				{
					{
						for(Map.Entry<Integer, ClientRequestHandler> x: clients.entrySet())
						{
							if(x.getKey()!=current.predecessorUID)
							{
								if(msgType==MessageType.FindMWOE)
									x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,msgType,null ,current.phase));
								else
								x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,msgType,min ,current.phase));
								System.out.println("BroadCast Message send to "+x.getKey()+"	with the message:"+msgType+" with phase"+current.phase);
								//System.out.println("Current.UID:"+current.UID+"	current.leaderUID:"+current.leaderUID+"	msgType:"+msgType);
							}
						}
						for(Map.Entry<Integer, ClientRequestHandler> x: current.connectedClients.entrySet())
						{
							if(!clients.containsKey(x.getKey()) || x.getKey()==current.predecessorUID)
							{
								System.out.println("Sending NULL to "+x.getKey());
								if(msgType==MessageType.FindMWOE)
									x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,MessageType.NULL,null ,current.phase));
								else
								x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,MessageType.NULL,min ,current.phase));
							}
								
						}
					}
					safe=true;
				}
				Message msg=current.getHeadMessageFromQueue();
				if(msg!=null)
				{
					if(msg.type==MessageType.NULL)
					{
						System.out.println(msg.type+" received from "+msg.senderUID);
						received++;
					}
					else if(msg.type==msgType)
					{
						tempMsgList.add(msg);
						received++;
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void merge()
	{
		tempMsgList.clear();
		MessageType msgType = MessageType.Merge;
		boolean check = (current.minWeightOGE!=null && current.UID==current.minWeightOGE.treeNodeUID);
		try {
			System.out.println("Checking broad cast for "+msgType);
			safe=false;
			canMove=false;
			count=current.connectedClients.size();
			int received=0;
			if(!check)
			{
				for(Map.Entry<Integer, ClientRequestHandler> x: current.connectedClients.entrySet())
				{
					System.out.println("Sending NULL to "+x.getKey());
					x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,MessageType.NULL,current.minWeightOGE ,current.phase));
						
				}
			}
			else
			{
				for(Map.Entry<Integer, ClientRequestHandler> x: current.connectedClients.entrySet())
				{
					if(x.getKey()!=current.minWeightOGE.nonTreeNodeUID)
					{
						System.out.println("Sending NULL to "+x.getKey());
						x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,MessageType.NULL,current.minWeightOGE ,current.phase));
					}
					else
					{
						current.connectedClients.get(current.minWeightOGE.nonTreeNodeUID).out.writeObject(new Message(current.UID,current.leaderUID,MessageType.Merge,current.minWeightOGE ,current.phase));
						current.inTreeClients.put(current.minWeightOGE.nonTreeNodeUID,current.connectedClients.get(current.minWeightOGE.nonTreeNodeUID));
						current.nonTreeClients.remove(current.minWeightOGE.nonTreeNodeUID);
					}
						
				}
				
			}	
			while(received!=count)
			{
				Message msg=current.getHeadMessageFromQueue();
				if(msg!=null)
				{
					if(msg.type==MessageType.NULL)
					{
						System.out.println(msg.type+" received from "+msg.senderUID);
						received++;
					}
					else if(msg.type==msgType)
					{
						tempMsgList.add(msg);
						received++;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void checkMerge()
	{
		for(Message msg: tempMsgList)
		{
				if(msg.minWeightOGE.treeNodeUID==current.minWeightOGE.nonTreeNodeUID )
				{
//					System.out.println("Sender UID"+msg.senderUID);
//					System.out.println("Current UID"+current.UID);
					if(msg.senderUID<current.UID)
					{
						current.leaderUID=current.minWeightOGE.treeNodeUID;
//						System.out.println(".....TREE NODE...."+current.leaderUID);
						current.predecessorUID=current.UID;
					}
				}
				current.inTreeClients.put(msg.minWeightOGE.treeNodeUID, current.connectedClients.get(msg.minWeightOGE.treeNodeUID));
				current.nonTreeClients.remove(msg.minWeightOGE.treeNodeUID);
		}
	}
	public void broadCastLeader()
	{
		broadCastl(MessageType.BroadCastLeader,current.inTreeClients,current.UID==current.leaderUID,null);
		current.phase++;
	}
	public void broadCastl(MessageType msgType,HashMap<Integer,ClientRequestHandler> clients,boolean check,MinWeightOGE min )
	{
		tempMsgList.clear();
		try {
			System.out.println("Checking broad cast for "+msgType);
			safe=false;
			canMove=false;
			count=current.connectedClients.size();
			int received=0;
			while(!check)
			{
				Message msg=current.getHeadMessageFromQueue();
				if(msg!=null)
				{
					if(msg.type==msgType)
					{
						System.out.println(msg.type+" received from "+msg.senderUID);
						current.predecessorUID= msg.senderUID;
						min=msg.minWeightOGE;
						current.minWeightOGE=min;
						check=true;
						if(msgType==MessageType.BroadCastLeader)
						current.leaderUID=msg.leaderUID;
						received++;
					}
					else if(msg.type==MessageType.NULL)
					{
						System.out.println(msg.type+" received from "+msg.senderUID);
						received++;
					}
				}
			}
			if(!safe)
			{
				List<Integer> temp= new ArrayList<Integer>();
					for(Map.Entry<Integer, ClientRequestHandler> x: clients.entrySet())
					{
						temp.add(x.getKey());
						if(x.getValue().clientUID!=current.predecessorUID)
						{
							successorUID.add(x.getKey());
							x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,msgType,min ,current.phase));
						}
					}
					for(Map.Entry<Integer, ClientRequestHandler> x: current.connectedClients.entrySet())
					{
						if(!temp.contains(x.getKey()) || (x.getKey()==current.predecessorUID && current.UID!=current.predecessorUID))
						{
							x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,MessageType.NULL,min ,current.phase));
						}
					}
					safe=true;
					
			}
			while(received!=count)
			{
				Message msg=current.getHeadMessageFromQueue();
				if(msg!=null)
				{
					if(msg.type==MessageType.NULL)
					{
						System.out.println(msg.type+" received from "+msg.senderUID);
						received++;
					}
					else if(msg.type==msgType)
					{
						tempMsgList.add(msg);
						received++;
					}
				}
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void leaderReceived()
	{
		System.out.println("LR started");
		boolean check=(successorUID==null || successorUID.size()==0);
		int received=0;
		int count=current.inTreeClients.size();
		safe=false;
		while(received!=count)
		{
//			System.out.println("Received at the start"+received);
//			System.out.println("Count at the start"+count);

			while(!check)
			{
				int lr=0;
				Message msg=current.getHeadMessageFromQueue();
				{
					if(msg!=null)
					{
						if(msg.type==MessageType.LR)
						{
							System.out.println("LR received from"+msg.senderUID);
							received++;
							System.out.println("Received value"+received);
							lr++;
							if(lr==successorUID.size())
							check=true;
						}
						else if(msg.type==MessageType.NULL)
						{
							System.out.println("NULL received from"+msg.senderUID);
							received++;
							System.out.println("Received value"+received);
							
						}
					}
					
				}
			}
			if(!safe)
			{
				System.out.println("I am HERE 1");
				System.out.println(current.UID);
				System.out.println(current.leaderUID);
				if(current.UID!=current.leaderUID)
				{
					try {
						current.connectedClients.get(current.predecessorUID).out.writeObject(new Message(current.UID,current.leaderUID,MessageType.LR,current.minWeightOGE,current.phase ));
						System.out.println("LR sent to "+current.predecessorUID+" with the phase "+current.phase);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for(Map.Entry<Integer, ClientRequestHandler> x: current.inTreeClients.entrySet())
				{
					if( (x.getKey()!=current.predecessorUID))
					{
						try {
							
							x.getValue().out.writeObject(new Message(current.UID,current.leaderUID,MessageType.NULL,current.minWeightOGE ,current.phase));
							System.out.println("NULL sent to "+x.getKey()+" with the phase "+current.phase);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				safe=true;
			}
			
			Message msg=current.getHeadMessageFromQueue();
			{
				//System.out.println("Processing Message");
				if(msg!=null)
				if(msg.type==MessageType.LR || msg.type==MessageType.NULL)
				{
					System.out.println(msg.type+" received from "+msg.senderUID);
					System.out.println("Received value"+received);
					received++;
				}
			}
		}
		System.out.println("LEADER RECEIVED!!!!!!!!.........");
	}
}
