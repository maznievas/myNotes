package apobooking.apobooking.com.mynotes.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import apobooking.apobooking.com.mynotes.MainActivity;
import apobooking.apobooking.com.mynotes.R;
import apobooking.apobooking.com.mynotes.ui.SaveNoteDialog;
import apobooking.apobooking.com.mynotes.util.Const;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by sts on 15.06.18.
 */

public class NewNoteFragment extends MvpAppCompatFragment implements NewNoteView, SaveNoteDialog.NoteButtonListener {

    @InjectPresenter
    NewNotePresenter newNotePresenter;
    @BindView(R.id.contentEditText)
    ExtractEditText content;
    @BindView(R.id.parentLayout)
    ViewGroup linearLayout;
    @BindView(R.id.noteTitleTextView)
    TextView noteTitle;
    @BindView(R.id.titleLayout)
    ViewGroup titleLayout;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;
    private String contentRecieved = "";
    private String title = "";
    private int id = -1;
    private boolean newNote = false;

    public static NewNoteFragment newInstance() {
        return new NewNoteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString(Const.FragmentDataTransfer.TITLE_BUNDLE, "");
            if (!TextUtils.isEmpty(title)) {
                titleLayout.setVisibility(View.VISIBLE);
                noteTitle.setText(title);
            }
            contentRecieved = bundle.getString(Const.FragmentDataTransfer.CONTENT_BUNDLE, "");
            id = bundle.getInt(Const.FragmentDataTransfer.NOTE_ID_BUNDLE, -1);
            content.setText(contentRecieved);
            newNote = bundle.getBoolean(Const.FragmentDataTransfer.NEW_NOTE, false);
            if (newNote) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        linearLayout.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        }

        return view;
    }

    public void requestKeyboard() {
        content.requestFocus();
        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(content, InputMethodManager.SHOW_FORCED);
    }

    public void init() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.loading));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void saveNoteContent() {
        if (contentRecieved.equals(content.getText().toString()))
            closeCurrentFragment();
        else {
            SaveNoteDialog saveNoteDialog = new SaveNoteDialog();
            Bundle bundle = new Bundle();
            bundle.putString(Const.FragmentDataTransfer.TITLE_BUNDLE, title);
            saveNoteDialog.setArguments(bundle);
            saveNoteDialog.setButtonClickListener(this);
            saveNoteDialog.show(getFragmentManager(), "SaveNote");
        }
    }

    @Override
    public void saveButtonClicked(String title) {
        newNotePresenter.saveNote(title, content.getText().toString(), id);
    }

    @Override
    public void exitButtonClicked() {
        closeCurrentFragment();
    }

    @Override
    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void closeCurrentFragment() {
        ((MainActivity) getActivity()).onBackPressedManually();
    }

    @OnClick(R.id.contentEditText)
    public void onContentClicked() {
        requestKeyboard();
    }
}
