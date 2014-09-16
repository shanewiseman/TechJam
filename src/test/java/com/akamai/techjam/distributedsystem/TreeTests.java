
package com.akamai.techjam.distributedsystem;

import org.junit.Test;


public class TreeTests {

	
	public TreeTests (){
	
	}
	
	
	
	
	@Test
	public void MultiNodeMergeTest() {
		
		Node[] nodes = new Node[10];
		Tree[] trees = new Tree[10];
		for(int i = 0; i < 10; i++ ){
			nodes[i] = new Node(Integer.toString(i));
			
			Node[] temp = new Node[1];
			temp[0] = nodes[i];
			
			trees[i] = new Tree(temp, nodes[i]);
		}
		
		
		for(int i = 0; i < 10; i++ ){
			System.out.println(trees[i].digestPeeringPeer().id);
		}
		
	}
	
	
	
	
	@Test
	public  void testOneArrayConstructor(){
		System.out.println("-----TC1-------");
		Node[] nodes = new Node[10];
		for(int i = 0; i < 10; i++ ){
			nodes[i] = new Node("test");
		}
		
		Tree tree = new Tree(nodes, nodes[0]);
			
		for( int i = 0; i < tree.structure.length; i++){
			System.out.println(tree.structure[i].id);
		}
		
		System.out.println("-----DONE-------");
	}
/**	
	@Test
	public  void testTwoArrayConstructor(){
		System.out.println("-----TC2-------");
		Node[] nodes = new Node[1];
		Node[] nodes2 = new Node[1];
		for(int i = 0; i < 1; i++ ){
			nodes[i] = new Node(Integer.toString(i));
			nodes2[i] = new Node(Integer.toString(i) + " 2");
		}
		
		//Tree tree = new Tree(nodes,nodes2);

		//System.out.println(tree.structure.length);
		//for( int i = 0; i < tree.structure.length; i++){
			
			//System.out.println(Integer.toString(i) + " " + tree.structure[i]);
			//System.out.println(tree.structure[i + 1].parent.id);
		}
		
		Node [] temp = new Node[2];
		temp = tree.returnNodes();
		for(int i = 0; i < temp.length; i++ ){
			System.out.println(temp[i].getId());
			
		}
		
		System.out.println("-----DONE-------");
	}		
**/
}