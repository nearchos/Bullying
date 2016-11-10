package org.inspirecenter.bullying;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Salah Eddin Alshaal
 * @author Nearchos Paspallis
 * 29/10/2016.
 */

class Utils {
    static final String STORIES_ASSETS_PATH = "stories";
    static String STORY_SERIALIZED = "STORY_SERIALIZED";

    static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(STORIES_ASSETS_PATH + "/" + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
