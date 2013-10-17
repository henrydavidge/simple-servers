package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebServer {
    public static String doc_root;
    public static int port;
    public static String server_name;
    public static int cache_size = 15 * 1024;
    public static int thread_pool_size;
    public static Cache c;
    public static ServerSocket conn_sock;

    public static void config(String[] args) {
        if (args.length != 3) {
            System.err.println("Too few arguments!");
            System.exit(1);
        }

        server_name = args[0];
        try {
            BufferedReader c_read = new BufferedReader(new FileReader(args[2]));
            String l;
            while ((l = c_read.readLine()) != null) {
                String[] split = l.split(" ");
                if (split[0].equals("Listen")) {
                    port = Integer.parseInt(split[1]);
                } else if (split[0].equals("DocumentRoot")) {
                    doc_root = split[1];
                } else if (split[0].equals("CacheSize")) {
                    cache_size = Integer.parseInt(split[1]) * 1024;
                } else if (split[0].equals("ThreadPoolSize")) {
                    thread_pool_size = Integer.parseInt(split[1]);
                    System.err.println("Thread Pool: " + thread_pool_size);
                }
            }
            c = new Cache(cache_size);

            conn_sock = new ServerSocket(port);
            System.out.println("server listening at: " + conn_sock);
            System.out.println("server www root: " + doc_root);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}
