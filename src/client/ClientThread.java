package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/11/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientThread extends Thread {
    private Socket s;
    private PrintWriter pr;
    private BufferedReader br;
    private InetAddress server;
    private int port;
    private int n_threads;
    private List<String> fnames;
    private List<Integer> t;
    private int n_req = 0;
    private int n_bytes = 0;

    public ClientThread(InetAddress server, int port, List<String> fnames) throws Exception{
        this.server = server;
        this.port = port;
        this.fnames = fnames;
    }

    public void run() {
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket s = new Socket(server, port);
                PrintWriter pr = new PrintWriter(s.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                pr.write("GET /" + fnames.get(i) + " HTTP/1.0\r\n\r\n");
                pr.flush();
                String l;
                int fl;
                while ((l = br.readLine()) != null) {
                    String[] sp = l.split(" ");
                    if (sp[0].equals("Content-Length:")) {
                        fl = Integer.parseInt(sp[1]);
                        n_bytes += fl;
                        // Throw away CRLF
                    }
                }
                n_req++;
                s.close();
                i = (i + 1) % fnames.size();
            } catch (Exception e) {
                System.err.println(e);
                return;
            }
        }
    }

    public int getNReq() {
        return n_req;
    }

    public int getNBytes() {
        return n_bytes;
    }
}