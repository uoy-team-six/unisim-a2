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
        return "Rain";
    }

    @Override
    public String getDescription() {
        return "Students are slightly unhappier";
    }

    @Override
    public EventType getType() {
        return EventType.NEGATIVE;
    }
}
