public class Weight {
	int UID1;
	int UID2;
	int weight;
	public Weight(int uid1, int uid2, int w)
	{
		if(uid1<uid2)
		{
			this.UID1=uid1;
			this.UID2=uid2;
		}
		else
		{
			this.UID2=uid1;
			this.UID1=uid2;
		}
		this.weight=w;
	}
}
