package org.spbstu.rider;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        work(args);
    }

    private static void work(String[] args) {
        Cli cli = new Cli();
        try {
            cli.parse(args);
            if (cli.dey != null && cli.key != null || cli.dey == null && cli.key == null) {
                System.err.println("Undefined work key");
                cli.printHelp();
            } else {
                String key = cli.key != null ? cli.key : cli.dey;
                Encryptor encryptor = new Encryptor(cli.input, cli.output, key);
                encryptor.crypt();
            }
        } catch (CmdLineException | IOException e) {
            cli.printHelp();
        }
    }
}

class Cli {
    @Option(name = "-c", usage = "Encryption key")
    String key = null;

    @Option(name = "-d", usage = "Decryption key")
    String dey = null;

    @Argument
    String input = "";

    @Option(name = "-o", usage = "Custom output file")
    String output = null;


    private CmdLineParser cmdLineParser;

    void parse(String args[]) throws CmdLineException {
        cmdLineParser = new CmdLineParser(this);
        cmdLineParser.parseArgument(args);
        if (output == null) output = input.replaceFirst("[.][^.]+$", "") + ".cry";
    }

    void printHelp() {
        cmdLineParser.printUsage(System.out);
    }
}