package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.unisim.GameCursor;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;

public class LeaderboardScreen extends ScreenAdapter {
    private final UniSimGame game;
    private final Stage stage;
    private final InputMultiplexer inputMultiplexer;
    private final Table scoreTable;

    public LeaderboardScreen(UniSimGame game) {
        this.game = game;
        stage = new Stage();
        inputMultiplexer = new InputMultiplexer();

        final var backButton = new TextButton("Back", GlobalState.defaultSkin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getStartMenuScreen());
            }
        });

        final var table = new Table();
        scoreTable = new Table();
        table.setFillParent(true);
        table.center().pad(100);
        table.add(backButton).bottom().width(350).height(67).row();
        table.add(new ScrollPane(scoreTable, GlobalState.defaultSkin)).expandX().fillX().padTop(10.0f);
        stage.addActor(table);

        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen.
        ScreenUtils.clear(GlobalState.UISecondaryColour);

        // Set the pointer cursor.
        game.setCursor(GameCursor.POINTER);

        // Draw the stage.
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Clear table and add spacing rows.
        scoreTable.clear();
        scoreTable.add().width(225);
        scoreTable.add().width(125);
        scoreTable.row();

        for (var name : game.getLeaderboard().getSortedNames()) {
            int score = game.getLeaderboard().getScore(name);
            var nameLabel = new Label(name, GlobalState.defaultSkin);
            var scoreLabel = new Label(String.valueOf(score), GlobalState.defaultSkin);
            nameLabel.setFontScale(2.0f);
            scoreLabel.setFontScale(2.0f);
            scoreTable.add(nameLabel).left().padLeft(5.0f);
            scoreTable.add(scoreLabel).right().padRight(5.0f).row();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
