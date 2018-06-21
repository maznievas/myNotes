package apobooking.apobooking.com.mynotes.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {NoteItem.class}, version = 3)
abstract public class AppDatabase extends RoomDatabase{
    public abstract NotesDao notesDao();
}