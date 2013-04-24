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
		if (OptionsStates.bill_options.name().equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Jerk",
					OptionsStates.bill_jerk_options.name(),
					OptionsActivity.class));
			ll.addView(createOptionButton("Garbage",
					PlayerStates.bill_garbage_audio.name(),
					PlayerActivity.class));
			ll.addView(createOptionButton("Thoughts",
					PlayerStates.bill_thoughts_audio.name(),
					PlayerActivity.class));
			ll.addView(createOptionButton("Driver",
					PlayerStates.bill_driver_audio.name(), PlayerActivity.class));
		} else if ("bill_jerk_options".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Man",
					PlayerStates.bill_jerk_man_audio.name(),
					PlayerActivity.class));
			ll.addView(createOptionButton("Woman",
					PlayerStates.bill_jerk_woman_audio.name(),
					PlayerActivity.class));
		} else if ("bill_garbage_options".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Rolling",
					PlayerStates.bill_garbage_rolling_audio.name(),
					PlayerActivity.class));
			ll.addView(createOptionButton("Newspaper",
					PlayerStates.bill_garbage_newspaper_audio.name(),
					PlayerActivity.class));
			ll.addView(createOptionButton("Food wrapper",
					PlayerStates.bill_garbage_foodwrapper_audio.name(),
					PlayerActivity.class));
		} else if ("bill_thoughts_options".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			// create buttons
			ll.addView(createOptionButton("Man",
					PlayerStates.bill_thoughts_man_audio.name(),
					PlayerActivity.class));
			ll.addView(createOptionButton("Woman",
					PlayerStates.bill_thoughts_woman_audio.name(),
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

	@Override
	protected void onResume() {
		AppUtils.getInstance().resume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		AppUtils.getInstance().pause();
		super.onPause();
	}
}
