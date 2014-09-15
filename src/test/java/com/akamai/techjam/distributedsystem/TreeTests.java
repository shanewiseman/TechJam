
package com.akamai.techjam.distributedsystem;

import org.junit.Test;


public class TreeTests {

	
	public TreeTests (){
	
	}
	
	@Test
	public  void testOneArrayConstructor(){
		System.out.println("-----TC1-------");
		Node[] nodes = new Node[10];
		for(int i = 0; i < 10; i++ ){
			nodes[i] = new Node("test");
		}
		
		Tree tree = new Tree(nodes);
			
		for( int i = 0; i < tree.structure.length; i++){
			System.out.println(tree.structure[i].id);
		}
		
		System.out.println("-----DONE-------");
	}
	
	@Test
	public  void testTwoArrayConstructor(){
		System.out.println("-----TC2-------");
		Node[] nodes = new Node[10];
		Node[] nodes2 = new Node[10];
		for(int i = 0; i < 10; i++ ){
			nodes[i] = new Node("test");
			nodes2[i] = new Node("test");
		}
		
		Tree tree = new Tree(nodes,nodes2);

			
		for( int i = 0; i < tree.structure.length; i++){
			//System.out.println(tree.structure[i].id);
			//System.out.println(tree.structure[i + 1].parent.id);
		}
		System.out.println("-----DONE-------");
	}		

}