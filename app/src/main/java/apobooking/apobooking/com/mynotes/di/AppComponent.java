package apobooking.apobooking.com.mynotes.di;


import javax.inject.Singleton;

import apobooking.apobooking.com.mynotes.database.AppDatabase;
import apobooking.apobooking.com.mynotes.fragments.AllNotesFragment;
import apobooking.apobooking.com.mynotes.fragments.AllNotesPresenter;
import apobooking.apobooking.com.mynotes.fragments.NewNotePresenter;
import dagger.Component;

@Singleton
@Component(modules = {
        DatabaseModule.class
})
public interface AppComponent {

    AppDatabase appDatabase();

    void inject(AllNotesPresenter allNotesPresenter);
    void inject(NewNotePresenter newNotePresenter);
}
