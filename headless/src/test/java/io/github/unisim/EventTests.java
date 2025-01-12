package io.github.unisim;

import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingType;
import io.github.unisim.event.DiscountEvent;
import io.github.unisim.event.DonationEvent;
import io.github.unisim.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Event and GameLogic integration tests.
 */
public class EventTests {
    private GameLogic gameLogic;

    @BeforeEach
    public void setup() {
        gameLogic = new GameLogic(new World(), Difficulty.NORMAL, () -> false);
    }

    @Test
    public void testEventStartPausesGame() {
        gameLogic.unpause();
        gameLogic.getEventManager().startNewEvent();
        assertTrue(gameLogic.isPaused());
    }

    @Test
    public void testDonationEvent() {
        int startingMoney = gameLogic.getMoney();
        gameLogic.getEventManager().startNewEvent(DonationEvent.class);
        gameLogic.unpause();
        gameLogic.update(10.0f);
        assertTrue(gameLogic.getMoney() > startingMoney);
    }

    @Test
    public void testDiscountEvent() {
        var building = new Building(null,
            0.0f,
            null,
            null,
            null,
            false,
            BuildingType.EATING,
            "",
            1000,
            0);
        assertEquals(gameLogic.getBuildingPrice(building), 1000);
        gameLogic.getEventManager().startNewEvent(DiscountEvent.class);
        assertEquals(gameLogic.getBuildingPrice(building), 800);
    }
}
