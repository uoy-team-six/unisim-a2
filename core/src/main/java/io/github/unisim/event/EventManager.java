package io.github.unisim.event;

import com.badlogic.gdx.math.MathUtils;

import java.util.List;

public class EventManager {
    private final List<Class<? extends GameEvent>> enabledEvents;
    private GameEvent currentEvent;
    private float nextEventProbability;
    private float checkEventTimer;

    public EventManager() {
        enabledEvents = List.of(DonationEvent.class, DiscountEvent.class, RainEvent.class, RosesEvent.class);
    }

    public void update(float deltaTime) {
        if (currentEvent != null) {
            currentEvent.update(deltaTime);
            if (currentEvent.isFinished()) {
                currentEvent = null;
            }
            return;
        }

        // No event is currently active, so generate a random number every 2 seconds to see if we should start a new
        // event. Bias the random number slightly to prevent events from happening to close to each other.
        nextEventProbability += deltaTime * 0.01f;
        checkEventTimer += deltaTime;
        if (checkEventTimer > 2.0f) {
            checkEventTimer = 0.0f;
            if (Math.min(MathUtils.random() + 0.1f, 1.0f) < nextEventProbability) {
                nextEventProbability = 0.0f;
                try {
                    var eventConstructor = enabledEvents.get(MathUtils.random(enabledEvents.size() - 1)).getConstructor();
                    currentEvent = eventConstructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean isEventActive() {
        return currentEvent != null;
    }

    public GameEvent getCurrentEvent() {
        return currentEvent;
    }
}
