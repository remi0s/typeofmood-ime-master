package typeofmood.ime.latin;

import android.os.IBinder;
import android.view.inputmethod.InputMethodSubtype;

public final class SubtypeState {
    private InputMethodSubtype mLastActiveSubtype;
    private boolean mCurrentSubtypeHasBeenUsed;

    public void setCurrentSubtypeHasBeenUsed() {
        mCurrentSubtypeHasBeenUsed = true;
    }

    public void switchSubtype(final IBinder token, final RichInputMethodManager richImm) {
        final InputMethodSubtype currentSubtype = richImm.getInputMethodManager()
                .getCurrentInputMethodSubtype();
        final InputMethodSubtype lastActiveSubtype = mLastActiveSubtype;
        final boolean currentSubtypeHasBeenUsed = mCurrentSubtypeHasBeenUsed;
        if (currentSubtypeHasBeenUsed) {
            mLastActiveSubtype = currentSubtype;
            mCurrentSubtypeHasBeenUsed = false;
        }
        if (currentSubtypeHasBeenUsed
                && richImm.checkIfSubtypeBelongsToThisImeAndEnabled(lastActiveSubtype)
                && !currentSubtype.equals(lastActiveSubtype)) {
            richImm.setInputMethodAndSubtype(token, lastActiveSubtype);
            return;
        }
        richImm.switchToNextInputMethod(token, true /* onlyCurrentIme */);
    }
}
