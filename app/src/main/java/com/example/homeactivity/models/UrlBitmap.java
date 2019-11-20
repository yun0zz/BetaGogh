package com.example.homeactivity.models;

import android.graphics.Bitmap;

/**
 * Created by Mingeon on 19/11/2019.
 */

public class UrlBitmap {

    static Bitmap Urlbitmap;

    public UrlBitmap(Bitmap urlbitmap) {
        Urlbitmap = urlbitmap;
    }

    public static Bitmap getUrlbitmap() {
        return Urlbitmap;
    }

    public static void setUrlbitmap(Bitmap urlbitmap) {
        Urlbitmap = urlbitmap;
    }
}
