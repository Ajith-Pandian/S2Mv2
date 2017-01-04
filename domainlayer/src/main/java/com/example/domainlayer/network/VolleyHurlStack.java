package com.example.domainlayer.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HurlStack;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.Map;

/**
 * Created by thoughtchimp on 1/4/2017.
 */

public class VolleyHurlStack extends HurlStack {
        @Override
        public HttpResponse performRequest(
                Request<?> request, Map<String, String> additionalHeaders)
                throws IOException, AuthFailureError {

            Map<String, String> headers = request.getHeaders();
            // put your global headers
            headers.put("Via", "netroid");
            headers.put("Accept-Charset", "UTF-8");
            headers.put("Origin", "http://netroid.cn/");

            return super.performRequest(request, additionalHeaders);
        }
}
