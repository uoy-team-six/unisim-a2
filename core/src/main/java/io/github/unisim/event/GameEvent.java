package io.github.unisim.event;

public abstract class GameEvent {
    private float durationTimer;

    protected GameEvent(float durationTimer) {
        this.durationTimer = durationTimer;
    }

    /**
     * Updates the event. Decreases the duration timer.
     *
     * @param deltaTime the delta time between the last call of update
     */
    public void update(float deltaTime) {
        durationTimer -= deltaTime;
    }

    /**
     * @return whether the event has run its duration
     */
    public boolean isFinished() {
        return durationTimer <= 0.0f;
    }

    /**
     * @return the name of the event to display in the UI
     */
    public abstract String getName();

    /**
     * @return a fun description of what the event does
     */
    public abstract String getDescription();

    /**
     * @return the type of event (positive, negative, or neutral)
     */
    public EventType getType() {
        return EventType.NEUTRAL;
    }
}
