package com.stedi.lsportfolio.ui.other;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stedi.lsportfolio.App;
import com.stedi.lsportfolio.R;
import com.stedi.lsportfolio.model.LsJob;
import com.stedi.lsportfolio.model.Model;
import com.stedi.lsportfolio.other.ContextUtils;
import com.stedi.lsportfolio.other.PicassoHelper;

import javax.inject.Inject;

public class JobsDialog extends DialogFragment {
    @Inject Model model;
    @Inject PicassoHelper picassoHelper;
    @Inject ContextUtils contextUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (model.getJobs() == null || model.getJobs().isEmpty()) {
            dismiss();
            return super.onCreateDialog(savedInstanceState);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatDialogTheme);
        builder.setTitle(R.string.want_create_application);
        builder.setPositiveButton(R.string.ok, null);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.jobs_dialog, null);

        LinearLayout container = (LinearLayout) dialogView.findViewById(R.id.jobs_dialog_container);
        for (LsJob job : model.getJobs()) {
            ImageView btn = (ImageView) inflater.inflate(R.layout.job_item, container, false);
            picassoHelper.load(job.getImageUrl(), btn);
            btn.setOnClickListener(v -> tryToShowJobInfo(job.getInfoUrl()));
            container.addView(btn);
        }
        builder.setView(dialogView);

        return builder.create();
    }

    private void tryToShowJobInfo(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            contextUtils.showToast(R.string.unknown_error);
        }
    }
}
