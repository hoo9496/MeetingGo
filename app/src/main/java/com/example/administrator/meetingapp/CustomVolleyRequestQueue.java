package com.example.administrator.meetingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

// 하트 리스트에서 네트워크이미지뷰로 이미지를 불러올 때 사용
public class CustomVolleyRequestQueue {

    private static CustomVolleyRequestQueue mInstance;
    private static Context mContext;
    private RequestQueue mRequestQuee;
    private ImageLoader mImageLoader;

    private CustomVolleyRequestQueue(Context context) {
        mContext = context;
        mRequestQuee = getRequestQuee();
        mImageLoader = new ImageLoader(mRequestQuee, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public RequestQueue getRequestQuee() {
        if (mRequestQuee == null) {
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 10*1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQuee = new RequestQueue(cache, network);
            mRequestQuee.start();
        }
        return  mRequestQuee;
    }

    public ImageLoader getImageLoader() {
        return  mImageLoader;
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CustomVolleyRequestQueue(context);
        }
        return mInstance;
    }

}
