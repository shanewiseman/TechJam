package com.akamai.techjam.distributedsystem;



//##############################################################################
//##############################################################################
public class Tree {
	
	treeNode[] structure;
	
	public Tree (Node [] nodes){
		
		this.structure = new treeNode[ nodes.length ];
		
		this.structure[0] = new treeNode(0, nodes[0]);
		
		for(float index = 1; index < nodes.length; index++){
			
			this.structure[ (int)(index) ] = new treeNode( (int)Math.round( ( index / 2  ) - 1 ), nodes[(int)(index)] );
			
			if( (index % 2) == 0 )
				this.structure[ Math.round(( index / 2 ) - 1) ].right = this.structure[ (int)(index) ];
			else 
				this.structure[ Math.round(( index / 2 ) - 1) ].left = this.structure[ (int)(index) ];
		}
	}
//##############################################################################
//##############################################################################	
	public Tree( Node [] xNodes, Node [] yNodes){
		
		this.structure = new treeNode[ (xNodes.length + yNodes.length) ];
		
		int xCounter = 0;
		int yCounter = 0;	
		float branch = 1;
		
		for( float index = 1; index < (xNodes.length + yNodes.length ); index++ ){
			
			branch = index;
			
			while( branch != 1 || branch != 2){
				branch = Math.round( (branch / 2 ) - 1);
			}
		
			if( branch == 1 ){
				if( xCounter < xNodes.length ){
					this.structure[ (int)(index) ] = new treeNode( ( Math.round( index /2 ) - 1 ), xNodes[xCounter] );
					this.structure[ ( Math.round( branch / 2 ) - 1) ].left = this.structure[(int)(index)];
					xCounter++;
					
				}

			} else if ( branch == 2){
				if(yCounter < yNodes.length){
					this.structure[ (int)(index) ] = new treeNode( (Math.round( index /2 ) - 1 ), yNodes[yCounter] );
					yCounter++;
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
