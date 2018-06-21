package apobooking.apobooking.com.mynotes.util;

/**
 * Created by sts on 18.06.18.
 */

public interface Const {
    class FragmentDataTransfer{
        public static final String CONTENT_BUNDLE = "content_bundle";
        public static final String TITLE_BUNDLE = "title_bundle";
        public static final String NOTE_ID_BUNDLE = "id";
        public static final String NEW_NOTE = "new_note";
    }

    class ActivityResult{
        public static final int PIN_CODE_SET = 43;
        public static final int PIN_CODE_CHECK = 44;
        public static final int PIN_CODE_REMOVE = 45;
        public static final String PASSWORD_EXTRA = "password";
    }
}
