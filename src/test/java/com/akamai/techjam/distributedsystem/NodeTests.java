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

		node1.addNode(node2.getId());
		node2.addNode(node1.getId());
		node2.addNode("what");
		node2.addNode("Node012");
		
		
		System.out.println("Node01 = " + node1.getHash().get("Node01"));
		System.out.println("Node02 = " + node2.getHash().get("Node02"));
		System.out.println("Node012 = " + node1.getHash().get("Node012"));
		
	}
	
}
