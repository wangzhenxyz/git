package cn.flyaudio.weather.activity;

import java.util.ArrayList;
import java.util.List;

import cn.flyaudio.Weather.R;

import cn.flyaudio.weather.adapter.CityAdapter;
import cn.flyaudio.weather.data.YahooWeatherApplication;

import cn.flyaudio.weather.util.Constant;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:15:03
 */
public class CityEditPageActivity extends Activity {
	private final String TAG = Constant.TAG;
	private final Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;
	private SharedPreferences preference = null;
	private GridView gridview;
	public List<String> mCityList;// 城市名称
	public List<String> mCityEnList;// 城市名称
	public List<String> mCityCodeList;// 城市代码
    private Toast mToast;
	private UpdateUIReceiver mUIReceiver;
	private View mRootView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.cityedit_activity_layout);
		mRootView = findViewById(R.id.cityeditpagebg_color);
		preference = getSharedPreferences("weather", MODE_PRIVATE);

		Button addButton = (Button) findViewById(R.id.addbutton);
		Button backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(backClickListener);
		addButton.setOnClickListener(addClickListener);

		mUIReceiver = new UpdateUIReceiver();
		IntentFilter iFilter = new IntentFilter(Constant.ACTION_UPDATE_ADAPTER);
		registerReceiver(mUIReceiver, iFilter);
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setNumColumns(1);
	}

	public void getCityListAndCityCodeListFormShared() {
		for (int i = 0; i < 4; i++) {
			String city = preference.getString(String.valueOf(i + 1), null);
			String cityen = preference.getString(String.valueOf(i*10 + 10), null);
			if (DEBUG_FLAG)
				Log.d(TAG,
						"[CityEditPageActivity] getCityListAndCityCodeListFormShared() city == "
								+ city);
			String code = readSharpPreference(i + 1);
			
			if (city != null) {
				mCityList.add(city);
				mCityEnList.add(cityen);
				mCityCodeList.add(code);
			}
		}
	}

	private Button.OnClickListener addClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			cancelToast();
			if (mCityList.size() < 4) {
				Intent mIntent = new Intent(CityEditPageActivity.this,
						AddCityActivity.class);
				startActivity(mIntent);
				CityEditPageActivity.this.finish();
			} else {
				showToast(getString(R.string.citynum_limit));
					
			}

		}
	};

	public void showToast(String text) {      
		mToast = Toast.makeText(CityEditPageActivity.this, text, Toast.LENGTH_SHORT);  
		mToast.show();  
    }  
      
    public void cancelToast() {  
            if (mToast != null) {  
                mToast.cancel();  
            }  
        }  
	
	private Button.OnClickListener backClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(CityEditPageActivity.this,
					WeatherDetailsActivity.class);
			startActivity(intent);
			CityEditPageActivity.this.finish();
		}
	};

	public void writeSharpPreference(int index, String cityname, String citynum) {
		SharedPreferences.Editor editor = preference.edit();
		switch (index) {
		case 1:
			editor.putString("1", cityname);
			editor.putString("city1", citynum);
			break;
		case 2:
			editor.putString("2", cityname);
			editor.putString("city2", citynum);
			break;
		case 3:
			editor.putString("3", cityname);
			editor.putString("city3", citynum);
			break;
		case 4:
			editor.putString("4", cityname);
			editor.putString("city4", citynum);
			break;
		default:
			break;
		}
		editor.commit();
	}

	public void removePreference() {
		SharedPreferences.Editor editor = preference.edit();
		editor.clear();
		editor.commit();
	}

	public String readSharpPreference(int index) {
		String city = null;
		switch (index) {
		case 1:
			city = preference.getString("city1", Constant.DEFAULT_CITYCODE);
			break;
		case 2:
			city = preference.getString("city2", "10000");
			break;
		case 3:
			city = preference.getString("city3", "10000");
			break;
		case 4:
			city = preference.getString("city4", "10000");
			break;
		default:
			break;
		}
		return city;
	}

	class UpdateUIReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			
			Log.d("zzz","UpdateUIReceiver");
			mCityCodeList.clear();
			mCityList.clear();
			mCityEnList.clear();
			for (int i = 1; i < 5; i++) {
				String city = preference.getString(String.valueOf(i), null);
				String cityen = preference.getString(String.valueOf(i*10), null);
				String code = preference.getString("city" + i, null);
				if (city != null && code != null) {
					mCityList.add(city);
					mCityCodeList.add(code);
					mCityEnList.add(cityen);
					if (DEBUG_FLAG)
						Log.d(TAG,
								"[CityEditPageActivity] UpdateUIReceiver  mCityList.i  == "
										+ city);
				}
			}

			CityAdapter ca = new CityAdapter(CityEditPageActivity.this,
					CityEditPageActivity.this,YahooWeatherApplication.isCNLanguage?mCityList:mCityEnList);
			gridview.setAdapter(ca);
			
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mUIReceiver);
	}

	@Override
	protected void onResume() {
//	mRootView.setBackgroundColor(Integer.parseInt(SystemProperties.get(Constant.PROPERTY_COLORTHEME,"-65536")));
		//mRootView.setBackgroundColor(getSharedPreferences("weather", 0).getInt("weather_widget_color", 0x9900ff00));
		// TODO Auto-generated method stub
		mCityList = new ArrayList<String>();
		mCityCodeList = new ArrayList<String>();
		mCityEnList = new ArrayList<String>();
		getCityListAndCityCodeListFormShared();
		gridview.setAdapter(new CityAdapter(CityEditPageActivity.this,
				CityEditPageActivity.this,YahooWeatherApplication.isCNLanguage? mCityList:mCityEnList));
		super.onResume();
	}
}