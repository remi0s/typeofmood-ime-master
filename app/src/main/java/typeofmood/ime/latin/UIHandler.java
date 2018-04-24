package typeofmood.ime.latin;

import android.content.res.Resources;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodSubtype;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import typeofmood.ime.R;
import typeofmood.ime.annotations.UsedForTesting;
import typeofmood.ime.keyboard.KeyboardId;
import typeofmood.ime.keyboard.KeyboardSwitcher;
import typeofmood.ime.latin.settings.SettingsValues;
import typeofmood.ime.latin.utils.LeakGuardHandlerWrapper;

public final class UIHandler extends LeakGuardHandlerWrapper<LatinIME> {
    private static final String TAG = LatinIME.class.getSimpleName();

    private static final int PENDING_IMS_CALLBACK_DURATION_MILLIS = 800;
    private static final long DELAY_WAIT_FOR_DICTIONARY_LOAD_MILLIS = TimeUnit.SECONDS.toMillis(2);
    private static final long DELAY_DEALLOCATE_MEMORY_MILLIS = TimeUnit.SECONDS.toMillis(10);

    private static final int MSG_UPDATE_SHIFT_STATE = 0;
    private static final int MSG_PENDING_IMS_CALLBACK = 1;
    private static final int MSG_UPDATE_SUGGESTION_STRIP = 2;
    private static final int MSG_SHOW_GESTURE_PREVIEW_AND_SUGGESTION_STRIP = 3;
    private static final int MSG_RESUME_SUGGESTIONS = 4;
    private static final int MSG_REOPEN_DICTIONARIES = 5;
    private static final int MSG_UPDATE_TAIL_BATCH_INPUT_COMPLETED = 6;
    private static final int MSG_RESET_CACHES = 7;
    private static final int MSG_WAIT_FOR_DICTIONARY_LOAD = 8;
    private static final int MSG_DEALLOCATE_MEMORY = 9;
    private static final int MSG_RESUME_SUGGESTIONS_FOR_START_INPUT = 10;
    private static final int MSG_SWITCH_LANGUAGE_AUTOMATICALLY = 11;
    // Update this when adding new messages
    private static final int MSG_LAST = MSG_SWITCH_LANGUAGE_AUTOMATICALLY;

    private static final int ARG1_NOT_GESTURE_INPUT = 0;
    private static final int ARG1_DISMISS_GESTURE_FLOATING_PREVIEW_TEXT = 1;
    private static final int ARG1_SHOW_GESTURE_FLOATING_PREVIEW_TEXT = 2;
    private static final int ARG2_UNUSED = 0;
    private static final int ARG1_TRUE = 1;

    private int mDelayInMillisecondsToUpdateSuggestions;
    private int mDelayInMillisecondsToUpdateShiftState;

    public UIHandler(@Nonnull final LatinIME ownerInstance) {
        super(ownerInstance);
    }

    public void onCreate() {
        final LatinIME latinIme = getOwnerInstance();
        if (latinIme == null) {
            return;
        }
        final Resources res = latinIme.getResources();
        mDelayInMillisecondsToUpdateSuggestions = res.getInteger(
                R.integer.config_delay_in_milliseconds_to_update_suggestions);
        mDelayInMillisecondsToUpdateShiftState = res.getInteger(
                R.integer.config_delay_in_milliseconds_to_update_shift_state);
    }

