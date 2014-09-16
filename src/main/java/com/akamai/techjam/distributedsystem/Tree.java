package com.akamai.techjam.distributedsystem;




//##############################################################################
//##############################################################################
public class Tree {
	
	private treeNode[] initStructure;
	public treeNode[] structure;
	private treeNode[] mergingStruct;
	private int mergeInterval = 1;
	
	Node       primaryNode;
	int        primaryNodeIndex;
	
	public Tree (Node [] nodes, Node primary, treeNode globalMap){
		
		this.structure = new treeNode[ nodes.length ];
		
		this.structure[0] = new treeNode(0, nodes[0]);
		
		for(int index = 1; index < nodes.length; index++){
			float nodeFind = index;
			
			if( nodes[index].id == primary.id){
				this.primaryNodeIndex = index;
			}
			
			
			this.structure[ index ] = new treeNode( (int)Math.round( ( nodeFind / 2  ) - 1 ), nodes[index] );
			
			if( (index % 2) == 0 )
				this.structure[ Math.round(( nodeFind / 2 ) - 1) ].right = this.structure[ index ];
			else 
				this.structure[ Math.round(( nodeFind / 2 ) - 1) ].left = this.structure[ index ];
		}
		
		this.initStructure = globalMap;
		this.primaryNode   = primary;
	}
//##############################################################################
//##############################################################################	
	public void Merge( Node [] xNodes, Node [] yNodes){
		
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
//##############################################################################	
//##############################################################################
	public Node digestPeeringPeer (){
		
		if( this.mergeInterval == 1){
			this.mergingStruct = this.initStructure;
		}
		
		
		double value = Math.pow(2,this.mergeInterval);
		
		if( (int)value > this.initStructure.length  ){
			
			if( this.primaryNodeIndex == 0  ){
				return this.mergingStruct[ (this.mergingStruct.length) ].node; 
			}
			
			this.mergeInterval = 1;
			return this.primaryNode; 
		}
		
		this.mergeInterval++;

		if( this.primaryNodeIndex % (((int)value)) == 0 ){
			
			if( this.mergingStruct.length >= (this.primaryNodeIndex + value ) ){
					
				//System.out.println(( this.primaryNodeIndex + (int)value) - 1);
				return this.mergingStruct[( this.primaryNodeIndex + (int)value) - 1].node;
			} else {
				return this.mergingStruct[ this.primaryNodeIndex - 1].node;
			}
		}
		
		return this.primaryNode;
	}
//##############################################################################
//##############################################################################	
	public void deletePeer(Node node){
		
		treeNode [] tempStruct = new treeNode[ this.mergingStruct.length - 1 ];
		
		for( int i = 0; i < this.mergingStruct.length; i++){
			if( mergingStruct[i].node.id != node.id){
				tempStruct[i] = mergingStruct[i];
			}
		}
		mergingStruct = tempStruct;
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
