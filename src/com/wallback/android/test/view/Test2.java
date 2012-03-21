package com.wallback.android.test.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wallback.android.test.R;

public class Test2 extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		TextView textView = (TextView) findViewById(R.id.hello);
		textView.setText("Test2");
	}
}
