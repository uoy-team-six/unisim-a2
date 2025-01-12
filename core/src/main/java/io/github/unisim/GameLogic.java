package io.github.unisim;

import com.badlogic.gdx.math.MathUtils;
import io.github.unisim.achievement.Achievement;
import io.github.unisim.achievement.AchievementManager;
import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingType;
import io.github.unisim.event.*;
import io.github.unisim.world.World;

/**
 * A class which manages the gameplay logic. This class is a new addition from the inherited codebase.
 */
public class GameLogic {
    // Time related constants.
    private static final int TOTAL_GAME_TIME = 5 * 60;
    private static final int ONE_YEAR_TIME = 100;
    private static final int SUMMER_TIME = 20;

    private static final int SLEEPING_BUILDING_STUDENT_COUNT = 20;
    private static final int STUDENT_TUITION_FEE = 1000;

    private final World world;
    private final AchievementManager achievementManager;
    private final EventManager eventManager;
    private GameState gameState;
    private float remainingTime;
    private int studentCount;
    private int money;
    private int lastTickedYear;

    // Satisfaction.
    private float satisfaction;
    private float newBuildingSatisfaction;

    public GameLogic(World world, Difficulty difficulty) {
        this.world = world;
        achievementManager = new AchievementManager(this);
        eventManager = new EventManager(this, difficulty);

        // Start in a paused state.
        gameState = GameState.PAUSED;
        remainingTime = TOTAL_GAME_TIME;
        money = difficulty.getStartingMoney();
    }

    /**
     * @return a score object for the current game state
     */
    public Score calculateScore() {
        // Calculate a final score based on the total value of placed buildings, ending satisfaction, and unlocked
        // achievements. Remaining money is not included in order to incentivise building placement.
        final int campusValue = world
            .getBuildingManager()
            .getBuildings()
            .stream()
            .mapToInt(b -> b.price)
            .sum();
        return new Score(
            campusValue,
            satisfaction,
            achievementManager.getUnlockedAchievements().stream().toList()
        );
    }

    /**
     * @param building a building
     * @return the price of the building including any discount
     */
    public int getBuildingPrice(Building building) {
        int buildingPrice = building.price;
        if (eventManager.getCurrentEvent() instanceof DiscountEvent discountEvent) {
            buildingPrice = discountEvent.applyDiscount(buildingPrice);
        }
        return buildingPrice;
    }

    /**
     * Places the currently selected building.
     *
     * @return true if the building was placed; false if not
     */
    public boolean placeBuilding() {
        int buildingPrice = getBuildingPrice(world.selectedBuilding);
        if (money < buildingPrice) {
            return false;
        }
        if (!world.placeBuilding()) {
            return false;
        }
        money -= buildingPrice;

        // Calculate new building satisfaction.
        newBuildingSatisfaction += Math.min(0.15f / (float) Math.pow(satisfaction, 0.5f), 1.0f);
        return true;
    }

    /**
     * Continuously updates the student satisfaction.
     *
     * @param deltaTime the delta time between the last call of updateSatisfaction
     */
    private void updateSatisfaction(float deltaTime) {
        // Slowly apply new building satisfaction.
        float newBuildingFactor = newBuildingSatisfaction * 1.5f * deltaTime;
        satisfaction += newBuildingFactor;
        newBuildingSatisfaction -= newBuildingFactor;
        newBuildingSatisfaction = Math.max(newBuildingSatisfaction, 0.0f);

        // Decay satisfaction over time, but lower the factor based on the amount of recreation buildings.
        float decayRate = 0.025f;
        decayRate -= world.getBuildingCount(BuildingType.RECREATION) / 250.0f;
        satisfaction -= Math.max(decayRate, 0.0025f) * deltaTime;

        // Apply any satisfaction changes from the current event.
        if (eventManager.getCurrentEvent() instanceof SatisfactionEvent satisfactionEvent) {
            satisfaction += satisfactionEvent.getSatisfaction(deltaTime);
        }

        // Clamp satisfaction between zero and one.
        satisfaction = MathUtils.clamp(satisfaction, 0.0f, 1.0f);
    }

    /**
     * Updates the game logic.
     *
     * @param deltaTime the delta time between the last call of update
     */
    public void update(float deltaTime) {
        // Move to GAME_OVER state when timer runs out.
        if (remainingTime <= 0.0f) {
            gameState = GameState.GAME_OVER;
        }

        if (gameState == GameState.PAUSED || gameState == GameState.GAME_OVER) {
            return;
        }

        // Tick timer down.
        remainingTime -= deltaTime;

        // Calculate student count.
        studentCount = world.getBuildingCount(BuildingType.SLEEPING) * SLEEPING_BUILDING_STUDENT_COUNT;

        // Add money if year has ticked. Each student brings in their yearly tuition fee.
        final int year = getYear();
        if (lastTickedYear != year && !isSummer()) {
            final int tuitionIncome = studentCount * STUDENT_TUITION_FEE;
            money += tuitionIncome;
            lastTickedYear = year;

            // Add money from building passive income.
            for (var building : world.getBuildingManager().getBuildings()) {
                money += building.passiveIncome;
            }
        }

        // Update satisfaction.
        updateSatisfaction(deltaTime);

        // Update achievements and events.
        achievementManager.update(deltaTime);
        eventManager.update(deltaTime);

        // Handle busy week event.
        var peopleManager = world.getPeopleManager();
        if (peopleManager != null) {
            if (eventManager.getCurrentEvent() instanceof BusyWeekEvent) {
                peopleManager.setSpawnRateMultiplier(20.0f);
            } else {
                peopleManager.setSpawnRateMultiplier(1.0f);
            }
        }


        // Handle donation event.
        if (eventManager.getCurrentEvent() instanceof DonationEvent donationEvent) {
            money += donationEvent.getMoney();
        }
    }

    /**
     * Pauses the game if currently in the playing state.
     */
    public void pause() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
        }
    }

    /**
     * Unpauses the game.
     */
    public void unpause() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
        }
    }

    public int getSatisfactionPercentage() {
        return MathUtils.ceil(satisfaction * 100.0f);
    }

    public Achievement getRecentlyUnlockedAchievement() {
        return achievementManager.getRecentlyUnlockedAchievement();
    }

    public int getYear() {
        return (int) (TOTAL_GAME_TIME - remainingTime - 0.5f) / ONE_YEAR_TIME + 1;
    }

    public int getSecondsIntoYear() {
        return (int) (TOTAL_GAME_TIME - remainingTime - 0.5f) % ONE_YEAR_TIME;
    }

    public int getSemester() {
        if (getSecondsIntoYear() >= (ONE_YEAR_TIME / 2 + SUMMER_TIME / 2)) {
            return 2;
        }
        return 1;
    }

    public boolean isSummer() {
        return getSecondsIntoYear() <= SUMMER_TIME;
    }

    public boolean isPaused() {
        return gameState == GameState.PAUSED;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public float getRemainingTime() {
        return remainingTime;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public int getMoney() {
        return money;
    }

    public float getSatisfaction() {
        return satisfaction;
    }
}
