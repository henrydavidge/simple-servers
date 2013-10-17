package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/10/13
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client{
    public static void main(String[] argv) throws Exception {
        String fname = "";
        String server = "localhost";
        int port = 8080;
        int n_threads = 5;
        int t = 1000;
        for (int i = 0; i < argv.length; i++){
            if (argv[i].equals("-server")) {
                server = argv[++i];
            } else if (argv[i].equals("-port")) {
                port = Integer.parseInt(argv[++i]);
            } else if (argv[i].equals("-parallel")) {
                n_threads = Integer.parseInt(argv[++i]);
            } else if (argv[i].equals("-files")) {
                fname = argv[++i];
            } else if (argv[i].equals("-T")) {
                t = Integer.parseInt(argv[++i]);
            }
        }

        BufferedReader br = new BufferedReader(new FileReader(fname));

        // Get list of filenames to request
        ArrayList<String> reqs = new ArrayList<String>();
        String f;
        while ((f = br.readLine()) != null) {
            reqs.add(f);
        }

        ClientThread[] threads = new ClientThread[n_threads];
        InetAddress h = InetAddress.getByName(server);
        for (int i = 0; i < n_threads; i++) {
            threads[i] = new ClientThread(h, port, new ArrayList<String>(reqs));
            threads[i].start();
        }

        Thread.currentThread().sleep(t);
        int sum = 0;

        for (int i = 0; i < n_threads; i++) {
            threads[i].interrupt();
            sum += threads[i].getNReq();
        }

        System.out.println(sum + " Requests Served");

        System.err.println("Interrupted");
    }
}
