package apobooking.apobooking.com.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import apobooking.apobooking.com.mynotes.R;
import apobooking.apobooking.com.mynotes.util.Const;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PinCodeActivity extends AppCompatActivity {


    private static final int PASSWORD_LENGTH = 4;
    @BindViews({R.id.dot1, R.id.dot2, R.id.dot3, R.id.dot4})
    List<View> dots;
    @BindView(R.id.infoTextView)
    TextView infoTextView;
    private String password;
    private boolean passwordChanging;
    private String oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);

        ButterKnife.bind(this);

        password = "";

        // passwordChanging = getIntent().getBooleanExtra(CHANGE_PIN_PARAM, false);
        // oldPassword = getIntent().getStringExtra(OLD_PASSWORD_PARAM);
    }

    @OnClick(R.id.cancel)
    void onCancelClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick({
            R.id.tv1, R.id.tv2, R.id.tv3,
            R.id.tv4, R.id.tv5, R.id.tv6,
            R.id.tv7, R.id.tv8, R.id.tv9,
            R.id.tv0
    })
    void onNumberClicked(TextView view) {
        dots.get(password.length()).setSelected(true);
        password += view.getText().toString();
        if (password.length() == PASSWORD_LENGTH) {
            finishActivityWithOK();
        }
    }

    private void resetView() {
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setSelected(false);
        }
        password = "";
    }

    private void finishActivityWithOK() {
        Intent intent = new Intent();
        intent.putExtra(Const.ActivityResult.PASSWORD_EXTRA, password);
        setResult(RESULT_OK, intent);
        finish();
    }
}
