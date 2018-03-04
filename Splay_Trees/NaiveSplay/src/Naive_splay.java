//Data Structures Homework 1
//Author: Shadasha Williams


public class Naive_splay {
	// creates a node class that contains a key of a node as well as its parent,
		// left, and right children
		Node root;

		static class Node {
			public Node(int vals) {
				this.value = vals;
			}

			int value;
			Node left, right, parent;
		}

		public Naive_splay() {
			root = null;
		}

		// inserts items into the splay tree if the tree is empty enter it as the
		// root node

		public void insert(int vals) {
			Node toInsert = new Node(vals);
			// if no root in the tree the node becomes the root
			if (root == null) {
				root = toInsert;
				return;
			}
			// check if the value is already in the tree
			else if (root != null) {
				Node current = root;
				Node parent;
				while (current != null) {
					parent = current;
					if (vals < current.value) {
						current = current.left;
						if (current == null) {
							current = toInsert;
							parent.left = current;
							current.parent = parent;
							splay(current);
							break;
						}

					} else if (vals > current.value) {
						current = current.right;
						if (current == null) {
							current = toInsert;
							parent.right = current; 
							current.parent = parent;
							splay(current);
							break;
						}

					} else if(vals == current.value) {
						// the value is already in the tree
						splay(current);
						break;
					}
					else
						return;
				}
			}
			toInsert = null;
		}

		// creates proper rotations for the zig, zig-zig, and zig-zag functions
		private void rotateRight(Node node) {

			if(node == null) {
				System.out.println("Null node");
			}
			else if (node.parent == null) {
				System.out.println("Node is already the root");
			}
			//else if (node.parent.left != node) {
			//	System.out.println("Node is not the parent's left child");
			//}
			
			Node exParent = node.parent;
			Node subtreeParent = exParent.parent;
			// moves the right subtree to the original parent
			
			if (node.right != null) {
				node.right.parent = exParent;
				exParent.left = node.right;
			} else {
				exParent.left = null;
			}
			// make the exparent become child of a node
			node.right = exParent;
			exParent.parent = node;
			// makes original node become a child of the exparent's exparent
			node.parent = subtreeParent;
			if (subtreeParent == null) {
				root = node;
			} else if (subtreeParent.right == exParent) {
				subtreeParent.right = node;
			} else {
				subtreeParent.left = node;
			}
		}

		// rotates a node up to it parent if it is the right child of the parent
		private void rotateLeft(Node node) {
			if (node == null || node.parent == null) {
				System.out.println("Illegal call to rotate left");
				return;
			}
			
			Node exParent = node.parent;
			Node subtreeParent = exParent.parent;

			// moves the left nodes subtree to its original parent
			if (node.left != null) {
				node.left.parent = exParent;
				exParent.right = node.left;
			} else {
				exParent.right = null;
			}
			// make the original parent a child of node
			node.left = exParent;
			exParent.parent = node;

			// make the node become a child of the original child's former parent
			node.parent = subtreeParent;
			if (subtreeParent == null) {
				root = node;
			} else if (subtreeParent.right == exParent) {
				subtreeParent.right = node;
			} else {
				subtreeParent.left = node;
			}
		}

		// creates a method for simple rotations right and left
		public void zig(Node node) {
			Node exParent = node.parent;
			if (node == null || exParent == null) {
				System.out.println("Illegal call to zig");
				return;
			}
			if (node == exParent.left) {
				rotateRight(node);
			} else if (node == exParent.right) {
				rotateLeft(node);
			} else {
				System.out.println("Illegal call to zig");
			}
		}
		
		public int findNode(int vals) {
			Node r = root;
			int count = 0;
			while (r.value != vals) {
				if (vals > r.value) {
					r = r.right;
					count++;
				} else if (vals < r.value) {
					r = r.left;
					count++;
				} if (r == null) {
					count = -1;
					return count;
				}
			}
			splay(r);
			return count;
		}

	public void splay(Node x) {
		Node z, y;
		y = x.parent;
		if (y == null) {
			// x is the root
			return;
		}
		z = y.parent;
		if (z == null) {
			zig(x);
			return;
		}
		// if the node is the child of the root then zig
		while (x != root) {
			zig(x);
		}
	}
	// function to search for a key in the tree

}



