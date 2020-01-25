
package reaktor;

import java.util.*;

/**
 *
 * @author sonja
 */
public class Reaktor {

    public static void main(String[] args) {        
        FileReader f = new FileReader();
        List<String> lines = f.readFileFromUbuntu();
        
        // if not ubuntu computer, use mocked data
        if(lines.isEmpty()){
            lines = f.readFakeFile();
        }
        
        Map<String, PackageObject> packages = f.convertLinesToObjects(lines);
        
        for (String p : packages.keySet()) {
            System.out.println(packages.get(p));
        }
    }
    
}
