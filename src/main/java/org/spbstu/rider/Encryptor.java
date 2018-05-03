package org.spbstu.rider;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;

public class Encryptor {
    @Option(name = "-c", usage = "Encryption key")
    private String key = null;

    @Option(name = "-d", usage = "Decryption key")
    private String dey = null;

    @Argument
    private String input = "";

    public String fname;
    @Option(name = "-o", usage = "Custom output file")
    private String output = null;

    private byte[] crypt;
    /**
     * true - encryption process
     * false - decryption process
     */
    private boolean type = true;

    public Encryptor(String[] args) {
        CmdLineParser cmdLineParser = new CmdLineParser(this);
        try {
            cmdLineParser.parseArgument(args);
            if (key != null && key.length() % 2 == 0) {
                crypt = stringToByte(key);
            } else if (dey != null && dey.length() % 2 == 0) {
                crypt = stringToByte(dey);
                type = false;
            } else {
                cmdLineParser.printUsage(System.out);
                return;
            }
            this.run();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            cmdLineParser.printUsage(System.out);
        }
    }

    private byte[] stringToByte(String string) {
        byte[] res = new byte[string.length() / 2];
        for (int i = 0; i < string.length(); i += 2) {
            res[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4)
                    + Character.digit(string.charAt(i + 1), 16));
        }
        return res;
    }

    private void run() {
        if (output != null) {
            fname = output;
        } else {
            fname = input.replaceFirst("[.][^.]+$", "") + ".cry";
        }
        if (type)
            encrypt(fname);
        else
            decrypt(fname);
    }

    private void decrypt(String fname) {
        byte[] cash = new byte[crypt.length];
        long len = new File(input).length();
        try (FileWriter writer = new FileWriter(fname, false)) {
            InputStream inp = new FileInputStream(new File(input));
            long ofset = 0;
            int p = 0;
            while (ofset < len) {
                int tmp = inp.read(cash);
                ofset += tmp;
                for (int i = 0; i < tmp; i++) {
                    if (cash[i] == ' ') continue;
                    writer.write((char) (cash[i] ^ crypt[p++]));
                    p = p < cash.length ? p : 0;
                }
                writer.flush();
            }
            inp.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void encrypt(String fname) {
        long len = new File(input).length();
        if (!new File(fname).exists()) {
            try {
                new File(fname).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter writer = new FileWriter(fname, false)) {
            InputStream inp = new FileInputStream(new File(input));
            long ofset = 0;
            while (ofset < len) {
                byte[] cash = new byte[crypt.length];
                int tmp = inp.read(cash);
                ofset += tmp;
                for (int i = 0; i < tmp; i++)
                    cash[i] = (byte) (cash[i] ^ crypt[i]);
                for (int i = 0; i < tmp; i++) {
                    writer.write(cash[i]);
                    writer.write(' ');
                }
                writer.flush();
            }
            inp.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}