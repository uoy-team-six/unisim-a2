package io.github.unisim.event;

public abstract class GameEvent {
    private float durationTimer;

    protected GameEvent(float durationTimer) {
        this.durationTimer = durationTimer;
    }

    public void update(float deltaTime) {
        durationTimer -= deltaTime;
    }

    public boolean isFinished() {
        return durationTimer <= 0.0f;
    }

    public abstract String getName();
    public abstract String getDescription();
    public abstract EventType getType();
}
