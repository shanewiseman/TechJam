package com.akamai.techjam.distributedsystem;



//##############################################################################
//##############################################################################
public class Tree {
	
	treeNode[] structure;
	
	public Tree (Node [] nodes){
		
		this.structure = new treeNode[ nodes.length ];
		
		this.structure[0] = new treeNode(0, nodes[0]);
		
		for(int index = 1; index < nodes.length; index++){
			float nodeFind = index;
			
			this.structure[ index ] = new treeNode( (int)Math.round( ( nodeFind / 2  ) - 1 ), nodes[index] );
			
			if( (index % 2) == 0 )
				this.structure[ Math.round(( nodeFind / 2 ) - 1) ].right = this.structure[ index ];
			else 
				this.structure[ Math.round(( nodeFind / 2 ) - 1) ].left = this.structure[ index ];
		}
	}
//##############################################################################
//##############################################################################	
	public Tree( Node [] xNodes, Node [] yNodes){
		
		this.structure = new treeNode[ (xNodes.length + yNodes.length) ];
		
		int xCounter = 1;
		int yCounter = 0;	
		float branch = 1;
		
		this.structure[0] = new treeNode(0, xNodes[0]);
		
		for( int index = 1; index < (xNodes.length + yNodes.length ); index++ ){
			branch = index;
			float nodeFind = index;

			while ( branch != 1 && (int)branch != 2 ){
				branch = Math.round( (branch / 2 ) - 1);
			}
			
			if( (int)branch == 1 || yCounter >= yNodes.length){
				if( xCounter < xNodes.length ){
					this.structure[ index ] = new treeNode( ( Math.round( nodeFind /2 ) - 1 ), xNodes[xCounter] );
					this.structure[ ( Math.round( nodeFind / 2 ) - 1) ].left = this.structure[index ];
					xCounter++;
					continue;	
				} 
				

			}
			if ( (int)branch == 2 || (xCounter >= xNodes.length)  ){
				if(yCounter < yNodes.length){
					this.structure[ index ] = new treeNode( (Math.round( nodeFind / 2 ) - 1 ), yNodes[yCounter] );
					yCounter++;
					continue;
				}
			}
	
		}
		
	}
//##############################################################################	
//##############################################################################	
	public Node[] returnNodes (){
		
		Node [] returnNodes = new Node [ this.structure.length ];
		for( int i = 0; i < this.structure.length; i++ ){
			returnNodes[i] = this.structure[i].node;
		}
		
		return returnNodes;
	}
	
}
//##############################################################################
//##############################################################################
class treeNode {
	
	public int      id;
	public int      parent;
	public treeNode left;
	public treeNode right;
	public Node     node;
	
	public static int idCounter = 0;
	
	public treeNode (int parent , Node node) {
				
		this.node   = node;
		this.parent = parent;
		this.left   = null;
		this.right  = null;
		this.id = idCounter;

		idCounter++;
	}
	
}
