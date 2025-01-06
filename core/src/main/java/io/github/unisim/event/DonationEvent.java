package io.github.unisim.event;

import com.badlogic.gdx.math.MathUtils;

public class DonationEvent extends GameEvent {
    private float accumulatedMoney;

    public DonationEvent() {
        super(MathUtils.random(10.0f, 20.0f));
    }

    public int getMoney() {
        int wholeMoney = (int) accumulatedMoney;
        accumulatedMoney -= wholeMoney;
        return wholeMoney;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        accumulatedMoney += MathUtils.random(10.0f, 100.0f) * deltaTime;
    }

    @Override
    public String getName() {
        return "Alumni Donations";
    }

    @Override
    public String getDescription() {
        return "Alumni are donating to the university!";
    }

    @Override
    public EventType getType() {
        return EventType.POSITIVE;
    }
}
