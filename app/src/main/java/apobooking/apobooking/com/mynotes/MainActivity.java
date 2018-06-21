package apobooking.apobooking.com.mynotes;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import apobooking.apobooking.com.mynotes.database.NoteItem;
import apobooking.apobooking.com.mynotes.fragments.AllNotesFragment;
import apobooking.apobooking.com.mynotes.fragments.NewNoteFragment;
import apobooking.apobooking.com.mynotes.util.Const;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.addNewNotefab)
    FloatingActionButton addNewNote;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment allNotesFragment = fragmentManager.findFragmentById(R.id.fragmentHolderLayout);

        if(allNotesFragment == null)
        {
            allNotesFragment = AllNotesFragment.newInstance();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .add(R.id.fragmentHolderLayout, allNotesFragment)
                .addToBackStack("AllNotes")
                .commit();
    }

    @OnClick(R.id.addNewNotefab)
    public void onAddNewClicked()
    {
        Log.d("mLog", "fabClicked");
        FragmentManager fragmentManager = getSupportFragmentManager();
        NewNoteFragment newNoteFragment = NewNoteFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.FragmentDataTransfer.NEW_NOTE, true);
        newNoteFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.fragmentHolderLayout, newNoteFragment)
                .addToBackStack("NewNote")
                .commit();

        addNewNote.setVisibility(View.GONE);
    }

    public void openSelectedItem(NoteItem noteItem)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NewNoteFragment newNoteFragment = NewNoteFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString(Const.FragmentDataTransfer.TITLE_BUNDLE, noteItem.getTitle());
        bundle.putString(Const.FragmentDataTransfer.CONTENT_BUNDLE, noteItem.getText());
        bundle.putInt(Const.FragmentDataTransfer.NOTE_ID_BUNDLE, noteItem.getId());
        newNoteFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.fragmentHolderLayout, newNoteFragment)
                .addToBackStack("ExistingNote")
                .commit();

        addNewNote.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentHolderLayout);
        if(currentFragment instanceof NewNoteFragment)
            ((NewNoteFragment) currentFragment).saveNoteContent();
        else {
            super.onBackPressed();
            finish();
        }
    }

    public void onBackPressedManually(){
        super.onBackPressed();
        //addNewNote.setVisibility(View.VISIBLE);
    }

    public void setFABVisible(boolean flag){
        if(flag)
        addNewNote.setVisibility(View.VISIBLE);
        else
        addNewNote.setVisibility(View.INVISIBLE);
    }


}
