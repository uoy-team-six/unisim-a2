package io.github.unisim;

import io.github.unisim.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameLogic unit tests.
 */
public class GameLogicTests {
    private GameLogic gameLogic;

    @BeforeEach
    public void setup() {
        gameLogic = new GameLogic(new World(), Difficulty.NORMAL, () -> false);
    }

    @Test
    public void testStartPaused() {
        assertTrue(gameLogic.isPaused());
        assertFalse(gameLogic.isGameOver());
    }

    @Test
    public void testUnpause() {
        gameLogic.unpause();
        assertFalse(gameLogic.isPaused());
        assertFalse(gameLogic.isGameOver());
    }

    @Test
    public void testPause() {
        gameLogic.unpause();
        gameLogic.pause();
        assertTrue(gameLogic.isPaused());
        assertFalse(gameLogic.isGameOver());
    }

    @Test
    public void testStartInSummer() {
        assertTrue(gameLogic.isSummer());
    }

    static Stream<Arguments> testStartingMoney() {
        // Test starting money matches requirements.
        return Stream.of(Arguments.of(Difficulty.EASY, 100000),
            Arguments.of(Difficulty.NORMAL, 75000),
            Arguments.of(Difficulty.HARD, 50000));
    }

    @ParameterizedTest
    @MethodSource
    public void testStartingMoney(Difficulty difficulty, int money) {
        gameLogic = new GameLogic(null, difficulty);
        assertEquals(gameLogic.getMoney(), money);
    }

    @Test
    public void testGameTimer() {
        // Test game starts with 5 minutes as per product brief.
        assertEquals(gameLogic.getRemainingTime(), 300.0f);

        // Test that nothing happens when paused.
        gameLogic.update(10.0f);
        assertEquals(gameLogic.getRemainingTime(), 300.0f);

        // Test that the right amount of time passes when unpaused.
        gameLogic.unpause();
        gameLogic.update(10.0f);
        assertEquals(gameLogic.getRemainingTime(), 290.0f);
    }

    @Test
    public void testGameOver() {
        // Unpause and run the whole game duration.
        gameLogic.unpause();
        gameLogic.update(300.0f);

        // Let another 5 seconds pass.
        gameLogic.update(5.0f);

        // Make sure game is over.
        assertTrue(gameLogic.isGameOver());

        // Make sure remaining time doesn't go below zero.
        assertEquals(gameLogic.getRemainingTime(), 0.0f);

        // Make sure attempting to pause the game doesn't affect the game over state.
        assertFalse(gameLogic.isPaused());
        gameLogic.pause();
        assertFalse(gameLogic.isPaused());
        assertTrue(gameLogic.isGameOver());
    }

    @Test
    public void testSeasons() {
        gameLogic.unpause();
        for (int year = 1; year <= 3; year++) {
            assertEquals(gameLogic.getYear(), year);

            // Year should start in summer period.
            assertTrue(gameLogic.isSummer());

            // Year should start in summer.
            gameLogic.update(21.0f);
            assertFalse(gameLogic.isSummer());

            // First semester.
            assertEquals(gameLogic.getSemester(), 1);
            gameLogic.update(40.0f);

            // Second semester.
            assertEquals(gameLogic.getSemester(), 2);
            gameLogic.update(40.0f);
        }
    }

    @Test
    @Disabled
    public void testGameTimerBounds() {
        gameLogic.unpause();
        gameLogic.update(500.0f);
        gameLogic.update(500.0f);
        assertTrue(gameLogic.isGameOver());

        // Make sure game stays at year 3 semester 2 on game end.
        assertEquals(gameLogic.getRemainingTime(), 0.0f);
        assertEquals(gameLogic.getYear(), 3);
        assertEquals(gameLogic.getSemester(), 2);
    }
}
