package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */

public class ThreadPerReqWebServer extends WebServer {

    public static void main(String args[]) throws Exception  {

        config(args);
        while (true) {

            try {

                // take a ready connection from the accepted queue
                Socket connectionSocket = conn_sock.accept();
                System.out.println("\nReceive request from " + connectionSocket);

                // process a request
                Thread t = new Thread(new WebRequestHandlerRunnable(connectionSocket, doc_root, c));
                t.start();
            } catch (Exception e) {
            }
        } // end of while (true)

    } // end of main

} // end of class SequentialWebServer {
