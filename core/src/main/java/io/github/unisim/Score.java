package io.github.unisim;

import com.badlogic.gdx.math.MathUtils;
import io.github.unisim.achievement.Achievement;

import java.util.List;

public record Score(int campusValue, float satisfaction, List<Achievement> unlockedAchievements) {
    public int campusValueScore() {
        return campusValue / 10;
    }

    public int satisfactionScore() {
        return MathUtils.round(satisfaction * 20000.0f);
    }

    public int satisfactionPercentage() {
        return MathUtils.ceil(satisfaction * 100.0f);
    }

    public int totalAchievementScore() {
        return unlockedAchievements.stream().mapToInt(Achievement::getScore).sum();
    }

    public int totalScore() {
        return campusValueScore() + satisfactionScore() + totalAchievementScore();
    }
}
