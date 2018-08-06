import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClient {
	Node current;
	int neighborUID;
	NeighborNode nbrn;
	Socket clientSocket;
	
	ObjectInputStream in;
	ObjectOutputStream out;
	
	TCPClient(Node current, int neighborUID, NeighborNode nbrn)
	{
		this.current = current;
		this.neighborUID= neighborUID;
		this.nbrn = nbrn;
	}
	public void listenSocket()
	{
		try {
			clientSocket = new Socket(nbrn.hostName,nbrn.portNumber);
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}

	public void handShake()
	{
		try {
			Message msg =new Message(current.UID,current.UID,MessageType.HandShake,0);
			out.writeObject(msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void listeningMessages()
	{
		try{
			while (true) 
			{
				Message msg = (Message) in.readObject();
				// add received messages to Blocking queue
				this.current.addMsg(msg);
				//System.out.println("Message received:"+msg.type);
			} 
		}
		catch(Exception e)
		{
			//System.out.println("Exception in client "+nbrn.UID);
			e.printStackTrace();
			System.exit(1);
		}
	}
}
