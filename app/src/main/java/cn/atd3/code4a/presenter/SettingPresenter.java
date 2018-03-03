package cn.atd3.code4a.presenter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.atd3.code4a.Constant;
import cn.atd3.code4a.model.model.SettingModel;
import cn.atd3.code4a.view.inter.SettingActivityInterface;


/**
 * Created by harry on 2018/1/15.
 *
 */

public class SettingPresenter {

    private SettingActivityInterface sfi;
    private SettingModel sm;


    public SettingPresenter(SettingActivityInterface sfi) {
        this.sfi = sfi;
        sm = readSettingFile();
    }

    public void setLanguage(String language) {
        sm.setLanguage(language);
    }

    public String getLanguage() {
        return sm.getLanguage();
    }

    public void setThemeColor(String color) {
        sm.setThemeColor(color);
        Constant.themeColor = color;
    }

    public String getThemeColor() {
        return sm.getThemeColor();
    }

    public void setDebug(boolean debug) {
        sm.setDebug(debug);
        Constant.debugmodeinfo = debug;
    }

    public boolean getDebug() {
        return sm.isDebug();
    }

    public void setCollection(boolean collection) {
        sm.setCollection(collection);
        Constant.collectioninfo = collection;
    }

    public boolean getCollection() {
        return sm.isCollection();
    }

    private SettingModel readSettingFile() {
        SettingModel sm = null;
        FileInputStream fs = null;
        ObjectInputStream os = null;
        try {
            fs = new FileInputStream(Constant.settingFile);
            os = new ObjectInputStream(fs);
            Object o = os.readObject();
            if (o instanceof SettingModel)
                sm = (SettingModel) o;
        } catch (Exception e) {
            e.printStackTrace();
            sm = new SettingModel();
        } finally {
            try {
                if (os != null)
                    os.close();
                if (fs != null)
                    fs.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sm;
    }

    private void writeSettingToFile(SettingModel sm) {
        FileOutputStream fl = null;
        ObjectOutputStream of = null;
        try {
            fl = new FileOutputStream(Constant.settingFile);
            of = new ObjectOutputStream(fl);
            if (sm != null)
                of.writeObject(sm);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (of != null)
                    of.close();
                if (fl != null)
                    fl.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void saveSetting() {
        writeSettingToFile(sm);
    }

}
