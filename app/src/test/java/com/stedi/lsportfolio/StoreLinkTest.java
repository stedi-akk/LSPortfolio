package com.stedi.lsportfolio;

import com.stedi.lsportfolio.model.StoreLink;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoreLinkTest {
    private final String[] GOOGLE_PLAY_URLS = {
            "https://play.google.com/store/apps/details?id=com.stedi.multitouchpaint",
            "https://play.google.com/store/apps/details?id=com.stedi.poweroffclick",
            "https://play.google.com/store/apps/details?id=com.loyverse.sale"
    };

    private final String[] APP_STORE_URLS = {
            "https://itunes.apple.com/us/app/loyverse-pos/id1070865387",
            "https://itunes.apple.com/us/app/loyaltyocean/id1113544456?l=pl&ls=1&mt=8",
            "https://itunes.apple.com/ua/app/loyverse/id1022387842?mt=8"
    };

    private final String[] WIN_STORE_URLS = {
            "https://www.microsoft.com/en-us/store/apps/twitter/9wzdncrfj140",
            "http://www.windowsphone.com/pl-pl/store/app/apps-center/8a6dfc83-c4c2-46e0-9e8e-2500849cdc6b",
            "https://www.microsoft.com/en-us/store/apps/whatsapp/9wzdncrdfwbs"
    };

    @Test
    public void testGooglePlayUrls() {
        for (String url : GOOGLE_PLAY_URLS) {
            StoreLink link = mock(StoreLink.class);
            when(link.getUrl()).thenReturn(url);
            when(link.getType()).thenCallRealMethod();

            assertThat(link.getType(), is(StoreLink.Type.GOOGLE_PLAY));
            assertThat(link.getType(), is(StoreLink.Type.GOOGLE_PLAY));

            verify(link, times(2)).getType();
        }
    }

    @Test
    public void testAppStoreUrls() {
        for (String url : APP_STORE_URLS) {
            StoreLink link = mock(StoreLink.class);
            when(link.getUrl()).thenReturn(url);
            when(link.getType()).thenCallRealMethod();

            assertThat(link.getType(), is(StoreLink.Type.APP_STORE));
            assertThat(link.getType(), is(StoreLink.Type.APP_STORE));

            verify(link, times(2)).getType();
        }
    }

    @Test
    public void testWinStoreUrls() {
        for (String url : WIN_STORE_URLS) {
            StoreLink link = mock(StoreLink.class);
            when(link.getUrl()).thenReturn(url);
            when(link.getType()).thenCallRealMethod();

            assertThat(link.getType(), is(StoreLink.Type.WIN_STORE));
            assertThat(link.getType(), is(StoreLink.Type.WIN_STORE));

            verify(link, times(2)).getType();
        }
    }
}