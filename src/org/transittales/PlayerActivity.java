package org.transittales;

import java.io.IOException;
import java.util.Properties;

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
import android.widget.TextView;

public class PlayerActivity extends Activity implements OnCompletionListener, Runnable {
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
		TextView textViewLabel;

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
		textViewLabel = (TextView) findViewById(R.id.textViewLabel);

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
				try {
					if (null != m && m.isPlaying()) {
						// pause
						play.setBackgroundResource(R.drawable.btn_play);
						m.pause();
					} else {
						// resume
						play.setBackgroundResource(R.drawable.btn_pause);
						m.start();
					}
				} catch (Exception e) {
					Log.e("PlayerActivity", "error in onClick");
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
		State.seenState(state);
		String file = null;
		int resID = -1;
		Properties stateProp = State.load(this, state);
		if (null == stateProp) {
			Log.e("PlayerActivity", "state not found: " + state);
			return;
		} else {
			textViewLabel.setText(stateProp.getProperty("text"));
			resID = getResources().getIdentifier(stateProp.getProperty("img"), "drawable", getPackageName());
			nextState = stateProp.getProperty("next_state");
			try {
				nextIntent = Class.forName(stateProp.getProperty("next_intent_class"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				Log.e("PlayerActivity", "class not found: " + stateProp.getProperty("next_intent_class"));
				return;
			}
			file = stateProp.getProperty("file");
		}

		/*
		 * if (PlayerStateName.bill_intro_audio.name().equals(state)) {
		 * textViewLabel.setText("Bill Intro"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = OptionsStateName.bill_options.name();
		 * nextIntent = OptionsActivity.class; file = "MP3/Bill/B01.mp3"; } else
		 * if (PlayerStateName.bill_garbage_audio.name().equals(state)) {
		 * textViewLabel.setText("Garbage"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState =
		 * OptionsStateName.bill_garbage_options.name(); nextIntent =
		 * OptionsActivity.class; file = "MP3/Bill/B03.mp3"; } else if
		 * (PlayerStateName.bill_thoughts_audio.name().equals(state)) {
		 * textViewLabel.setText("Thoughts"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState =
		 * OptionsStateName.bill_thoughts_options.name(); nextIntent =
		 * OptionsActivity.class; file = "MP3/Bill/B04.mp3"; } else if
		 * (PlayerStateName.bill_driver_audio.name().equals(state)) {
		 * textViewLabel.setText("Driver"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = OptionsStateName.bill_options.name();
		 * nextIntent = OptionsActivity.class; file = "MP3/Bill/B05.mp3"; } else
		 * if (PlayerStateName.bill_jerk_man_audio.name().equals(state)) {
		 * textViewLabel.setText("Biggest Jerk"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = "bill_options"; nextIntent =
		 * OptionsActivity.class; file = "MP3/Bill/B02A.mp3"; } else if
		 * (PlayerStateName.bill_jerk_woman_audio.name().equals(state)) {
		 * textViewLabel.setText("Biggest Jerk"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = OptionsStateName.bill_options.name();
		 * nextIntent = OptionsActivity.class; file = "MP3/Bill/B02B.mp3"; }
		 * else if
		 * (PlayerStateName.bill_garbage_rolling_audio.name().equals(state)) {
		 * textViewLabel.setText("Rolling Garbage"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = "bill_options"; nextIntent =
		 * OptionsActivity.class; file = "MP3/Bill/B03A.mp3"; } else if
		 * (PlayerStateName.bill_garbage_newspaper_audio.name().equals( state))
		 * { textViewLabel.setText("Newpaper Garbage"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = OptionsStateName.bill_options.name();
		 * nextIntent = OptionsActivity.class; file = "MP3/Bill/B03B.mp3"; }
		 * else if
		 * (PlayerStateName.bill_garbage_foodwrapper_audio.name().equals(
		 * state)) { textViewLabel.setText("Food wrapper Garbage"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = OptionsStateName.bill_options.name();
		 * nextIntent = OptionsActivity.class; file = "MP3/Bill/B03C.mp3"; }
		 * else if
		 * (PlayerStateName.bill_thoughts_man_audio.name().equals(state)) {
		 * textViewLabel.setText("Thoughts of a Man"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = OptionsStateName.bill_options.name();
		 * nextIntent = OptionsActivity.class; file = "MP3/Bill/B04A.mp3"; }
		 * else if
		 * (PlayerStateName.bill_thoughts_woman_audio.name().equals(state)) {
		 * textViewLabel.setText("Thoughts of a Woman"); resID =
		 * getResources().getIdentifier("audio_bill", "drawable",
		 * getPackageName()); nextState = OptionsStateName.bill_options.name();
		 * nextIntent = OptionsActivity.class; file = "MP3/Bill/B04B.mp3"; }
		 * else { // no man land Log.e("PlayerActivity", "*** invalid state");
		 * return; }
		 */
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
			m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			m.prepare();
			pb.setMax(m.getDuration());
			m.setOnCompletionListener(this);
			m.setVolume(1f, 1f);
			m.start();
			play.setBackgroundResource(R.drawable.btn_pause);
			new Thread(this).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onCompletion(MediaPlayer arg0) {
		if (!pingFinished) {
			// play glass ping
			try {
				m.release();
				m = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = getAssets().openFd("MP3/glass_ping.mp3");
				m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				m.setVolume(.5f, .5f);
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
				m = null;
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
