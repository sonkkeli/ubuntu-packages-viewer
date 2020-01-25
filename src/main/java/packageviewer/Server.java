package packageviewer;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author sonja
 */
public class Server {
    final int PORT = 8080;
    Map<String, PackageObject> packages;
    ServerSocket server;
    Socket socket;
    PrintWriter pw;
    Scanner scanner;

    public Server() {        
    }
    
    public void openConnection(Map<String, PackageObject> p) throws Exception{
        this.server = new ServerSocket(PORT);
        this.packages = p;

        while (true) {
            this.socket = server.accept();
            this.scanner = new Scanner(socket.getInputStream());
            this.pw = new PrintWriter(socket.getOutputStream());
            pw.println("HTTP/1.1 200 OK");
            pw.println("Content-Type: application/json");
            pw.println("");
            Gson gson = new Gson();
            String json = gson.toJson(packages);
            pw.println(json);
            pw.flush();
            scanner.close();
            pw.close();
            socket.close();
        }
    }
}
