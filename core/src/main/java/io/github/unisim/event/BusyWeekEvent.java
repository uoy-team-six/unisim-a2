package io.github.unisim.event;

import com.badlogic.gdx.math.MathUtils;

public class BusyWeekEvent extends GameEvent {
    public BusyWeekEvent() {
        super(MathUtils.random(5.0f, 15.0f));
    }

    @Override
    public String getName() {
        return "Busy Week";
    }

    @Override
    public String getDescription() {
        return "There are more students than normal on campus this week";
    }

    @Override
    public EventType getType() {
        return EventType.NEUTRAL;
    }
}
