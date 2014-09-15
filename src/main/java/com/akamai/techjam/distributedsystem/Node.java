package com.akamai.techjam.distributedsystem;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
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
	Map<String, AtomicInteger> distribution = Maps.newHashMap();
	List<String> nodes;
	
	
	public Node (String id) {	
		this.id = id;
		nodes = Lists.newArrayList(this.id);
		cache = new HashMap<String, String>();
		hash = new RendezvousHash(hfunc, strFunnel, strFunnel, getNodes(distribution));
	}
	
	public Map<String, String> getCache() {
		return cache;
	}
	
	public RendezvousHash<String, String> getHash() {
		return hash;
	}
	
	public String getId() {
		return id;
	}
	
	public List<String> getNodes() {
		return nodes;
	}
	
	public void addNode(String node) {
		nodes.add(node);
		distribution.put(node, new AtomicInteger());
		hash = new RendezvousHash(hfunc, strFunnel, strFunnel, nodes);	
	}
	
	private List<String> getNodes(Map<String, AtomicInteger> distribution) {		
		nodes.add(id);
		distribution.put(id, new AtomicInteger());
		return nodes;
	}
	
	public Map<String, AtomicInteger> getDistribution() {
		return distribution;
	}
}
