package p455w0rdslib;

/**
 * @author p455w0rd
 *
 */
public class LibTimer {
	public static LibTimer INSTANCE;
	protected float timer = 0.0F;

	public class Timer {
		public Timer(LibTimer x) {
			INSTANCE = new LibTimer();
		}

		public float getTimer() {
			return timer;
		}

		public void setTimer(float time) {
			timer = time;
		}
	}
}
