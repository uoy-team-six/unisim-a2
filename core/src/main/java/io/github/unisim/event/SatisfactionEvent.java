package io.github.unisim.event;

public abstract class SatisfactionEvent extends GameEvent {
    protected SatisfactionEvent(float durationTimer) {
        super(durationTimer);
    }

    public abstract float getSatisfaction(float deltaTime);
}
