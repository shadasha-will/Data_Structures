public class Fibonacci_classic {
	
	Node root;
	int nodeNumbs;
	Node[] all_elements;
	
	static class Node {
		
		public Node(int element, int key) {
			this.key = key;
			this.element = element;
		}

		int key;
		int element;
		Node left_sibling, right_sibling, parent, child;
		boolean flag;
		int degree;
	}
	
	public Fibonacci_classic(int N) {
		nodeNumbs = 0;
		this.all_elements = new Node[N];
		root = new Node(-1, -1);
		root.child = null;
		
	}
	
	
	public void append_node(Node newChild, Node newParent) {
		// check if new child is previous parent's only child
		Node oldParent = newChild.parent;
		if (newChild == newChild.right_sibling) {
			if(oldParent != null) {
				oldParent.child = null;
				oldParent.degree--;
			}	
		}
		// if it is not the only child update previous pointers
		else {
			if (newChild == oldParent.child) {
				oldParent.child = newChild.right_sibling;
			}
			Node prevLeft = newChild.left_sibling;
			Node prevRight = newChild.right_sibling;
			prevLeft.right_sibling = newChild.right_sibling;
			prevRight.left_sibling = newChild.left_sibling;
			oldParent.degree--;
		}
		// start updating the new pointers for siblings
		if (newParent.child == null) {
			newParent.child = newChild;
			newChild.right_sibling = newChild;
			newChild.left_sibling = newChild;
		}
		else {
			Node newSibling = newParent.child;
			newSibling.left_sibling.right_sibling = newChild;
			newChild.left_sibling = newSibling.left_sibling;
			newChild.right_sibling = newSibling;
			newSibling.left_sibling = newChild; }
		
		newChild.parent = newParent;
		newParent.degree++;
	}
	
	public void insert(int elem, int key_vals) {
		Node newNode = new Node(elem, key_vals);
		newNode.degree = 0;
		newNode.child = null;
		newNode.flag = false;
		newNode.right_sibling = newNode;
		newNode.left_sibling = newNode;
		all_elements[elem] = newNode;
		nodeNumbs = nodeNumbs + 1;
		// if no root in the tree the node becomes the root
		if (root.child == null) {
			root.child = newNode;
			root.child.left_sibling = root.child;
			root.child.right_sibling = root.child;
			root.child.parent = root;
			root.degree += 1;
			return;
		}
		else {
			append_node(newNode, root);
			if(key_vals < root.child.key) {
				root.child = newNode;
				// if the value is less than the minimum then change the minimum pointer
				return;
			}	
		} 
	}
	
	public long extract_Min() {
		long steps = 0;
		long cons_steps = 0;
		if (root.child == null) {
			return 0;
		}
		else {
			// get all of the nodes details
			Node currentChild = root.child.child;
			
			all_elements[root.child.element] = null;
			steps += root.child.degree;
			
			if (currentChild != null) {
				while(currentChild != null) {
					append_node(currentChild, root); 
					currentChild = root.child.child;}				
			}  
			// if the minimum is the only item in the root list then the tree is empty
			Node right = root.child.right_sibling;
			if (root.child == right) {
				root.child = null;
			}
			else {
				//update the neighbors
				Node old_left = root.child.left_sibling;
				Node old_right = root.child.right_sibling;
				old_left.right_sibling = old_right;
				old_right.left_sibling = old_left;
				root.child = old_right;
				root.degree -= 1;
				cons_steps = consolidate();
				update_min();
				
			}
			
			
		}
		nodeNumbs -= 1;
		steps = steps + cons_steps;
		return steps;
	}
	
	public void update_min() {
		Node current;
		current = root.child.right_sibling;
		while(current != root.child) {
			if(current.key < root.child.key) {
				root.child = current;	
			}
			current = current.right_sibling;
		}
		root.child = root.child;
	}
	
	public Node heap_link(Node y, Node x) {
		if(y.key < x.key) {
			Node tmp = y;
			y = x;
			x = tmp;
		}
		append_node(y, x);
		y.flag = false;
		return x;
	}
	
	public long consolidate() {
		// check if the heap is empty
		long steps = 0;
		if (root.child != null) {
			Node[] a = new Node[45];
			// use starter node for loop
			Node start = new Node(-2,-2);
			Node next = root.child;
			start.right_sibling = next;
			start.left_sibling = next.left_sibling;
			next.left_sibling.right_sibling = start;
			next.left_sibling = start;
			
			 while(next != start) {
				Node x = next;
				Node next_root = next.right_sibling;
				int d = x.degree;
				while (a[d] != null) {
					Node y = a[d];					
					x = heap_link (y,x);
					a[d] = null;
					d = d + 1;
					steps++; }
				a[d] = x;
				next = next_root; }
			start.left_sibling.right_sibling = start.right_sibling;
			start.right_sibling.left_sibling = start.left_sibling;
			}
			
			return steps;
			}
	
	public void decrease_key(int e, int k) {
		Node nodeToDecrease = all_elements[e];
		if (nodeToDecrease == null) {
			return;
		}
		if (k > nodeToDecrease.key) {
			System.out.println("The new key inserted is greater than the current key");
			return;
		}
		
		nodeToDecrease.key = k;
		Node Parent = nodeToDecrease.parent;
		
		if (Parent != root && nodeToDecrease.key < Parent.key) {
			fibo_cut(nodeToDecrease);
			fibo_cascading_cut(Parent);
		}
		if (nodeToDecrease.key < root.child.key) {
			root.child = nodeToDecrease;
		}
	}
	
	public void fibo_cut(Node x) {
		append_node(x, root);
		x.flag = false;
	}
	public void fibo_cascading_cut(Node y) {
		Node z = y.parent;
		if (z != root) {
			if(y.flag == false) {
				y.flag = true;
			}
			else {
				fibo_cut(y);
				fibo_cascading_cut(z);
			}
		}
	}
}
