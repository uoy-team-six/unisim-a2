package io.github.unisim.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;

public class GameOverUiStage extends Stage {
    private final GameLogic gameLogic;
    private final Table table;
    private final Label gameOverLabel;
    private final Label satisfactionLabel;
    private final Label campusValueLabel;
    private final Label finalScoreLabel;
    private final TextField nameTextField;
    private final Cell<TextField> nameTextFieldCell;
    private final Cell<TextButton> buttonCell;
    private int finalScore;

    public GameOverUiStage(UniSimGame game, GameLogic gameLogic) {
        super(new ScreenViewport());
        this.gameLogic = gameLogic;
        table = new Table();
        table.setFillParent(true);

        // Play button.
        final var mainMenuButton = new TextButton("Return to Main Menu", GlobalState.defaultSkin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Switch to the start menu screen.
                game.setScreen(game.getStartMenuScreen());

                // Add leaderboard entry if player has entered a name.
                if (!nameTextField.getText().isBlank()) {
                    game.getLeaderboard().addScore(nameTextField.getText(), finalScore);
                }
            }
        });

        gameOverLabel = new Label("Game Over!", GlobalState.defaultSkin);
        satisfactionLabel = new Label("", GlobalState.defaultSkin);
        campusValueLabel = new Label("", GlobalState.defaultSkin);
        finalScoreLabel = new Label("", GlobalState.defaultSkin);
        table.add(gameOverLabel).top().row();
        table.add(satisfactionLabel).left().row();
        table.add(campusValueLabel).left().row();
        table.add(finalScoreLabel).left().row();

        nameTextField = new TextField("", GlobalState.defaultSkin);
        nameTextField.setMaxLength(20);
        nameTextField.setMessageText("Enter name here");
        nameTextFieldCell = table.add(nameTextField);
        table.row();

        buttonCell = table.add(mainMenuButton).bottom();
        addActor(table);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        final var satisfactionScore = MathUtils.round(gameLogic.getSatisfaction() * 20000.0f);
        final var satisfactionText = String.format("Satisfaction: +%d (%d%%)", satisfactionScore, gameLogic.getSatisfactionPercentage());

        int campusValue = 0;
        for (var building : gameLogic.getWorld().getBuildingManager().getBuildings()) {
            campusValue += building.price;
        }
        final var campusValueScore = campusValue / 10;
        final var campusValueText = String.format("Campus Value: +%d (£%d)", campusValueScore, campusValue);

        finalScore = satisfactionScore + campusValueScore;
        final var finalScoreText = String.format("Final Score: %d", finalScore);

        satisfactionLabel.setText(satisfactionText);
        campusValueLabel.setText(campusValueText);
        finalScoreLabel.setText(finalScoreText);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        table.setBounds(0, 0, width, height * 0.1f);
        gameOverLabel.setFontScale(height * 0.006f);
        satisfactionLabel.setFontScale(height * 0.004f);
        campusValueLabel.setFontScale(height * 0.004f);
        finalScoreLabel.setFontScale(height * 0.004f);
        nameTextFieldCell.width(width * 0.3f).height(height * 0.05f).padTop(height * 0.01f).padBottom(height * 0.01f);
        buttonCell.width(width * 0.3f).height(height * 0.1f);
    }
}
