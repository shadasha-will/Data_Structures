
// naive implementation
public class naive_fibonacci extends Fibonacci_classic {
	
	public naive_fibonacci(int N) {
		super(N);
	}
	
	public void decrease_key(int e, int k) {
		Node nodeToDecrease = all_elements[e];
		if (nodeToDecrease == null) {
			return;
		}
		if (k > nodeToDecrease.key) {
			return;
		}
		nodeToDecrease.key = k;
		Node parent = nodeToDecrease.parent;
		if (nodeToDecrease.key < parent.key) {
			append_node(nodeToDecrease, root);
		}
		
		if (nodeToDecrease.key < root.child.key) {
			root.child = nodeToDecrease;
		}
	}
	}
	
