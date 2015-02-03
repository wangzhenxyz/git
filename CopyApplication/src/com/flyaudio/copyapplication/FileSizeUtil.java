package com.flyaudio.copyapplication;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.List;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FileSizeUtil {
	private final static String TAG = "FileSizeUtil";

	public static long size = 0;
	public static long fileSize = 0;
	private static FileInputStream fis = null;

	/**
	 * 
	 * 获取指定文件大小
	 * 
	 * @param f
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static long getFileSize(File file) throws Exception

	{

		long size = 0;
		Flog.d(TAG, "getFileSize---start");
		if (file.exists() && file != null) {

			fis = new FileInputStream(file);

			size = fis.available();
			fis.close();

			Flog.d(TAG, "getFileSize---size---++++" + size);
		}

		else {

			// file.createNewFile();

			Flog.e("获取文件大小", "文件不存在!");

		}
		return size;

	}

	/**
	 * 
	 * 获取指定文件夹大小
	 * 
	 * @param f
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public void getFileSizes(File f, Handler handler)

	{

		Flog.d(TAG, "getFileSizesxyz---start");
		try {

			if (f != null && f.exists()) {// xyz
				Flog.d(TAG, "getFileSizesxyz---start---" + f.getAbsolutePath());
				File flist[] = f.listFiles();
				if (flist != null) {// xyz
					Flog.d(TAG, "getFileSizesxyz---start---" + flist.toString());
					for (int i = 0; i < flist.length; i++) {
						if (flist[i].exists()) {

							if (flist[i].isDirectory()) {

								getFileSizes(flist[i], handler);

							}

							else {
								size++;
								fileSize = fileSize + getFileSize(flist[i]);

							}
						}
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			Flog.d(TAG, "getFileSizes---Exception");

		}

	}

	/**
	 * 
	 * 转换文件大小
	 * 
	 * @param fileS
	 * 
	 * @return
	 */

	public static String FormetFileSize(long fileS)

	{
		Flog.d(TAG, "FormetFileSize---start");
		DecimalFormat df = new DecimalFormat("#.00");

		String fileSizeString = "";

		String wrongSize = "0B";

		if (fileS == 0) {
			Flog.d(TAG, "FormetFileSize--0-start");
			return wrongSize;

		}

		if (fileS < 1024) {
			Flog.d(TAG, "FormetFileSize---b");
			fileSizeString = df.format((double) fileS) + "B";

		}

		else if (fileS < 1048576) {
			Flog.d(TAG, "FormetFileSize---kb");
			fileSizeString = df.format((double) fileS / 1024) + "KB";

		}

		else if (fileS < 1073741824) {
			Flog.d(TAG, "FormetFileSize---mb");
			fileSizeString = df.format((double) fileS / 1048576) + "MB";

		}

		else {
			Flog.d(TAG, "FormetFileSize---gb");
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";

		}
		Flog.d(TAG, "FormetFileSize---return");
		return fileSizeString;

	}

}