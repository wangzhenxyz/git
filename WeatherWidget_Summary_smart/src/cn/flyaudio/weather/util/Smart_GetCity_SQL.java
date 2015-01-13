package cn.flyaudio.weather.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.flyaudio.Weather.R;
import cn.flyaudio.weather.objectInfo.CityResult;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Contactables;
import android.util.Log;

public class Smart_GetCity_SQL {

	public Smart_GetCity_SQL(Context context) {
		super();
		this.context = context;
	}

	private Context context;
	private SQLiteDatabase database;
	public void importInitDatabase() {
		Log.d("zzz","importInitDatabase");
		//数据库路径
		String dirPath = "/data/data/cn.flyaudio.Weather/databases";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		//创建或打开数据库
		File dbfile = new File(dir, "weather_db_weather.db");
		try {
			if (!dbfile.exists()) {
				dbfile.createNewFile();
			}
			InputStream is = context.getResources().openRawResource(R.raw.weather);
			FileOutputStream fos = new FileOutputStream(dbfile);
			byte[] buffere = new byte[is.available()];
			is.read(buffere);
			fos.write(buffere);
			is.close();
			fos.close();
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<CityResult> query(String cityname){
		importInitDatabase();
		String path = "/data" + Environment.getDataDirectory().getAbsolutePath()
				+ "/cn.flyaudio.Weather/databases/weather_db_weather.db";
		System.out.println("path : " + path);
		database = SQLiteDatabase.openOrCreateDatabase(path, null);
		List<CityResult> result = new ArrayList<CityResult>();
		
	//	String getProvincesSQL = "select city_num,provinces_table, cities_table from cities_table where (city_name='" + cityname + "'"+" or province_name='" + cityname + "')";
	String getProvincesSQL = "select cityId, CountyEnName,CountyCnName,cityEnName,cityCnName,provEnName,provCnName from weathercitytable "
	                                               +"where CountyCnName like '%"+cityname+"%'"
			                                            +"Or CountyEnName like '%"+cityname+"%'"
			                                            +"Or cityCnName like '%"+cityname+"%'"
			                                            +"Or cityEnName like '%"+cityname+"%'"
			                                            +"Or provEnName like '%"+cityname+"%'"
			                                            +"Or provCnName like '%"+cityname+"%'";
		Cursor cursor = database.rawQuery(getProvincesSQL, null);
		System.out.println("cursor count : " + cursor.getCount());
		while (cursor.moveToNext()) {
			CityResult cityResult = new CityResult();
			cityResult.setCityName(cursor.getString(2));
			cityResult.setCityname_pinyin(cursor.getString(1));
			cityResult.setAdmin2(cursor.getString(4));
		    cityResult.setAdmin2_en(cursor.getString(3));
			cityResult.setAdmin1(cursor.getString(6));
			cityResult.setAdmin1_en(cursor.getString(5));
			cityResult.setAreaid(cursor.getString(0));
			Log.d("zzz","cursor"+cursor.getString(0));
			Log.d("zzz","cursor"+cursor.getString(1));
			result.add(cityResult);
		}
		return result;
		
		
	}
	
}
