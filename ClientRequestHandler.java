import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {

	Node current;
	Socket client;
	ObjectInputStream in;
	ObjectOutputStream out;
	int clientUID;

	ClientRequestHandler(Socket requestHandler, Node current) {
		this.current = current;
		this.client = requestHandler;
	}

	public Socket getClient() {
		return client;
	}

	public ObjectInputStream getInputReader() {
		return in;
	}

	public ObjectOutputStream getOutputWriter() {
		return out;
	}

	public void run() {
		try {
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			Message msg = (Message) in.readObject();
			if (msg.type == MessageType.HandShake) {
				this.clientUID = msg.senderUID;
				System.out.println("Text received from client: " + this.clientUID);
				current.addClient(this.clientUID, this);
			} else {
				// add received messages to msgList
				this.current.addMsg(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getClientUID() {
		return this.clientUID;
	}
}
