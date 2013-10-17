package server;

import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebRequestHandlerRunnable implements Runnable {

    private WebRequestHandler wrh;
    public WebRequestHandlerRunnable(Socket conn_sock, String doc_root, Cache c) throws Exception {
        wrh = new WebRequestHandler(conn_sock, doc_root, c);
    }

    @Override
    public void run() {
        wrh.processRequest();
    }
}
