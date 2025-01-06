package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GlobalState;
import io.github.unisim.Score;
import io.github.unisim.UniSimGame;

public class GameOverUiStage extends Stage {
    private final Table table;
    private final Label gameOverLabel;
    private final Table breakdownTable;
    private final Cell<?> breakdownLeftSpacer;
    private final Cell<?> breakdownRightSpacer;
    private final TextField nameTextField;
    private final Cell<TextField> nameTextFieldCell;
    private final Cell<TextButton> buttonCell;
    private int finalScore;

    public GameOverUiStage(UniSimGame game) {
        super(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);

        // Play button.
        final var mainMenuButton = new TextButton("Return to Main Menu", GlobalState.defaultSkin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to the start menu screen.
                game.setScreen(game.getStartMenuScreen());

                // Add leaderboard entry if player has entered a name.
                if (!nameTextField.getText().isBlank()) {
                    game.getLeaderboard().addScore(nameTextField.getText(), finalScore);
                }
            }
        });

        gameOverLabel = new Label("Game Over!", GlobalState.defaultSkin);
        table.add(gameOverLabel).top().row();

        breakdownTable = new Table();
        breakdownLeftSpacer = breakdownTable.add();
        breakdownRightSpacer = breakdownTable.add();
        breakdownTable.row();
        table.add(breakdownTable).expandX().fillX().row();

        nameTextField = new TextField("", GlobalState.defaultSkin);
        nameTextField.setMaxLength(20);
        nameTextField.setMessageText("Enter name here");
        nameTextFieldCell = table.add(nameTextField);
        table.row();

        buttonCell = table.add(mainMenuButton).bottom();
        addActor(table);
    }

    public void onGameOver(Score score) {
        // Store final score for the leaderboard entry button.
        finalScore = score.totalScore();

        final var satisfactionText = String.format("+%d (%d%%)", score.satisfactionScore(), score.satisfactionPercentage());
        final var campusValueText = String.format("+%d (£%d)", score.campusValueScore(), score.campusValue());

        breakdownTable.add(new Label("Satisfaction:", GlobalState.defaultSkin)).left().padLeft(5.0f);
        breakdownTable.add(new Label(satisfactionText, GlobalState.defaultSkin)).right().padRight(5.0f).row();
        breakdownTable.add(new Label("Campus Value:", GlobalState.defaultSkin)).left().padLeft(5.0f);
        breakdownTable.add(new Label(campusValueText, GlobalState.defaultSkin)).right().padRight(5.0f).row();
        for (var achievement : score.unlockedAchievements()) {
            breakdownTable.add(new Label(achievement.getName() + ":", GlobalState.defaultSkin)).left().padLeft(5.0f);
            breakdownTable.add(new Label(String.format("+%d", achievement.getScore()), GlobalState.defaultSkin)).right().padRight(5.0f).row();
        }
        breakdownTable.add(new Label("Final Score:", GlobalState.defaultSkin)).left().padLeft(5.0f);
        breakdownTable.add(new Label(String.valueOf(score.totalScore()), GlobalState.defaultSkin)).right().padRight(5.0f).row();

        // Force a resize after adding new elements.
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        table.setBounds(0, 0, width, height * 0.1f);
        gameOverLabel.setFontScale(height * 0.006f);
        nameTextFieldCell.width(width * 0.75f).height(height * 0.05f).padTop(height * 0.01f).padBottom(height * 0.01f);
        buttonCell.width(width * 0.75f).height(height * 0.1f);

        // Set column width for breakdown table.
        breakdownLeftSpacer.width(width * 0.45f);
        breakdownRightSpacer.width(width * 0.3f);

        // Size all labels in the breakdown table.
        for (var label : breakdownTable.getChildren()) {
            ((Label) label).setFontScale(height * 0.004f);
        }
    }
}
