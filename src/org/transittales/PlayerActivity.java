package org.transittales;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;

public class PlayerActivity extends Activity {
	// Media Player
	private MediaPlayer m;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		Bundle b = getIntent().getExtras();
		String file = b.getString("file");

		try {
			AssetFileDescriptor descriptor;
			descriptor = getAssets().openFd(file);
			if (null != m && m.isPlaying()) {
				m.stop();
				m.release();
				m = new MediaPlayer();
			} else if (null == m) {
				m = new MediaPlayer();
			}
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			m.prepare();
			// m.setVolume(1f, 1f);
			m.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != m && m.isPlaying()) {
				m.stop();
				m.release();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
