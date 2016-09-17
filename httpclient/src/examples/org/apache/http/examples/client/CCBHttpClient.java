package org.apache.http.examples.client;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public class CCBHttpClient {

    private static  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static CloseableHttpClient httpclient = null;
    private static Object lock = new Object();
    private static int SO_TIME = 5000;
    private static int CON_TIME = 5000;
    private static int CON_REQ_TIME = 5000;

    static{

        if(httpclient == null){
            synchronized (lock){
                if(httpclient == null){
                    httpclient = HttpClients.createDefault();
                }
            }
        }
    }

    public static void httpPostCCB(String url,int socketTime,int connectTimeout,int connectionRequestTimeout){


        try{
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(socketTime == 0 ? SO_TIME : socketTime)
                    .setConnectTimeout(connectTimeout == 0 ? CON_TIME :connectTimeout)
                    .setConnectionRequestTimeout(connectionRequestTimeout == 0 ? CON_REQ_TIME : connectionRequestTimeout)
                    .build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("imgCode","019999"));
            String format = simpleDateFormat.format(new Date());
            nvps.add(new BasicNameValuePair("day",format));
            nvps.add(new BasicNameValuePair("bondType","2"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(EntityUtils.toString(response.getEntity()));
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {

        }
    }


    public static void main(String[] args) {
        CCBHttpClient client = new CCBHttpClient();
        String url = "http://wap.ccb.com/mbs_web/invest_metal_marketTrendList.do";
        httpPostCCB(url,0,0,0);
    }
}
