package io.github.unisim.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Gdx;

/**
 * Handles displaying floating text feedback for money changes.
 */
public class MoneyFeedback {
    private Label feedbackLabel;
    private float displayTime;
    private float alpha = 1.0f;
    private float yOffset = 0;
    private static final float DISPLAY_DURATION = 1.5f;
    private static final float FADE_START = 0.75f;
    private static final float MOVE_SPEED = -50.0f;
    
    public MoneyFeedback(Stage stage, Skin skin) {
        feedbackLabel = new Label("", skin);
        feedbackLabel.setVisible(false);
        feedbackLabel.setAlignment(Align.center); // Center the text
        stage.addActor(feedbackLabel);
    }
    
    /**
     * Show feedback for a money change.
     * @param amount The amount of money changed (positive for gains, negative for losses)
     * @param x The x position to show the feedback at (center point)
     * @param y The y position to show the feedback at (top of screen)
     */
    public void showFeedback(int amount, float x, float y) {
        feedbackLabel.setText(amount >= 0 ? String.format("+£%d", amount) : String.format("-£%d", -amount));
        feedbackLabel.setColor(amount >= 0 ? Color.GREEN : Color.RED);
        float centerX = x - feedbackLabel.getWidth() / 2;  // Center position
        float midpointX = x + (centerX - x) / 2;  // Halfway between left (x) and center
        feedbackLabel.setPosition(midpointX, y - feedbackLabel.getHeight());
        feedbackLabel.setVisible(true);
        displayTime = DISPLAY_DURATION;
        alpha = 1.0f;
        yOffset = 0;
    }
    
    /**
     * Update the feedback animation.
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        if (displayTime > 0) {
            displayTime -= deltaTime;
            
            if (displayTime < FADE_START) {
                alpha = displayTime / FADE_START;
            }
            
            yOffset += MOVE_SPEED * deltaTime;
            
            feedbackLabel.setColor(feedbackLabel.getColor().r, feedbackLabel.getColor().g, feedbackLabel.getColor().b, alpha);
            feedbackLabel.setY(feedbackLabel.getY() + MOVE_SPEED * deltaTime);
            
            if (displayTime <= 0) {
                feedbackLabel.setVisible(false);
            }
        }
    }
    
    /**
     * Update the font scale based on screen size.
     * @param height The screen height
     */
    public void resize(int height) {
        feedbackLabel.setFontScale(height * 0.002f); 
    }
} 