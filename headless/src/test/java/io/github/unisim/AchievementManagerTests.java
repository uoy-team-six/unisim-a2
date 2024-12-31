package io.github.unisim;

import io.github.unisim.achievement.Achievement;
import io.github.unisim.achievement.AchievementManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.*;

public class AchievementManagerTests {
    private AchievementManager achievementManager;
    private int money;
    private float satisfaction;
    private int studentCount;

    @BeforeEach
    public void setup() {
        achievementManager = new AchievementManager(() -> money, () -> satisfaction, () -> studentCount);
        money = 0;
        satisfaction = 0.0f;
        studentCount = 0;
    }

    @Test
    public void testStartLocked() {
        stream(Achievement.values()).map(achievementManager::isUnlocked).forEach(Assertions::assertFalse);
    }

    @Test
    public void testUnlockAll() {
        for (final var achievement : Achievement.values()) {
            achievementManager.unlock(achievement);
            assertTrue(achievementManager.isUnlocked(achievement));
        }
    }

    static Stream<Arguments> testSimpleAchievement() {
        return Stream.of(Arguments.of(Achievement.ENTREPRENEUR, 0, 0, 0, false),
            Arguments.of(Achievement.ENTREPRENEUR, 500_001, 0, 0, true),
            Arguments.of(Achievement.STUDENT_COUNT, 1_000_000, 1.0f, 99, false),
            Arguments.of(Achievement.STUDENT_COUNT, 0, 0.5f, 100, true),
            Arguments.of(Achievement.I_HEART_UNI, 10_000_000, 1.0f, 1000, false));
    }

    @ParameterizedTest
    @MethodSource
    public void testSimpleAchievement(Achievement achievement, int money, float satisfaction, int studentCount, boolean expectUnlocked) {
        this.money = money;
        this.satisfaction = satisfaction;
        this.studentCount = studentCount;
        achievementManager.update(1.0f);
        assertEquals(achievementManager.isUnlocked(achievement), expectUnlocked);
    }

    @Test
    public void testRecentlyUnlockedSequence() {
        // Unlock all achievements in a sequence, and let the unlock expire after each one.
        for (final var achievement : Achievement.values()) {
            assertNull(achievementManager.getRecentlyUnlockedAchievement());
            achievementManager.unlock(achievement);
            assertEquals(achievementManager.getRecentlyUnlockedAchievement(), achievement);
            achievementManager.update(AchievementManager.ACHIEVEMENT_DISPLAY_TIME);
        }
        assertNull(achievementManager.getRecentlyUnlockedAchievement());
    }

    @Test
    public void testRecentlyUnlockedTogether() {
        assertNull(achievementManager.getRecentlyUnlockedAchievement());

        // Unlock all achievements in one go.
        stream(Achievement.values()).forEach(achievementManager::unlock);

        // Make sure that we still get the unlocks displayed in order.
        stream(Achievement.values()).forEach(achievement -> {
            assertEquals(achievementManager.getRecentlyUnlockedAchievement(), achievement);
            achievementManager.update(AchievementManager.ACHIEVEMENT_DISPLAY_TIME);
        });
        assertNull(achievementManager.getRecentlyUnlockedAchievement());
    }
}