    @Override
    public void handleMessage(final Message msg) {
        final LatinIME latinIme = getOwnerInstance();
        if (latinIme == null) {
            return;
        }
        final KeyboardSwitcher switcher = latinIme.mKeyboardSwitcher;
        switch (msg.what) {
            case MSG_UPDATE_SUGGESTION_STRIP:
                cancelUpdateSuggestionStrip();
                latinIme.mInputLogic.performUpdateSuggestionStripSync(
                        latinIme.mSettings.getCurrent(), msg.arg1 /* inputStyle */);
                break;
            case MSG_UPDATE_SHIFT_STATE:
                switcher.requestUpdatingShiftState(latinIme.getCurrentAutoCapsState(),
                        latinIme.getCurrentRecapitalizeState());
                break;
            case MSG_SHOW_GESTURE_PREVIEW_AND_SUGGESTION_STRIP:
                if (msg.arg1 == ARG1_NOT_GESTURE_INPUT) {
                    final SuggestedWords suggestedWords = (SuggestedWords) msg.obj;
                    latinIme.showSuggestionStrip(suggestedWords);
                } else {
                    latinIme.showGesturePreviewAndSuggestionStrip((SuggestedWords) msg.obj,
                            msg.arg1 == ARG1_DISMISS_GESTURE_FLOATING_PREVIEW_TEXT);
                }
                break;
            case MSG_RESUME_SUGGESTIONS:
                latinIme.mInputLogic.restartSuggestionsOnWordTouchedByCursor(
                        latinIme.mSettings.getCurrent(), false /* forStartInput */,
                        latinIme.mKeyboardSwitcher.getCurrentKeyboardScriptId());
                break;
            case MSG_RESUME_SUGGESTIONS_FOR_START_INPUT:
                latinIme.mInputLogic.restartSuggestionsOnWordTouchedByCursor(
                        latinIme.mSettings.getCurrent(), true /* forStartInput */,
                        latinIme.mKeyboardSwitcher.getCurrentKeyboardScriptId());
                break;
            case MSG_REOPEN_DICTIONARIES:
                // We need to re-evaluate the currently composing word in case the script has
                // changed.
                postWaitForDictionaryLoad();
                latinIme.resetDictionaryFacilitatorIfNecessary();
                break;
            case MSG_UPDATE_TAIL_BATCH_INPUT_COMPLETED:
                final SuggestedWords suggestedWords = (SuggestedWords) msg.obj;
                latinIme.mInputLogic.onUpdateTailBatchInputCompleted(
                        latinIme.mSettings.getCurrent(),
                        suggestedWords, latinIme.mKeyboardSwitcher);
                latinIme.onTailBatchInputResultShown(suggestedWords);
                break;
            case MSG_RESET_CACHES:
                final SettingsValues settingsValues = latinIme.mSettings.getCurrent();
                if (latinIme.mInputLogic.retryResetCachesAndReturnSuccess(
                        msg.arg1 == ARG1_TRUE /* tryResumeSuggestions */,
                        msg.arg2 /* remainingTries */, this /* handler */)) {
                    // If we were able to reset the caches, then we can reload the keyboard.
                    // Otherwise, we'll do it when we can.
                    latinIme.mKeyboardSwitcher.loadKeyboard(latinIme.getCurrentInputEditorInfo(),
                            settingsValues, latinIme.getCurrentAutoCapsState(),
                            latinIme.getCurrentRecapitalizeState());
                }
                break;
            case MSG_WAIT_FOR_DICTIONARY_LOAD:
                Log.i(TAG, "Timeout waiting for dictionary load");
                break;
            case MSG_DEALLOCATE_MEMORY:
                latinIme.deallocateMemory();
                break;
            case MSG_SWITCH_LANGUAGE_AUTOMATICALLY:
                latinIme.switchLanguage((InputMethodSubtype) msg.obj);
                break;
        }
    }

    public void postUpdateSuggestionStrip(final int inputStyle) {
        sendMessageDelayed(obtainMessage(MSG_UPDATE_SUGGESTION_STRIP, inputStyle,
                0 /* ignored */), mDelayInMillisecondsToUpdateSuggestions);
    }

    public void postReopenDictionaries() {
        sendMessage(obtainMessage(MSG_REOPEN_DICTIONARIES));
    }

    private void postResumeSuggestionsInternal(final boolean shouldDelay,
                                               final boolean forStartInput) {
        final LatinIME latinIme = getOwnerInstance();
        if (latinIme == null) {
            return;
        }
        if (!latinIme.mSettings.getCurrent().isSuggestionsEnabledPerUserSettings()) {
            return;
        }
        removeMessages(MSG_RESUME_SUGGESTIONS);
        removeMessages(MSG_RESUME_SUGGESTIONS_FOR_START_INPUT);
        final int message = forStartInput ? MSG_RESUME_SUGGESTIONS_FOR_START_INPUT
                : MSG_RESUME_SUGGESTIONS;
        if (shouldDelay) {
            sendMessageDelayed(obtainMessage(message),
                    mDelayInMillisecondsToUpdateSuggestions);
        } else {
            sendMessage(obtainMessage(message));
        }
    }

