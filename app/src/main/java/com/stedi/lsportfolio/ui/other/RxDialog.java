package com.stedi.lsportfolio.ui.other;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.other.CachedUiRunnables;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.RxDialogTheme);
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
        return inflater.inflate(R.layout.rx_dialog, container, false);
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
                .subscribe(new Observer<T>() {
                    @Override
                    public void onCompleted() {
                    }

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
