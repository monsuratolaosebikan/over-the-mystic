package org.medfordhistorical.overthemystic;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm")
                                                                    .deleteRealmIfMigrationNeeded()
                                                                    .build();
        Realm.setDefaultConfiguration(config);
    }
}
