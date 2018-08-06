
public class InvokeMain {
	public static void main(String[] args) {
		System.out.println("ALGORITHM STARTED...");
		//current means this node, corresponding to this dc machine
		Node current = new Node();
		current.phase=0;
		//path stores the path where the config file is present
		String path = "/home/010/c/cx/cxs172130/configGHS.txt";
		
		ParseConfig pc = new ParseConfig(current, path,args[0]);
		
		System.out.println("Initializing Server with UID: " + current.UID);
		
		//Starting the server
		Runnable serverRunnable = new Runnable() {
			public void run()
			{
				TCPServer server = new TCPServer(current);
				server.listenSocket();
			}
		};
		Thread serverThread = new Thread(serverRunnable);
		serverThread.start();
		
		System.out.println("Server started and listening to the client requests......");
		//The server is started wait for 2s before start sending the client requests 
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Send client request to all the neighbors
		current.nbs.forEach((k,v)->{
			Runnable clientRunnable = new Runnable() {
				public void run()
				{
					TCPClient client = new TCPClient(current,k,v);
					client.listenSocket();
					client.handShake();
					client.listeningMessages();
				}
			};
			Thread clientThread = new Thread(clientRunnable);
			clientThread.start();
		});
		
		//The client requests are sent wait for 2s before starting the SyncGHS
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Client set successfully...");
		SyncGHS ghs= new SyncGHS(current);
		
	}
}
