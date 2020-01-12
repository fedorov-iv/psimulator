package ru.somecompany.psimulator;

import ru.somecompany.psimulator.configuration.Config;
import ru.somecompany.psimulator.servers.MasterCardServer;
import ru.somecompany.psimulator.servers.VisaServer;

public class Main {


    public static void main(String[] args){

        try{
            Config cfg = Config.getInstance();

            String masterCardPort = System.getProperty("masterCardPort") != null ? System.getProperty("masterCardPort") : cfg.masterCardPort();
            String visaPort = System.getProperty("visaPort") != null ? System.getProperty("visaPort") : cfg.visaPort();

            MasterCardServer mcs = new MasterCardServer(Integer.parseInt(masterCardPort));
            Thread.sleep(2000);
            VisaServer vs = new VisaServer(Integer.parseInt(visaPort));

        }catch (Exception e){

            e.printStackTrace();

        }
    }
}
