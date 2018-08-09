public class ServerTimer {
    private final long mStartTime;

    private static ServerTimer instance = new ServerTimer();

    private ServerTimer() {
        mStartTime = System.currentTimeMillis();
    }

    public long getCurrentServerTime() {
        return System.currentTimeMillis() - mStartTime;
    }

    public static final ServerTimer getInstance() {
        return instance;
    }
}