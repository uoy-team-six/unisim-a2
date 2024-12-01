package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.unisim.GlobalState;

/**
 * The settings screen that allows the player to adjust the volume.
 */
public class SettingsScreen extends ScreenAdapter {
    private final Stage stage;
    private final Table table;
    private final Skin skin = GlobalState.defaultSkin;
    private final Slider volumeSlider;
    private final Label volumeLabel;
    private final TextButton backButton;
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    /**
     * Create a new Settings screen and draw the initial UI layout.
     */
    public SettingsScreen() {
        stage = new Stage();
        table = new Table();

        // Volume label
        volumeLabel = new Label("Volume: ", skin);
        volumeLabel.setColor(new Color(0.9f, 0.9f, 0.9f, 1.0f));

        // Volume slider
        volumeSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        volumeSlider.setValue(GlobalState.settings.getVolume()); // Set current volume
        volumeSlider.setPosition(150, 150);
        volumeSlider.setSize(200, 50);
        volumeSlider.addListener(event -> {
            // Adjust the game volume based on slider value
            GlobalState.settings.setVolume(volumeSlider.getValue());
            return false;
        });

        // Back button
        backButton = new TextButton("Back", skin);
        backButton.setPosition(150, 80);
        backButton.setSize(200, 60);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Go back to the start menu
                GlobalState.currentScreen = GlobalState.startScreen;
            }
        });

        // Add UI elements to stage
        table.setFillParent(true);
        table.center().center();
        table.pad(100, 100, 100, 100);
        table.add(backButton).center().width(250).height(67).padBottom(10);
        table.row();
        table.add(volumeLabel).center();
        table.row();
        table.add(volumeSlider).center().width(250).height(67);
        stage.addActor(table);

        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(GlobalState.UISecondaryColour);

        // Draw the stage containing the volume slider and buttons
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
