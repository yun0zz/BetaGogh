package com.example.homeactivity.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.homeactivity.models.UrlBitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mingeon on 29/10/2019.
 */

public class ImageManager {


    private static final String TAG = "ImageManager";

    public static Bitmap getBitmap(String imgUrl) {
        imgUrl = imgUrl.replace("//", "\\\\");

        Log.d(TAG, "getBitmap: 쓰레드전");
        String finalImgUrl = imgUrl;
        Thread t = new Thread(new Runnable() {
            File imageFile = new File(finalImgUrl);
            FileInputStream fis = null;
            Bitmap bitmap = null;

            @Override
            public void run() {
                if (finalImgUrl.contains("http")) {
                    try {
                        URL url = new URL(finalImgUrl);
                        bitmap = BitmapFactory.decodeStream(url.openStream());
                        UrlBitmap.setUrlbitmap(bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(fis!= null) {
                                fis.close();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "getBitmap: IOException: " + e.getMessage());
                        }
                    }
                } else {
                    try {
                        fis = new FileInputStream(imageFile);
                        bitmap = BitmapFactory.decodeStream(fis);
                        UrlBitmap.setUrlbitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage());
                    } finally {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            Log.e(TAG, "getBitmap: IOException: " + e.getMessage());
                        }
                    }
                }
            }
        });
        Log.d(TAG, "getBitmap: 쓰레드시작전");
        t.start();

        Log.d(TAG, "getBitmap: 쓰레드시작 후");
        Bitmap bitmap = UrlBitmap.getUrlbitmap();
        Bitmap comfirm = bitmap;
        while (bitmap == comfirm) {
            bitmap = UrlBitmap.getUrlbitmap();
            Log.d(TAG, "getBitmap: 비트맵 출력 부분 ");
        }

        Log.d(TAG, "getBitmap: 쓰레드 리턴전" + bitmap);
        return bitmap;
    }

    /**
     * return byte array from a bitmap
     * quality is greater than 0 but less than 100
     *
     * @param bm
     * @param quality
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bm, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}
