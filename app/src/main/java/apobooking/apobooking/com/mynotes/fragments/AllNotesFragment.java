package apobooking.apobooking.com.mynotes.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import apobooking.apobooking.com.mynotes.MainActivity;
import apobooking.apobooking.com.mynotes.R;
import apobooking.apobooking.com.mynotes.database.NoteItem;
import apobooking.apobooking.com.mynotes.ui.PinCodeActivity;
import apobooking.apobooking.com.mynotes.ui.PopupSettings;
import apobooking.apobooking.com.mynotes.util.Const;
import apobooking.apobooking.com.mynotes.util.NotesAdapter;
import apobooking.apobooking.com.mynotes.util.SwipeController;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AllNotesFragment extends MvpAppCompatFragment implements AllNotesView, NotesAdapter.NotesListener,
        PopupSettings.MenuItemListener {

    @InjectPresenter
    AllNotesPresenter allNotesPresenter;

    @BindView(R.id.notesRecyclerView)
    RecyclerView notesRecyclerView;

    @BindView(R.id.parentLayout)
    ViewGroup parentLayout;

    @BindView(R.id.backgroundImage)
    ImageView background;

    @BindView(R.id.noNotesTextView)
    TextView noNotesTextView;

    private NotesAdapter notesAdapter;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;
    private PopupSettings mPopupWindow;
    private NoteItem currentNoteItem;


    public static AllNotesFragment newInstance() {
        return new AllNotesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_notes, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.loading));

        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesAdapter = new NotesAdapter();
        notesAdapter.setNotesListener(this);
        notesRecyclerView.setAdapter(notesAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new SwipeController(notesAdapter, getContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(notesRecyclerView);

        allNotesPresenter.getAllNotes();

        mPopupWindow = new PopupSettings(getContext());
        mPopupWindow.setMenuItemListener(this);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                background.setVisibility(View.GONE);
                ((MainActivity) getActivity()).setFABVisible(true);
            }
        });
    }

    @Override
    public void showMEssage(int resourceId) {
        new AlertDialog.Builder(getContext())
                .setMessage(getString(resourceId))
                .setPositiveButton(R.string.OK, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void displayAllNotes(List<NoteItem> notesList) {
        if(notesList.size() > 0)
            notesAdapter.setNotesList(notesList);
        else
            noNotesTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void openSelectedNote(NoteItem noteItem) {
        if (!TextUtils.isEmpty(noteItem.getPassword()))
        {
            currentNoteItem = noteItem;
            Intent intent = new Intent(getContext(), PinCodeActivity.class);
            startActivityForResult(intent, Const.ActivityResult.PIN_CODE_CHECK);
        }
        else
            ((MainActivity) getActivity()).openSelectedItem(noteItem);
    }

    @Override
    public void showLoadingState() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingState() {
        progressDialog.dismiss();
    }

    @Override
    public void refreshNotesList(int itemId) {
        notesAdapter.refresh(itemId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void noteSelected(int id) {
        allNotesPresenter.openNoteById(id);
    }

    @Override
    public void noteDelete(NoteItem noteItem) {
        allNotesPresenter.deleteNote(noteItem);
    }

    @Override
    public void noteSetPassword(NoteItem noteItem, float y) {
        currentNoteItem = noteItem;
        mPopupWindow.setFocusable(true);
        if (!TextUtils.isEmpty(noteItem.getPassword()))
            mPopupWindow.setMenuItemText(getString(R.string.remove_password));
        else
            mPopupWindow.setMenuItemText(getString(R.string.set_password));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(parentLayout, Gravity.CENTER, 0, (int) y);
        background.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setFABVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setFABVisible(true);
    }

    @Override
    public void onSetPasswordClicked() {
        Intent intent = new Intent(getContext(), PinCodeActivity.class);
        if(TextUtils.isEmpty(currentNoteItem.getPassword()))
            startActivityForResult(intent, Const.ActivityResult.PIN_CODE_SET);
        else
            startActivityForResult(intent, Const.ActivityResult.PIN_CODE_REMOVE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Const.ActivityResult.PIN_CODE_SET:
                if (resultCode == RESULT_OK) {
                    String password = data.getStringExtra(Const.ActivityResult.PASSWORD_EXTRA);
                    currentNoteItem.setPassword(password);
                    allNotesPresenter.setPassword(currentNoteItem);
                }
                break;
            case Const.ActivityResult.PIN_CODE_CHECK:
                if(resultCode == RESULT_OK)
                {
                    if(currentNoteItem.getPassword().equals(data.getStringExtra(Const.ActivityResult.PASSWORD_EXTRA)))
                        ((MainActivity) getActivity()).openSelectedItem(currentNoteItem);
                    else
                        showMEssage(R.string.wrong_password);
                }
                break;
            case Const.ActivityResult.PIN_CODE_REMOVE:
                if (resultCode == RESULT_OK) {
                    if(currentNoteItem.getPassword().equals(data.getStringExtra(Const.ActivityResult.PASSWORD_EXTRA))) {
                        currentNoteItem.setPassword("");
                        allNotesPresenter.setPassword(currentNoteItem);
                    }
                    else
                    showMEssage(R.string.wrong_password);
                }
                break;
        }
    }
}