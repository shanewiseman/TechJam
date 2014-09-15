package com.akamai.techjam.distributedsystem;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Node {

	private static final HashFunction hfunc = Hashing.murmur3_128();
	private static final Funnel<CharSequence> strFunnel = Funnels.stringFunnel(Charset.defaultCharset());
	
	String id;
	RendezvousHash<String, String> hash;
	Map<String, String> cache;
	//Map<String, AtomicInteger> distribution = Maps.newHashMap();
	ArrayList<String> nodes;
	
	
	public Node (String id) {	
		initNode(id);
	}
	
	public Map<String, String> getCache() {
		return cache;
	}
	
	public RendezvousHash<String, String> getHash() {
		return hash;
	}
	
	public String get(String key) {
		String nodeString = hash.get(key);
//		if (cache.get(id).equals("cached") || cache.get(id).equals("forwaded")) {
//			
//		}
		if (nodeString.equals(id)) {
			cache.put(id, "cached");
		} else {
			cache.put(id, "forwarded");
		}
		return nodeString;
	}
	
	public String getId() {
		return id;
	}
	
	public List<String> getNodes() {
		return nodes;
	}
	
	public void addNodes(ArrayList<Node> currentNodes) {
		
		Iterator<Node> iter = currentNodes.iterator();
		while(iter.hasNext()) {
			Node node = iter.next();
			if (!node.getId().equals(id)) {
				nodes.add(node.getId());
			}
			//distribution.put(node.getId(), new AtomicInteger());
		}

		hash = new RendezvousHash(hfunc, strFunnel, strFunnel, nodes);	
	}
	
	private void initNode(String id) {	
		this.id = id;
		nodes = Lists.newArrayList(this.id);
		cache = new HashMap<String, String>();		
//		nodes.add(id);
		//distribution.put(id, new AtomicInteger());
		hash = new RendezvousHash(hfunc, strFunnel, strFunnel, nodes);
	}
	
//	public Map<String, AtomicInteger> getDistribution() {
//		return distribution;
//	}
}
