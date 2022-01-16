package org.dbg.checksum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        File file = new File("checksumTestInput.txt");

        // MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        String md5Checksum = getFileChecksum(md5Digest, file);
        System.out.println("MD5 checksum: " + md5Checksum);

        //SHA-256 algorithm
        MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
        String sha256Checksum = getFileChecksum(sha256Digest, file);
        System.out.println("SHA-256 checksum: " + sha256Checksum);
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

}
