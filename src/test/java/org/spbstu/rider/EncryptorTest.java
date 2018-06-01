package org.spbstu.rider;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class EncryptorTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void encrypt() throws IOException {
        File file = temporaryFolder.newFile("input");
        Files.write(file.toPath(), "Chesherskej".getBytes());
        Encryptor encryptor = new Encryptor(file.getAbsolutePath(), file.getAbsolutePath() + ".cry", "54656c74");
        encryptor.crypt();
        Assert.assertArrayEquals(new byte[]{0x17, 0xd, 0x9, 0x7, 0x3c, 0x0, 0x1e, 0x7, 0x3f, 0, 0x6},
                Files.readAllBytes(new File(file.getAbsolutePath() + ".cry").toPath()));
    }

    @Test
    public void decrypt() throws IOException {
        File file = temporaryFolder.newFile("input");
        Files.write(file.toPath(), new byte[]{0x17, 0xd, 0x9, 0x7, 0x3c, 0x0, 0x1e, 0x7, 0x3f, 0, 0x6});
        Encryptor encryptor = new Encryptor(file.getAbsolutePath(), file.getAbsolutePath() + ".cry", "54656c74");
        encryptor.crypt();
        Assert.assertArrayEquals("Chesherskej".getBytes(),
                Files.readAllBytes(new File(file.getAbsolutePath() + ".cry").toPath()));
    }
}