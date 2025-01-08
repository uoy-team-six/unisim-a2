package io.github.unisim;

import io.github.unisim.achievement.Achievement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ScoreTests {
    @Test
    public void testCampusValue() {
        var score = new Score(100, 0.0f, List.of());
        assertNotEquals(score.campusValueScore(), 0);
        assertEquals(score.satisfactionScore(), 0);
        assertEquals(score.totalAchievementScore(), 0);
        assertEquals(score.totalScore(), score.campusValueScore());
    }

    @Test
    public void testSastisfaction() {
        var score = new Score(0, 0.5f, List.of());
        assertEquals(score.campusValueScore(), 0);
        assertNotEquals(score.satisfactionScore(), 0);
        assertEquals(score.totalAchievementScore(), 0);
        assertEquals(score.totalScore(), score.satisfactionScore());
    }

    @Test
    public void testAchievement() {
        var score = new Score(0, 0.0f, List.of(Achievement.ENTREPRENEUR));
        assertEquals(score.campusValueScore(), 0);
        assertEquals(score.satisfactionScore(), 0);
        assertEquals(score.totalAchievementScore(), Achievement.ENTREPRENEUR.getScore());
        assertEquals(score.totalScore(), score.totalAchievementScore());
    }

    static Stream<Arguments> testSatisfactionPercentage() {
        return Stream.of(Arguments.of(0.0f, 0),
            Arguments.of(1.0f, 100),
            Arguments.of(0.1f, 10),
            Arguments.of(0.5f, 50),
            Arguments.of(0.65f, 65));
    }

    @ParameterizedTest
    @MethodSource
    public void testSatisfactionPercentage(float satisfaction, int expectedPercentage) {
        var score = new Score(0, satisfaction, List.of());
        assertEquals(score.satisfactionPercentage(), expectedPercentage);
    }
}
