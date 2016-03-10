package domain;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private static Settings instance = null;
    private Map<Key, Object> settings;
    public enum Key{
        KEEP_HISTORY
    }
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    private Settings(){
        settings = new HashMap<>();
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    public static Settings getInstance(){
        if (instance == null){
            instance = new Settings();
        }
        return instance;
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    public Object getProperty(Key key){
        return settings.get(key);
    }

    public Object getProperty(Key key, Object defaultValue){
        Object data = getProperty(key);
        return data == null ? defaultValue : data;
    }

    public void setProperty(Key key, Object data){
        settings.put(key, data);
    }
    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">

    //</editor-fold>
}
