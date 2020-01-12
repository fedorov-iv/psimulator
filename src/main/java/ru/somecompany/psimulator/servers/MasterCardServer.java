package ru.somecompany.psimulator.servers;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOSource;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.slf4j.LoggerFactory;
import ru.somecompany.psimulator.configuration.Config;
import ru.somecompany.psimulator.security.Encryptor;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MasterCardServer extends BaseServer{

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MasterCardServer.class);

    public MasterCardServer(int port) throws Exception {
        Config cfg = Config.getInstance();
        String logging = System.getProperty("logging") != null ?  System.getProperty("logging") : cfg.logging();

        Logger logger = new Logger ();
        logger.addListener (new SimpleLogListener(System.out));

        InputStream instr = MasterCardServer.class.getResourceAsStream("/MasterCard.xml");
        NACChannel channel = new NACChannel(new GenericPackager(instr),
                Encryptor.hexToBytes(""));

        if(!logging.equals("0"))
            ((LogSource)channel).setLogger(logger, "mastercard-channel");

        ISOServer server = new ISOServer(port, channel, null);

        if(!logging.equals("0"))
            server.setLogger (logger, "MasterCard server");

        server.addISORequestListener(this);
        new Thread(server).start();

        log.info("MasterCard Server started successfully on port {}", port);


    }

    public boolean process(final ISOSource source, ISOMsg in) {

        try {

            in.unset(53);
            in.unset(52);

            in.setResponseMTI();

            SimpleDateFormat format = new SimpleDateFormat("MMdd");
            String dateString = format.format(new Date());
            in.set(15, dateString);

            Random rand = new Random();// making random values for 38 and 63 fields
            in.set(38, String.format("%06d", rand.nextInt(999999)));
            in.set(63, "PSM" + String.format("%09d", rand.nextInt(999999999)));
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
