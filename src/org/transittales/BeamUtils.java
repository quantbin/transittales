package org.transittales;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

public class BeamUtils {
	private Activity a;

	public BeamUtils(Activity _a) {
		a = _a;
	}

	public boolean process() {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(a.getIntent().getAction())) {
			NdefMessage[] messages = getNdefMessages(a.getIntent());
			byte[] payload = messages[0].getRecords()[0].getPayload();
			String pl = new String(payload);
			if (pl.length() > 3) {
				pl = pl.substring(3);
			}
			if (pl.length() > 7) {
				String http = pl.substring(0, 7);
				if (pl.length() > 7 && http.equalsIgnoreCase("http://")) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pl));
					a.startActivity(browserIntent);
					return true;
				}
			}
			// show you win as default
			new YouWinDialog(a);
			return true;
		}
		return false;
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
		}
		return msgs;
	}
}
