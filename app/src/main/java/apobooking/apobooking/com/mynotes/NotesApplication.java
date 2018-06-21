package apobooking.apobooking.com.mynotes;

import android.app.Application;

import apobooking.apobooking.com.mynotes.di.AppComponent;
import apobooking.apobooking.com.mynotes.di.DaggerAppComponent;
import apobooking.apobooking.com.mynotes.di.DatabaseModule;

/**
 * Created by sts on 14.06.18.
 */

public class NotesApplication extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent()
    {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .build();
    }
}
