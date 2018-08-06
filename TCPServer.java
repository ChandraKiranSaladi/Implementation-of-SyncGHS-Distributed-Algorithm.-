import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class TCPServer {

	Node current;
	ServerSocket serverSocket;

	TCPServer(Node current) {
		this.current = current;
	}

	public void listenSocket() {
		try {
			// System.out.println(current.portNumber);
			serverSocket = new ServerSocket(current.portNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				Socket clientRequestSocket = serverSocket.accept();				
				ClientRequestHandler requestHandler = new ClientRequestHandler(clientRequestSocket, current);				
	
				Thread t = new Thread(requestHandler);
				t.start();				

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
