package com.stedi.lsportfolio.ui.other;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.stedi.lsportfolio.R;

public class AboutDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about)
                .setMessage(R.string.about_text)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
