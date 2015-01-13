package cn.flyaudio.weather.webserviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import cn.flyaudio.weather.objectInfo.FullWeatherInfo;
import cn.flyaudio.weather.util.Constant;
import cn.flyaudio.weather.util.SaxToolsForYahoo;

import android.util.Log;

/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午2:56:38
 */
public class WebServiceYaHooImpl {

	private final String TAG = Constant.TAG;
	private final Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;
	
	
	private FullWeatherInfo mForecastyweathers;
	SAXParserFactory factory = null;
	SAXParser saxParser = null;
	XMLReader xmlReader = null;
	SaxToolsForYahoo tools = null;

	public FullWeatherInfo getWeatherInfo(String citycode) {
		try {
			factory = SAXParserFactory.newInstance();
			saxParser = factory.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			mForecastyweathers = new FullWeatherInfo();
			tools = new SaxToolsForYahoo(mForecastyweathers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			
			HttpURLConnection connection = null;
			   InputStream content = null;
			    URL url = new URL(Constant.YAHOO_WEATHER_URL + citycode
						+ Constant.YAHOO_WEATHER_URL_PARAM);
			    connection = (HttpURLConnection) url.openConnection();
			    connection.setConnectTimeout(5000);
			    connection.setRequestMethod("GET");
			    connection.setRequestProperty("Accept-Language", "zh-CN");
			    connection.setRequestProperty("Referer", url.toString());
			    connection.setRequestProperty("Charset", "UTF-8");
			    connection.setRequestProperty("Connection", "Keep-Alive");
			    content=connection.getInputStream();
			    
	//		HttpClient httpClient = new DefaultHttpClient();
			//HttpClient httpClient =initHttp();
			//HttpGet httpGet = new HttpGet(Constant.YAHOO_WEATHER_URL + citycode
			//		+ Constant.YAHOO_WEATHER_URL_PARAM);
			//HttpResponse response = httpClient.execute(httpGet);
			//StatusLine statusLine = response.getStatusLine();
			//int statusCode = statusLine.getStatusCode();
			//if (statusCode == HttpStatus.SC_OK) {
				//HttpEntity entity = response.getEntity();
				//InputStream content = entity.getContent();
				xmlReader.setContentHandler(tools);
				xmlReader
						.parse(new InputSource(new InputStreamReader(content)));
			//} else {
				//if (DEBUG_FLAG)
					Log.e(TAG,
							"[WebServiceYaHooImpl] getWeatherInfo() HttpStatus.SC_EXPECTATION_FAILED!!!");
			//}
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return mForecastyweathers;
	}
	
	
	private HttpClient initHttp(){
		HttpClient cilent= new DefaultHttpClient();
		cilent.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, Constant.GETWEATHER_TIME_OUT_DELAY);
		cilent.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,Constant.GETWEATHER_TIME_OUT_DELAY);
		return cilent;
	}

}
