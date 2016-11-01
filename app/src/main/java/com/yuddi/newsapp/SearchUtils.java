package com.yuddi.newsapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 10/30/2016.
 */
public final class SearchUtils {

    private SearchUtils() {

    }

    public static List<Story> fetchSearchData(String terms){
        URL url = createUrl(terms);
        Log.i("oie","Agulha! " + url.toString());
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractBooks(jsonResponse);
    }

    private static URL createUrl(String terms) {
        URL url = null;
        final String GOOGLEAPIS_BASE_URL = "http://content.guardianapis.com/search";
        final String QUERY_PARAM = "q";
        final String API_KEY = "api-key";
        Uri builtUri = Uri.parse(GOOGLEAPIS_BASE_URL)
                .buildUpon()
                .appendQueryParameter(QUERY_PARAM, terms)
                .appendQueryParameter(API_KEY, "test")
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(MainActivity.LOG_TAG, "Error with creating URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(MainActivity.LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(MainActivity.LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Story> extractBooks(String storyJSON) {

        List<Story> stories = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(storyJSON);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for(int i = 0; i < results.length(); i++){
                JSONObject result = results.getJSONObject(i);

                String title = result.getString("webTitle");
                String section = result.getString("sectionName");
                String date = result.getString("webPublicationDate");
                String url = result.getString("webUrl");

                stories.add(new Story(title, section, date, url));
            }

        } catch (JSONException e) {
            Log.e("SearchUtils", "Problem parsing the JSON results", e);
        }

        return stories;
    }
}
