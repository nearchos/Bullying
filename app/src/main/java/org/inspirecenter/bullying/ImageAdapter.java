package org.inspirecenter.bullying;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.inspirecenter.bullying.model.Resource;
import org.inspirecenter.bullying.model.Story;

import java.io.InputStream;
import java.util.Vector;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 * 29/10/2016.
 */
class ImageAdapter extends BaseAdapter {

    public static final String TAG = "bullying.ImageAdapter";

    private Context mContext;
    private Vector<Story> stories;

    ImageAdapter(final Context context, final Vector<Story> stories) {
        this.mContext = context;
        this.stories = stories;
    }

    public int getCount() {
        return stories.size();
    }

    public Object getItem(int position) {
        return stories.elementAt(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
     * create a new ImageView for each item referenced by the Adapter
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int width = (int) mContext.getResources().getDimension(R.dimen.story_image_icon_width);
        final int padding = (int) mContext.getResources().getDimension(R.dimen.story_image_icon_padding);
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(padding, padding, padding, padding);
        } else {
            imageView = (ImageView) convertView;
        }

        Log.d(TAG, "stories[position]: " + stories.get(position));
//        final Story story = new GsonBuilder().create().fromJson(Utils.loadJSONFromAsset(mContext, stories[position]), Story.class);
        final Story story = stories.elementAt(position);
        final String thumbnailResourceId = story.getThumbnail();
        final Resource thumbnailResource = story.getResourceById(thumbnailResourceId);

        final String thumbnailSourceUrl = thumbnailResource.getSource();

        new DownloadImageTask(imageView).execute(thumbnailSourceUrl);

        return imageView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                final InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}