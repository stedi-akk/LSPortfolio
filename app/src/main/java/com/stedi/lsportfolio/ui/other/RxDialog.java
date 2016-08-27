package com.stedi.lsportfolio.ui.other;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.other.CachedUiRunnables;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

public class RxDialog<T> extends DialogFragment {
    private final Injections injections = new Injections();

    private OnBackground<T> onBackground;

    public interface OnBackground<T> {
        Observable<T> getObservable();
    }

    public static class Injections {
        @Inject CachedUiRunnables cur;

        public Injections() {
            App.getComponent().inject(this);
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

    public RxDialog<T> on(Fragment fragment) {
        setTargetFragment(fragment, 0);
        return this;
    }

    public RxDialog<T> with(OnBackground<T> onBackground) {
        this.onBackground = onBackground;
        return this;
    }

    public void subscribe(Action1<? super T> onNext) {
        subscribe(onNext, null, null);
    }

    public void subscribe(Action1<? super T> onNext, Action1<Throwable> onError) {
        subscribe(onNext, onError, null);
    }

    public void subscribe(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onCompleted) {
        if (getTargetFragment() == null)
            throw new IllegalArgumentException("Target fragment is not specified. Did you forget to call on(Fragment fragment) ?");
        ConnectableObservable<T> co = prepare();
        if (onError == null)
            co.subscribe(onNext);
        else if (onCompleted == null)
            co.subscribe(onNext, onError);
        else
            co.subscribe(onNext, onError, onCompleted);
        super.show(getTargetFragment().getFragmentManager(), getClass().getSimpleName());
        co.connect();
    }

    private ConnectableObservable<T> prepare() {
        ConnectableObservable<T> co = onBackground.getObservable()
                .subscribeOn(Schedulers.io())
                .publish();
        co.subscribe(Actions.empty(),
                throwable -> injections.cur.post(this::dismiss),
                () -> injections.cur.post(this::dismiss));
        return co;
    }
}
