package server;

import java.net.Socket;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class BusyWaitWorker implements Runnable {
    Queue<Socket> q;
    String doc_root;
    Cache c;

    public BusyWaitWorker(Queue<Socket> q, String doc_root, Cache c) {
        this.q = q;
        this.doc_root = doc_root;
        this.c = c;
    }

    public void run() {
        while (true) {
            Socket s = null;
            while (s == null) {
                synchronized (q) {
                    if (!q.isEmpty()) {
                        s = q.remove();
                    }
                }
            }

            try {
                WebRequestHandler wrt = new WebRequestHandler(s, doc_root, c);
                wrt.processRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
