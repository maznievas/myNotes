package apobooking.apobooking.com.mynotes.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import apobooking.apobooking.com.mynotes.database.AppDatabase;
import dagger.Module;
import dagger.Provides;

/**
 * Created by sts on 14.06.18.
 */

@Module
public class DatabaseModule {

    Context context;

    public DatabaseModule(Context context)
    {
        this.context = context;
    }

    @Provides
    @Singleton
    AppDatabase provideDatabase(){
        return Room.databaseBuilder(context, AppDatabase.class, "NotesDatabase.db")
                .fallbackToDestructiveMigration()
                .build();
    }
}
