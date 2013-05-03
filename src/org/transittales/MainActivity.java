package org.transittales;

import java.util.Properties;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	final Context cont = this;
	ImageButton button_Bill;
	ImageButton button_Tina;
	ImageButton button_Abraham;
	NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mNdefExchangeFilters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
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
		
        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
/*
        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (MalformedMimeTypeException e) { }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
*/
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
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            String pl = new String(payload);
            //setIntent(new Intent()); // Consume this intent.
        }
        AppUtils.getInstance().resume();
	}

	@Override
	protected void onPause() {
		AppUtils.getInstance().pause();
		super.onPause();
	}

	NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				msgs = new NdefMessage[] { msg };
			}
		} else {
			Log.d("MainActivity", "Unknown intent.");
			finish();
		}
		return msgs;
	}
}
