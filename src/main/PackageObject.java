package main;

import java.util.*;

/**
 *
 * @author sonja
 */
public class PackageObject {
    private String packageName;
    private String description;
    private String version;
    private List<String> dependencies;
    private List<String> reverseDependencies;

    public PackageObject() {
        this.dependencies = new ArrayList<>();
        this.reverseDependencies  = new ArrayList<>();
    }

    public PackageObject(String packageName, String description, 
            String version, List<String> dependencies, 
            List<String> reverseDependencies) {
        
        this.packageName = packageName;
        this.description = description;
        this.version = version;
        this.dependencies = dependencies;
        this.reverseDependencies = reverseDependencies;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
    
    public void addDependency(String po){
        this.dependencies.add(po);
    }
    
    public void addReverseDependency(String po){
        this.reverseDependencies.add(po);
    }    

    public List<String> getReverseDependencies() {
        return reverseDependencies;
    }

    public void setReverseDependencies(List<String> reverseDependencies) {
        this.reverseDependencies = reverseDependencies;
    }

    @Override
    public String toString() {
        return "package: " + packageName + ", description: " + description + ", version: " + version + ", deps: " + dependencies + ", reverseDeps:" + reverseDependencies;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.packageName);
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + Objects.hashCode(this.version);
        hash = 37 * hash + Objects.hashCode(this.dependencies);
        hash = 37 * hash + Objects.hashCode(this.reverseDependencies);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PackageObject other = (PackageObject) obj;
        if (!Objects.equals(this.packageName, other.packageName)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.dependencies, other.dependencies)) {
            return false;
        }
        if (!Objects.equals(this.reverseDependencies, other.reverseDependencies)) {
            return false;
        }
        return true;
    }
    
    
}