    public void postResumeSuggestions(final boolean shouldDelay) {
        postResumeSuggestionsInternal(shouldDelay, false /* forStartInput */);
    }

    public void postResumeSuggestionsForStartInput(final boolean shouldDelay) {
        postResumeSuggestionsInternal(shouldDelay, true /* forStartInput */);
    }

    public void postResetCaches(final boolean tryResumeSuggestions, final int remainingTries) {
        removeMessages(MSG_RESET_CACHES);
        sendMessage(obtainMessage(MSG_RESET_CACHES, tryResumeSuggestions ? 1 : 0,
                remainingTries, null));
    }

    public void postWaitForDictionaryLoad() {
        sendMessageDelayed(obtainMessage(MSG_WAIT_FOR_DICTIONARY_LOAD),
                DELAY_WAIT_FOR_DICTIONARY_LOAD_MILLIS);
    }

    public void cancelWaitForDictionaryLoad() {
        removeMessages(MSG_WAIT_FOR_DICTIONARY_LOAD);
    }

    public boolean hasPendingWaitForDictionaryLoad() {
        return hasMessages(MSG_WAIT_FOR_DICTIONARY_LOAD);
    }

    public void cancelUpdateSuggestionStrip() {
        removeMessages(MSG_UPDATE_SUGGESTION_STRIP);
    }

    public boolean hasPendingUpdateSuggestions() {
        return hasMessages(MSG_UPDATE_SUGGESTION_STRIP);
    }

    public boolean hasPendingReopenDictionaries() {
        return hasMessages(MSG_REOPEN_DICTIONARIES);
    }

    public void postUpdateShiftState() {
        removeMessages(MSG_UPDATE_SHIFT_STATE);
        sendMessageDelayed(obtainMessage(MSG_UPDATE_SHIFT_STATE),
                mDelayInMillisecondsToUpdateShiftState);
    }

    public void postDeallocateMemory() {
        sendMessageDelayed(obtainMessage(MSG_DEALLOCATE_MEMORY),
                DELAY_DEALLOCATE_MEMORY_MILLIS);
    }

    public void cancelDeallocateMemory() {
        removeMessages(MSG_DEALLOCATE_MEMORY);
    }

    public boolean hasPendingDeallocateMemory() {
        return hasMessages(MSG_DEALLOCATE_MEMORY);
    }

    @UsedForTesting
    public void removeAllMessages() {
        for (int i = 0; i <= MSG_LAST; ++i) {
            removeMessages(i);
        }
    }

    public void showGesturePreviewAndSuggestionStrip(final SuggestedWords suggestedWords,
                                                     final boolean dismissGestureFloatingPreviewText) {
        removeMessages(MSG_SHOW_GESTURE_PREVIEW_AND_SUGGESTION_STRIP);
        final int arg1 = dismissGestureFloatingPreviewText
                ? ARG1_DISMISS_GESTURE_FLOATING_PREVIEW_TEXT
                : ARG1_SHOW_GESTURE_FLOATING_PREVIEW_TEXT;
        obtainMessage(MSG_SHOW_GESTURE_PREVIEW_AND_SUGGESTION_STRIP, arg1,
                ARG2_UNUSED, suggestedWords).sendToTarget();
    }

    public void showSuggestionStrip(final SuggestedWords suggestedWords) {
        removeMessages(MSG_SHOW_GESTURE_PREVIEW_AND_SUGGESTION_STRIP);
        obtainMessage(MSG_SHOW_GESTURE_PREVIEW_AND_SUGGESTION_STRIP,
                ARG1_NOT_GESTURE_INPUT, ARG2_UNUSED, suggestedWords).sendToTarget();
    }

    public void showTailBatchInputResult(final SuggestedWords suggestedWords) {
        obtainMessage(MSG_UPDATE_TAIL_BATCH_INPUT_COMPLETED, suggestedWords).sendToTarget();
    }

    public void postSwitchLanguage(final InputMethodSubtype subtype) {
        obtainMessage(MSG_SWITCH_LANGUAGE_AUTOMATICALLY, subtype).sendToTarget();
    }

