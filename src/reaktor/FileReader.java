
package reaktor;

import java.nio.file.*;
import java.util.*;

/**
 *
 * @author sonja
 */
public class FileReader {

    public FileReader() {
    }
    
    /**
     * converts the file lines in to a HashMap object, from where one can easily
     * find the right object with the package name (String)
     * @param lines
     * @return 
     */
    public Map<String, PackageObject> convertLinesToObjects(List<String> lines){
        
        Map<String, PackageObject> packages = new HashMap<>();
        Iterator<String> i = lines.iterator();
        PackageObject po = new PackageObject();
        
        int last = 0;
        while(i.hasNext()){
            String line = i.next();
            if(line.contains("Package:")){
                po.setPackageName(line.split(":")[1].trim());
                last = 0;

            } else if (line.contains("Version:")) {
                po.setVersion(line.split(":")[1].trim());
                last = 0;
                
            } else if (line.contains("Depends:")){
                int splitPoint = 9;
                if (line.contains("Pre-Depends:")) splitPoint = 13;
                
                String[] pieces = line.substring(splitPoint).split(",");
                for (String p : pieces){
                    String[] nameAndVersion = p.split("\\(");
                    po.addDependency(nameAndVersion[0].trim());
                }
                last = 0;
                
            } else if (line.contains("Description:")){
                po.setDescription(line.split(":")[1].trim());
                last = 1;
                
            }  else if(line.isEmpty()){
                packages.put(po.getPackageName(), po);
                po = new PackageObject();
                last = 0;
                
            // latest line was description, which is usually multi-line,
            // so let's continue setting the description text
            } else if (last == 1 && line.charAt(0) == 32) {
                String before = po.getDescription();
                po.setDescription(before + line);
            }
        }
        return setReverseDeps(packages);
    }
    
    /**
     * maps through the packages and adds their reverse dependencies
     * @param packages
     * @return packages
     */
    private Map<String, PackageObject> 
        setReverseDeps(Map<String, PackageObject> packages){
            
        for (String temp : packages.keySet()){
            List<String> deps = packages.get(temp).getDependencies();
            if(!deps.isEmpty()){
                for (String str : deps){
                    PackageObject original = packages.get(str);
                    if(original != null) original.addReverseDependency(temp);
                }
            }            
        }
        return packages;
    }
    
    /**
     * reads the real file on ubuntu computer, from /var/lib/dpkg/status
     * @return List of lines
     */
    public List<String> readFileFromUbuntu(){
        
        List<String> lines = new ArrayList<>();        
        try {
            Files.lines(Paths.get("/var/lib/dpkg/status"))                
                .forEach(rivi -> lines.add(rivi));
            System.out.println("read real file, " + lines.size() + " lines");
        } catch (Exception e) {
            System.out.println("Virhe: " + e.getMessage());
        }        
        return lines;
    }
    
    /** 
     * reads the fake file saved inside the jar in data-directory
     * @return List of lines
     */
    public List<String> readFakeFile(){
        
        List<String> lines = new ArrayList<>();        
        String url = this.getClass().getResource("/data/status.real").getPath();        
        try {
            Files.lines(Paths.get(url.substring(3)))                
                .forEach(rivi -> lines.add(rivi));
            System.out.println("read fake file, " + lines.size() + " lines");
        } catch (Exception e) {
            System.out.println("Virhe: " + e.getMessage());
        }        
        return lines;
    }
}
