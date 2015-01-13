package cn.flyaudio.weather.util;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.content.Context;
import android.widget.Toast;

public class WebAccessTools {
	
//	private Context context;
	public WebAccessTools(/*Context context*/) {
//		this.context = context;
	}
	public  InputStream getWebContent(String url) {
		HttpGet request = new HttpGet(url);
		HttpParams params=new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		HttpClient httpClient = new DefaultHttpClient(params);
		try{
			HttpResponse response = httpClient.execute(request);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response.getEntity().getContent();//content
			} else {
	//			Toast.makeText(context, "Data loading``", Toast.LENGTH_LONG).show();
			}
			
		}catch(Exception e) {
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}
}