import java.io.*;
import java.util.Scanner;
//this is a traditional splay tree which uses a node class, an insert class, and a splay class

public class Splaytree {
	static int count = 0;
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

	public Splaytree() {
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
		if (root != null) {
			Node current = root;
			Node parent;
			while (true) {
				parent = current;
				if (current.value > vals) {
					current = current.left;
					if (current == null) {
						splay(parent);
						Node l = parent.left;
						toInsert.left = l;
						if (l != null)
							l.parent = toInsert;
						parent.left = toInsert;
						toInsert.parent = parent;
						break;
					}

				} else if (vals < current.value) {
					current = current.right;
					if (current == null) {
						Node r = parent.right;
						splay(parent);
						toInsert.right = r;
						if (r != null)
							r.parent = toInsert;
						parent.right = toInsert;
						toInsert.parent = parent;
						break;
					}

				} else
					return;
			}
		}
		toInsert = null;
	}

	// creates proper rotations for the zig, zig-zig, and zig-zag functions
	private void rotateRight(Node node) {
		if (node == null || node.parent == null || node.parent.left != node) {
			System.out.println("Illegal call to rotate right");
			return;
		}
		Node exParent = node.parent;
		Node subtreeParent = exParent.parent;
		// moves the right subtree to the original parent
		exParent.left = node.right;
		if (node.right != null) {
			node.right.parent = exParent;
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
		if (node == null || node.parent == null || node.parent.right != node) {
			System.out.println("Illegal call to rotate left");
			return;
		}
		Node exParent = node.parent;
		Node subtreeParent = exParent.parent;

		// moves the left nodes subtree to its original parent
		exParent.right = node.left;
		if (node.left != null) {
			node.left.parent = exParent;
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
		if (node == null || node.parent == null) {
			System.out.println("Illegal call to zig");
			return;
		}
		if (node == node.parent.left) {
			rotateRight(node);
		} else if (node == node.parent.right) {
			rotateLeft(node);
		} else {
			System.out.println("Illegal call to zig");
		}
	}

	// creates a zigzag movement if the node is the left child
	// the node moves right and then left to reach the root
	private void zigzag(Node node) {
		if (node == node.parent.left) {
			rotateRight(node);
			rotateLeft(node);
		} else {
			rotateLeft(node);
			rotateRight(node);
		}

	}

	// double rotation of the node
	private void zigzig(Node node) {
		if (node == node.parent.left) {
			rotateLeft(node.parent);
			rotateLeft(node);
		} else {
			rotateRight(node.parent);
			rotateRight(node);

		}

	}

	public boolean search(int element) {
		return findNode(element) != null;
	}

	private Node findNode(int vals) {
		Node r = root;
		while (r != null) {
			if (vals > r.value) {
				r = r.right;
				count++;
			} else if (vals < r.value) {
				r = r.left;
				count++;
			} else {
				splay(r);
				return r;
			}
		}
		return null;
		// splay(element);
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
		boolean yIsLeftChild = (y == z.left);
		boolean xIsLeftChild = (x == y.left);
		boolean yIsRightChild = (y == z.right);
		boolean xIsRightChild = (x == y.right);
		// if(root != null) {
		// return;
		// }
		// if the node is the child of the root then zig
		while (x != root) {
			if (x.parent == root) {
				zig(x);

			}
			// if the child and parent node are the same direction of children
			// then zigzig
			else if (xIsLeftChild && yIsLeftChild || xIsRightChild && yIsRightChild) {
				zigzig(x);
			}
			// if the children are in different directions as the parents then
			// zigzag
			else {
				zigzag(x);
			}
		}
	}
	// function to search for a key in the tree

	// creates a buffered reader to read input from test files
	public static void main(String[] args) throws IOException {
		Splaytree sptree = new Splaytree();

		// read from standard input
		Scanner sc = new Scanner(System.in);
		String line = "";
		int finds = 0;
		// store the size
		int size = 0;

		while (sc.hasNextLine()) {
			line = sc.nextLine();
			if (line.contains("#")) {
				if (finds != 0) {
                                        System.out.println(finds);
					System.out.println(size + " " + ((double) count) / finds);
					count = 0;
					finds = 0;
				}
				sptree = new Splaytree();
				String[] sizes = line.split(" ");
				String sizes2 = sizes[1];
				size = Integer.parseInt(sizes2);
				//System.out.println(size);
				//;
			}
			if (line.contains("I")) {
				String[] inserts = line.split(" ");
				String insert2 = inserts[1];
				int result = Integer.parseInt(insert2);
				sptree.insert(result);
			
			} else if (line.contains("F")) {
				finds++;
				String[] parts = line.split(" ");
				String part2 = parts[1];
				int result2 = Integer.parseInt(part2);
				sptree.search(result2);

			}

	}
			
		// print the last tree
		System.out.println(size + " " + ((double) count) / finds);
		sc.close();
	} 
}



