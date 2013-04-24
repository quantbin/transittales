package org.transittales;

public class AppUtils {
	private static AppUtils instance = null;
	private int counter = 0;

	public static AppUtils getInstance() {
		if (null == instance) {
			instance = new AppUtils();
		}
		return instance;
	}

	public void resume() {
		Geo.getInstance().resume();
		counter++;
	}

	public void pause() {
		Geo.getInstance().pause();
		counter--;
	}

	public boolean isRunning() {
		return counter > 0;
	}
}
