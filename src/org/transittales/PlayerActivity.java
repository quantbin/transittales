package org.transittales;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class PlayerActivity extends Activity implements OnCompletionListener,
		Runnable {
	final Context cont = this;
	// Media Player
	private MediaPlayer m;
	private ProgressBar pb;
	private boolean pingFinished = false;
	private String state;
	private String nextState;
	@SuppressWarnings("rawtypes")
	private Class nextIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ImageButton rewind;
		ImageButton forward;
		final ImageButton play;
		ImageButton home;
		ImageView imageViewCharachter;
		ImageView imageButtonRideFinished;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		home = (ImageButton) findViewById(R.id.imageButtonHome);
		imageButtonRideFinished = (ImageButton) findViewById(R.id.imageButtonRideFinished);
		rewind = (ImageButton) findViewById(R.id.imageButtonRew);
		forward = (ImageButton) findViewById(R.id.ImageButtonFwd);
		play = (ImageButton) findViewById(R.id.ImageButtonPlayPause);
		pb = (ProgressBar) findViewById(R.id.progressBarPlayer);
		imageViewCharachter = (ImageView) findViewById(R.id.imageViewCharacter);

		imageButtonRideFinished.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != m && m.isPlaying()) {
					m.stop();
				}
				finish();
				moveTaskToBack(true);
			}
		});
		home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != m && m.isPlaying()) {
					m.stop();
				}
				Intent i = new Intent(cont, MainActivity.class);
				startActivity(i);
			}
		});
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != m && m.isPlaying()) {
					// pause
					play.setBackgroundResource(R.drawable.btn_play);
					m.pause();
				} else {
					// resume
					play.setBackgroundResource(R.drawable.btn_pause);
					m.start();
				}
			}
		});
		forward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != m && m.isPlaying()) {
					m.seekTo(m.getCurrentPosition() + 10000);
				}
			}
		});
		rewind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != m && m.isPlaying()) {
					int newPos = m.getCurrentPosition() - 10000;
					m.seekTo(newPos > 0 ? newPos : 0);
				}
			}
		});
		Bundle bin = getIntent().getExtras();
		state = bin.getString("state");
		String file = null;
		int resID = -1;
		if ("bill_intro_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B01.mp3";
		} else if ("bill_garbage_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_garbage_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B03.mp3";
		} else if ("bill_thoughts_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_thoughts_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B04.mp3";
		} else if ("bill_driver_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B05.mp3";
		} else if ("bill_jerk_man_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B02A.mp3";
		} else if ("bill_jerk_woman_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B02B.mp3";
		} else if ("bill_garbage_rolling_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B03A.mp3";
		} else if ("bill_garbage_newspaper_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B03B.mp3";
		} else if ("bill_garbage_foodwrapper_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B03C.mp3";
		} else if ("bill_thoughts_man_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B04A.mp3";
		} else if ("bill_thoughts_woman_audio".equals(state)) {
			resID = getResources().getIdentifier("audio_bill", "drawable",
					getPackageName());
			nextState = "bill_options";
			nextIntent = OptionsActivity.class;
			file = "MP3/Bill/B04B.mp3";
		} else {
			// no man land
			Log.e("PlayerActivity", "*** invalid state");
			return;
		}
		imageViewCharachter.setImageResource(resID);
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
			pb.setMax(m.getDuration());
			m.setOnCompletionListener(this);
			// m.setVolume(1f, 1f);
			m.start();
			play.setBackgroundResource(R.drawable.btn_pause);
			new Thread(this).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onCompletion(MediaPlayer arg0) {
		if (pingFinished) {
			// go to next state
		} else {
			// play glass ping
			try {
				m.release();
				m = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = getAssets().openFd("MP3/glass_ping.mp3");
				m.setDataSource(descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				m.prepare();
				m.setOnCompletionListener(this);
				m.start();
				pingFinished = true;
				Intent i = new Intent(cont, nextIntent);
				Bundle b = new Bundle();
				b.putString("state", nextState);
				i.putExtras(b);
				startActivity(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	@Override
	public void run() {
		while (null != m && !pingFinished) {
			try {
				Thread.sleep(500);
				if (null != m && m.isPlaying()) {
					pb.setProgress(m.getCurrentPosition());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
