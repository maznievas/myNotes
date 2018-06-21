package apobooking.apobooking.com.mynotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NoteItem noteItem);

    @Delete
    void delete(NoteItem noteItem);

    @Query("SELECT * FROM notes")
    Maybe<List<NoteItem>> selectAll();

    @Query("SELECT * FROM notes WHERE id=:noteId")
    Single<NoteItem> selectNoteById(int noteId);
}
