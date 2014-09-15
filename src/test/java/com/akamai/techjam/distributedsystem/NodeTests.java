package com.akamai.techjam.distributedsystem;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.akamai.techjam.distributedsystem.RendezvousHash;
import com.google.common.collect.Sets;
import com.google.common.hash.AlwaysOneHashFunction;
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
	public void testConsistentAfterRemove() {
		
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
	
}
