package com.qinniuclient.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by oubingjian on 2015/8/11.
 */
public class HttpUtil {
    //申明base url
    public static final String BASE_URL = "http://172.18.42.114:8080/qinNiu/";

    //通过url获得HttpGet对象
    public static HttpGet getHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        return request;
    }

    //通过url获得HttpPost对象
    public static HttpPost getHttpPost(String url) {
        HttpPost request = new HttpPost(url);
        return request;
    }

    //通过httpGet 获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpResponse response = new DefaultHttpClient(httpParameters).execute(request);
        return response;
    }

    //通过httpPost 获得HttpResponse对象
    public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpResponse response = new DefaultHttpClient(httpParameters).execute(request);
        return response;
    }

    //通过url 发送post 请求，返回请求结果
    public static String queryStringForPost(String url, List<NameValuePair> para) {
        HttpPost request = HttpUtil.getHttpPost(url);
        String result = null;
        try {
            request.setEntity(new UrlEncodedFormEntity(para, HTTP.UTF_8));
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        }
        return null;
    }

    //通过url 发送get 请求，返回请求结果
    public static String queryStringForGet(String url) {
        HttpGet request = HttpUtil.getHttpGet(url);
        String result = null;
        try {
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        }
        return null;
    }

    //通过HttpPost 发送post 请求,返回请求结果
    public static String queryStringForPost(HttpPost request) {
        String result = null;
        try {
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        }
        return null;
    }

    //通过HttpGet 发送get 请求,返回请求结果
    public static String queryStringForGet(HttpGet request) {
        String result = null;
        try {
            //获得HttpResponse实例
            HttpResponse response = getHttpResponse(request);
            //判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "network anomaly";
            return result;
        }
        return null;
    }
}
