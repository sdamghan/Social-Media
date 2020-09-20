
import java.util.ArrayList;

public class Treap {
	
	//private static int timer = 1;
	private static TNode minTNode;
	final static int maxSize = 1000;
	
	
	private class TNode {
		
		public int numOfHit;
		public int heapKey;
		public String pageSlug;
		
		String filename;
		int offset;
			
		public TNode parent;
		public TNode leftChild;
		public TNode rightChild;
		
		TNode ( String ps, TNode father, int hk ) {
			
			parent = father;
			pageSlug = ps;
			numOfHit = hk;
			heapKey = (int)priority(numOfHit);
			
		}
	}
	
	private int treapSize;
	private TNode root;
	
	Treap () {
		
		root = new TNode("", null, 1);
		treapSize = 0;
	}
	
	TNode extractMax( String pageSlug ) {
		
		return find ( root, pageSlug );
	}

	public void add( ArrayList<String> temp ) {
		
		for (int i = 0; i < temp.size(); i++) {
			
			hit(temp.get(i));
		}
	}
	
	private void update ( TNode findedNode ) {
		
		findedNode.numOfHit++;
		findedNode.heapKey = (int)priority(findedNode.numOfHit);
		rotation( findedNode );
	}
	
	public void hit ( String pageSlug ) {
		
		if ( root == null ){

			root = new TNode ( pageSlug, null, 100 );
			return;
		}
			
			
			
		TNode findedNode = find ( root, pageSlug );
		
		if ( findedNode != null ){
			
			update ( findedNode );			
		}
		
		else {
		
			insert ( pageSlug );
		}		
	}
	
	private void insert ( String pageSlug ) {
		 
		if ( treapSize >= maxSize ){
			
			deleteMin();
		}
		putInProperPlace( pageSlug );		
	}
	
	private void putInProperPlace ( String pageSlug ){
		
		TNode father = null
			, tempParent = root
			, tempRChild = root.rightChild
			, tempLChild = root.leftChild;
		
		
		int slugDiff=0;
		
		while ( tempParent != null ) {
			
			slugDiff = pageSlug.compareTo ( tempParent.pageSlug );	
			
			if ( slugDiff < 0 ) {
				
				father = tempParent;
				tempParent = tempLChild;
				
				if ( tempParent != null ) {
					
					tempRChild = tempParent.rightChild;
					tempLChild = tempParent.leftChild;
				}
			}
			
			
			else if ( slugDiff > 0 ) {
				
				father = tempParent;
				tempParent = tempRChild;
				
				if ( tempParent != null ) {
					
					tempRChild = tempParent.rightChild;
					tempLChild = tempParent.leftChild;
				}			
			}			
		}
		
		treapSize++;
		tempParent = new TNode ( pageSlug, father, 100 );
		
		if ( slugDiff < 0 )
			father.leftChild = tempParent;
		
		else if ( slugDiff > 0 )
			father.rightChild = tempParent;
			
		
		if ( tempParent.parent != null )
            rotation ( tempParent );
		
		
	}
	
	private void deleteMin () {
		
		minTNode = root;
		findMin( root );
		
		
		if ( minTNode == minTNode.parent.leftChild ){
			minTNode.parent.leftChild = null;System.err.println("-----left");}
		
		if ( minTNode == minTNode.parent.rightChild ){
			minTNode.parent.rightChild = null;}
	}
	
	private void findMin ( TNode tempRoot ) {
				
		if ( tempRoot.heapKey < minTNode.heapKey ){
		
			minTNode = tempRoot;
		}		
		

		if ( tempRoot.rightChild != null ) 
			findMin ( tempRoot.rightChild );
		
		if ( tempRoot.leftChild != null )
			findMin ( tempRoot.leftChild );
		
	}
	
	private void rotation( TNode tempChild ) {
		
		if( tempChild.parent == null )
			return;
		
		TNode currParent = tempChild.parent;
		
		if ( currParent.heapKey >= tempChild.heapKey )
			return;
		
		do{
			
			if ( tempChild == currParent.rightChild ) {
				
				leftRotate( currParent );
				currParent = tempChild.parent;
			}
			
			else if ( tempChild == currParent.leftChild ) {
				
				rightRotate( currParent );
				currParent = tempChild.parent;
			}			
		}while ( tempChild.parent != null && currParent.heapKey <= tempChild.heapKey );
	}
	
	private void rightRotate ( TNode currNode ) {
		
		TNode currLChild = currNode.leftChild
			, currParent = currNode.parent;
		
		currNode.leftChild = currLChild.rightChild;
		if ( currLChild.rightChild != null )
			currLChild.rightChild.parent = currNode;
		
		currLChild.parent = currParent;
		
		if ( currNode.parent == null )
			root = currLChild;
		
		else if ( currNode == currNode.parent.leftChild )
			currParent.leftChild = currLChild;
		
		else
			currParent.rightChild = currLChild;
				
		currLChild.rightChild = currNode;
		currNode.parent = currLChild;
				
	}
	
	private void leftRotate ( TNode currNode ) {
		
		TNode currRChild = currNode.rightChild
			, currParent = currNode.parent;
		
		currNode.rightChild = currRChild.leftChild;
		if ( currRChild.leftChild != null )
			currRChild.leftChild.parent = currNode;
		
		currRChild.parent = currParent;
		
		if ( currNode.parent == null )
			root = currRChild;
		
		else if ( currNode == currNode.parent.leftChild )
			currParent.leftChild = currRChild;
		
		else
			currParent.rightChild = currRChild;
				
		currRChild.leftChild = currNode;
		currNode.parent = currRChild;
				
	}
	
	public TNode find ( TNode tempNode, String pageSlug ) {
		
		if ( tempNode.pageSlug.compareTo( pageSlug ) == 0 )	
			return tempNode;
		
		if ( tempNode.leftChild != null  ) {
			
			TNode temp = find( tempNode.leftChild, pageSlug );
			
			if ( temp != null ) 
				return temp;
		}
		
		if ( tempNode.rightChild != null  ) {
			
			TNode temp = find( tempNode.rightChild, pageSlug );
			
			if ( temp != null ) 
				return temp;
		}
	
		return null;
	}

    void printTreap() {

        print( root );
    }

	private void print( TNode tempRoot ){
		
		System.out.println( "-->" + tempRoot.pageSlug + ", #" + tempRoot.heapKey );
		
		if ( tempRoot.rightChild == null && tempRoot.leftChild == null )
			return;
		

		if ( tempRoot.rightChild != null ) 
			print ( tempRoot.rightChild );
		
		if ( tempRoot.leftChild != null )
			print ( tempRoot.leftChild );
		
		
		
	}
	
	private double priority ( int numOfHit ) {
									     // hamun 1/3 :D
		return java.lang.Math.pow ( numOfHit, 0.333 );
	}
}
