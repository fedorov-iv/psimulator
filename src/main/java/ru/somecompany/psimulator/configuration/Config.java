package ru.somecompany.psimulator.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс - читатель файла конфигурации app.properties (singleton)
 */
public class Config {

    private final String file = "app.properties";
    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static Config instance;
    private Properties prop;

    /**
     * Порт симулятора MasterCard
     * @return порт симулятора MasterCard
     */
    public String masterCardPort(){
        return prop.getProperty("masterCardPort");
    }

    /**
     * Порт симулятора VISA
     * @return порт симулятора VISA
     */
    public String visaPort(){
        return prop.getProperty("visaPort");
    }


    /**
     * Логирование
     * @return логирование
     */
    public String logging(){
        return prop.getProperty("logging");
    }



    public static Config getInstance(){

        if(instance != null){
            return instance;
        }

        instance = new Config();
        return instance;

    }

    private Config(){

        try {
            prop = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);
            log.info("Config file {} is loaded successfully", file);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file " + file + " not found in the classpath");
            }

        } catch (Exception e) {
            log.error("Exception: " + e);
        }

    }
}
