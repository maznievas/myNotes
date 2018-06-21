package apobooking.apobooking.com.mynotes.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import apobooking.apobooking.com.mynotes.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PopupSettings extends PopupWindow{
    private Unbinder unbinder;
    MenuItemListener menuItemListener;

    @BindView(R.id.setPasswordTextView)
    TextView  setPassword;

    public PopupSettings(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_window, null);

        unbinder = ButterKnife.bind(this, view);

        setContentView(view);
        setOutsideTouchable(true); //dismiss window when touch outside
        setBackgroundDrawable(new ColorDrawable()); // doesn't work without this
        update(0, 0, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.setPasswordTextView)
    public void onSetPasswordClicked()
    {
        menuItemListener.onSetPasswordClicked();
        dismiss();
    }

    public void setMenuItemText(String text)
    {
        setPassword.setText(text);
    }

    public void setMenuItemListener(MenuItemListener menuItemListener)
    {
        this.menuItemListener = menuItemListener;
    }

    public interface MenuItemListener{
        void onSetPasswordClicked();
    }
}
