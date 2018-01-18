package cn.dxkite.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 可存储数据
 * Created by DXkite on 2018/1/15 0015.
 */

public class StorageData implements Serializable {

    public static StorageData load(File file) throws FileNotFoundException {
        return (StorageData)loadObject(file);
    }

    public boolean save(File file) {
       return saveObject(file,this);
    }

    public static Object loadObject(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        ObjectInputStream input;
        try {
            input = new ObjectInputStream(new FileInputStream(file));
            Object obj =  input.readObject();
            input.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object loadObject(InputStream file) {
        ObjectInputStream input;
        try {
            input = new ObjectInputStream(file);
            Object obj =  input.readObject();
            input.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  boolean saveObject(File file,Object object) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
            output.writeObject(object);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
