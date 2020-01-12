package ru.somecompany.psimulator.servers;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOSource;
import org.jpos.iso.channel.VAPChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.slf4j.LoggerFactory;
import ru.somecompany.psimulator.configuration.Config;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class VisaServer extends BaseServer {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(VisaServer.class);

    public VisaServer(int port) throws IOException, ISOException {

        Config cfg = Config.getInstance();
        String logging = System.getProperty("logging") != null ?  System.getProperty("logging") : cfg.logging();

        Logger logger = new Logger ();
        logger.addListener (new SimpleLogListener (System.out));

        InputStream instr = VisaServer.class.getResourceAsStream("/Base1.xml");
        VAPChannel channel = new VAPChannel(new GenericPackager(instr));
/*
        channel.setSrcId("808801");
        channel.setDstId("332812");*/
       /* NACChannel channel = new NACChannel(new GenericPackager(instr),
                Encryptor.hexToBytes(""));*/
       if(!logging.equals("0"))
            ((LogSource)channel).setLogger (logger, "visa-channel");

        ISOServer server = new ISOServer(port, channel, null);
        server.addISORequestListener(this);

        if(!logging.equals("0"))
            server.setLogger (logger, "VISA server");

        new Thread(server).start();

        log.info("VISA Server started successfully on port {}", port);

    }

    public boolean process(final ISOSource source, ISOMsg in) {

        try {

            in.setResponseMTI();
            //in.unset(118);

            /*SimpleDateFormat format = new SimpleDateFormat("MMdd");
            String dateString = format.format(new Date());
            in.set(15, dateString);*/

            Random rand = new Random();// making random values for 38 field
            in.set(38, String.format("%06d", rand.nextInt(999999)));
            in.set(39, "00");
            source.send(in);
        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


}