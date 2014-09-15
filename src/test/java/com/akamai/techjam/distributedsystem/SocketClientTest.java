package com.akamai.techjam.distributedsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hmahadev
 */
public class SocketClientTest {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException {
        final List<String> requests = new ArrayList<String>();
        requests.add("CCC");
        requests.add("CCC");
        requests.add("AAA");
        final List<Integer> portNumbers = Arrays.asList(20000, 20001, 20002);
        for (int i = 0; i < portNumbers.size(); i++) {
            final Socket s = new Socket("localhost", portNumbers.get(i));
            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            out.write(requests.get(i));
            out.newLine();
            out.flush();
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            System.out.println("portNumber:"+ portNumbers.get(i) + " | reply:"+answer);
            s.close();
        }
    }
}
