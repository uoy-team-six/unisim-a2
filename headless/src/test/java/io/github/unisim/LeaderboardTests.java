package io.github.unisim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Leaderboard unit tests.
 */
public class LeaderboardTests {
    private Leaderboard leaderboard;

    @BeforeEach
    public void setup() {
        leaderboard = new Leaderboard();
        leaderboard.clear();
    }

    @Test
    public void testStartEmpty() {
        assertTrue(leaderboard.getSortedNames().isEmpty());
    }

    @Test
    public void testNonExistentPlayer() {
        assertEquals(leaderboard.getScore("foo"), 0);
    }

    @Test
    public void testSorting() {
        leaderboard.addScore("foo", 1000);
        leaderboard.addScore("bar", 5000);
        leaderboard.addScore("baz", 3000);

        final var sorted = leaderboard.getSortedNames();
        assertEquals(sorted.size(), 3);
        assertEquals(sorted.get(0), "bar");
        assertEquals(sorted.get(1), "baz");
        assertEquals(sorted.get(2), "foo");
    }

    @Test
    public void testKeepMaxScore() {
        leaderboard.addScore("foo", 1000);
        assertEquals(leaderboard.getScore("foo"), 1000);
        leaderboard.addScore("foo", 500);
        assertEquals(leaderboard.getScore("foo"), 1000);
        leaderboard.addScore("foo", 2000);
        assertEquals(leaderboard.getScore("foo"), 2000);
    }
}
