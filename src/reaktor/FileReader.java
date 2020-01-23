
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
    
    public void convertLinesToObjects(List<String> lines){
        Map<PackageObject, List<PackageObject>> packages = new HashMap<>();
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
                String[] pieces = line.substring(9).split(",");
                for (String p : pieces){
                    String[] nameAndVersion = p.split("\\(");
                    po.addDependency(nameAndVersion[0].trim());
                }
                last = 0;
                
            } else if (line.contains("Description:")){
                po.setDescription(line.split(":")[1].trim());
                last = 1;
                
            }  else if(line.isEmpty()){
                packages.put(po, new ArrayList<>());
                System.out.println(po);
                po = new PackageObject();
                last = 0;
                
            } else if (last == 1 && line.charAt(0) == 32) {
                String before = po.getDescription();
                po.setDescription(before + line);
            }
        }
        System.out.println(packages.size());
        
        for (PackageObject temp : packages.keySet()){
            for (String str : temp.getDependencies()){
                
            }
        }
    }
    
    public List<String> readFileFromUbuntu(){
        List<String> lines = new ArrayList<>();        
        try {
            Files.lines(Paths.get("/var/lib/dpkg/status"))                
                .forEach(rivi -> lines.add(rivi));
        } catch (Exception e) {
            System.out.println("Virhe: " + e.getMessage());
        }        
        System.out.println("loaded the real file with " + lines.size() + " lines");
        return lines;
    }
    
    public List<String> readFakeFile(){
        List<String> lines = new ArrayList<>();        
        String url = this.getClass().getResource("/data/status.real").getPath();        
        try {
            Files.lines(Paths.get(url.substring(3)))                
                .forEach(rivi -> lines.add(rivi));
        } catch (Exception e) {
            System.out.println("Virhe: " + e.getMessage());
        }
        System.out.println("loaded the fake file with " + lines.size() + " lines");
        return lines;
    }
}
