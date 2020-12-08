package com.example.audiodatabaseapi;

import java.net.URL;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * ApiRequests- a class for showing the menu and navigational element.
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class ApiRequests {
    String response="";

    /**
     * using Okhhtp for Api request and response during app execution
     * exchange.
     * {@linktourl  https://square.github.io/okhttp/}
     * {@linktourl   https://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html}
     * @param requestURL
     * @return
     */
    public String  sendPOSTRequest(final String requestURL) {


        URL url;
        try  {
            url = new URL(requestURL);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET",null)
                    .build();
            System.out.println("Request Object Made");
            Call call=client.newCall(request);
            Response resp =null;
            resp = call.execute();
            response+=resp.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            response+=e.getMessage();
        }

        System.out.println(response);
        return response;
    }
}

