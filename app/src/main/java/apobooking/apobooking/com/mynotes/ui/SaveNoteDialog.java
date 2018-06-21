package apobooking.apobooking.com.mynotes.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import apobooking.apobooking.com.mynotes.R;
import apobooking.apobooking.com.mynotes.util.Const;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sts on 15.06.18.
 */

public class SaveNoteDialog extends DialogFragment {

    Context context;
    private NoteButtonListener buttonClickListener;

    @BindView(R.id.titleEditText)
    EditText title;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.dialog_save_note, null);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title.setText(bundle.getString(Const.FragmentDataTransfer.TITLE_BUNDLE, ""));
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    public interface NoteButtonListener {
        void saveButtonClicked(String title);
        void exitButtonClicked();
    }

    public void setButtonClickListener(NoteButtonListener listener)
    {
        buttonClickListener = listener;
    }

    @OnClick(R.id.saveButton)
    void onSaveButtonClicked()
    {
        buttonClickListener.saveButtonClicked(title.getText().toString());
        dismiss();
    }

    @OnClick(R.id.exitButton)
    void onExitButtonClicked()
    {
        buttonClickListener.exitButtonClicked();
        dismiss();
    }
}
