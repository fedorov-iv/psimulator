package ru.somecompany.psimulator.servers;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import java.io.IOException;


public class BaseServer implements ISORequestListener {

    public boolean process(final ISOSource source, ISOMsg in) {

        try {
            in.setResponseMTI();
            in.set (39, "00");
            source.send(in);
        } catch (ISOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
