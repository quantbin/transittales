package org.transittales;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	final Context cont = this;
	private ImageButton button_Bill;
	private ImageButton button_Tina;
	private ImageButton button_Abraham;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// fins characters' buttons
		button_Bill = (ImageButton) findViewById(R.id.button_Bill);
		button_Tina = (ImageButton) findViewById(R.id.button_Tina);
		button_Abraham = (ImageButton) findViewById(R.id.button_Abraham);
		// draw characters
		setCharacterListener(button_Bill, "bill_character");
		setCharacterListener(button_Tina, "tt_character");
		setCharacterListener(button_Abraham, "abraham_character");
		// init geo
		Geo.getInstance().setLM((LocationManager) getSystemService(LOCATION_SERVICE));
	}

	private void setCharacterListener(ImageButton btn, final String state) {
		Properties stateProp = State.load(this, state);
		final String nextState;
		String _nextState = stateProp.getProperty("option.1.next_state");
		final String nextIntentClass;
		String _nextIntentClass = null;
		@SuppressWarnings("rawtypes")
		final Class nextIntent;
		if (null != _nextState) {
			_nextIntentClass = stateProp.getProperty("option.1.next_intent_class");
		} else {
			if (null != stateProp.getProperty("option.1.once.next_state")) {
				_nextState = stateProp.getProperty("option.1.once.next_state");
				_nextIntentClass = stateProp.getProperty("option.1.once.next_intent_class");
				if (State.hasSeenState(_nextState)) {
					_nextState = stateProp.getProperty("option.1.rest.next_state");
					_nextIntentClass = stateProp.getProperty("option.1.rest.next_intent_class");
				}
			} else {
				Log.e("MainActiviry", "state misconfigured: " + state);
				return;
			}
		}
		nextState = _nextState;
		nextIntentClass = _nextIntentClass;
		try {
			nextIntent = Class.forName(nextIntentClass);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(cont, nextIntent);
					Bundle b = new Bundle();
					b.putString("state", nextState);
					i.putExtras(b);
					startActivity(i);
				}
			});
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Log.e("OptionsActivity", "class not found: " + nextIntentClass);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		new BeamUtils(this).process();
		AppUtils.getInstance().resume();
	}

	@Override
	protected void onPause() {
		AppUtils.getInstance().pause();
		super.onPause();
	}
}
