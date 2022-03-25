package checksum;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import com.basho.riak.client.api.RiakClient;

public class RiakChecksum {

    public static void main(String[] args)
            throws NoSuchAlgorithmException, IOException, ExecutionException, InterruptedException {
//        File file = new File("checksumTestInput.txt");
//        System.out.println(Checksum.getChecksum(file, "MD5"));

        RiakClient client = RiakClient.newClient(
                "192.168.5.31, 192.168.5.32, 192.168.5.33, 192.168.5.34, 192.168.5.35"
        );

        System.exit(0);
    }
}
