package itkach.aard2;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Downloader extends AsyncTask<URL, Integer, String> {
    protected String doInBackground(URL... urls) {
        try {
            //URL oracle = new URL(urls[0]);
            URLConnection yc = urls[0].openConnection();
            //yc.connect();

            InputStream stream = yc.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader in = new BufferedReader(reader);
            String inputLine;
            StringBuilder build = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                build.append(inputLine);
            in.close();
            return build.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}