package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class WelcomeSocketWebServer extends WebServer {
    public static void main(String[] args) {
        config(args);

        ExecutorService es = Executors.newFixedThreadPool(thread_pool_size);
        es.submit(new WelcomeSocketWorker(conn_sock, doc_root, c));
    }
}
