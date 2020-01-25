package packageviewer;

import com.alibaba.fastjson.JSON;
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
    Map<String, PackageObject> packages;
    ServerSocket server;
    Socket socket;
    PrintWriter pw;
    Scanner scanner;

    public Server() {        
    }
    
    public void openConnection(Map<String, PackageObject> p) throws Exception{
        int port = Integer.parseInt(System.getenv("PORT"));
        this.server = new ServerSocket(port);
        this.packages = p;

        while (true) {
            this.socket = server.accept();
            scanner = new Scanner(socket.getInputStream());
            String url = "";
            if(scanner.hasNextLine()){
                url = scanner.nextLine();
                System.out.println(url);
            }            
            
            if(url.contains("/closeconnectionbutdontbetooobvious")) break;
            
            if(!url.isEmpty() && url.contains("/api/package/")){
                String packageName = url.split("/")[3].split("HTTP")[0].trim();
                
                if(packages.get(packageName) != null){
                    this.pw = new PrintWriter(socket.getOutputStream());
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Access-Control-Allow-Origin: *");
                    pw.println("Content-Type: application/json");
                    pw.println("");                    
                    String json = JSON.toJSONString(packages.get(packageName));
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
                String json = JSON.toJSONString(packages.keySet().stream().sorted().collect(Collectors.toList()));
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
            pw.close();            
        }
        socket.close();
        scanner.close();
    }
}
