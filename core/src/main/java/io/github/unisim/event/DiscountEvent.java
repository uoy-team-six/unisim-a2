package io.github.unisim.event;

public class DiscountEvent extends GameEvent {
    public DiscountEvent() {
        super(20.0f);
    }

    /**
     * Applies the discount to the given price.
     *
     * @param price the original price
     * @return the price with the discount applied
     */
    public int applyDiscount(int price) {
        // Apply 20% discount.
        return price / 10 * 8;
    }

    @Override
    public String getName() {
        return "Construction Sale";
    }

    @Override
    public String getDescription() {
        return "The construction companies are having a massive sale! All buildings are 20% off. Time to expand your campus!";
    }

    @Override
    public EventType getType() {
        return EventType.POSITIVE;
    }
}
