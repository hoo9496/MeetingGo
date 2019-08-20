package com.example.administrator.meetingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

// 하트리스트에서 네트워크이미지뷰로 이미지 불러올 때 사용
public class ImageListAdapter extends BaseAdapter {

    private ImageLoader mImageLoader;
    private Context mContext;
    private List<String> listURL;

    public ImageListAdapter(Context context, List<String> list) {
        mContext = context;
        listURL = list;
        mImageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return listURL.size();
    }

    @Override
    public Object getItem(int position) {
        return listURL.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_item, null);
        NetworkImageView img = (NetworkImageView)v.findViewById(R.id.network_img_view);
        mImageLoader.get(listURL.get(position), ImageLoader.getImageListener(img, R.drawable.loading_image, R.mipmap.ic_launcher));
        img.setImageUrl(listURL.get(position), mImageLoader);

        return v;
    }
}
