package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameCursor;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;
import io.github.unisim.ui.BuildingMenu;
import io.github.unisim.ui.GameOverMenu;
import io.github.unisim.ui.InfoBar;
import io.github.unisim.world.UiInputProcessor;
import io.github.unisim.world.World;
import io.github.unisim.world.WorldInputProcessor;

/**
 * Game screen where the main game is rendered and controlled.
 * Supports pausing the game with a pause menu.
 */
public class GameScreen extends ScreenAdapter {
    private final UniSimGame game;
    private final World world = new World();
    private final GameLogic gameLogic = new GameLogic(world);
    private final Stage stage = new Stage(new ScreenViewport());
    private final InfoBar infoBar;
    private final BuildingMenu buildingMenu;
    private final InputProcessor uiInputProcessor = new UiInputProcessor(stage);
    private final WorldInputProcessor worldInputProcessor = new WorldInputProcessor(world, gameLogic);
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private final GameOverMenu gameOverMenu;
    private final GlyphLayout glyphLayout;
    private final Label achievementLabel;

    /**
     * Constructor for the GameScreen.
     */
    public GameScreen(UniSimGame game) {
        this.game = game;
        infoBar = new InfoBar(stage, gameLogic, world);
        buildingMenu = new BuildingMenu(stage, world, gameLogic);
        gameOverMenu = new GameOverMenu(game);

        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(uiInputProcessor);
        inputMultiplexer.addProcessor(worldInputProcessor);

        glyphLayout = new GlyphLayout();
        achievementLabel = new Label("", GlobalState.defaultSkin);
        stage.addActor(achievementLabel);
    }

    @Override
    public void render(float deltaTime) {
        // Set cursor.
        if (world.isZoomingIn()) {
            game.setCursor(GameCursor.ZOOM_IN);
        } else if (world.isZoomingOut()) {
            game.setCursor(GameCursor.ZOOM_OUT);
        } else if (worldInputProcessor.isPanning()) {
            game.setCursor(GameCursor.HAND_OPEN);
        } else {
            game.setCursor(GameCursor.POINTER);
        }

        final var displayAchievement = gameLogic.getRecentlyUnlockedAchievement();
        achievementLabel.setVisible(displayAchievement != null);
        if (displayAchievement != null) {
            achievementLabel.setText(String.format("Achievement Unlocked: %s", displayAchievement.getName()));

            achievementLabel.setFontScale(Gdx.graphics.getHeight() * 0.003f);
            glyphLayout.setText(achievementLabel.getStyle().font, achievementLabel.getText());

            float textWidth = glyphLayout.width * achievementLabel.getFontScaleX() / 2.0f;
            float textHeight = glyphLayout.height * achievementLabel.getFontScaleY();
            achievementLabel.setPosition(Gdx.graphics.getWidth() / 2.0f - textWidth, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.05f + textHeight + 10.0f));
        }

        world.render();
        gameLogic.update(deltaTime);

        if (gameLogic.isGameOver()) {
            Gdx.input.setInputProcessor(gameOverMenu.getInputProcessor());
        }

        stage.act(deltaTime);
        infoBar.update();
        buildingMenu.update();
        stage.draw();
        if (gameLogic.isGameOver()) {
            world.zoom((world.getMaxZoom() - world.getZoom()) * 2f);
            world.pan((150 - world.getCameraPos().x) / 10, -world.getCameraPos().y / 10);
            gameOverMenu.render(deltaTime);
        }
    }

    @Override
    public void resize(int width, int height) {
        world.resize(width, height);
        stage.getViewport().update(width, height, true);
        infoBar.resize(width, height);
        buildingMenu.resize(width, height);
        gameOverMenu.resize(width, height);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void pause() {
        // Pause game timer if game loses focus.
        gameLogic.pause();
    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
    }
}
