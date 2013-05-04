package org.transittales;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

enum Act {
	youWin, webSite
}

public class BeamUtils {
	private Activity activity;
	private Act act;
	private String tagData;

	public BeamUtils(Activity _a) {
		activity = _a;
	}

	public boolean tagDetected() {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(activity.getIntent().getAction())) {
			NdefMessage[] messages = getNdefMessages(activity.getIntent());
			byte[] payload = messages[0].getRecords()[0].getPayload();
			tagData = new String(payload);
			if (tagData.length() > 3) {
				tagData = tagData.substring(3);
			}
			act = Act.youWin;
			if (tagData.length() > 7) {
				String http = tagData.substring(0, 7);
				if (tagData.length() > 7 && http.equalsIgnoreCase("http://")) {
					act = Act.webSite;
				}
			}
			return true;
		}
		return false;
	}

	public void processTag() {
		if (Act.webSite == act) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tagData));
			activity.startActivity(browserIntent);
		} else {
			// show you win as default
			new YouWinDialog(activity);
		}
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
