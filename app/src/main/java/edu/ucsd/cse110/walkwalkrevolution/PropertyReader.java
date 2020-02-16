package edu.ucsd.cse110.walkwalkrevolution;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private Context context;
    private Properties properties;
    private String file;

    public PropertyReader(Context context, String file) {
        this.context = context;
        this.file = file;
    }

    public Properties get() {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(file);
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception e){
            System.out.print(e.getMessage());
            properties = null;
        }
        return properties;
    }
}
