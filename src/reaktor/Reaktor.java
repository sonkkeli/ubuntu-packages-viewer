
package reaktor;

import java.util.*;

/**
 *
 * @author sonja
 */
public class Reaktor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        FileReader f = new FileReader();
        List<String> lines = f.readFileFromUbuntu();
        
        // if not ubuntu computer, use mocked data
        if(lines.isEmpty()){
            lines = f.readFakeFile();
        }
        
        f.convertLinesToObjects(lines);
    }
    
}
