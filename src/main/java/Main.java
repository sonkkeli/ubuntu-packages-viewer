import java.util.*;
import packageviewer.*;

/**
 * reading the file and opening a server into port 8080
 * @author sonja
 */
public class Main {

    public static void main(String[] args) throws Exception {
        FileReader f = new FileReader();
        List<String> lines = f.readFileFromUbuntu();
        if(lines.isEmpty()) lines = f.readFakeFile();       
        Map<String, PackageObject> packages = f.convertLinesToObjects(lines);        
        Server s = new Server();
        s.openConnection(packages);
    }
}
