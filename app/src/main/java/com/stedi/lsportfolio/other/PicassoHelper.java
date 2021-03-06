package com.stedi.lsportfolio.other;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import com.stedi.lsportfolio.R;

import javax.inject.Inject;

public class PicassoHelper {
    private final Picasso picasso;

    @Inject
    public PicassoHelper(Picasso picasso) {
        this.picasso = picasso;
    }

    public Picasso getPicasso() {
        return picasso;
    }

    public void load(String url, ImageView iv) {
        load(url, iv, null);
    }

    public void load(String url, ImageView iv, Transformation transformation) {
        RequestCreator creator = picasso
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder);
        if (transformation != null)
            creator.transform(transformation);
        creator.into(iv);
    }
}
