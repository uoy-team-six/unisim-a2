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

    public abstract String getName();
    public abstract String getDescription();
    public abstract EventType getType();
}
