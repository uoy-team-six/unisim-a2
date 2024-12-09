package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.building.BuildingType;
import io.github.unisim.world.World;

/**
 * Create a Title bar with basic info.
 */
public class InfoBar {
    private ShapeActor bar;
    private Table infoTable = new Table();
    private Table titleTable = new Table();
    private Table buildingCountersTable = new Table();
    private Label[] buildingCounterLabels = new Label[4];
    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    private Label satisfactionLabel = new Label("86%", skin);
    private Label periodLabel = new Label("", skin);
    private Label moneyLabel = new Label("", skin);
    private Label titleLabel = new Label("UniSim", skin);
    private Label timerLabel;
    private Texture pauseTexture = new Texture("ui/pause.png");
    private Texture playTexture = new Texture("ui/play.png");
    private Image pauseImage = new Image(pauseTexture);
    private Image playImage = new Image(playTexture);
    private final GameLogic gameLogic;
    private Cell<Label> timerLabelCell;
    private Cell<Label> satisfactionLabelCell;
    private Cell<Image> pauseButtonCell;
    private Cell<Table> buildingCountersTableCell;
    private Cell<Label> periodLabelCell;
    private Cell<Label> moneyLabelCell;
    private Cell[] buildingCounterCells;
    private World world;

    /**
     * Create a new infoBar and draws its' components onto the provided stage.
     *
     * @param stage - The stage on which to draw the InfoBar.
     */
    public InfoBar(Stage stage, GameLogic gameLogic, World world) {
        this.gameLogic = gameLogic;
        this.world = world;
        buildingCounterCells = new Cell[4];

        // Building counter table
        for (int i = 0; i < 4; i++) {
            buildingCounterLabels[i] = new Label("", skin);
        }
        buildingCounterCells[0] = buildingCountersTable.add(buildingCounterLabels[0]);
        buildingCounterCells[1] = buildingCountersTable.add(buildingCounterLabels[1]);
        buildingCountersTable.row();
        buildingCounterCells[2] = buildingCountersTable.add(buildingCounterLabels[2]);
        buildingCounterCells[3] = buildingCountersTable.add(buildingCounterLabels[3]);

        // Info Table
        timerLabel = new Label("", skin);
        infoTable.center().center();
        pauseButtonCell = infoTable.add(playImage).align(Align.center);
        timerLabelCell = infoTable.add(timerLabel).align(Align.center);
        periodLabelCell = infoTable.add(periodLabel).align(Align.center);
        moneyLabelCell = infoTable.add(moneyLabel).align(Align.center);
        satisfactionLabelCell = infoTable.add(satisfactionLabel).align(Align.center);
        buildingCountersTableCell = infoTable.add(buildingCountersTable).expandX().align(Align.right);

        // Pause button
        pauseImage.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                gameLogic.pause();
            }
        });

        // Play button
        playImage.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                gameLogic.unpause();
            }
        });

        titleTable.add(titleLabel).expandX().align(Align.center);

        bar = new ShapeActor(GlobalState.UIPrimaryColour);
        stage.addActor(bar);
        stage.addActor(infoTable);
        stage.addActor(titleTable);
    }

    /**
     * Called when the UI needs to be updated, usually on every frame.
     */
    public void update() {
        // Update timer label.
        float remainingTime = gameLogic.getRemainingTime();
        int remainingMinutes = (int) (remainingTime / 60.0f);
        int remainingSeconds = ((int) remainingTime) % 60;
        timerLabel.setText(String.format("%d:%02d", remainingMinutes, remainingSeconds));

        // Update period label.
        if (gameLogic.isSummer()) {
            periodLabel.setText(String.format("Year %d Summer", gameLogic.getYear()));
        } else {
            periodLabel.setText(String.format("Year %d Semester %d", gameLogic.getYear(), gameLogic.getSemester()));
        }

        // Update money and satisfaction labels.
        moneyLabel.setText(String.format("£%d", gameLogic.getMoney()));
        satisfactionLabel.setText(String.format("%d%%", MathUtils.ceil(gameLogic.getSatisfaction() * 100.0f)));

        buildingCounterLabels[0].setText("Recreation: "
            + Integer.toString(world.getBuildingCount(BuildingType.RECREATION)));
        buildingCounterLabels[1].setText("Learning: "
            + Integer.toString(world.getBuildingCount(BuildingType.LEARNING)));
        buildingCounterLabels[2].setText("Eating: "
            + Integer.toString(world.getBuildingCount(BuildingType.EATING)));
        buildingCounterLabels[3].setText("Sleeping: "
            + Integer.toString(world.getBuildingCount(BuildingType.SLEEPING)));
        pauseButtonCell.setActor(gameLogic.isPaused() ? playImage : pauseImage);
    }

    /**
     * Update the bounds of the background & table actors to fit the new size of the screen.
     *
     * @param width  - The new width of the screen in pixels.
     * @param height - The enw height of the screen in pixels.
     */
    public void resize(int width, int height) {
        bar.setBounds(0, height * 0.95f, width, height * 0.05f);
        infoTable.setBounds(0, height * 0.95f, width, height * 0.05f);
        titleTable.setBounds(0, height * 0.95f, width, height * 0.05f);

        float counterTableWidth = height * 0.27f;
        buildingCountersTableCell.width(counterTableWidth).height(height * 0.05f);
        for (int i = 0; i < 4; i++) {
            buildingCounterLabels[i].setFontScale(height * 0.0015f);
            buildingCounterCells[i].width(counterTableWidth * 0.5f).height(height * 0.025f);
        }

        timerLabel.setFontScale(height * 0.002f);
        timerLabelCell.width(height * 0.08f).height(height * 0.05f);
        timerLabelCell.padLeft(height * 0.005f);
        periodLabel.setFontScale(height * 0.002f);
        periodLabelCell.width(height * 0.24f).height(height * 0.05f);
        moneyLabel.setFontScale(height * 0.002f);
        moneyLabelCell.width(height * 0.16f).height(height * 0.05f);
        satisfactionLabel.setFontScale(height * 0.002f);
        satisfactionLabelCell.width(height * 0.04f).height(height * 0.05f);
        pauseButtonCell.width(height * 0.03f).height(height * 0.03f)
            .padLeft(height * 0.01f).padRight(height * 0.01f);

        titleLabel.setFontScale(height * 0.003f);
    }
}
