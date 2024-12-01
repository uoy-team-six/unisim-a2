package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.unisim.GlobalState;

/**
 * The start menu screen which presents the player with the option to start the
 * game
 * or access the settings menu.
 */
public class StartMenuScreen extends ScreenAdapter {
    private Stage stage;
    private Table table;
    private Skin skin;
    private TextButton playButton;
    private TextButton settingsButton;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    /**
     * Create a new StartMenuScreen and draw the initial UI layout.
     */
    public StartMenuScreen() {
        stage = new Stage();
        table = new Table();
        skin = GlobalState.defaultSkin;

        // Play button
        playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Switch to the game screen
                GlobalState.currentScreen = GlobalState.gameScreen;
            }
        });

        // Settings button
        settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Switch to the settings screen
                GlobalState.currentScreen = GlobalState.settingScreen;
            }
        });

        // Add UI elements to the stage
        table.setFillParent(true);
        table.center().center();
        table.pad(100, 100, 100, 100);
        table.add(playButton).center().width(250).height(100).padBottom(10);
        table.row();
        table.add(settingsButton).center().width(250).height(67);
        stage.addActor(table);

        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(GlobalState.UISecondaryColour);

        // Draw the stage containing buttons
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
