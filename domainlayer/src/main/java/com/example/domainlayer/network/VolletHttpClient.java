package com.example.domainlayer.network;

import com.android.volley.toolbox.HttpClientStack;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

/**
 * Created by thoughtchimp on 1/4/2017.
 */

public class VolletHttpClient  extends HttpClientStack {
        public VolletHttpClient(HttpClient client) {
            super(client);
        }

        @Override
        protected void onPrepareRequest(HttpUriRequest request) throws IOException {
            // put your global headers
            request.setHeader("Via", "netroid");
            request.setHeader("Accept-Charset", "UTF-8");
            request.setHeader("Origin", "http://netroid.cn/");
            super.onPrepareRequest(request);
        }
}
