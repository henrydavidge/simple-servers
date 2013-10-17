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
public class SuspendWaitWorker implements Runnable {
    Queue<Socket> q;
    String doc_root;
    Cache c;

    public SuspendWaitWorker(Queue<Socket> q, String doc_root, Cache c) {
        this.q = q;
        this.doc_root = doc_root;
        this.c = c;
    }

    public void run() {
        while (true) {
            try {
                Socket s = null;
                synchronized (q) {
                    while (q.isEmpty()) {
                        q.wait();
                    }
                    s = q.remove();
                }

                WebRequestHandler wrt = new WebRequestHandler(s, doc_root, c);
                wrt.processRequest();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
