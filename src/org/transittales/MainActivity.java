package org.transittales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

		button_Bill = (ImageButton) findViewById(R.id.button_Bill);
		button_Tina = (ImageButton) findViewById(R.id.button_Tina);
		button_Abraham = (ImageButton) findViewById(R.id.button_Abraham);

		setCharacterListener(button_Bill, "bill_intro_audio",
				PlayerActivity.class);
		setCharacterListener(button_Tina, "tina_intro_audio",
				PlayerActivity.class);
		setCharacterListener(button_Abraham, "abraham_intro_audio",
				PlayerActivity.class);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