    // Working variables for the following methods.
    private boolean mIsOrientationChanging;
    private boolean mPendingSuccessiveImsCallback;
    private boolean mHasPendingStartInput;
    private boolean mHasPendingFinishInputView;
    private boolean mHasPendingFinishInput;
    private EditorInfo mAppliedEditorInfo;

    public void startOrientationChanging() {
        removeMessages(MSG_PENDING_IMS_CALLBACK);
        resetPendingImsCallback();
        mIsOrientationChanging = true;
        final LatinIME latinIme = getOwnerInstance();
        if (latinIme == null) {
            return;
        }
        if (latinIme.isInputViewShown()) {
            latinIme.mKeyboardSwitcher.saveKeyboardState();
        }
    }

    private void resetPendingImsCallback() {
        mHasPendingFinishInputView = false;
        mHasPendingFinishInput = false;
        mHasPendingStartInput = false;
    }

    private void executePendingImsCallback(final LatinIME latinIme, final EditorInfo editorInfo,
                                           boolean restarting) {
        if (mHasPendingFinishInputView) {
            latinIme.onFinishInputViewInternal(mHasPendingFinishInput);
        }
        if (mHasPendingFinishInput) {
            latinIme.onFinishInputInternal();
        }
        if (mHasPendingStartInput) {
            latinIme.onStartInputInternal(editorInfo, restarting);
        }
        resetPendingImsCallback();
    }

    public void onStartInput(final EditorInfo editorInfo, final boolean restarting) {
        if (hasMessages(MSG_PENDING_IMS_CALLBACK)) {
            // Typically this is the second onStartInput after orientation changed.
            mHasPendingStartInput = true;
        } else {
            if (mIsOrientationChanging && restarting) {
                // This is the first onStartInput after orientation changed.
                mIsOrientationChanging = false;
                mPendingSuccessiveImsCallback = true;
            }
            final LatinIME latinIme = getOwnerInstance();
            if (latinIme != null) {
                executePendingImsCallback(latinIme, editorInfo, restarting);
                latinIme.onStartInputInternal(editorInfo, restarting);
            }
        }
    }

    public void onStartInputView(final EditorInfo editorInfo, final boolean restarting) {
        if (hasMessages(MSG_PENDING_IMS_CALLBACK)
                && KeyboardId.equivalentEditorInfoForKeyboard(editorInfo, mAppliedEditorInfo)) {
            // Typically this is the second onStartInputView after orientation changed.
            resetPendingImsCallback();
        } else {
            if (mPendingSuccessiveImsCallback) {
                // This is the first onStartInputView after orientation changed.
                mPendingSuccessiveImsCallback = false;
                resetPendingImsCallback();
                sendMessageDelayed(obtainMessage(MSG_PENDING_IMS_CALLBACK),
                        PENDING_IMS_CALLBACK_DURATION_MILLIS);
            }
            final LatinIME latinIme = getOwnerInstance();
            if (latinIme != null) {
                executePendingImsCallback(latinIme, editorInfo, restarting);
                latinIme.onStartInputViewInternal(editorInfo, restarting);
                mAppliedEditorInfo = editorInfo;
            }
            cancelDeallocateMemory();
        }
    }

    public void onFinishInputView(final boolean finishingInput) {
        if (hasMessages(MSG_PENDING_IMS_CALLBACK)) {
            // Typically this is the first onFinishInputView after orientation changed.
            mHasPendingFinishInputView = true;
        } else {
            final LatinIME latinIme = getOwnerInstance();
            if (latinIme != null) {
                latinIme.onFinishInputViewInternal(finishingInput);
                mAppliedEditorInfo = null;
            }
            if (!hasPendingDeallocateMemory()) {
                postDeallocateMemory();
            }
        }
    }

    public void onFinishInput() {
        if (hasMessages(MSG_PENDING_IMS_CALLBACK)) {
            // Typically this is the first onFinishInput after orientation changed.
            mHasPendingFinishInput = true;
        } else {
            final LatinIME latinIme = getOwnerInstance();
            if (latinIme != null) {
                executePendingImsCallback(latinIme, null, false);
                latinIme.onFinishInputInternal();
            }
        }
    }
}
