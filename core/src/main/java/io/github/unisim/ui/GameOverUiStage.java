package io.github.unisim.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;

public class GameOverUiStage extends Stage {
    private final Table table;
    private final Label gameOverLabel;
    private final Cell<TextButton> buttonCell;

    public GameOverUiStage(UniSimGame game) {
        super(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);

        // Play button.
        final var mainMenuButton = new TextButton("Return to Main Menu", GlobalState.defaultSkin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Switch to the start menu screen.
                game.setScreen(game.getStartMenuScreen());
            }
        });

        gameOverLabel = new Label("Game Over!", GlobalState.defaultSkin);
        table.add(gameOverLabel).top();
        table.row();

        // Add UI elements to the stage.
        buttonCell = table.add(mainMenuButton).bottom();
        addActor(table);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        table.setBounds(0, 0, width, height * 0.1f);
        gameOverLabel.setFontScale(height * 0.006f);
        buttonCell.width(width * 0.3f).height(height * 0.1f);
    }
}
