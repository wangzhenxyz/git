package com.flyaudio.copyapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

public class CopyUtil {
	private final static String TAG = "CopyUtil";
	private static FileInputStream input;
	private static FileOutputStream output;
	public static boolean isFristCopy = true;
	public static long mAvialable;

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath, Handler handler) {
		Flog.d(TAG, "copyFolder");
		if (isFristCopy) {
			isFristCopy = false;
			File mfFile = new File(oldPath);
			File[] fileList = mfFile.listFiles();
			for (int k = 0; k < fileList.length; k++) {
				if (fileList[k].exists()) {
					Flog.d(TAG,
							"copyFolder---"
									+ (newPath + "/" + fileList[k].getName()
											.toString()));
					new CopyUtil().deleteFiles(newPath + "/"
							+ fileList[k].getName().toString());
				}

			}
		}
		readDiskCard();

		Flog.d(TAG, "copyFolderxyz---try");
		(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
		if (new File(oldPath).exists()) {

			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			Flog.d(TAG, "copyFolderxyz---for");
			for (int i = 0; i < file.length; i++) {
				try {
					if (CopyActivity.cancelCopy == false) {
						if (oldPath.endsWith(File.separator)) {
							temp = new File(oldPath + file[i]);

						} else {
							temp = new File(oldPath + File.separator + file[i]);
						}
						Flog.d(TAG, "copyFolderxyz---" + temp);
						if (temp.isFile()) {
							Message m2 = handler.obtainMessage();
							m2.what = 0x001;
							m2.obj = temp.toString();
							m2.sendToTarget();

							input = new FileInputStream(temp);
							output = new FileOutputStream(newPath + "/"
									+ (temp.getName()).toString());
							byte[] b = new byte[1024 * 1024 * 2];
							int len;
							while (CopyActivity.cancelCopy == false
									&& (len = input.read(b)) != -1) {
								Flog.d(TAG,
										"copyFolder-----------------------------------------**************************************"
												+ len);

								output.write(b, 0, len);
								Message m3 = handler.obtainMessage();
								m3.what = 0x002;
								m3.obj = len;
								m3.sendToTarget();
								Flog.d(TAG, "copyFolder()-------"
										+ (double) mAvialable);
								Flog.d(TAG, "copyFolder()-------"
										+ (double) FileSizeUtil.fileSize);

								if (CopyActivity.isParserFinish == false
										&& (double) mAvialable < (double) FileSizeUtil.fileSize) {//

									Message m4 = handler.obtainMessage();
									m4.what = 0x006;
									m4.sendToTarget();

								}
							}
							output.flush();
							output.close();
							input.close();

						} else {
							Flog.d(TAG,
									"copyFolderxyz**********************************************");
							temp.mkdirs();
						}
						if (temp.isDirectory()) {// 如果是子文件夹
							copyFolder(oldPath + "/" + file[i], newPath + "/"
									+ file[i], handler);
						}

					}
				} catch (Exception e1) {
					// TODO: handle exception

					try {
						output.close();
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Message m4 = handler.obtainMessage();
					m4.what = 0x005;
					m4.sendToTarget();
				}
				Flog.d(TAG, "copyFolderxyz---end");

			}

			Message m4 = handler.obtainMessage();
			m4.what = 0x003;
			m4.sendToTarget();

		} else {
			Message m4 = handler.obtainMessage();
			m4.what = 0x005;
			m4.sendToTarget();
		}

		Flog.d(TAG, "copyFolder---end");
	}

	public static void deleteFiles(String mFile) {
		File file = new File(mFile);
		Flog.d(TAG, "deleteFiles---start");
		if (file.isFile() && file.exists()) {
			Flog.d(TAG, "deleteFiles---1" + file.toString());
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {

				deleteFiles(childFiles[i].getAbsolutePath());
			}
			Flog.d(TAG, "deleteFiles---2" + file.toString());
			file.delete();
		}
		Flog.d(TAG, "deleteFiles---end");
	}

	public static void readDiskCard() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			mAvialable = blockSize * availCount;
			Flog.d(TAG, "readSDCard()---" + mAvialable);

		}
	}
}
