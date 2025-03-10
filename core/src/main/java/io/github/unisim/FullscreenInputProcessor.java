package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Runs before the WorldInputProcessor and handles any input events generated from the UI.
 */
public class FullscreenInputProcessor extends InputAdapter {
    int[] windowSize = new int[2];
    boolean fullscreen = false;

    /**
     * Inform the object about the current window size.
     *
     * @param width  - The new width of the window
     * @param height - The new height of the window
     */
    public void resize(int width, int height) {
        if (!fullscreen) {
            windowSize[0] = width;
            windowSize[1] = height;
        }
    }

    /**
     * Called when a key is pressed and handles logic related to keypresses
     * within UI components.
     *
     * @param keycode - The unique identifier for the Key pressed.
     * @return whether the event has been handled and needs to be further processed.
     */
    public boolean keyDown(int keycode) {
        switch (keycode) {
            // Toggle fullscreen
            case Keys.F11:
                Monitor currentMonitor = Gdx.graphics.getMonitor();
                DisplayMode displayMode = Gdx.graphics.getDisplayMode(currentMonitor);
                fullscreen = !fullscreen;
                if (fullscreen) {
                    Gdx.graphics.setFullscreenMode(displayMode);
                } else {
                    Gdx.graphics.setWindowedMode(windowSize[0], windowSize[1]);
                }
                return true;

            default:
                return false;
        }
    }
}
