package io.github.unisim;

public class GameLogic {
    private static final float TOTAL_GAME_TIME = 5.0f * 60.0f;

    private GameState gameState;
    private float remainingTime;

    public GameLogic() {
        // Start in a paused state.
        gameState = GameState.PAUSED;
        remainingTime = TOTAL_GAME_TIME;
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

    public boolean isPaused() {
        return gameState == GameState.PAUSED;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    public GameState getGameState() {
        return gameState;
    }

    public float getRemainingTime() {
        return remainingTime;
    }
}
