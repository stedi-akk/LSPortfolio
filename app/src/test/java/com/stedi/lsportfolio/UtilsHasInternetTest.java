package com.stedi.lsportfolio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.stedi.lsportfolio.other.Utils;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UtilsHasInternetTest {
    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiInfo;
    private NetworkInfo mobileInfo;

    @Before
    public void before() {
        context = mock(Context.class);
        connectivityManager = mock(ConnectivityManager.class);
        wifiInfo = mock(NetworkInfo.class);
        when(wifiInfo.getTypeName()).thenReturn("WIFI");
        mobileInfo = mock(NetworkInfo.class);
        when(mobileInfo.getTypeName()).thenReturn("MOBILE");
    }

    @Test
    public void testAllConnected() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getAllNetworkInfo()).thenReturn(new NetworkInfo[] {mobileInfo, wifiInfo});
        when(wifiInfo.isConnected()).thenReturn(true);
        when(mobileInfo.isConnected()).thenReturn(true);
        assertTrue(new Utils(context, null).hasInternet());
    }

    @Test
    public void testWifiConnected() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getAllNetworkInfo()).thenReturn(new NetworkInfo[] {mobileInfo, wifiInfo});
        when(wifiInfo.isConnected()).thenReturn(true);
        when(mobileInfo.isConnected()).thenReturn(false);
        assertTrue(new Utils(context, null).hasInternet());
    }

    @Test
    public void testMobileConnected() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getAllNetworkInfo()).thenReturn(new NetworkInfo[] {mobileInfo, wifiInfo});
        when(wifiInfo.isConnected()).thenReturn(false);
        when(mobileInfo.isConnected()).thenReturn(true);
        assertTrue(new Utils(context, null).hasInternet());
    }

    @Test
    public void testNotConnected() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getAllNetworkInfo()).thenReturn(new NetworkInfo[] {mobileInfo, wifiInfo});
        when(wifiInfo.isConnected()).thenReturn(false);
        when(mobileInfo.isConnected()).thenReturn(false);
        assertFalse(new Utils(context, null).hasInternet());
    }

    @Test
    public void testNoNetworkInfo() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getAllNetworkInfo()).thenReturn(new NetworkInfo[] {});
        assertFalse(new Utils(context, null).hasInternet());
    }

    @Test
    public void testNullNetworkInfo() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getAllNetworkInfo()).thenReturn(null);
        assertFalse(new Utils(context, null).hasInternet());
    }

    @Test
    public void testNoConnectivityManager() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(null);
        assertFalse(new Utils(context, null).hasInternet());
    }
}
