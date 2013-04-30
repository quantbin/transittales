package org.transittales;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class State {
	public static ArrayList<String> onceStates = new ArrayList<String>();
	public static HashMap<String, Properties> stateCache = new HashMap<String, Properties>();

	public static void seenState(String stateName) {
		if (!onceStates.contains(stateName)) {
			onceStates.add(stateName);
		}
	}

	public static boolean hasSeenState(String stateName) {
		if (onceStates.contains(stateName)) {
			return true;
		} else {
			return false;
		}
	}

	public static Properties load(Activity activity, String stateName) {
		if (null != stateCache.get(stateName)) {
			return stateCache.get(stateName);
		}
		Resources resources = activity.getResources();
		AssetManager assetManager = resources.getAssets();
		Properties properties = new Properties();
		try {
			InputStream inputStream = assetManager.open("states/" + stateName);
			properties.load(inputStream);
		} catch (IOException e) {
			System.err.println("Failed to open property file: " + stateName);
			e.printStackTrace();
		}
		stateCache.put(stateName, properties);
		return properties;
	}
}
