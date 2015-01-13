package cn.flyaudio.weather.activity;

import java.util.ArrayList;
import java.util.List;

import cn.flyaudio.Weather.R;

import cn.flyaudio.weather.data.YahooWeatherApplication;
import cn.flyaudio.weather.objectInfo.CityResult;
import cn.flyaudio.weather.service.YahooService;
import cn.flyaudio.weather.util.Constant;
import cn.flyaudio.weather.util.Smart_GetCity_SQL;
import cn.flyaudio.weather.util.UtilsTools;
import cn.flyaudio.weather.util.Yahoo_GetCity_Client;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.OnDismissListener;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCityActivity extends Activity {
	private GridView grid;
	private SharedPreferences preference = null;
	public List<String> mCityList; // 城市名称
	public List<String> mCityCodeList; // 城市代码Woeid
	private Boolean searching = false;
	private AutoCompleteTextView autoCompleteTextView;
	private TextView slideText;
	private List<CityResult> citylist = new ArrayList<CityResult>();
	private List<String> mstringArray;
	private Boolean isVisibility =false;
	private String[] citynamecopy;
	int grid_page_size;
	int current_grid_page_size = 0;
	ArrayAdapter<String> adapter;
	private Toast mToast;
	private View  mRootView;
	private View  cityView;
	private Yahoo_GetCity_Client mCityClient;
	@Override
	protected void onResume() {
//		mRootView.setBackgroundColor(Integer.parseInt(SystemProperties.get(Constant.PROPERTY_COLORTHEME,"-65536")));
		// TODO Auto-generated method stub
		//mRootView.setBackgroundColor(getSharedPreferences("weather", 0).getInt("weather_widget_color", 0x9900ff00));
		
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		final String[] cityname = { getString(R.string.city1),
				getString(R.string.city2), getString(R.string.city3),
				getString(R.string.city4), getString(R.string.city5),
				getString(R.string.city6), getString(R.string.city7),
				getString(R.string.city8), getString(R.string.city9),
				getString(R.string.city10), getString(R.string.city11),
				getString(R.string.city12), getString(R.string.city13),
				getString(R.string.city14), getString(R.string.city15),
				getString(R.string.city16), getString(R.string.city17),
				getString(R.string.city18), getString(R.string.city19),
				getString(R.string.city20), getString(R.string.city21),
				getString(R.string.city22), getString(R.string.city23),
				getString(R.string.city24), getString(R.string.city25),
				getString(R.string.city26), getString(R.string.city27),
				getString(R.string.city28), getString(R.string.city29),
				getString(R.string.city30), getString(R.string.city31),
				getString(R.string.city32), getString(R.string.city33),
				getString(R.string.city34), getString(R.string.city35),
				getString(R.string.city36) };

		 mCityClient = new Yahoo_GetCity_Client(
				AddCityActivity.this);
		
		citynamecopy = cityname.clone();
		mstringArray = new ArrayList<String>();

		grid_page_size = (int) Math.ceil(citynamecopy.length / (12 * 1.0));

		getcurrentData(current_grid_page_size);

		setContentView(R.layout.addcity_activity_layout);
		mRootView = findViewById(R.id.addcity_bg_color);
		preference = getSharedPreferences("weather", MODE_PRIVATE);
		mCityList = new ArrayList<String>();
		mCityCodeList = new ArrayList<String>();
		Button backButton = (Button) findViewById(R.id.back);// return Button
		slideText = (TextView) findViewById(R.id.choose_city_slider_page_num);
		slideText.setText((current_grid_page_size + 1) + "/" + grid_page_size);
		backButton.setOnClickListener(backClickListener);
		getCityListAndCityCodeListFormShared();
		cityView =  findViewById(R.id.citylist);
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete_city);
		

		CityAdapter mCityAdapter = new CityAdapter(this, null);
		autoCompleteTextView.setAdapter(mCityAdapter);
	
		autoCompleteTextView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				Log.d("zzz"," AutoCompleteTextView.OnDismissListener()");
				isVisibility = false;
				cityView.setVisibility(View.VISIBLE);
			}
			
			
		});
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				boolean different = true;
				CityResult result = citylist.get(position);

				for (int i = 0; i < mCityList.size(); i++) {
					
					
						if (mCityList.get(i).equals((result.getCityName().equals(result.getAdmin2())?result.getCityName():
							(result.getAdmin2()+","+result.getCityName())))){
							different = false;
							break;
						}
					
						
				}

				if (different == true) {
					mCityList.add(result.getCityName());
					if(YahooWeatherApplication.isINfoFromYahoo){
						mCityCodeList.add(result.getWoeid());
					}else{
						mCityCodeList.add(result.getAreaid());
					}
					
					
					int current = preference.getInt("current", 1);
					SharedPreferences.Editor edit = preference.edit();
					edit.putInt("current", current);
					edit.commit();
					for (int j = 0; j < 4; j++) {
						if (preference.getString(String.valueOf(j + 1), null) == null) {
							
							if(YahooWeatherApplication.isINfoFromYahoo){
								writeSharpPreference(j + 1,result.getCityname_pinyin(),result.getCityName(),
										result.getWoeid());
							}else{
								autoCompleteTextView.setText("");
								
								
								
								writeSharpPreference(j + 1, (result.getCityname_pinyin().equals(result.getAdmin2_en())?result.getCityname_pinyin():
									(result.getAdmin2_en()+","+result.getCityname_pinyin())),
									(result.getCityName().equals(result.getAdmin2())?result.getCityName():
										(result.getAdmin2()+","+result.getCityName())),
										result.getAreaid());
							}
							
							
							
							break;
						}
					}
					AddCityActivity.this.startService(new Intent(
							AddCityActivity.this, YahooService.class));
					Intent mIntent = new Intent(AddCityActivity.this,
							CityEditPageActivity.class);
					startActivity(mIntent);
					AddCityActivity.this.finish();
				} else {
					autoCompleteTextView.setText("");
					Toast.makeText(AddCityActivity.this,
							getResources().getString(R.string.had_existed),
							Toast.LENGTH_LONG).show();
				}

			}

		});

		adapter = new ArrayAdapter<String>(this, R.layout.gridview_cityitem,
				mstringArray);

		grid = (GridView) findViewById(R.id.commoncity);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				autoCompleteTextView.setText(cityname[position
						+ current_grid_page_size * 12]);
			}
		});

	}

	private Button.OnClickListener backClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(AddCityActivity.this,
					CityEditPageActivity.class);
			startActivity(intent);
			AddCityActivity.this.finish();
		}
	};

	public void searchCityOnclick(View v) {
		cancelToast();
		
		if (!UtilsTools.isNetworkAvailable(AddCityActivity.this)&&YahooWeatherApplication.isINfoFromYahoo) {
			showToast(getResources().getString(R.string.neworkconnect));		
			return;
		}
		autoCompleteTextView.setText(autoCompleteTextView.getEditableText()
				.toString());
		searching = true;
	}

	public void previous_button_OnclickListener(View v) {
		if (current_grid_page_size - 1 >= 0) {
			current_grid_page_size--;
			getcurrentData(current_grid_page_size);
		}
		adapter.notifyDataSetChanged();
		slideText.setText((current_grid_page_size + 1) + "/" + grid_page_size);
	}

	public void next_button_OnclickListener(View v) {
		if (current_grid_page_size + 1 < grid_page_size) {
			current_grid_page_size++;
			getcurrentData(current_grid_page_size);
		}
		adapter.notifyDataSetChanged();
		slideText.setText((current_grid_page_size + 1) + "/" + grid_page_size);

	}

	public void getcurrentData(int page) {
		int start = page * 12;
		int end = start + 12;
		mstringArray.clear();
		while (start < citynamecopy.length && start < end) {
			mstringArray.add(citynamecopy[start]);
			start++;
		}

	}

	public void getCityListAndCityCodeListFormShared() {
		for (int i = 0; i < 4; i++) {
			String city = preference.getString(String.valueOf(i + 1), null);
			String code = readSharpPreference(i + 1);
			if (city != null) {
				mCityList.add(city);
				mCityCodeList.add(code);
			}
		}
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
	
	public void showToast(String text) {      
		mToast = Toast.makeText(AddCityActivity.this, text, Toast.LENGTH_SHORT);  
		mToast.show();  
    }  
      
    public void cancelToast() {  
            if (mToast != null) {  
                mToast.cancel();  
            }  
        }  

	public void writeSharpPreference(int index, String cityname_pinyin,String cityname  , String citynum) {
		SharedPreferences.Editor editor = preference.edit();
		switch (index) {
		case 1:
			editor.putString("1", cityname);
			editor.putString("10", cityname_pinyin);
			editor.putString("city1", citynum);
			break;
		case 2:
			editor.putString("2", cityname);
			editor.putString("20", cityname_pinyin);
			editor.putString("city2", citynum);
			break;
		case 3:
			editor.putString("3", cityname);
			editor.putString("30", cityname_pinyin);
			editor.putString("city3", citynum);
			break;
		case 4:
			editor.putString("4", cityname);
			editor.putString("40", cityname_pinyin);
			editor.putString("city4", citynum);
			break;
		default:
			break;
		}
		editor.commit();
	}

	public class CityAdapter extends ArrayAdapter<CityResult> implements
			Filterable {
		private Context context;

		public CityAdapter(Context context, List<CityResult> citylist) {
			super(context, R.layout.cityresult_layout, citylist);
			// TODO Auto-generated constructor stub
		
			this.context = context;

		}

		@Override
		public int getCount() {

			if (citylist != null) {
				return citylist.size();

			}
			return 0;
		}

		@Override
		public CityResult getItem(int position) {

			if (citylist != null) {
				return citylist.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {

			if (citylist != null) {
				return citylist.get(position).hashCode();

			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(R.layout.cityresult_layout,
						parent, false);
			}
			if (citylist != null) {		   
				   String cityName;
				   String adminName;
				   String admin2Name;
				   String countryName;
				   
				TextView tvTextView = (TextView) view
						.findViewById(R.id.cityitem);
				
				if(YahooWeatherApplication.isINfoFromYahoo){
					cityName = citylist.get(position).getCityName();
					 adminName = citylist.get(position).getAdmin1();
					 countryName = citylist.get(position).getCountry();
					tvTextView.setText((cityName != null ? (cityName + ",") : "")
							+ (adminName != null ? (adminName + ",") : "")
							+ (countryName != null ? countryName : ""));
				}else{		
					
					
					 cityName = YahooWeatherApplication.isCNLanguage?
							 citylist.get(position).getCityName():
						 citylist.get(position).getCityname_pinyin();
					admin2Name	= 	  YahooWeatherApplication.isCNLanguage?
							 citylist.get(position).getAdmin2():
						 citylist.get(position).getAdmin2_en();
							 
					 adminName = YahooWeatherApplication.isCNLanguage?citylist.get(position).getAdmin1():citylist.get(position).getAdmin1_en();
					 countryName = citylist.get(position).getCountry();
					
					  tvTextView.setText(
								 (adminName != null ? (adminName + ",") : "")+
								 (admin2Name != null ? (admin2Name + ",") : "")+
								(cityName != null ? (cityName + "") : "")
								);
				   
					
				}
				
			}

			return view;

		}

		@Override
		public Filter getFilter() {
			
			
			Filter cityFilter = new Filter() {
				@Override
				protected void publishResults(CharSequence contain,
						FilterResults results) {
					if(isVisibility){
						cityView.setVisibility(View.INVISIBLE);
					}
					citylist = (List<CityResult>) results.values;
					notifyDataSetChanged();
				}

				@Override
				protected FilterResults performFiltering(CharSequence contait) {
					FilterResults results = new FilterResults();
					
					if (contait == null || contait.length() < 1 ) {
						return results;
					}
					if (searching == true) {
						List<CityResult> citylist;
						if(YahooWeatherApplication.isINfoFromYahoo ){
						citylist = mCityClient
								.getCityList(contait.toString());
						}else{
							
							Log.d("zzz","Smart_GetCity_SQL");
							citylist = new Smart_GetCity_SQL(getApplicationContext()).query(contait.toString());
						}
						
						
						results.values = citylist;
						results.count = citylist.size();
						if (results.count >0) {
							isVisibility = true;
						}else  {
							searching = false;
							//showToast(getResources().getString(R.string.nullsearch));
						}				
						searching = false;
					}
					return results;
				}
			};		
			return cityFilter;
		}

	}

}
