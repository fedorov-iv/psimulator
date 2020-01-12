package ru.somecompany.psimulator;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.channel.VAPChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.junit.Assert;
import org.junit.Test;
import ru.somecompany.psimulator.security.Encryptor;

import java.io.InputStream;
import java.util.Date;
import java.util.Random;

public class BaseTest {


    //private static final Logger log = LoggerFactory.getLogger(MasterCardServer.class);

    @Test
    public void testMaster1() throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        InputStream instr = BaseTest.class.getResourceAsStream("/MasterCard.xml");
        NACChannel channel = new NACChannel("localhost", 8888, new GenericPackager(instr),
                Encryptor.hexToBytes(""));
        ((LogSource) channel).setLogger(logger, "mastercard-channel");
        channel.connect();
        ISOMsg m = new ISOMsg();
        m.setMTI("0100");
        m.set(2, "5469131000001357");
        m.set(3, "000000");
        m.set(4, "000000000100");
        m.set(7, "0403135513");
        m.set(11, String.valueOf(System.currentTimeMillis() % 1000000));
        m.set(12, ISODate.getTime(new Date()));
        m.set(13, ISODate.getDate(new Date()));
        m.set(14, "2012");
        m.set(18, "5411");
        m.set(19, "643");
        m.set(22, "901");
        m.set(32, "005275     ");
        m.set(33, "200012     ");
        m.set(35, "5469131000001357D20122011999901230000");
        m.set(37, "289581721380");
        m.set(41, "36251002");
        m.set(42, "1536251001    ");
        m.set(43, "VERNYI UL  PAVLOVSKAJA VSEVOLOJSK    RUS");
        m.set(48, "52363130353030303031");
        m.set(49, "643");
        //m.set (52, "2A54830CA30DD483");
        m.set(53, "9701100002000000");
        m.set(61, "0000000000300643188640    ");
        channel.send(m);
        ISOMsg r = channel.receive();
        channel.disconnect();

        Assert.assertEquals("00", r.getString(39));

    }

    @Test
    public void testVisa1() throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        InputStream instr = BaseTest.class.getResourceAsStream("/Base1.xml");
        NACChannel channel = new NACChannel("localhost", 8889, new GenericPackager(instr),
                Encryptor.hexToBytes(""));
        ((LogSource) channel).setLogger(logger, "visa-channel");
        channel.connect();

        ISOMsg m = new ISOMsg();
        m.setMTI("0100");
        m.set(7, "0403123610");
        m.set(2, "5469131000001357");
        m.set(3, "090000");
        Random rand = new Random();
        m.set(11, String.format("%06d", rand.nextInt(999999)));
        m.set(37, String.format("%012d", rand.nextInt(999999)));
        m.set(70, "071");
        channel.send(m);
        ISOMsg r = channel.receive();
        channel.disconnect();

        Assert.assertEquals("00", r.getString(39));

    }

    @Test
    public void testMasterCard800() throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        InputStream instr = BaseTest.class.getResourceAsStream("/MasterCard.xml");
        NACChannel channel = new NACChannel("localhost", 8888, new GenericPackager(instr),
                Encryptor.hexToBytes(""));
        ((LogSource) channel).setLogger(logger, "mastercard-channel");
        channel.connect();
        ISOMsg m = new ISOMsg();
        m.setMTI("0800");
        m.set(7, "0403123610");
        Random rand = new Random();
        m.set(11, String.format("%06d", rand.nextInt(999999)));
        m.set(37, String.format("%012d", rand.nextInt(999999)));
        m.set(70, "071");

        channel.send(m);
        ISOMsg r = channel.receive();
        channel.disconnect();

        Assert.assertEquals("00", r.getString(39));

    }



    @Test
    public void testVisa800() throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        InputStream instr = BaseTest.class.getResourceAsStream("/Base1.xml");
        VAPChannel channel = new VAPChannel("localhost", 8889, new GenericPackager(instr));
        /*NACChannel channel = new NACChannel("localhost", 8889, new GenericPackager(instr),
                Encryptor.hexToBytes(""));*/
        ((LogSource) channel).setLogger(logger, "visa-channel");
        channel.connect();
        ISOMsg m = new ISOMsg();
        m.setMTI("0800");
        m.set(7, "0403123610");
        Random rand = new Random();
        m.set(11, String.format("%06d", rand.nextInt(999999)));
        m.set(37, String.format("%012d", rand.nextInt(999999)));
        m.set(70, "071");
        channel.send(m);
        ISOMsg r = channel.receive();
        channel.disconnect();

        Assert.assertEquals("00", r.getString(39));

    }

    @Test
    public void testMasterCard87() throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        InputStream instr = BaseTest.class.getResourceAsStream("/MasterCard.xml");
        NACChannel channel = new NACChannel("localhost", 8888, new GenericPackager(instr),
                Encryptor.hexToBytes(""));
        ((LogSource) channel).setLogger(logger, "mastercard-channel");
        channel.connect();
        ISOMsg m = new ISOMsg();
        m.setMTI("0100");
        m.set(7, "0403123610");
        m.set(3, "090000");
        Random rand = new Random();
        m.set(11, String.format("%06d", rand.nextInt(999999)));
        m.set(37, String.format("%012d", rand.nextInt(999999)));
        m.set(70, "071");

        channel.send(m);
        ISOMsg r = channel.receive();
        channel.disconnect();

        Assert.assertEquals("00", r.getString(39));

    }

    @Test
    public void testMasterMultipleMessage() throws Exception {
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        InputStream instr = BaseTest.class.getResourceAsStream("/MasterCard.xml");
        NACChannel channel = new NACChannel("localhost", 8888, new GenericPackager(instr),
                Encryptor.hexToBytes(""));
        ((LogSource) channel).setLogger(logger, "mastercard-channel");
        channel.connect();


        for (int i = 0; i < 5; i++) {
            ISOMsg m = new ISOMsg();
            m.setMTI("0800");
            m.set(70, "081");
            //m.set(37, String.valueOf(i));
            channel.send(m);
            ISOMsg r = channel.receive();
            Assert.assertEquals("00", r.getString(39));
            Thread.sleep(200);
        }

        channel.disconnect();


    }


}
