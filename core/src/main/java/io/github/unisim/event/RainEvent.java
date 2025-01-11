package io.github.unisim.event;

import com.badlogic.gdx.math.MathUtils;

public class RainEvent extends SatisfactionEvent {
    public RainEvent() {
        super(MathUtils.random(10.0f, 25.0f));
    }

    @Override
    public float getSatisfaction(float deltaTime) {
        return -0.02f * deltaTime;
    }

    @Override
    public String getName() {
        return "Rainy Day";
    }

    @Override
    public String getDescription() {
        return "It's pouring outside! Students are getting soaked between classes, and everyone's a bit grumpy.";
    }

    @Override
    public EventType getType() {
        return EventType.NEGATIVE;
    }
}
