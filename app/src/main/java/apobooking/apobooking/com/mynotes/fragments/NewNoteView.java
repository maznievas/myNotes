package apobooking.apobooking.com.mynotes.fragments;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by sts on 15.06.18.
 */

@StateStrategyType(SkipStrategy.class)
public interface NewNoteView extends MvpView {
    void showLoadingDialog();
    void hideLoadingDialog();
    void closeCurrentFragment();
}
