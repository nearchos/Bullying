package org.inspirecenter.bullying;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by salah on 29/10/2016.
 */

class Utils {
    static final String STORIES_ASSETS_PATH = "stories";
    static String STORY_ID_MSG = "STORY_ID";

    static String loadJSONFromAsset(Context c, String fileName) {
        String json = null;
        try {
            InputStream is = c.getAssets().open(STORIES_ASSETS_PATH + "/" + fileName);
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
