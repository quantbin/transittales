package org.transittales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	final Context cont = this;
	ImageButton button_Bill;
	ImageButton button_Tina;
	ImageButton button_Abraham;

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
		setCharacterListener(button_Bill, PlayerStates.bill_intro_audio.name(),
				PlayerActivity.class);
		setCharacterListener(button_Tina, PlayerStates.tina_intro_audio.name(),
				PlayerActivity.class);
		setCharacterListener(button_Abraham,
				PlayerStates.abraham_intro_audio.name(), PlayerActivity.class);
		// init geo
		Geo.getInstance().setLM(
				(LocationManager) getSystemService(LOCATION_SERVICE));
	}

	private void setCharacterListener(ImageButton btn, final String state,
			@SuppressWarnings("rawtypes") final Class intentClass) {
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
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
