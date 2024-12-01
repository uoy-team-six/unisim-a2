package io.github.unisim.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;

/**
 * Menu that is displayed when the timer has run out. This is where the final score
 * will be calculated in future.
 */
public class GameOverMenu {
    private final Stage stage;
    private final Skin skin;
    private final ShapeActor bar = new ShapeActor(GlobalState.UISecondaryColour);
    private final Table table;
    private final TextButton mainMenuButton;
    private final Cell<TextButton> buttonCell;
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    /**
     * Creates a new GameOverMenu and initialises all events and UI elements used in the menu.
     */
    public GameOverMenu(UniSimGame game) {
        stage = new Stage(new ScreenViewport());
        table = new Table();
        skin = GlobalState.defaultSkin;

        // Play button
        mainMenuButton = new TextButton("Return to Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Switch to the start menu screen.
                game.setScreen(game.getStartMenuScreen());
            }
        });

        // Add UI elements to the stage
        buttonCell = table.add(mainMenuButton).center();
        stage.addActor(bar);
        stage.addActor(table);

        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when the game window is resized and we need to adjust the scale of the UI elements.
     *
     * @param width  - The new game window width in pixels
     * @param height - The new game window height in pixels
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.setBounds(0, 0, width, height * 0.1f);
        bar.setBounds(0, 0, width, height * 0.1f);
        buttonCell.width(width * 0.3f).height(height * 0.1f);
    }

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }
}
