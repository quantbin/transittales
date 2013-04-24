package org.transittales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class OptionsActivity extends Activity {
	final Context cont = this;
	private String state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);

		Bundle bin = getIntent().getExtras();
		state = bin.getString("state");

		((ImageButton) findViewById(R.id.imageButtonHome))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(cont, MainActivity.class);
						startActivity(i);
					}
				});
		ImageView iv = (ImageView) findViewById(R.id.imageViewCharacter);
		LinearLayout ll = (LinearLayout) findViewById(R.id.layout_Buttons);
		int resID = -1;
		if ("bill_options".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Jerk", "bill_jerk_options",
					OptionsActivity.class));
			ll.addView(createOptionButton("Garbage", "bill_garbage_audio",
					PlayerActivity.class));
			ll.addView(createOptionButton("Thoughts", "bill_thoughts_audio",
					PlayerActivity.class));
			ll.addView(createOptionButton("Driver", "bill_driver_audio",
					PlayerActivity.class));
		} else if ("bill_jerk_options".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Man", "bill_jerk_man_audio",
					PlayerActivity.class));
			ll.addView(createOptionButton("Woman", "bill_jerk_woman_audio",
					PlayerActivity.class));
		} else if ("bill_garbage_options".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Rolling",
					"bill_garbage_rolling_audio", PlayerActivity.class));
			ll.addView(createOptionButton("Newspaper",
					"bill_garbage_newspaper_audio", PlayerActivity.class));
			ll.addView(createOptionButton("Food wrapper",
					"bill_garbage_foodwrapper_audio", PlayerActivity.class));
		} else if ("bill_thoughts_options".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Man", "bill_thoughts_man_audio",
					PlayerActivity.class));
			ll.addView(createOptionButton("Woman", "bill_thoughts_woman_audio",
					PlayerActivity.class));
		} else {
			// no man land
			Log.e("OptionsActivity", "*** invalid state");
			return;
		}
		iv.setImageResource(resID);
	}

	private Button createOptionButton(String label, final String state,
			@SuppressWarnings("rawtypes") final Class intentClass) {
		Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.btn_blank);
		btn.setTextColor(getResources().getColor(R.color.white));
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
		btn.setPadding(0, 0, 0, 18);
		btn.setHeight(25);
		btn.setText(label);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(cont, intentClass);
				Bundle b = new Bundle();
				b.putString("state", state);
				i.putExtras(b);
				startActivity(i);
			}
		});
		return btn;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill, menu);
		return true;
	}
}
