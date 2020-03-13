package ru.testfield.rt.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public final class ApplicationPropertiesProvider extends Properties {

    private static volatile Properties instance=null;

    private ApplicationPropertiesProvider(){}

    public static Properties getInstance(String propertiesFileName) {
        Properties localInstance = instance;
        if (localInstance == null) {
            synchronized (ApplicationPropertiesProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    File propertiesFile = new File(System.getProperty("user.dir")+"/"+propertiesFileName);
                    try(InputStream propertiesStream = new FileInputStream(propertiesFile)) {
                        Yaml yaml = new Yaml(new Constructor(Properties.class));
                        instance = localInstance = yaml.loadAs(propertiesStream, Properties.class);
                    }catch (FileNotFoundException e){
                        throw new RuntimeException("Properties file not found: "+System.getProperty("user.dir"));
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to read properties file: "+System.getProperty("user.dir"));
                    }
                }
            }
        }
        return localInstance;
    }
}
