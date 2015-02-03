package com.flyaudio.copyapplication;

import java.io.File;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.concurrent.ThreadPoolExecutor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.MailTo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CopyActivity extends Activity implements OnClickListener {
	private final static String TAG = "CopyActivity";

	private TextView mTitleTxt;
	private TextView mAllTotal;
	private Button mCancel;
	private Button mHidden;
	private TextView mSingleTxt;
	private TextView mTotalTxt;
	private TextView mSinglePrecent;
	private TextView mTotalPrecent;
	private ProgressBar mSingleProgressBar;
	private ProgressBar mTotalProgressBar;
	public static boolean cancelCopy;
	private String oldPath = "/storage/udisk/FlyData/";
	// private String oldPath = "/storage/emulated/0/Download";
	public String newPath = "/storage/emulated/0";

	private long mSingleCount;
	private long mSingleCopyCount;
	private long mCopyFolderCount;
	private long mCopyFolderTotal;
	private boolean isTrue;// 获取stream
	private boolean isFinish;// 复制完成
	public static boolean isParserFinish;
	private int i;
	private CopyHandler handler;

	private long mFileNumder;

	private BroadcastSD mBroadcastSD;
	private boolean isHome;
	private boolean isCopying;
	private AlertDialog.Builder builder;

	private CustomerDialog newDialog;
	private Button mGoto;
	private Button mStop;
	private TextView mContent;

	private CustomerDialog newDialog2;
	private Button mGoto2;
	private Button mStop2;
	private TextView mContent2;

	private CustomerDialog newDialog3;
	private Button mGoto3;
	private TextView mContent3;

	private CustomerDialog newDialog4;
	private Button mGoto4;
	private TextView mContent4;
	private Button mStop4;

	private CustomerDialog newDialog5;
	private Button mGoto5;
	private TextView mContent5;

	private LayoutInflater inflater;
	private RelativeLayout layout1;
	private RelativeLayout layout2;

	private File[] fileList;

	private CopyTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Flog.d(TAG, "onCreate");
		setContentView(R.layout.copy_layout);
		CopyUtil.readDiskCard();
		mBroadcastSD = new BroadcastSD();
		// 在IntentFilter中选择你要监听的行为
		IntentFilter mFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);// sd卡被插入，且已经挂载
		mFilter.setPriority(1000);// 设置最高优先级
		mFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);// sd卡存在，但还没有挂载
		mFilter.addAction(Intent.ACTION_MEDIA_REMOVED);// sd卡被移除
		mFilter.addAction(Intent.ACTION_MEDIA_SHARED);// sd卡作为 USB大容量存储被共享，挂载被解除
		mFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);// sd卡已经从sd卡插槽拔出，但是挂载点还没解除
		mFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		mFilter.addDataScheme("file");
		registerReceiver(mBroadcastSD, mFilter);// 注册监听函数
		handler = new CopyHandler(this);
		mTitleTxt = (TextView) findViewById(R.id.title);
		mTitleTxt.setText(StringUtil.mTitle0);
		mAllTotal = (TextView) findViewById(R.id.totoal);
		mCancel = (Button) findViewById(R.id.cancel);
		mHidden = (Button) findViewById(R.id.Hidden);

		mSingleTxt = (TextView) findViewById(R.id.single_filename);
		mTotalTxt = (TextView) findViewById(R.id.totoal_number);
		mSinglePrecent = (TextView) findViewById(R.id.single_perent);
		mTotalPrecent = (TextView) findViewById(R.id.total_percent);

		mSingleProgressBar = (ProgressBar) findViewById(R.id.singal_progress);
		mTotalProgressBar = (ProgressBar) findViewById(R.id.total_progress);

		layout1 = (RelativeLayout) findViewById(R.id.mlayout);
		layout2 = (RelativeLayout) findViewById(R.id.mlayout2);
		mCancel.setOnClickListener(this);
		mHidden.setOnClickListener(this);
		inflater = LayoutInflater.from(this);
		if ((double) CopyUtil.mAvialable / 1024 / 1024 / 1024 < 10) {
			View mGoTipView5 = inflater.inflate(R.layout.tip_dialog, null);
			newDialog5 = new CustomerDialog(CopyActivity.this, R.style.Dialog,
					StringUtil.mTipAviable, StringUtil.mCopyDialogOk, null,
					mGoTipView5, 5);
			newDialog5.setCanceledOnTouchOutside(false);
			View view5 = newDialog5.getCustomView();
			mContent5 = (TextView) view5.findViewById(R.id.conent5);
			mGoto5 = (Button) view5.findViewById(R.id.go5);

			mGoto5.setOnClickListener(this);

			newDialog5.show();
			dialogStyle(newDialog5);
		} else {
			// newDialog5.dismiss();
			View mGoTipView = inflater.inflate(
					R.layout.double_btn_dialog_layout, null);
			newDialog = new CustomerDialog(CopyActivity.this, R.style.Dialog,
					StringUtil.mCopyDialog, StringUtil.mCopyDialogYes,
					StringUtil.mCopyDialogStop, mGoTipView, 1);
			newDialog.setCanceledOnTouchOutside(false);
			View mView = newDialog.getCustomView();
			mContent = (TextView) mView.findViewById(R.id.conent);
			mGoto = (Button) mView.findViewById(R.id.go);
			mStop = (Button) mView.findViewById(R.id.stop);
			mGoto.setOnClickListener(this);
			mStop.setOnClickListener(this);
			newDialog.show();
			dialogStyle(newDialog);
		}

		Flog.d(TAG, "onCreate---end");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void dialogStyle(Dialog dialog) {
		WindowManager.LayoutParams params2 = dialog.getWindow().getAttributes();
		params2.width = 626;
		params2.height = 220;
		dialog.getWindow().setAttributes(params2);

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		// copying = false;
		unregisterReceiver(mBroadcastSD);
		System.exit(0);

	}

	public static boolean usbExists() {
		File usbFile = new File(StringUtil.PATH_USB + "/");
		if (!usbFile.exists() || usbFile.listFiles().length == 0)
			return false;

		if (usbFile.listFiles() == null)
			return false;
		return usbFile.listFiles().length > 0;
	}

	private void executCopyInit() {
		layout1.setVisibility(View.VISIBLE);
		layout2.setVisibility(View.VISIBLE);
		mCancel.setVisibility(View.VISIBLE);
		mHidden.setVisibility(View.VISIBLE);
		mSingleTxt.setText(StringUtil.mCopySingleProgress + ".........");
		mTotalTxt.setText(StringUtil.mCopyTotalProgress + 0 + "/" + 0);

		CopyActivity.cancelCopy = false;
		Flog.d(TAG, "exists--------------------" + new File(oldPath).exists());
		mCopyFolderTotal = FileSizeUtil.fileSize;
		mTotalProgressBar.setMax((int) (mCopyFolderTotal / 1024 / 1024));
		mAllTotal.setText(StringUtil.mCopyFileTotal
				+ FileSizeUtil.FormetFileSize(FileSizeUtil.fileSize));
		Flog.d(TAG, "exists---mCopyFolderTotal---+++" + mCopyFolderTotal);
		mTitleTxt.setText(StringUtil.mTitle);
		mTotalPrecent.setText(0 + "%");
		mSinglePrecent.setText(0 + "%");
		task = new CopyTask();
		task.execute();
		Flog.d(TAG, "executCopyInit------end");

	}

	private void backMainLayout() {

		if (isHome) {
			isHome = false;
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setClass(CopyActivity.this, CopyActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}

	}

	private void beginThread() {

		isParserFinish = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Flog.d(TAG,
						"mCopyDialog----------------------------------FileSizeUtil.getFileSizes(new File(oldPath))");
				try {

					new FileSizeUtil().getFileSizes(new File(oldPath), handler);
					Flog.d(TAG,
							"mCopyDialog-----------------2-----------------FileSizeUtil.getFileSizes(new File(oldPath))");
					isParserFinish = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Flog.d(TAG,
						"mCopyDialog---------------------------------while (isParserFinish) {");
				while (isParserFinish) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Flog.d(TAG,
								"mCopyDialog---------------2------------------while (isParserFinish) {");
						e.printStackTrace();
					}
					Message m3 = handler.obtainMessage();
					m3.what = 0x004;
					m3.sendToTarget();

				}

			}
		}).start();

	}

	private class CopyTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			CopyUtil.isFristCopy = true;

			new CopyUtil().copyFolder(oldPath, newPath, handler);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

	}

	private class CopyHandler extends Handler {

		private WeakReference<CopyActivity> mReference;

		public CopyHandler(CopyActivity copyActivity) {
			// TODO Auto-generated constructor stub
			mReference = new WeakReference<CopyActivity>(copyActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (mReference.get() != null) {
				CopyActivity theActivity = mReference.get();
				theActivity.updateText(msg);
			}
		}

	}

	public void updateText(Message msg) {
		// TODO Auto-generated method stub
		if (isCopying) {

			if (msg.what == 0x001) {
				isTrue = false;
				Flog.d(TAG, "Handler---001");
				String mSingleFileName = msg.obj.toString();
				Flog.d(TAG,
						"updateText------------001----------------------mSingleFileName------------------"
								+ mSingleFileName);
				if (!mSingleFileName.equals(" ") && mSingleFileName != null) {
					Flog.d(TAG, "updateText---" + mSingleFileName);
					mSingleCopyCount = 0;
					i++;

					String mName = mSingleFileName.substring(mSingleFileName
							.lastIndexOf("/") + 1);

					mSingleTxt.setText(StringUtil.mCopySingleProgress + mName);
					try {
						mSingleCount = FileSizeUtil.getFileSize(new File(
								mSingleFileName));
						isTrue = true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mTotalTxt.setText(StringUtil.mCopyTotalProgress + i + "/"
							+ FileSizeUtil.size);

					mSingleProgressBar.setMax((int) mSingleCount / 1024 / 1024);
					mSingleProgressBar.setProgress(0);
					mSinglePrecent.setText(0 + "%");
					Flog.d(TAG, "updateText-001--mSingleCount" + mSingleCount);
				}

			} else if (msg.what == 0x002) {
				Flog.d(TAG, "Handler---002");
				if (isTrue) {

					int data = msg.obj.hashCode();

					mSingleCopyCount = mSingleCopyCount + data;
					mCopyFolderCount = mCopyFolderCount + data;
					Flog.d(TAG, "Handler---002---mSingleCopyCount---"
							+ mSingleCopyCount);
					Flog.d(TAG, "Handler---002---mCopyFolderCount---"
							+ mCopyFolderCount);

					mTotalProgressBar
							.setProgress((int) (mCopyFolderCount / 1024 / 1024));
					mSingleProgressBar
							.setProgress((int) (mSingleCopyCount / 1024 / 1024));
					Flog.d(TAG, "xyzHandler1-002xyz--" + (int) mSingleCopyCount
							/ 1024 / 1024);
					Flog.d(TAG, "xyzHandler2--002xyz-" + (int) mSingleCount
							/ 1024 / 1024);
					Flog.d(TAG, "xyzHandler3--002xyz-" + (int) mCopyFolderCount
							/ 1024 / 1024);
					Flog.d(TAG, "xyzHandler4--002xyz-" + (int) mCopyFolderTotal
							/ 1024 / 1024);
					BigDecimal bg = new BigDecimal((double) mSingleCopyCount
							/ (double) mSingleCount);
					double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					Flog.d(TAG, "Handler---002xyz----mSinglePrecent---++" + f1);
					Flog.d(TAG, "Handler---002xyz----mSinglePrecent---++"
							+ (int) Math.floor(f1 * 100));
					mSinglePrecent.setText((int) Math.floor(f1 * 100) + "%");

					BigDecimal bg2 = new BigDecimal((double) mCopyFolderCount
							/ (double) mCopyFolderTotal);
					double f2 = bg2.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					Flog.d(TAG, "Handler---002xyz----mAllPrecent---++" + f2);
					Flog.d(TAG, "Handler---002xyz----mAllPrecent---++"
							+ (int) Math.floor(f2 * 100));
					if (f2 >= 100) {
						mTotalPrecent.setText(99 + "%");
					} else {
						mTotalPrecent.setText((int) Math.floor(f2 * 100) + "%");
					}

				}
			} else if (msg.what == 0x003
					&& mCopyFolderTotal - mCopyFolderCount < 100
					&& isParserFinish == false) {
				isFinish = true;
				Flog.d(TAG, "Handler---003");
				backMainLayout();
				mSingleProgressBar.setMax(1);
				mTotalProgressBar.setMax(1);
				mSingleProgressBar.setProgress(1);
				mTotalProgressBar.setProgress(1);
				mSinglePrecent.setText(100 + "%");
				mTotalPrecent.setText(100 + "%");
				mTitleTxt.setText(StringUtil.mCopyFinishTitle);

				View mGoTipView3 = inflater.inflate(
						R.layout.single_btn_dialog_layout3, null);
				newDialog3 = new CustomerDialog(CopyActivity.this,
						R.style.Dialog, StringUtil.mCopyFinishContent,
						StringUtil.mCopyDialogOk, null, mGoTipView3, 3);
				newDialog3.setCanceledOnTouchOutside(false);
				View view = newDialog3.getCustomView();
				mContent3 = (TextView) view.findViewById(R.id.conent3);
				mGoto3 = (Button) view.findViewById(R.id.go3);

				mGoto3.setOnClickListener(this);

				newDialog3.show();
				dialogStyle(newDialog3);
			} else if (msg.what == 0x004) {
				Flog.d(TAG, "Handler---004");
				mCopyFolderTotal = FileSizeUtil.fileSize;
				mTotalProgressBar
						.setMax((int) (mCopyFolderTotal / 1024 / 1024));
				mAllTotal.setText(StringUtil.mCopyFileTotal
						+ FileSizeUtil.FormetFileSize(FileSizeUtil.fileSize));

				mTotalTxt.setText(StringUtil.mCopyTotalProgress + i + "/"
						+ FileSizeUtil.size);
			} else if (msg.what == 0x005) {
				Flog.d(TAG, "Handler---005");
				isCopying = false;
				task.cancel(true);
				errorDialog();
			} else if (msg.what == 0x006) {
				backMainLayout();
				Flog.d(TAG, "Handler---006");
				isCopying = false;
				task.cancel(true);
				View mGoTipView3 = inflater.inflate(
						R.layout.single_btn_dialog_layout3, null);
				newDialog3 = new CustomerDialog(CopyActivity.this,
						R.style.Dialog, StringUtil.mAviable,
						StringUtil.mCopyDialogOk, null, mGoTipView3, 3);
				newDialog3.setCanceledOnTouchOutside(false);
				View view = newDialog3.getCustomView();
				mContent3 = (TextView) view.findViewById(R.id.conent3);
				mGoto3 = (Button) view.findViewById(R.id.go3);

				mGoto3.setOnClickListener(this);

				newDialog3.show();
				dialogStyle(newDialog3);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancel:
			Flog.d(TAG, "onClick---cancel");

			View mGoTipView2 = inflater.inflate(
					R.layout.double_btn_dialog_layout2, null);
			newDialog2 = new CustomerDialog(CopyActivity.this, R.style.Dialog,
					StringUtil.mCopyCancel, StringUtil.mCopyDialogYesyes,
					StringUtil.mCopyDialogNO, mGoTipView2, 2);
			newDialog2.setCanceledOnTouchOutside(false);
			// newDialog2.setTitle(StringUtil.mDialogTitle);
			View view = newDialog2.getCustomView();
			mContent2 = (TextView) view.findViewById(R.id.conent2);
			mGoto2 = (Button) view.findViewById(R.id.go2);
			mStop2 = (Button) view.findViewById(R.id.stop2);
			mGoto2.setOnClickListener(this);
			mStop2.setOnClickListener(this);
			newDialog2.show();
			dialogStyle(newDialog2);
			break;
		case R.id.Hidden:
			Flog.d(TAG, "onClick---Hidden");
			isHome = true;
			Intent iIntent = new Intent(Intent.ACTION_MAIN);
			iIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			iIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(iIntent);
			break;

		case R.id.go:
			mSingleCopyCount = 0;
			mCopyFolderCount = 0;
			mCopyFolderTotal = 0;
			FileSizeUtil.fileSize = 0;
			FileSizeUtil.size = 0;
			i = 0;
			if (new File(oldPath).exists()) {
				try {
					// isParserFinish = true;
					beginThread();
					mCopyFolderTotal = FileSizeUtil.fileSize;
					if (!new File(oldPath).exists() || mCopyFolderTotal == 0
							|| !usbExists()) {

						Flog.d(TAG, "R.id.go------not--------exist");
						mContent.setText(StringUtil.mCopyRemove);
						mGoto.setText(StringUtil.mCopyDialogAgain);

					} else {

						Flog.d(TAG, "R.id.go-------exist");

						isCopying = true;
						executCopyInit();
						newDialog.dismiss();
					}

					Flog.d(TAG, "R.id.go---mCopyFolderTotal---+++"
							+ mCopyFolderTotal);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				mContent.setText(StringUtil.mCopyRemove);
				mGoto.setText(StringUtil.mCopyDialogAgain);

			}

			break;

		case R.id.stop:
			finish();
			break;

		case R.id.go2:

			finish();

			break;

		case R.id.stop2:

			newDialog2.dismiss();
			break;
		case R.id.go3:
			finish();

			break;
		case R.id.go5:
			newDialog5.dismiss();
			View mGoTipView = inflater.inflate(
					R.layout.double_btn_dialog_layout, null);
			newDialog = new CustomerDialog(CopyActivity.this, R.style.Dialog,
					StringUtil.mCopyDialog, StringUtil.mCopyDialogYes,
					StringUtil.mCopyDialogStop, mGoTipView, 1);
			newDialog.setCanceledOnTouchOutside(false);
			View mView = newDialog.getCustomView();
			mContent = (TextView) mView.findViewById(R.id.conent);
			mGoto = (Button) mView.findViewById(R.id.go);
			mStop = (Button) mView.findViewById(R.id.stop);
			mGoto.setOnClickListener(this);
			mStop.setOnClickListener(this);
			newDialog.show();
			dialogStyle(newDialog);

			break;

		default:
			break;
		}

	}

	public void errorDialog() {
		Flog.d(TAG, "errorDialog");
		mSingleCopyCount = 0;
		mCopyFolderCount = 0;
		mCopyFolderTotal = 0;
		FileSizeUtil.fileSize = 0;
		FileSizeUtil.size = 0;
		i = 0;
		backMainLayout();
		cancelCopy = true;
		if (isFinish == false) {
			Flog.d(TAG, "errorDialog------------isCopying");
			isCopying = false;
			Flog.d(TAG, "errorDialog------------mGoTipView4");
			View mGoTipView4 = inflater.inflate(
					R.layout.single_btn_dialog_layout4, null);
			newDialog4 = new CustomerDialog(CopyActivity.this, R.style.Dialog,
					StringUtil.mCopyEroor, StringUtil.mCopyDialogAgain,
					StringUtil.mCopyDialogStop, mGoTipView4, 4);
			newDialog4.setCanceledOnTouchOutside(false);
			View view = newDialog4.getCustomView();
			mContent4 = (TextView) view.findViewById(R.id.conent4);
			mGoto4 = (Button) view.findViewById(R.id.go4);
			mStop4 = (Button) view.findViewById(R.id.stop4);

			mGoto4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (new File(oldPath).exists()) {
						Flog.d(TAG, "errorDialog-------mGoto4");
						try {
							beginThread();
							mCopyFolderTotal = FileSizeUtil.fileSize;
							if (!new File(oldPath).exists() || !usbExists()
									|| mCopyFolderTotal == 0) {

								Flog.d(TAG,
										"errorDialog---------------mGoto4----------exist");
								mContent4.setText(StringUtil.mCopyRemove);
								mGoto4.setText(StringUtil.mCopyDialogAgain);

							} else {

								Flog.d(TAG,
										"errorDialog---------------mGoto4-------exist");

								isCopying = true;
								executCopyInit();
								if (newDialog != null && newDialog.isShowing()) {
									newDialog.dismiss();
								} else if (newDialog2 != null
										&& newDialog2.isShowing()) {
									newDialog2.dismiss();
								} else if (newDialog3 != null
										&& newDialog3.isShowing()) {
									newDialog3.dismiss();
								}
								if (newDialog4 != null) {
									newDialog4.dismiss();
								}
							}

							Flog.d(TAG,
									"errorDialog---------------mGoto4---+++"
											+ mCopyFolderTotal);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						mContent4.setText(StringUtil.mCopyRemove);
						mGoto4.setText(StringUtil.mCopyDialogAgain);
					}
				}
			});
			mStop4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			if (newDialog != null && newDialog.isShowing()) {
				newDialog.dismiss();
			} else if (newDialog2 != null && newDialog2.isShowing()) {
				newDialog2.dismiss();
			} else if (newDialog3 != null && newDialog3.isShowing()) {
				newDialog3.dismiss();
			}
			if (!newDialog4.isShowing()) {

				newDialog4.show();
				dialogStyle(newDialog4);
			}
		}

	}

	private class BroadcastSD extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction()
					.equals("android.intent.action.MEDIA_MOUNTED")) {// SD卡已经成功挂载

			} else if (intent.getAction().equals(
					"android.intent.action.MEDIA_REMOVED")
					|| intent.getAction().equals(
							"android.intent.action.ACTION_MEDIA_UNMOUNTED")
					|| intent.getAction().equals(
							"android.intent.action.ACTION_MEDIA_BAD_REMOVAL")
					|| Intent.ACTION_MEDIA_EJECT.equals(intent.getAction())) {
				Flog.d(TAG, "android.intent.action.MEDIA_REMOVED----------"
						+ intent.getAction());

				// errorDialog();

			}
		}
	};

}