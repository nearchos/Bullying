package org.inspirecenter.bullying;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;

import org.inspirecenter.bullying.Model.Resource;
import org.inspirecenter.bullying.Model.StoryGson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by salah on 29/10/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    // references to our images
    private String[] stories;

    public ImageAdapter(Context c) {
        mContext = c;
        // populate the stories from asset/stories folder
        AssetManager assetManager = mContext.getAssets();
        try {
            stories = assetManager.list(Utils.STORIES_ASSETS_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return stories.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }

        StoryGson story = new GsonBuilder().create().fromJson(Utils.loadJSONFromAsset(mContext, stories[position]), StoryGson.class);
        String thumbResName = story.getThubmnail();
        List<Resource> resources = story.getResources();

        int resPos = -1;
        for (Resource res : resources) {
            if (res.getId().equals(thumbResName)) {
                resPos = resources.indexOf(res);
            }
        }

        String imgUrl = story.getResources().get(resPos).getSource();

        new DownloadImageTask(imageView)
                .execute(imgUrl);

        return imageView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}