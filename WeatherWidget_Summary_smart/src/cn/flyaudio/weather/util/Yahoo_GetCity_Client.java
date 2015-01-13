package cn.flyaudio.weather.util;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import cn.flyaudio.weather.activity.CityEditPageActivity;
import cn.flyaudio.weather.objectInfo.CityResult;
import cn.flyaudio.Weather.R;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

public class Yahoo_GetCity_Client {
	private final static String TAG = Constant.TAG;
	private final static Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;
	private Context mContext;
	private static int MAX_CITY_RESULT = 8;
	public static String YAHOO_GEO_URL = "http://where.yahooapis.com/v1";
	public static String YAHOO_WEATHER_URL = "http://weather.yahooapis.com/forecastrss";
	private static String APPID = "fNlZ4grV34GPmwzwyEXhn7Mxle9p5bhCDU.BmhqD7sT8wzZAmb535uYbOXGwVGLhasw-";
    private Toast mToast;
	public Yahoo_GetCity_Client(Context Context) {

		this.mContext = Context;
	}
	
	public void showToast(String text) {      
		mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);  
		mToast.show();  
    } 
	
	public void cancelToast() {  
        if (mToast != null) {  
            mToast.cancel();  
        }  
    }  
	
	public List<CityResult> getCityList(String cityName) {
		List<CityResult> result = new ArrayList<CityResult>();
		Boolean  httpconnect = true; 
		
		try {
			String query = makeQueryCityURL(cityName);
	//		HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient =  initHttp();
			HttpGet httpGet = new HttpGet(query);
			if(DEBUG_FLAG)Log.e(TAG, query.toString());
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int code = httpResponse.getStatusLine().getStatusCode();
			if(DEBUG_FLAG)Log.e(TAG, code+"!!!!");
			if (code != HttpStatus.SC_OK) {
				if(DEBUG_FLAG)Log.e(TAG, "sorry,ti is erro!");
			}
			HttpEntity entity = httpResponse.getEntity();
			InputStream content = entity.getContent();
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(content, "UTF-8");
			if(DEBUG_FLAG)Log.d(TAG, "XML Parser ok");
			int event = parser.getEventType();

			CityResult city = null;
			String tagName = null;
			String currentTag = null;

			// We start parsing the XML
			while (event != XmlPullParser.END_DOCUMENT) {
				tagName = parser.getName();

				if (event == XmlPullParser.START_TAG) {
					if (tagName.equals("place")) {
						// place Tag Found so we create a new CityResult
						city = new CityResult();

					}
					currentTag = tagName;

				} else if (event == XmlPullParser.TEXT) {
					// We found some text. let's see the tagName to know the tag
					// related to the text
					if ("woeid".equals(currentTag))
						city.setWoeid(parser.getText());
					else if ("name".equals(currentTag))
						city.setCityName(parser.getText());
					else if ("country".equals(currentTag))
						city.setCountry(parser.getText());
					else if ("admin1".equals(currentTag))
						city.setAdmin1(parser.getText());
					// We don't want to analyze other tag at the moment
				} else if (event == XmlPullParser.END_TAG) {
					if ("place".equals(tagName))
						result.add(city);
				}

				event = parser.next();
			}
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			httpconnect = false;
			cancelToast();
			showToast(mContext.getResources().getString(R.string.connectiontimeout));	
		} catch (ClientProtocolException e){
			httpconnect = false;
			cancelToast();
			showToast(mContext.getResources().getString(R.string.connectiontimeout));	
			e.printStackTrace();
		}  catch (Throwable t) {
			t.printStackTrace();
			// Log.e("Error in getCityList", t.getMessage());
		} 
		if(httpconnect&&result.isEmpty()){
			cancelToast();
			showToast(mContext.getResources().getString(R.string.nullsearch));	
		}
		return result;
	}

	private String makeQueryCityURL(String cityName) {
		Resources resources = mContext.getResources();
		Configuration config = resources.getConfiguration();
		String mlang = config.locale.toString();
		mlang = mlang.replaceAll("_", "-");
		return YAHOO_GEO_URL + "/places.q(" + cityName + "%2A);count="
				+ MAX_CITY_RESULT + "?lang=" + mlang + "&appid=" + APPID;
	}

	private HttpClient initHttp(){
		HttpClient cilent= new DefaultHttpClient();
		cilent.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, Constant.GETCITY_TIME_OUT_DELAY);
		cilent.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, Constant.GETCITY_TIME_OUT_DELAY);
		return cilent;
	}
	
}
