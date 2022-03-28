package checksum;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;

public class RiakChecksum {

    public static void main(String[] args)
            throws NoSuchAlgorithmException, IOException, ExecutionException, InterruptedException {
        File file = new File("checksumTestInput.txt");
        System.out.println("Original file checksum:");
        System.out.println(Checksum.getChecksum(file, "MD5") + "\n");

        RiakClient client = RiakClient.newClient(
                "192.168.5.31, 192.168.5.32, 192.168.5.33, 192.168.5.34, 192.168.5.35"
        );

        riakChecksum(client);

        System.exit(0);
    }

    private static void riakChecksum(RiakClient client)
            throws IOException, ExecutionException, InterruptedException, NoSuchAlgorithmException {
        System.out.println("\n- Storing file ...");
        File file                       = new File("checksumTestInput.txt");
        StoreValue.Response response    = storeFile(file, client);
        System.out.println(response);

        System.out.println("\n- Fetching file ...");
        RiakObject objectFile   = fetchFileData(client);
        Files.write(Paths.get("checksumTestOutput.txt"), objectFile.getValue().getValue());

        System.out.print("- File checksum: ");
        File outputFile   = new File("checksumTestOutput.txt");
        System.out.println(Checksum.getChecksum(outputFile, "MD5") + "\n");
    }

    private static StoreValue.Response storeFile(File file, RiakClient client)
            throws IOException, ExecutionException, InterruptedException {
        byte[] bytes    = Files.readAllBytes(file.toPath());

        RiakObject fileObject           = new RiakObject()
                .setContentType("application/octet-stream")
                .setValue(BinaryValue.create(bytes));
        Namespace checksumBucket        = new Namespace("checksum");
        Location checksumObjectLocation = new Location(checksumBucket, "checksumTestInput");
        StoreValue storeOp              = new StoreValue.Builder(fileObject)
                .withLocation(checksumObjectLocation)
                .build();

        return client.execute(storeOp);
    }

    private static RiakObject fetchFileData(RiakClient client)
            throws ExecutionException, InterruptedException {
        Namespace checksumBucket        = new Namespace("checksum");
        Location checksumObjectLocation = new Location(checksumBucket, "checksumTestInput");
        FetchValue fetchOp              = new FetchValue.Builder(checksumObjectLocation).build();

        return client.execute(fetchOp).getValue(RiakObject.class);
    }
}
