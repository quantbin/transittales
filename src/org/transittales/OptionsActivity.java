package org.transittales;

import java.util.Properties;

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

		((ImageButton) findViewById(R.id.imageButtonHome)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(cont, MainActivity.class);
				startActivity(i);
			}
		});
		ImageView iv = (ImageView) findViewById(R.id.imageViewCharacter);
		LinearLayout ll = (LinearLayout) findViewById(R.id.layout_Buttons);
		int resID = -1;
		Properties stateProp = State.load(this, state);
		State.seenState(state);
		if (null == stateProp) {
			Log.e("PlayerActivity", "state not found: " + state);
			return;
		} else {
			resID = getResources().getIdentifier(stateProp.getProperty("img"), "drawable", getPackageName());
			for (int i = 1; i < 10; i++) {
				String text = stateProp.getProperty(String.format("option.%d.text", i));
				if (null == text) {
					// end of options
					break;
				}
				String nextState = stateProp.getProperty(String.format("option.%d.next_state", i));
				String nextIntentClass = stateProp.getProperty(String.format("option.%d.next_intent_class", i));
				if (null != nextState) {
					try {
						ll.addView(createOptionButton(text, nextState, Class.forName(nextIntentClass)));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						Log.e("OptionsActivity", "class not found: " + nextIntentClass);
					}
				} else if (null != stateProp.getProperty(String.format("option.%d.once.next_state", i))) {
					text = stateProp.getProperty(String.format("option.%d.text", i));
					nextState = stateProp.getProperty(String.format("option.%d.once.next_state", i));
					nextIntentClass = stateProp.getProperty(String.format("option.%d.once.next_intent_class", i));
					if (!State.hasSeenState(nextState)) {
						try {
							ll.addView(createOptionButton(text, nextState, Class.forName(nextIntentClass)));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							Log.e("OptionsActivity", "class not found: " + nextIntentClass);
						}
					} else {
						text = stateProp.getProperty(String.format("option.%d.text", i));
						nextState = stateProp.getProperty(String.format("option.%d.rest.next_state", i));
						nextIntentClass = stateProp.getProperty(String.format("option.%d.rest.next_intent_class", i));
						try {
							ll.addView(createOptionButton(text, nextState, Class.forName(nextIntentClass)));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							Log.e("OptionsActivity", "class not found: " + nextIntentClass);
						}
					}
				} else {
					Log.e("OptionsActivity", "invalid options config for state: " + state);
					return;
				}
			}
		}
		iv.setImageResource(resID);
	}

	private Button createOptionButton(String label, final String state, @SuppressWarnings("rawtypes") final Class intentClass) {
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
