package io.github.unisim.event;

public abstract class SatisfactionEvent extends GameEvent {
    protected SatisfactionEvent(float durationTimer) {
        super(durationTimer);
    }

    /**
     * @param deltaTime the delta time between the last call of getSatisfaction
     * @return a satisfaction delta to apply this frame
     */
    public abstract float getSatisfaction(float deltaTime);
}
