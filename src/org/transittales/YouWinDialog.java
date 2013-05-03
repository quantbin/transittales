package org.transittales;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class YouWinDialog {
	public YouWinDialog(Activity a) {
		final Dialog youWinDialog;
		youWinDialog = new Dialog(a);
		youWinDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		youWinDialog.setContentView(a.getLayoutInflater().inflate(R.layout.you_win_layout, null));
		ImageButton youWinClose = (ImageButton)youWinDialog.findViewById(R.id.imageButton_close);
		youWinClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				youWinDialog.dismiss();
			}
		});
		youWinDialog.show();
	}
}
