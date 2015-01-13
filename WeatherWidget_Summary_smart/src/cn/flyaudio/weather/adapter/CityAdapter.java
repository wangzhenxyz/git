package cn.flyaudio.weather.adapter;

import java.util.List;

import cn.flyaudio.Weather.R;
import cn.flyaudio.weather.activity.CityEditPageActivity;
import cn.flyaudio.weather.activity.WeatherDetailsActivity;
import cn.flyaudio.weather.data.YahooWeatherApplication;
import cn.flyaudio.weather.service.YahooService;
import cn.flyaudio.weather.util.Constant;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:16:08
 */
public class CityAdapter extends BaseAdapter {

	private final String TAG = Constant.TAG;
	private final Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;
	private Context mContext;
	private List<String> mCityList;
	private Activity mActivity;

	public CityAdapter(Activity a, Context context, List<String> citys) {
		mActivity = a;
		mContext = context;
		mCityList = citys;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mCityList.size();
	}

	public void setCityList(List<String> citys) {
		mCityList = citys;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCityList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ItemViews itemViews;
		if (convertView == null) {

			View v = LayoutInflater.from(mContext).inflate(
					R.layout.weather_city_item, null);
			itemViews = new ItemViews();
			itemViews.cityname = (TextView) v.findViewById(R.id.gvcityname);
			itemViews.delete = (Button) v.findViewById(R.id.delete);
			v.setTag(itemViews);
			final String currentCity = mCityList.get(position);
			SharedPreferences shared = mContext.getSharedPreferences("weather",
					0);

			if(YahooWeatherApplication.isCNLanguage){
				if (shared.getString(String.valueOf(shared.getInt("current", -1)),
						"") == currentCity) {
					itemViews.cityname
							.setBackgroundResource(R.drawable.weather_on_d);
					itemViews.cityname.setPadding(50, 0, 0, 0);
                                        itemViews.cityname.setTextColor(Color.WHITE);
					
				}
			}else{
				if (shared.getString(String.valueOf(shared.getInt("current", -1)*10),
						"") == currentCity) {
					itemViews.cityname
							.setBackgroundResource(R.drawable.weather_on_d);
					itemViews.cityname.setPadding(50, 0, 0, 0);
					itemViews.cityname.setTextColor(Color.WHITE);
					
				}
			}
			 

			itemViews.cityname.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					SharedPreferences shared = mContext.getSharedPreferences(
							"weather", 0);

					SharedPreferences.Editor edit = shared.edit();
					int current = 1;
					for (int i = 1; i < 5; i++) {
						
						if(YahooWeatherApplication.isCNLanguage){
							if (shared.getString(String.valueOf(i), "").equals(
									currentCity))
								current = i;
						}else{
							if (shared.getString(String.valueOf(i*10), "").equals(
									currentCity))
								current = i;
						}

						
					}
					edit.putInt("current", current);
					edit.commit();

					Intent intent = new Intent(Constant.ACTION_APPWIDGET_UPDATE);
					intent.putExtra("current", current);
					for (int i = 0; i < 4; i++) {
						YahooService.backups[i] = false;
						if (DEBUG_FLAG)
							Log.d(TAG,
									"[CityAdapter] itemViews.select.setOnClick()  backup"
											+ (i + 1) + " == "
											+ YahooService.backups[i]);
					}
					mContext.sendBroadcast(intent);
					Intent mIntentHome = new Intent(mContext,WeatherDetailsActivity.class);
					mContext.startActivity(mIntentHome);
					mActivity.finish();

				}
			});
			itemViews.delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("zzz","itemViews.delete");
					
					if (getCityCount() > 1) {
						updateWeatherShared(currentCity);
						mContext.sendBroadcast(new Intent(
								Constant.ACTION_UPDATE_ADAPTER));
					}
				}
			});
			convertView = v;
		} else {
			itemViews = (ItemViews) convertView.getTag();
		}
		itemViews.cityname.setText(mCityList.get(position));

		return convertView;
	}

	protected void updateWeatherShared(String currentcity) {

		if (DEBUG_FLAG)
			Log.d(TAG, "[CityAdapter] updateWeatherShared()  delete city  == "
					+ currentcity);
		SharedPreferences shared = mContext.getSharedPreferences("weather", 0);
		SharedPreferences.Editor edit = shared.edit();
		int index = 1;

		// for get delete index
		for (int i = 1; i <= 4; i++) {
			
			
			String city =YahooWeatherApplication.isCNLanguage? shared.getString(String.valueOf(i), null):shared.getString(String.valueOf(i*10), null);
			
			
			if (city != null && city.equals(currentcity)) {
				index = i;
			}
		}

		// remove info city
		removeCity(edit, String.valueOf(index));
		removeCity(edit, String.valueOf(index*10));
		removeCity(edit, "city" + index);
		clearSharedWithIndex(index);

		int current = shared.getInt("current", 1);

		if (current == index) {
			for (int i = 0; i < 4; i++) {
				String city = shared.getString(String.valueOf(i + 1), null);
				if (city != null) {
					current = i + 1;
					break;
				}
			}
			edit.putInt("current", current);
			edit.commit();
			mContext.sendBroadcast(new Intent(Constant.ACTION_APPWIDGET_UPDATE));
		}
	}

	private void removeCity(SharedPreferences.Editor edit, String key) {
		edit.remove(key);
		edit.commit();
	}

	public int getCityCount() {
		SharedPreferences shared = mContext.getSharedPreferences("weather", 0);
		int count = 0;
		for (int i = 0; i < 4; i++) {
			String city = shared.getString(String.valueOf(i + 1), null);
			if (city != null)
				count++;
		}
		Log.d("zzz","count"+count);
		return count;
	}

	protected void clearSharedWithIndex(int index) {
		if (DEBUG_FLAG)
			Log.d(TAG, "[CityAdapter] clearSharedWithIndex()  index  == "
					+ index);
		switch (index) {
		case 1:
			ClearShread(mContext.getSharedPreferences("FirstWeather", 0).edit());
			break;
		case 2:
			ClearShread(mContext.getSharedPreferences("SecondWeather", 0)
					.edit());
			break;
		case 3:
			ClearShread(mContext.getSharedPreferences("ThirdWeather", 0).edit());
			break;
		case 4:
			ClearShread(mContext.getSharedPreferences("FouthWeather", 0).edit());
			break;
		default:
			break;
		}
	}

	public void ClearShread(SharedPreferences.Editor edit) {
		edit.clear();
		edit.commit();
	}

	class ItemViews {
		TextView cityname;
		Button delete;
	}

}
