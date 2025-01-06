package io.github.unisim.achievement;

import io.github.unisim.GameLogic;

import java.util.*;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class AchievementManager {
    // The time in seconds to show achievements for.
    public static final float ACHIEVEMENT_DISPLAY_TIME = 5.0f;

    private final IntSupplier moneySupplier;
    private final Supplier<Float> satisfactionSupplier;
    private final IntSupplier studentCountSupplier;
    private final Set<Achievement> unlockedAchievements;

    // Keep track of recently unlocked achievements.
    private final List<Achievement> recentlyUnlockedAchivements;
    private float recentlyUnlockedTimer;

    private float highStudentSatisfactionTime;

    public AchievementManager(IntSupplier moneySupplier, Supplier<Float> satisfactionSupplier, IntSupplier studentCountSupplier) {
        this.moneySupplier = moneySupplier;
        this.satisfactionSupplier = satisfactionSupplier;
        this.studentCountSupplier = studentCountSupplier;
        unlockedAchievements = new HashSet<>();
        recentlyUnlockedAchivements = new ArrayList<>();
    }

    public AchievementManager(GameLogic gameLogic) {
        this(gameLogic::getMoney, gameLogic::getSatisfaction, gameLogic::getStudentCount);
    }

    public void unlock(Achievement achievement) {
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
        if (moneySupplier.getAsInt() > 500_000) {
            unlock(Achievement.ENTREPRENEUR);
        }

        // I heart uni achievement (> 75% satisfaction for 2 minutes).
        if (satisfactionSupplier.get() > 0.75f) {
            highStudentSatisfactionTime += deltaTime;
            if (highStudentSatisfactionTime >= 120.0f) {
                unlock(Achievement.I_HEART_UNI);
            }
        } else {
            highStudentSatisfactionTime = 0.0f;
        }

        // Rising population achievement (>= 100 students).
        if (studentCountSupplier.getAsInt() >= 100) {
            unlock(Achievement.STUDENT_COUNT);
        }
    }

    public boolean isUnlocked(Achievement achievement) {
        return unlockedAchievements.contains(achievement);
    }

    /**
     * @return an unmodifiable set of unlocked achievements
     */
    public Set<Achievement> getUnlockedAchievements() {
        return Collections.unmodifiableSet(unlockedAchievements);
    }

    public Achievement getRecentlyUnlockedAchievement() {
        if (recentlyUnlockedAchivements.isEmpty()) {
            return null;
        }
        return recentlyUnlockedAchivements.get(0);
    }
}
