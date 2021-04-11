package com.test.test;

import android.app.Application;

import com.fxn.stash.Stash;
import com.bosphere.filelogger.FL;
import com.bosphere.filelogger.FLConfig;
import com.bosphere.filelogger.FLConst;
import java.io.File;
import android.os.Environment;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
        FL.init(new FLConfig.Builder(this)
                .minLevel(FLConst.Level.V)
                .logToFile(true)
                .dir(new File(Environment.getExternalStorageDirectory(), "ShipmentLog"))
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
                .build());
        FL.setEnabled(false);
    }
}
