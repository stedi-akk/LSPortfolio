package com.stedi.lsportfolio.model;

import com.stedi.lsportfolio.R;

import java.io.Serializable;

public class StoreLink implements Serializable {
    private String url;
    private Type type;

    public enum Type {
        GOOGLE_PLAY(R.drawable.img_google_play_badge, "play.google.com/"),
        APP_STORE(R.drawable.img_app_store_badge, "itunes.apple.com/"),
        WINDOWS_STORE(R.drawable.img_win_store_badge, "windowsphone.com/", "microsoft.com/");

        private final int iconResId;
        private final String[] urls;

        Type(int iconResId, String... urls) {
            this.iconResId = iconResId;
            this.urls = urls;
        }

        public int getIconResId() {
            return iconResId;
        }
    }

    public String getUrl() {
        return url;
    }

    public Type getType() {
        if (type == null) {
            for (Type t : Type.values()) {
                for (String tUrl : t.urls) {
                    if (url.contains(tUrl))
                        type = t;
                }
            }
        }
        return type;
    }
}
