package org.inspirecenter.bullying;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.inspirecenter.bullying.model.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 * 29/10/2016.
 */

class Utils {
    private static final String TAG = "bullying.Utils";

    static final String STORIES_ASSETS_PATH = "stories";
    static final String STORIES_FILESYSTEM_PATH = "stories";
    static String STORY_SERIALIZED = "STORY_SERIALIZED";

    static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            final InputStream inputStream = context.getAssets().open(STORIES_ASSETS_PATH + "/" + fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
            return null;
        }
        return json;
    }

    static private String getFileName(final String storyId, final Resource resource) {
        return storyId + "$" + Uri.parse(resource.getSource()).getLastPathSegment();
    }

    static boolean hasResourceInCache(final Context context, final String storyId, final Resource resource) {
        final File cacheFolder = context.getFilesDir();

        // check for particular resource
        final String fileName = getFileName(storyId, resource);
        final File [] resourceFiles = cacheFolder.listFiles();
        for(final File resourceFile: resourceFiles) {
            if(fileName.equals(resourceFile.getName())) return true;
        }

        return false; // if we reach this point, then the resource was not found
    }

    static void saveResourceInCache(final Context context, final String storyId, final Resource resource, final byte [] data) {
        Log.d(TAG, "Writing " + data.length + " bytes to a file...");
        try {
            final String fileName = getFileName(storyId, resource);
            final FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ioe) {
            Log.e(TAG, "I/O error while writing resource to cache: " + ioe.getMessage());
        }
    }

    static byte [] getResourceFromCache(final Context context, final String storyId, final Resource resource) {

        try {
            final String fileName = getFileName(storyId, resource);
            final FileInputStream inputStream = context.openFileInput(fileName);
            final byte [] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            Log.d(TAG, "Read '" + resource.getSource() + "' containing " + data.length + " bytes");
            return data;
        } catch (IOException ioe) {
            Log.e(TAG, "I/O error while writing resource to cache: " + ioe.getMessage());
            return null;
        }
    }

    static String getResourcePath(final Context context, final String storyId, final Resource resource) {
        return context.getFilesDir() + File.separator + getFileName(storyId, resource);
    }
}
