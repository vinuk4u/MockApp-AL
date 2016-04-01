package com.al.mockapp.utils;

import android.net.Uri;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by vineeth on 01/04/16.
 */
public class MAFormatUtil {
    private static final String SPACE_CHARACTER = "%20";

    public static String getImageUrl(String imageUrl) {
        imageUrl = imageUrl.replace(" ", SPACE_CHARACTER);
        try {
            int pos = imageUrl.lastIndexOf('/') + 1;
            URI uri = new URI(imageUrl.substring(0, pos) + Uri.encode(imageUrl.substring(pos)));
            return uri.toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
