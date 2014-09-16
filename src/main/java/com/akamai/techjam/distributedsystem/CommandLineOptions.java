package com.akamai.techjam.distributedsystem;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created with IntelliJ IDEA.
 * User: hmahadev
 */
public class CommandLineOptions {
    private String[] args = null;
    private Options options = new Options();
    private int numOfNodes;

    public CommandLineOptions(String[] args) {

        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("nnode", "num-nodes", true, "Set the amount of nodes to be part of this cluster");
    }

    public void parse() {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h"))
                help();

            if (cmd.hasOption("num-nodes") || cmd.hasOption("n-nodes")) {
                numOfNodes = Integer.valueOf(cmd.getOptionValue("num-nodes"));
            } else {
                System.out.println("Missing num-nodes option");
                help();
            }
        } catch (ParseException e) {
            System.out.println("Failed to parse comand line properties" + e);
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Help for the distributed Management From Akoni", options);
        System.exit(0);
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }
}
