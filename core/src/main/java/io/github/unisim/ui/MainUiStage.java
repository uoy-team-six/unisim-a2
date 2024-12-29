package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.screen.GameScreen;

public class MainUiStage extends Stage {
    private final GameLogic gameLogic;
    private final InfoBar infoBar;
    private final BuildingMenu buildingMenu;
    private final GameOverMenu gameOverMenu;

    private final GlyphLayout glyphLayout;
    private final Label achievementLabel;

    public MainUiStage(GameScreen gameScreen) {
        super(new ScreenViewport());
        gameLogic = gameScreen.getGameLogic();
        infoBar = new InfoBar(this, gameLogic, gameScreen.getWorld());
        buildingMenu = new BuildingMenu(this, gameScreen.getWorld(), gameLogic);
        gameOverMenu = new GameOverMenu(gameScreen.getGame());

        glyphLayout = new GlyphLayout();
        achievementLabel = new Label("", GlobalState.defaultSkin);
        addActor(achievementLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Update achievement label.
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

        // Update components.
        // TODO: These should be made into Actors.
        infoBar.update();
        buildingMenu.update();
    }

    @Override
    public void draw() {
        super.draw();

        // TODO: Separate into its own stage class.
        if (gameLogic.isGameOver()) {
            Gdx.input.setInputProcessor(gameOverMenu.getInputProcessor());
            gameOverMenu.render(Gdx.graphics.getDeltaTime());
        }
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        infoBar.resize(width, height);
        buildingMenu.resize(width, height);
        gameOverMenu.resize(width, height);
    }
}
