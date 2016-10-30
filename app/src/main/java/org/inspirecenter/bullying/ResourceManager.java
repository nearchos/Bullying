package org.inspirecenter.bullying;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.inspirecenter.bullying.Model.Resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by salah on 29/10/2016.
 */

class ResourceManager {

    private Context mContext = null;

    public ResourceManager(Context c) {
        mContext = c;
    }

    void DownloadResources(List<Resource> resources) {
        for (Resource res : resources) {
            // if file doesn't already exists, download it
            if (!fileExists(res.getId()))
                downloadResource(res);
        }
    }

    public boolean fileExists(String fname) {
        File file = mContext.getFileStreamPath(fname);
        return file.exists();
    }

    private void downloadResource(Resource res) {
        new DownloadFileTask(res.getId()).execute(res.getSource());
    }

    private class DownloadFileTask extends AsyncTask<String, Integer, String> {
        String Filename;

        DownloadFileTask(String filename) {
            this.Filename = filename;
        }

        @Override
        protected String doInBackground(String... urls) {
            int count;
            try {
                URL myUrl = new URL(urls[0]);
                URLConnection conexion = myUrl.openConnection();
                conexion.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int lengthOfFile = conexion.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(myUrl.openStream());

                OutputStream output = new FileOutputStream(Filename);

                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lengthOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}
