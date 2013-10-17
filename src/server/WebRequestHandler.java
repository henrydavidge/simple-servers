package server;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: hhd
 * Date: 10/16/13
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */

class WebRequestHandler {

    static boolean _DEBUG = true;
    static int     reqCount = 0;

    String WWW_ROOT;
    Socket connSocket;
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    Cache c;

    String urlName;
    String fileName;
    File fileInfo;

    public WebRequestHandler(Socket connectionSocket,
                             String WWW_ROOT) throws Exception
    {
        reqCount ++;

        this.WWW_ROOT = WWW_ROOT;
        this.connSocket = connectionSocket;

        inFromClient =
                new BufferedReader(new InputStreamReader(connSocket.getInputStream()));

        outToClient =
                new DataOutputStream(connSocket.getOutputStream());

    }

    public WebRequestHandler(Socket conn_sock, String WWW_ROOT, Cache c) throws Exception {
        this(conn_sock, WWW_ROOT);
        this.c = c;
    }

    public void processRequest() {
        try {
            String requestMessageLine = inFromClient.readLine();
            DEBUG("Request " + reqCount + ": " + requestMessageLine);

            // process the request
            String[] request = requestMessageLine.split("\\s");

            if (request.length < 2 || !request[0].equals("GET"))
            {
                outputError(500, "Bad request");
                return;
            }

            // parse URL to retrieve file name
            urlName = request[1];

            if ( urlName.startsWith("/") == true )
                urlName  = urlName.substring(1);
                // First check cache
            byte[] contents = null;
            if (c != null) {
                contents = c.get(urlName);
            }
            if (contents == null) {
                mapURL2File();

                if ( fileInfo != null ) // found the file and knows its info
                {
                    outputResponseHeader();
                    outputResponseBody();
                }
            } else {
                outputResponseHeader();
                outputResponseBody(contents);
            }

            connSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            outputError(400, "Server error");
        }



    } // end of processARequest

    private void mapURL2File() throws Exception
    {



        // debugging
        if (_DEBUG) {
            String line = inFromClient.readLine();
            while ( !line.equals("") ) {
                DEBUG( "Header: " + line );
                line = inFromClient.readLine();
            }
        }

        // map to file name
        fileName = WWW_ROOT + urlName;
        DEBUG("Map to File name: " + fileName);

        fileInfo = new File( fileName );
        if ( !fileInfo.isFile() )
        {
            System.err.println("Not a file");
            outputError(404,  "Not Found");
            fileInfo = null;
        }

    } // end mapURL2file


    private void outputResponseHeader() throws Exception
    {
        outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
        outToClient.writeBytes("Set-Cookie: MyCool433Seq12345\r\n");

        if (urlName.endsWith(".jpg"))
            outToClient.writeBytes("Content-Type: image/jpeg\r\n");
        else if (urlName.endsWith(".gif"))
            outToClient.writeBytes("Content-Type: image/gif\r\n");
        else if (urlName.endsWith(".html") || urlName.endsWith(".htm"))
            outToClient.writeBytes("Content-Type: text/html\r\n");
        else
            outToClient.writeBytes("Content-Type: text/plain\r\n");
    }

    private void outputResponseBody() throws Exception
    {

        int numOfBytes = (int) fileInfo.length();
        outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
        outToClient.writeBytes("\r\n");

        // send file content
        FileInputStream fileStream  = new FileInputStream(fileName);

        byte[] fileInBytes = new byte[numOfBytes];
        fileStream.read(fileInBytes);

        // Try putting it in the cache
        synchronized (c) {
            System.err.println("In cache");
            c.put(urlName, fileInBytes);
        }

        outToClient.write(fileInBytes, 0, numOfBytes);
    }

    private void outputResponseBody(byte[] content) throws Exception {
        System.err.println("Writing from cache");
        outToClient.writeBytes("Content-Length: " + content.length + "\r\n");
        outToClient.writeBytes("\r\n");

        // write it
        outToClient.write(content);
    }

    void outputError(int errCode, String errMsg)
    {
        System.err.println("Error");
        try {
            outToClient.writeBytes("HTTP/1.0 " + errCode + " " + errMsg + "\r\n");
        } catch (Exception e) {}
    }

    static void DEBUG(String s)
    {
        if (_DEBUG)
            System.out.println( s );
    }
}
