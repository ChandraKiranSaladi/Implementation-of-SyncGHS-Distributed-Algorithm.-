import java.io.Serializable;
public class Message implements Serializable, Comparable<Message> {
	MessageType type;
	int senderUID;
	int leaderUID;
	boolean processed;
	MinWeightOGE minWeightOGE;
	int phase;
	Message(int UID, int leaderUID, MessageType type, int phase)
	{
		this.senderUID=UID;
		this.type=type;
		this.leaderUID= leaderUID;
		this.processed=false;
		this.minWeightOGE=new MinWeightOGE();
		this.phase=phase;
	}
	Message(int UID, int leaderUID, MessageType type,MinWeightOGE min,int phase)
	{
		this.senderUID=UID;
		this.type=type;
		this.leaderUID= leaderUID;
		this.processed=false;
		this.minWeightOGE=min;
		this.phase=phase;
	}

	public int compareTo(Message msg) {
		if (this.phase < msg.phase) {
			return -1;
		}

		if (this.phase > msg.phase) {
			return 1;
		}
		// Should not reach here
		return 0;
	}
	
}
