package org.transittales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BillActivity extends Activity {
	final Context cont = this;
	Button button_Garbage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);

		Bundle b = getIntent().getExtras();
		String src = b.getString("src");

		ImageView iv = (ImageView) findViewById(R.id.imageView);
		int resID = getResources().getIdentifier(src, "drawable",
				getPackageName());
		iv.setImageResource(resID);

		LinearLayout ll = (LinearLayout) findViewById(R.id.layout_Buttons);
		Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.btn_blank);
		btn.setTextColor(getResources().getColor(R.color.white));
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
		btn.setPadding(0, 0, 0, 18);
		btn.setText("Garbage");
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(cont, PlayerActivity.class);
				Bundle b = new Bundle();
				b.putString("file", "MP3/Bill/B03.mp3");
				i.putExtras(b);
				startActivity(i);
			}
		});
		ll.addView(btn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill, menu);
		return true;
	}
}
