package itkach.aard2.AnkiExporter;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

import java.util.concurrent.ExecutionException;

public class Downloader extends AsyncTask<URL, Integer, String> {
    protected String doInBackground(URL... urls) {
        InputStream stream = null;
        try {
            URLConnection connection = urls[0].openConnection();
            stream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader in = new BufferedReader(reader);
            StringBuilder build = new StringBuilder();

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                build.append(inputLine);
            in.close();
            return build.toString();
        } catch (IOException e) {}
        finally{
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    static public String getWordDef(String url){
        Downloader downloader = new Downloader();
        try{
            downloader.execute(new URL(url));
            String html = downloader.get();
            return html;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }
}