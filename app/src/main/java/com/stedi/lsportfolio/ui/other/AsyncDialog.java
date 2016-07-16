package com.stedi.lsportfolio.ui.other;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.Utils;

public abstract class AsyncDialog<Result> extends DialogFragment implements Runnable {
    public interface OnResult<Result> {
        void onResult(Exception exception, Result result);
    }

    protected abstract Result doInBackground() throws Exception;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        App.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.onPause();
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
        setTargetFragment(fragment, 0);
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
        App.postOnResume(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                Fragment fragment = getTargetFragment();
                if (fragment != null && fragment instanceof OnResult)
                    ((OnResult<Result>) fragment).onResult(exception, result);
                else
                    Utils.showToast("fail here");
                dismiss();
            }
        });
    }
}
