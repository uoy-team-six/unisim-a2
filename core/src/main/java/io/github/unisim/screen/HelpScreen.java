package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.unisim.GameCursor;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;

public class HelpScreen extends ScreenAdapter {
    private final UniSimGame game;
    private final Stage stage;
    private final InputMultiplexer inputMultiplexer;

    public HelpScreen(UniSimGame game) {
        this.game = game;
        stage = new Stage();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);

        final var table = new Table();
        table.setFillParent(true);
        table.top();

        final var label = new Label("""
            Welcome to UniSim! Your objective is to maximise your score in a 5 minute
            timeframe by placing various campus buildings. The 5 minute time period
            is split into 3 years, each with a 20 second summer period followed by two
            40 second semesters. The game will start paused, and can be paused and
            resumed with the space bar. You will start with either £100,000, £75,000,
            or £50,000 depending on the difficulty. At the start of every semester of
            each year, each student will bring £1000 in tuition fees. Each accomodation
            building holds 25 students, which determines the total number of students.
            Your goal is to maximise student satisfaction by placing more buildings and
            keeping a balanced ratio of them. During gameplay, events may happen
            randomly, which will be displayed in the top right of the info bar with a
            tooltip explaining the effect of the event, which may be positive or negative!""", GlobalState.defaultSkin);
        label.setFontScale(2.5f);

        final var backButton = new TextButton("Back", GlobalState.defaultSkin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getStartMenuScreen());
            }
        });

        table.add(label).padTop(20.0f).padBottom(15.0f).row();
        table.add(backButton).width(350).height(67);
        stage.addActor(table);
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
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
