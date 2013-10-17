package server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class BusyWaitWebServer  extends WebServer {
    public static void main (String[] args) throws Exception {
        config(args);

        LinkedList<Socket> q = new LinkedList<Socket>();
        ExecutorService es = Executors.newFixedThreadPool(thread_pool_size);
        es.submit(new BusyWaitWorker(q, doc_root, c));

        while (true) {
            Socket s = conn_sock.accept();
            synchronized(q) {
                q.add(s);
            }
        }
    }
}
