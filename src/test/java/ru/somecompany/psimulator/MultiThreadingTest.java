package ru.somecompany.psimulator;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.junit.Test;
import ru.somecompany.psimulator.security.Encryptor;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

public class MultiThreadingTest {

    private static final int NUM_THREADS = 100;
    private static int C_TIMEOUT = 60000;

    @Test
    public void simpleMultiThreading() throws Exception{

        ExecutorService executor = Executors.newCachedThreadPool();

        for(int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Hello " + threadName);
            });
        }

        //Thread.sleep(5000);

        executor.awaitTermination(5, TimeUnit.SECONDS);
       // executor.shutdown();

    }


    @Test
    public void multiThreadingClientTest() throws Exception {

        String C_HOST = "localhost";
        int C_PORT = 8888;

        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener(System.out));
        InputStream instr = BaseTest.class.getResourceAsStream("/MasterCard.xml");
        NACChannel channel = new NACChannel(C_HOST, C_PORT, new GenericPackager(instr),
                Encryptor.hexToBytes(""));
        //((LogSource) channel).setLogger(logger, "mastercard-channel");
        channel.connect();

        ExecutorService service = Executors.newSingleThreadExecutor();

        List<Callable<ISOMsg>> listOfCallable = new ArrayList<>();

        for(int i = 0; i < NUM_THREADS; i++){

            listOfCallable.add(()->{
                ISOMsg m = new ISOMsg();
                m.setMTI("0800");
                m.set(7, "0403123610");
                Random rand = new Random();
                m.set(11, String.format("%06d", rand.nextInt(999999)));
                m.set(37, String.format("%012d", rand.nextInt(999999)));
                m.set(70, "071");

                channel.send(m);
                ISOMsg r = channel.receive();
                return r;

            });
        }




        try {

            List<Future<ISOMsg>> futures = service.invokeAll(listOfCallable);

            futures.forEach(f->{

                try{
                    System.out.println(f.get().getString(37));
                }catch (Exception e){

                }

            });


        } catch (InterruptedException e) {// thread was interrupted
            e.printStackTrace();
        } finally {

            // shut down the executor manually
            service.shutdown();
            channel.disconnect();

        }

    }


}
