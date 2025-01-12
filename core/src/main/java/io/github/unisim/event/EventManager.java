package io.github.unisim.event;

import com.badlogic.gdx.math.MathUtils;
import io.github.unisim.Difficulty;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<Class<? extends GameEvent>> enabledEvents;
    private GameEvent currentEvent;
    private float nextEventProbability;
    private float checkEventTimer;
    private final GameLogic gameLogic;

    public EventManager(GameLogic gameLogic, Difficulty difficulty) {
        this.gameLogic = gameLogic;
        enabledEvents = new ArrayList<>();
        enabledEvents.addAll(List.of(BusyWeekEvent.class, DonationEvent.class));

        // Don't add negative events on easy difficulty.
        if (difficulty != Difficulty.EASY) {
            enabledEvents.addAll(List.of(RainEvent.class, RosesEvent.class));
        }

        // Don't add random discount even on hard difficulty.
        if (difficulty != Difficulty.HARD) {
            enabledEvents.add(DiscountEvent.class);
        }
    }

    /**
     * Forcibly starts a new event.
     */
    public void startNewEvent() {
        try {
            var eventConstructor = enabledEvents.get(MathUtils.random(enabledEvents.size() - 1)).getConstructor();
            currentEvent = eventConstructor.newInstance();
            gameLogic.pause();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the current active event if there is one, otherwise may randomly start a new event.
     *
     * @param deltaTime the delta time between the last call of update
     */
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
                startNewEvent();
            }
        }
    }

    /**
     * @return whether there's an event currently active
     */
    public boolean isEventActive() {
        return currentEvent != null;
    }

    /**
     * @return the active event if any; null otherwise
     */
    public GameEvent getCurrentEvent() {
        return currentEvent;
    }
}
