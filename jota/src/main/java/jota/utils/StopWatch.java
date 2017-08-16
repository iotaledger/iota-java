package jota.utils;

/**
 * @author adrian
 */
public class StopWatch {

    private long startTime = 0;
    private boolean running = false;
    private long currentTime = 0;

    /**
     *
     */
    public StopWatch() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    /**
     *
     */
    public void reStart() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    /**
     * @return
     */
    public StopWatch stop() {
        this.running = false;
        return this;
    }

    /**
     *
     */
    public void pause() {
        this.running = false;
        currentTime = System.currentTimeMillis() - startTime;
    }

    /**
     *
     */
    public void resume() {
        this.running = true;
        this.startTime = System.currentTimeMillis() - currentTime;
    }

    /**
     * Elapsed time in milliseconds.
     *
     * @return
     */
    public long getElapsedTimeMili() {
        long elapsed = 0;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        }
        return elapsed;
    }

    /**
     * Elapsed time in seconds.
     *
     * @return
     */
    public long getElapsedTimeSecs() {
        long elapsed = 0;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime) / 1000;
        }
        return elapsed;
    }

    /**
     * Elapsed time in minutes.
     *
     * @return
     */
    public long getElapsedTimeMin() {
        long elapsed = 0;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime) / 1000 / 60;
        }
        return elapsed;
    }

    /**
     * Elapsed time in hours.
     *
     * @return
     */
    public long getElapsedTimeHour() {
        long elapsed = 0;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000 / 3600);
        }
        return elapsed;
    }
}
