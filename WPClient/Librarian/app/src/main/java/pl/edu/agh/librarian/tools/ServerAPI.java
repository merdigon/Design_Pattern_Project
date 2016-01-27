package pl.edu.agh.librarian.tools;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import pl.edu.agh.librarian.Exceptions.Exception226;
import pl.edu.agh.librarian.Exceptions.Exception401;
import pl.edu.agh.librarian.Exceptions.Exception404;
import pl.edu.agh.librarian.Exceptions.Exception500;

public class ServerAPI {

    public static String POST(String url, List<NameValuePair> args) throws Exception401, Exception404, Exception500, ConnectTimeoutException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpPost httppost = new HttpPost("http://89.77.193.66:8080" + url);
        String responseBody = null;

        try {
            httppost.setEntity(new UrlEncodedFormEntity(args));
            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int responseCode = statusLine.getStatusCode();
                if (responseCode == 200 || responseCode == 201) {
                    responseBody = EntityUtils.toString(response.getEntity());
                } else if (responseCode == 404) {
                    throw new Exception404("Server returned 404");
                } else if (responseCode == 401) {
                    throw new Exception401("Server returned 401");
                } else {
                    throw new Exception500("Server returned " + responseCode);
                }
            } catch (IOException e) {
                Log.e("ServerAPI", "IO_POST_Exception");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public static String GET(String url, List<NameValuePair> args) throws Exception401, Exception404,
            Exception500, ConnectTimeoutException, Exception226 {
        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpGet httpget = new HttpGet("http://89.77.193.66:8080" + url + "?" + URLEncodedUtils.format(args, "utf-8"));
        String responseBody = null;

        try {
            HttpResponse response = httpclient.execute(httpget);
            StatusLine statusLine = response.getStatusLine();
            int responseCode = statusLine.getStatusCode();
            if (responseCode == 200 || responseCode == 201) {
                responseBody = EntityUtils.toString(response.getEntity());
            } else if (responseCode == 404) {
                throw new Exception404("Server returned 404");
            } else if (responseCode == 401) {
                throw new Exception401("Server returned 401");
            } else if (responseCode == 226){ //e.g. when book is not available
                throw new Exception226("N/A");
            } else {
                throw new Exception500("Server returned " + responseCode);
            }
        } catch (IOException e) {
            Log.e("ServerAPI", "IO_GET_Exception");
        }

        return responseBody;
    }

}
