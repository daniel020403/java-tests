package plaintext;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class RiakPlainText {

    public static void main(String[] args)
            throws UnknownHostException, ExecutionException, InterruptedException {
        RiakClient client = RiakClient.newClient(
                "192.168.5.31, 192.168.5.32, 192.168.5.33, 192.168.5.34, 192.168.5.35"
        );

        System.out.println("\nTest: Riak Object - text/plain\n-----");
        RiakObjectTextPlain(client);

        System.exit(0);
    }

    private static void RiakObjectTextPlain(RiakClient client)
            throws ExecutionException, InterruptedException {
        System.out.println("\n- Storing new object ...");
        RiakObject quoteObject = new RiakObject()
                .setContentType("text/plain")
                .setValue(BinaryValue.create("You're dangerous, Maverick"));
        Namespace quotesBucket = new Namespace("quotes");
        Location quotesObjectLocation = new Location(quotesBucket, "Icemand");
        StoreValue storeOp = new StoreValue.Builder(quoteObject)
                .withLocation(quotesObjectLocation)
                .build();
        StoreValue.Response response = client.execute(storeOp);
        System.out.println(response);

        System.out.println("\n- Reading object ...");
        FetchValue fetchOp = new FetchValue.Builder(quotesObjectLocation).build();
        RiakObject fetchedObject = client.execute(fetchOp).getValue(RiakObject.class);
        System.out.println(fetchedObject.getValue().toString());

        System.out.println("\nUpdating object ...");
        fetchedObject.setValue(BinaryValue.create("You can be my wingman any time."));
        StoreValue updateOp = new StoreValue.Builder(fetchedObject)
                .withLocation(quotesObjectLocation)
                .build();
        StoreValue.Response updateOpResp = client.execute(updateOp);
        System.out.println(updateOpResp);
    }

}
