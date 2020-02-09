package packageviewer;

import com.alibaba.fastjson.JSON;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.*;
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
    private boolean isRunningInHeroku = false;

    public Server() {        
    }
    
    public void openConnection (Map<String, PackageObject> p) throws Exception {
        int port = 8081; // running locally
        if (System.getenv("PORT") != null){
            port = Integer.parseInt(System.getenv("PORT")); // running in heroku
            isRunningInHeroku = true;
        }        
        
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        this.packages = p;
        
        HttpContext context = server.createContext("/api/packages", (exchange -> {                  
            String respText = JSON.toJSONString(packages.keySet().stream().sorted().collect(Collectors.toList()));            
          
            Headers h = exchange.getResponseHeaders();
            this.addHeaders(h);
            
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();

            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        }));
        
        HttpContext context2 = server.createContext("/api/package/", (exchange -> {                  
            URI test = exchange.getRequestURI();
            String param = getQueryParam(test.toString());
            
            String respText = JSON.toJSONString(packages.getOrDefault(param, null));
            if(!respText.equals("null")){
                Headers h = exchange.getResponseHeaders();
                this.addHeaders(h);
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(404, -1);// 404 Not found
            }            
            exchange.close();
        }));
        
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    
    private void addHeaders(Headers h){
        if (isRunningInHeroku){
            h.add("Access-Control-Allow-Origin", "https://package-frontend.herokuapp.com");
        } else {
            h.add("Access-Control-Allow-Origin", "http://localhost:3000");
        }                
        h.add("Content-Type", "application/json");   
    }
    
    private String getQueryParam(String url){
        String[] parts = url.split("/");
        System.out.println(parts[3]);
        return parts[3];
    }
}
