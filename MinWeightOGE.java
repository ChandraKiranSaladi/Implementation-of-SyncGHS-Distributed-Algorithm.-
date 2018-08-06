import java.io.Serializable;
public class MinWeightOGE implements Serializable
{
	int treeNodeUID;
	int nonTreeNodeUID;
	int weight;
	MinWeightOGE()
	{
		this.treeNodeUID =0;
		this.nonTreeNodeUID=0;
		this.weight=Integer.MAX_VALUE;
	}
}