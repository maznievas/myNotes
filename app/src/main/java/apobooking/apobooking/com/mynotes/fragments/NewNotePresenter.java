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
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sts on 15.06.18.
 */

@InjectViewState
public class NewNotePresenter extends MvpPresenter<NewNoteView> {

    @Inject
    AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;
    private NotesDao notesDao;

    public NewNotePresenter() {
        NotesApplication.getAppComponent().inject(this);
        init();
    }

    public void init() {
        compositeDisposable = new CompositeDisposable();
        notesDao = appDatabase.notesDao();
    }

    public void saveNote(String title, String content, int id) {
        //NoteItem noteItem1 = new NoteItem();
        compositeDisposable.add(
                Flowable.just(id)
                        .flatMap(noteId -> {
                           if (noteId == -1)
                                return Flowable.just(new NoteItem());
                            else
                                return notesDao.selectNoteById(noteId).toFlowable();
                        })
                        .map(noteItem -> {
                            noteItem.setTitle(title);
                            noteItem.setText(content);
                            return noteItem;
                        })
                        .flatMapCompletable(noteItem -> {
                            return Completable.fromAction(() -> notesDao.insert(noteItem));
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(c -> getViewState().showLoadingDialog())
                        .doOnTerminate(() -> getViewState().hideLoadingDialog())
                        .subscribe(() -> {
                            getViewState().closeCurrentFragment();
                        }, throwable -> {
                            Log.e("mLog", "save note");
                            throwable.printStackTrace();
                        })
        );
    }
}
