package com.akamai.techjam.distributedsystem;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

@SuppressWarnings("serial")
public class NodeTests {
	private static final Random rand = new Random();
	private static final HashFunction hfunc = Hashing.murmur3_128();
	private static final Funnel<String> strFunnel = new Funnel<String>(){ 
		public void funnel(String from, PrimitiveSink into) {
			into.putBytes(from.getBytes());
		}};
	
	/**
	 * Ensure the same node returned for same key after a large change to the pool of nodes
	 */
	@Test
	public void testHash() {
		
		Node node1 = new Node("Node01");		
		Node node2 = new Node("Node02");
		Node node3 = new Node("Node03");
		Node node4 = new Node("Node04");
		Node node5 = new Node("Node05");
		Node node6 = new Node("Node06");
		
		node1.getHash().add(node2.getId());
		node1.getHash().add(node3.getId());
		node1.getHash().add(node4.getId());
		node1.getHash().add(node5.getId());
		node1.getHash().add(node6.getId());
		
		node2.getHash().add(node5.getId());
		node2.getHash().add(node6.getId());
		
//		System.out.println(node1.getHash().get("Harish"));
//		System.out.println(node1.getHash().get("Endri"));
//		System.out.println(node1.getHash().get("Shane"));
//		System.out.println(node1.getHash().get("Brian"));
		
		for (int i=0; i<=10; i++) {
			System.out.println(node1.getHash().get(String.valueOf(i)));
		}
		System.out.println("---------------------------------");
		for (int i=0; i<=10; i++) {
			System.out.println(node2.getHash().get(String.valueOf(i)));
		}
		
//		node1.getHash().add("Node07");
//		node1.getHash().add("Node08");
//		node1.getHash().add("Node09");
//		node1.getHash().add("Node10");
//		node1.getHash().add("Node11");
//		node1.getHash().add("Node12");
//		node1.getHash().add("Node13");
//		node1.getHash().add("Node14");
//		node1.getHash().add("Node15");
//		node1.getHash().add("Node16");
//		
//		System.out.println(node1.getHash().get("Brian"));
	}
	
	/**
	 * Ensure the same node returned for same key after a large change to the pool of nodes
	 */
//	@Test
//	public void testCache() {
//
//		Node node1 = new Node("Node01");
//		Node node2 = new Node("Node02");
//		Node node3 = new Node("Node03");
//
//		ArrayList<Node> nodes = new ArrayList<Node>();
//		nodes.add(node2);
//		nodes.add(node3);
//		node1.addNodes(nodes);
//
//		System.out.println(node1.getCache().get(node1.getId()));
//		System.out.println(node1.get("ENdri"));
//		System.out.println(node1.getCache().get(node1.getId()));
//		System.out.println(node1.get("ENdri"));
//		System.out.println(node1.getCache().get(node1.getId()));
//
//		System.out.println(node2.get("ENdri"));
//		System.out.println(node2.getCache().get(node2.getId()));
//	}
	
	@Test
	public void testTree() {
		
		Node[] node1 = new Node[1];
//		for (int i = 0; i < 10; i ++) {
//			node1[i] = new Node("Node0" + i);
//		}
		node1[0] = new Node("Node01");
		Node[] node2 = new Node[1];
//		for (int i = 0; i < 10; i ++) {
//			node2[i] = new Node("Node2" + i);
//		}
		node2[0] = new Node("Node02");
		Node[] node3 = new Node[1];
//		for (int i = 0; i < 10; i ++) {
//			node3[i] = new Node("Node3" + i);
//		}
		node3[0] = new Node("Node03");
		
//		Tree tree = new Tree(node1, node2);
//		Node[] nodes = tree.returnNodes();
		
//		for (int i = 0; i < nodes.length; i++) {
////			//System.out.println(nodes[i].getId());
//			System.out.println(nodes[i].getNodes());
//			System.out.println("Shane = " + nodes[i].get("Shane"));
////			//System.out.println(nodes[i].get("ENdri"));
//		}
//		
//		ArrayList<Node> nodeList = new ArrayList<Node>();
//		nodeList.add(nodes[0]);
//		nodeList.add(nodes[1]);
//		node1[0].addNodes(nodeList);
//		node2[0].addNodes(nodeList);
//		
//		System.out.println("Shane = " + nodes[0].get("Shane"));
//		System.out.println("Shane = " + nodes[1].get("Shane"));
//		
//		for (int i = 0; i < node1.length; i++) {
//			System.out.println(node1[0].getNodes());
//		}
//		
//		for (int i = 0; i < node1.length; i++) {
//			System.out.println(node1[0].getNodes());
//		}
	}
	
}
