
public class NeighborNode {
	int UID;
	String hostName;
	int portNumber;
	int weight;
	NeighborNode(int UID,String hostName, int portNumber, int weight)
	{
		this.UID = UID;
		this.hostName= hostName;
		this.portNumber= portNumber;
		this.weight=weight;
	}
}
