package server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class WelcomeSocketWorker implements Runnable {
    private Cache c;
    private String doc_root;
    private ServerSocket welcome;

    public WelcomeSocketWorker(ServerSocket welcome, String doc_root, Cache c) {
        this.welcome = welcome;
        this.doc_root = doc_root;
        this.c = c;
    }

    public void run() {
        while (true) {
            synchronized (welcome) {
                try {
                    Socket conn_sock = welcome.accept();
                    WebRequestHandler wrh = new WebRequestHandler(conn_sock, doc_root, c);
                    wrh.processRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
