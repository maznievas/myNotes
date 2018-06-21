package apobooking.apobooking.com.mynotes.fragments;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import apobooking.apobooking.com.mynotes.NotesApplication;
import apobooking.apobooking.com.mynotes.database.AppDatabase;
import apobooking.apobooking.com.mynotes.database.NoteItem;
import apobooking.apobooking.com.mynotes.database.NotesDao;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sts on 14.06.18.
 */

@InjectViewState
public class AllNotesPresenter extends MvpPresenter<AllNotesView> {

    @Inject
    AppDatabase appDatabase;

    private CompositeDisposable compositeDisposable;

    private NotesDao notesDao;

    public AllNotesPresenter() {
        NotesApplication.getAppComponent().inject(this);
        init();
    }

    public void init() {
        compositeDisposable = new CompositeDisposable();
        notesDao = appDatabase.notesDao();
    }

    public void getAllNotes() {
        compositeDisposable.add(notesDao.selectAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notesList -> {
                    getViewState().displayAllNotes(notesList);
                }, throwable -> {
                    Log.e("mLog", "Get all notes");
                    throwable.printStackTrace();
                }));
    }

    public void openNoteById(int id) {
        compositeDisposable.add(notesDao.selectNoteById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noteItem -> {
                    getViewState().openSelectedNote(noteItem);
                }, throwable -> {
                    throwable.printStackTrace();
                }));
    }

    public void deleteNote(NoteItem noteItem) {
        compositeDisposable.add(
                Completable.fromAction(() -> notesDao.delete(noteItem))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(c -> getViewState().showLoadingState())
                        .doOnTerminate(() -> getViewState().hideLoadingState())
                        .subscribe()
        );
    }

    public void setPassword(NoteItem noteItem) {
        compositeDisposable.add(Completable.fromAction(() -> notesDao.insert(noteItem))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(c -> getViewState().showLoadingState())
                .doOnTerminate(() -> getViewState().hideLoadingState())
                .subscribe(() -> {
                    getViewState().refreshNotesList(noteItem.getId());
                }));
    }
}
