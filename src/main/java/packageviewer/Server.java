package packageviewer;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author sonja
 */
public class Server {
    final int PORT = 8081;
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
            String url = scanner.nextLine();
            
            if(!url.isEmpty() && url.contains("/api/package/")){
                String packageName = url.split("/")[3].split("HTTP")[0].trim();
                
                if(packages.get(packageName) != null){
                    this.pw = new PrintWriter(socket.getOutputStream());
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Access-Control-Allow-Origin: *");
                    pw.println("Content-Type: application/json");
                    pw.println("");
                    Gson gson = new Gson();
                    String json = gson.toJson(packages.get(packageName));
                    pw.println(json);
                } else {
                    pw.println("HTTP/1.1 404");
                    pw.println("");
                }
                pw.flush();
                
            } else if(!url.isEmpty() && url.contains("/api/packages")){
                this.pw = new PrintWriter(socket.getOutputStream());
                pw.println("HTTP/1.1 200 OK");
                pw.println("Access-Control-Allow-Origin: *");
                pw.println("Content-Type: application/json");
                pw.println("");
                Gson gson = new Gson();
                String json = gson.toJson(packages.values().stream().sorted().collect(Collectors.toList()));
                pw.println(json);
                pw.flush();
                
            } else {
                this.pw = new PrintWriter(socket.getOutputStream());
                pw.println("HTTP/1.1 200 OK");
                pw.println("");
                Files.lines(Paths.get("index.html"))
                    .forEach(line -> pw.println(line));
                pw.flush();
            }
            
            scanner.close();
            pw.close();
            socket.close();
        }
    }
}
