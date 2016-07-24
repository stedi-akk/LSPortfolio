package com.stedi.lsportfolio.ui.other;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.other.PendingUiRunnables;

import javax.inject.Inject;

/**
 * For doing background operation with DialogFragment, and posting result of this operation with Bus
 *
 * @param <Result> type of result
 */
public abstract class AsyncDialog<Result> extends DialogFragment implements Runnable {
    /**
     * @return type of result
     * @throws Exception if any (will be posted with Bus)
     */
    protected abstract Result doInBackground() throws Exception;

    private final Injections injections = new Injections();

    public static class Injections {
        @Inject Bus bus;
        @Inject PendingUiRunnables pur;

        public Injections() {
            App.getInjector().inject(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AsyncDialogTheme);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.async_dialog, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        injections.pur.postMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        injections.pur.cachingMode();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        throw new RuntimeException("Use execute() to show dialog");
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        throw new RuntimeException("Use execute() to show dialog");
    }

    public void execute(Fragment fragment) {
        super.show(fragment.getFragmentManager(), getClass().getSimpleName());
        new Thread(this).start();
    }

    @Override
    public void run() {
        Result result = null;
        Exception exception = null;
        try {
            result = doInBackground();
        } catch (Exception e) {
            exception = e;
        }
        onAfterExecute(exception, result);
    }

    private void onAfterExecute(final Exception exception, final Result result) {
        injections.pur.post(new Runnable() {
            @Override
            public void run() {
                if (exception != null)
                    injections.bus.post(exception);
                else
                    injections.bus.post(result);
                dismiss();
            }
        });
    }
}
