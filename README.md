Implemented a simple message based Synchronous Distributed System using Sockets and Threads. 
SyncGHS Algorithm: Construction of a minimum Spanning tree using the synchronous version of the infamous GHS Algorithm. 

All the nodes start as a spanning tree with one node(itself) and the leader of the tree being themselves. 

Thereafter, using Merge and Absorb operations these tiny spanning tree's either get absorbed to other neighboring forests or merge to become spanning forests.

Finally, at termination we find ourselves a minimum spanning tree and the leader of the tree being the leader of the network.

Implementation is done using Java and Linux shell scripts. Shell script is used to open multiple terminals and ssh to a server to make each terminal act as a node. 
