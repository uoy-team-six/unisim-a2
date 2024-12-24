package io.github.unisim.achievement;

import io.github.unisim.GameLogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AchievementManager {
    // The time in seconds to show achievements for.
    private static final float ACHIEVEMENT_DISPLAY_TIME = 5.0f;

    private final GameLogic gameLogic;
    private final Set<Achievement> unlockedAchievements;

    // Keep track of recently unlocked achievements.
    private final List<Achievement> recentlyUnlockedAchivements;
    private float recentlyUnlockedTimer;

    private float highStudentSatisfactionTime;

    public AchievementManager(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        unlockedAchievements = new HashSet<>();
        recentlyUnlockedAchivements = new ArrayList<>();
    }

    private void unlock(Achievement achievement) {
        if (!unlockedAchievements.add(achievement)) {
            // Achievement was already unlocked.
            return;
        }

        // New achivement unlocked!
        recentlyUnlockedAchivements.add(achievement);
        if (recentlyUnlockedTimer <= 0.0f) {
            recentlyUnlockedTimer = ACHIEVEMENT_DISPLAY_TIME;
        }
    }

    public void update(float deltaTime) {
        // Update recently unlocked achievement logic.
        recentlyUnlockedTimer -= deltaTime;
        if (recentlyUnlockedTimer <= 0.0f && !recentlyUnlockedAchivements.isEmpty()) {
            recentlyUnlockedAchivements.remove(0);
            if (!recentlyUnlockedAchivements.isEmpty()) {
                recentlyUnlockedTimer = ACHIEVEMENT_DISPLAY_TIME;
            }
        }

        // Entrepreneur achievement (earned more than 500k).
        if (gameLogic.getMoney() > 500_000) {
            unlock(Achievement.ENTREPRENEUR);
        }

        // I heart uni achievement (> 75% satisfaction for 2 minutes).
        if (gameLogic.getSatisfaction() > 0.75f) {
            highStudentSatisfactionTime += deltaTime;
            if (highStudentSatisfactionTime >= 120.0f) {
                unlock(Achievement.I_HEART_UNI);
            }
        } else {
            highStudentSatisfactionTime = 0.0f;
        }

        // Rising population achievement (>= 100 students).
        if (gameLogic.getStudentCount() >= 100) {
            unlock(Achievement.STUDENT_COUNT);
        }
    }

    public Achievement getRecentlyUnlockedAchievement() {
        if (recentlyUnlockedAchivements.isEmpty()) {
            return null;
        }
        return recentlyUnlockedAchivements.get(0);
    }
}
