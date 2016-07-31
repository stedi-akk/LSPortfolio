package com.stedi.lsportfolio.ui.other;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.squareup.otto.Bus;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.other.CachedUiRunnables;
import com.stedi.lsportfolio.other.SimpleObserver;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * For doing background operation with RxJava, and posting result of this operation with Bus
 */
public class RxDialog<T> extends DialogFragment {
    private final Injections injections = new Injections();

    private OnBackground<T> onBackground;

    public interface OnBackground<T> {
        Observable<T> getObservable();
    }

    public static class Injections {
        @Inject Bus bus;
        @Inject CachedUiRunnables cur;

        public Injections() {
            App.getInjector().inject(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatDialogTheme);
            builder.setView(R.layout.appcompat_rx_dialog);
            dialog = builder.create();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.DialogTheme);
            progressDialog.setMessage(getString(R.string.please_wait));
            dialog = progressDialog;
        }

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        injections.cur.postMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        injections.cur.cachingMode();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    public RxDialog with(OnBackground<T> onBackground) {
        this.onBackground = onBackground;
        return this;
    }

    public void execute(Fragment fragment) {
        super.show(fragment.getFragmentManager(), getClass().getSimpleName());
        onBackground.getObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new SimpleObserver<T>() {
                    @Override
                    public void onError(Throwable e) {
                        injections.cur.post(() -> {
                            injections.bus.post(e);
                            dismiss();
                        });
                    }

                    @Override
                    public void onNext(T t) {
                        injections.cur.post(() -> {
                            injections.bus.post(t);
                            dismiss();
                        });
                    }
                });
    }
}
