package com.automationanywhere.botcommand.samples.Utils;
import okhttp3.*;
import org.apache.http.HttpEntity;
import java.io.FileOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

public class HTTPRequest {

    public static String Request(String url, String method, String auth) throws IOException, ParseException {
        URL urlForRequest = new URL(url);
        String readLine;
        String output = "";
        HttpURLConnection connection = (HttpURLConnection) urlForRequest.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", auth);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            output = response.toString();

        } else {
            output = "An error occurred. Response Code: " + responseCode;
        }
        return output;
    }

    public static String POST(String auth, String url, String body) throws IOException {
        URL urlForGetRequest = new URL(url);
        String readLine;
        String output="";
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", auth);
        //Include JSON Body in POST request
        connection.setDoOutput(true);
        OutputStream outStream = connection.getOutputStream();
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
        outStreamWriter.write(body);
        outStreamWriter.flush();
        outStreamWriter.close();
        outStream.close();
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            output = response.toString();
        } else {
            output = "An error occurred. Response Code: " + responseCode;
        }
        return output;
    }

    //private static String LINE_FEED = "\r\n";

    public static String POSTwFile(String url, String filepath) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", filepath,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(filepath)))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Cookie", "b=bqjwz6qwia1zsqok1ring9va3")
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
