package com.casual_dev.casualmessenger.serialization;

import com.casual_dev.casualmessenger.Message;
import com.casual_dev.casualmessenger.WatchMessaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * provides a means of storage for the CASUALMessenger sync operations
 * Created by adamoutler on 11/9/14.
 */
public class Storage {

    /**
     * writes a watchmessaging object to disk
     *
     * @param watchMessaging
     * @throws IOException
     */
    public static void storeMap(WatchMessaging watchMessaging) throws IOException {
        ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(new File(watchMessaging.dataFolder, WatchMessaging.STORAGE)));
        file.writeObject(watchMessaging.getTable());
    }

    /**
     * reads a watchmessaging object from the disk
     *
     * @param watchMessaging base watchmessaging
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static WatchMessaging loadMap(WatchMessaging watchMessaging) throws IOException, ClassNotFoundException {
        ObjectInputStream file = new ObjectInputStream(new FileInputStream(new File(watchMessaging.dataFolder, WatchMessaging.STORAGE)));
        watchMessaging.setTable((Message) file.readObject());
        return watchMessaging;
    }
}