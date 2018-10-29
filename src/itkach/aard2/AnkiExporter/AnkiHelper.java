package itkach.aard2.AnkiExporter;

import com.ichi2.anki.api.AddContentApi;

import android.content.Context;

public class AnkiHelper {

    /**
     * Whether or not the API is available to use.
     * The API could be unavailable if AnkiDroid is not installed or the user explicitly disabled the API
     * @return true if the API is available to use
     */
    public static boolean isAnkiAPIAvailable(Context context){
        return AddContentApi.getAnkiDroidPackageName(context) != null;
    }
}
