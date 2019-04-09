package org.iota.jota.utils;

/**
 * @author adrian
 */
public class StopWatch {

    private long startTime = 0;
    private boolean running = false;
    private long currentTime = 0;

    /**
     * Initializes a new instance of the StopWatch class.
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
     *
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
     * @return The elapsed time in milliseconds.
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
     * @return The elapsed time in seconds.
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
     * @return The elapsed time in minutes.
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
     * @return The elapsed time in hours.
     */
    public long getElapsedTimeHour() {
        long elapsed = 0;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000 / 3600);
        }
        return elapsed;
    }
}
