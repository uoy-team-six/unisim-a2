package io.github.unisim.event;

import com.badlogic.gdx.math.MathUtils;

public class RosesEvent extends SatisfactionEvent {
    public RosesEvent() {
        super(MathUtils.random(20.0f, 25.0f));
    }

    @Override
    public float getSatisfaction(float deltaTime) {
        return 0.02f * deltaTime;
    }

    @Override
    public String getName() {
        return "Roses Tournament";
    }

    @Override
    public String getDescription() {
        return "Roses has arrived! Students will compete in this exciting sports event, boosting school spirit and student satisfaction!";
    }

    @Override
    public EventType getType() {
        return EventType.POSITIVE;
    }
}
