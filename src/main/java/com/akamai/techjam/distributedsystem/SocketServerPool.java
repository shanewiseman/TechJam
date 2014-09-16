package com.akamai.techjam.distributedsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hmahadev
 */
public class SocketServerPool {

    final static HashMap<String, Thread> SERVER_SOCKET_PROCS = new HashMap<String, Thread>();

    public static void START(final int numOfNodes) {
        final int portNumToStart = 20000;
        final List<Integer> portNumbers = new ArrayList<Integer>();
        final Map<String, Node> portNumberToNode = new HashMap<String, Node>();
        final Node[] rootNodes = new Node[numOfNodes];
        final Tree[] trees = new Tree[numOfNodes];

        for (int i = 0; i < numOfNodes; i++) {
            final int portNumber = portNumToStart + i;
            final Node node = new Node(String.valueOf(portNumber));
            portNumbers.add(portNumber);
            portNumberToNode.put(String.valueOf(portNumber), node);
            rootNodes[i] = node;
        }

        final Tree globalMap = new Tree(rootNodes);

        for(int i=0; i < numOfNodes; i++) {
            Node[] temp = new Node[1];
            temp[0] = rootNodes[i];
            trees[i] = new Tree(temp, i , globalMap.structure);
        }

        for (int k=0; k<5; k++) {
            for(int j = 0; j < 10; j++ ){
                trees[j].digestPeeringPeer();
            }
        }

        for (int i = 0; i < numOfNodes; i++) {
            final Node[] nodes = trees[i].returnNodes();
            rootNodes[i].addNodes(new ArrayList<Node>(Arrays.asList(nodes)));
        }

        for (Integer portNumber : portNumbers) {
            final Thread t = new Thread(new SocketServerThread(portNumber,portNumberToNode.get(portNumber.toString())));
            SERVER_SOCKET_PROCS.put(portNumber.toString(), t);
            t.start();
        }
    }
}
