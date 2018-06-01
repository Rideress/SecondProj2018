package org.spbstu.rider;

import java.io.*;

class Encryptor {
    private byte[] crypt;
    private String input;
    private String output;
    /**
     * true - encryption process
     * false - decryption process
     */
    private boolean type = true;

    Encryptor(String input, String output, String key) {
        crypt = stringToByte(key);
        this.input = input;
        this.output = output;
    }

    private byte[] stringToByte(String string) {
        byte[] res = new byte[string.length() / 2];
        for (int i = 0; i < string.length(); i += 2) {
            res[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4)
                    + Character.digit(string.charAt(i + 1), 16));
        }
        return res;
    }

    void crypt() throws IOException {
        long len = new File(input).length();
        try (FileWriter writer = new FileWriter(output, false)) {
            InputStream inp = new FileInputStream(new File(input));
            long ofset = 0;
            int p = 0;
            byte[] cash = new byte[crypt.length];
            while (ofset < len) {
                int tmp = inp.read(cash);
                for (int i = 0; i < tmp; i++)
                    writer.write((char) (cash[i] ^ crypt[i]));
                ofset += tmp;
                writer.flush();
            }
            inp.close();
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found!");
            System.exit(0);
        }
    }
}