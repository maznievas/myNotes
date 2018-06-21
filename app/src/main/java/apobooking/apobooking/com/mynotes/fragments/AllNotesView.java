package apobooking.apobooking.com.mynotes.fragments;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import apobooking.apobooking.com.mynotes.database.NoteItem;

@StateStrategyType(SkipStrategy.class)
public interface AllNotesView extends MvpView {
    void showMEssage(int messageId);
    void displayAllNotes(List<NoteItem> notesList);
    void openSelectedNote(NoteItem noteItem);
    void showLoadingState();
    void hideLoadingState();
    void refreshNotesList(int itemId);
}
