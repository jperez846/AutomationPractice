package config;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;

public class PropertiesFileReader {
    private Properties prop = null;
    public PropertiesFileReader(String filePath){
        prop = new Properties();
        try(InputStream input = new FileInputStream(filePath)){
            prop.load(input);
        } catch(IOException e){
            e.printStackTrace();

        }

    }
    public String getPropertyValue(String key){
        return prop.getProperty(key);
    }
}