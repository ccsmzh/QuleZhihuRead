package qule.gino.zhihuread.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import qule.gino.zhihuread.BuildConfig;

public class HttpManger {
    private static final String TAG = "HttpManger";

    private static final String USER_AGENT = "QuleZhihuRead/Android";

    final int ConnectionTimeout = 30000;
    final int SocketTimeout = 60000;
    final int SocketBufferSize = 4096;
    private HttpClient httpclient;
    private HttpContext localContext = new BasicHttpContext();
    public final static int MAX_TOTAL_CONNECTIONS = 5;

    private byte[] buffer = new byte[4096];

    private void ensureInitialized() {
        if (httpclient == null) {
            // synchronized (this) {
            HttpParams httpParameters = new BasicHttpParams();
            httpParameters.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
            ConnManagerParams.setTimeout(httpParameters, ConnectionTimeout);
            ConnManagerParams.setMaxTotalConnections(httpParameters, MAX_TOTAL_CONNECTIONS);
            // Set the timeout in milliseconds until a connection is
            // established.
            HttpConnectionParams.setConnectionTimeout(httpParameters, ConnectionTimeout);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            HttpConnectionParams.setSoTimeout(httpParameters, SocketTimeout);
            HttpConnectionParams.setSocketBufferSize(httpParameters, SocketBufferSize);

            HttpClientParams.setRedirecting(httpParameters, false);
            HttpProtocolParams.setUserAgent(httpParameters, USER_AGENT);

            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(httpParameters, schReg);

            httpclient = new DefaultHttpClient(conMgr, httpParameters);
        }
    }

    public byte[] get(String urlStr) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "- Request Url is - " + urlStr);
        }
        HttpGet httpGet = new HttpGet(urlStr);
        try {
            ensureInitialized();

            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = null;
            response = httpclient.execute(httpGet, localContext);
            int statusCode = response.getStatusLine().getStatusCode();
            // check the response status code, only HTTP-200 be allowed.
            if (statusCode != HttpStatus.SC_OK) {
                throw new IOException("http status exception, statusCode: " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            InputStream responseStream = entity.getContent();
            ByteArrayOutputStream memStream = new ByteArrayOutputStream();
            int bytesRead = 0;
            while ((bytesRead = responseStream.read(buffer)) > 0)
                memStream.write(buffer, 0, bytesRead);

            return memStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public byte[] httpGet(String urlStr) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "- Request Url is - " + urlStr);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return out.toByteArray();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
