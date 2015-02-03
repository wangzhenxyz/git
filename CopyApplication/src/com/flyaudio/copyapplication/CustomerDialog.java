package com.flyaudio.copyapplication;

import java.io.File;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CustomerDialog extends Dialog implements
		android.view.View.OnClickListener {
	private final static String TAG = "CustomerDialog";
	private Context context;
	private View customView;
	private String mContent;
	private String mBtn1;
	private String mBtn2;
	private int j;

	public CustomerDialog(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public CustomerDialog(Context context, int theme, String content,
			String btn1, String btn2, View view, int i) {

		super(context, theme);
		Flog.d(TAG, "CustomerDialog");
		this.context = context;
		// LayoutInflater inflater = LayoutInflater.from(context);
		customView = view;
		mContent = content;
		mBtn1 = btn1;
		mBtn2 = btn2;
		j = i;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Flog.d(TAG, "onCreate");
		this.setContentView(customView);
		if (j == 1) {
			Button mButton1 = (Button) findViewById(R.id.go);
			mButton1.setText(mBtn1);
			Button mButton2 = (Button) findViewById(R.id.stop);
			mButton2.setText(mBtn2);

			TextView mDialogContent = (TextView) findViewById(R.id.conent);
			mDialogContent.setText(mContent);
		} else if (j == 2) {
			Button mButton1 = (Button) findViewById(R.id.go2);
			mButton1.setText(mBtn1);
			Button mButton2 = (Button) findViewById(R.id.stop2);
			mButton2.setText(mBtn2);

			TextView mDialogContent = (TextView) findViewById(R.id.conent2);
			mDialogContent.setText(mContent);
		} else if (j == 3) {
			Button mButton1 = (Button) findViewById(R.id.go3);
			mButton1.setText(mBtn1);
			TextView mDialogContent = (TextView) findViewById(R.id.conent3);
			mDialogContent.setText(mContent);

		} else if (j == 4) {
			Button mButton1 = (Button) findViewById(R.id.go4);
			mButton1.setText(mBtn1);
			Button mButton2 = (Button) findViewById(R.id.stop4);
			mButton2.setText(mBtn2);
			TextView mDialogContent = (TextView) findViewById(R.id.conent4);
			mDialogContent.setText(mContent);
		} else if (j == 5) {
			Button mButton1 = (Button) findViewById(R.id.go5);
			mButton1.setText(mBtn1);
			TextView mDialogContent = (TextView) findViewById(R.id.conent5);
			mDialogContent.setText(mContent);
		}
	}

	@Override
	public View findViewById(int id) {
		// TODO Auto-generated method stub
		Flog.d(TAG, "findViewById");
		return super.findViewById(id);
	}

	public View getCustomView() {
		Flog.d(TAG, "getCustomView");
		return customView;
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

	}
}