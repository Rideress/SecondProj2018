package org.spbstu.rider;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Arrays;

public class Encryptor {
    @Option(name = "-c", usage = "Encryption key")
    private String key = null;

    @Option(name = "-d", usage = "Decryption key")
    private String dey = null;

    @Argument
    private String input = "";

    @Option(name = "-o", usage = "Custum output file")
    private String output = "";

    private byte[] crypt;

    public Encryptor(String[] args) {
        CmdLineParser cmdLineParser = new CmdLineParser(this);
        try {
            cmdLineParser.parseArgument(args);
            if (key != null && key.length() % 2 == 0) {
                for (int i = 0; i < key.length(); i += 2) {
                    crypt[i / 2] = (byte) ((Character.digit(key.charAt(i), 16) << 4)
                            + Character.digit(key.charAt(i + 1), 16));
                }
            } else if (dey != null && dey.length() % 2 == 0) {
                for (int i = 0; i < dey.length(); i += 2) {
                    crypt[i / 2] = (byte) ((Character.digit(dey.charAt(i), 16) << 4)
                            + Character.digit(dey.charAt(i + 1), 16));
                }
            } else {
                cmdLineParser.printUsage(System.out);
                return;
            }
            this.encrypt();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            cmdLineParser.printUsage(System.out);
        }
    }

    private void encrypt() {
        String fname;
        if (output != null) {
            fname = output;
        } else {
            fname = input.replaceFirst("[.][^.]+$", "") + ".cry";
        }
        byte[] cash = new byte[crypt.length];
        long len = new File(input).length();
        try (FileWriter writer = new FileWriter(fname, false)) {
            InputStream inp = new FileInputStream(new File(input));
            long ofset = 0;
            while (ofset < len) {
                int tmp = inp.read(cash, Math.toIntExact(ofset), cash.length);
                ofset += tmp;
                for (int i = 0; i < tmp; i++)
                    cash[(int) (ofset + i)] = (byte) (cash[(int) (ofset + i)] ^ crypt[i]);
                if (dey != null)
                    writer.append(Arrays.toString(cash));
                else
                    for (byte b : cash) writer.append(String.valueOf(b)).append(" ");
                writer.flush();
            }
            inp.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}