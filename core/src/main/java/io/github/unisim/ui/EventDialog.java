package io.github.unisim.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import io.github.unisim.GameLogic;
import io.github.unisim.event.EventType;
import io.github.unisim.event.GameEvent;

public class EventDialog extends Dialog {
    private final GameLogic gameLogic;

    public EventDialog(GameEvent event, GameLogic gameLogic, Skin skin) {
        super("", skin);
        this.gameLogic = gameLogic;

        // Create a container table for better padding and background
        Table container = new Table();
        container.setBackground(skin.getDrawable("window"));
        container.pad(20);

        // Style the title based on event type
        Label titleLabel = new Label(event.getName() + "!", skin);
        titleLabel.setAlignment(Align.center);
        Color titleColor = switch (event.getType()) {
            case POSITIVE -> new Color(0.2f, 0.8f, 0.2f, 1.0f);  // Softer green
            case NEGATIVE -> new Color(0.8f, 0.2f, 0.2f, 1.0f);  // Softer red
            default -> Color.WHITE;
        };
        titleLabel.setColor(titleColor);
        titleLabel.setFontScale(1.5f);
        container.add(titleLabel).expandX().center().padBottom(30).row();

        // Add a separator line
        Image separator = new Image(skin.getDrawable("white"));
        separator.setColor(titleColor);
        container.add(separator).height(2).expandX().fillX().pad(10).row();

        // Style the description
        Label descriptionLabel = new Label(event.getDescription(), skin);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setWrap(true);
        descriptionLabel.setColor(new Color(0.9f, 0.9f, 0.9f, 1.0f));  // Light gray
        container.add(descriptionLabel).width(450).pad(20).row();

        // Style the continue button
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.pad(10, 30, 10, 30);
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameLogic.unpause();
                hide();
                remove();
            }
        });
        container.add(continueButton).padTop(20);

        getContentTable().add(container);

        // Don't block input to the rest of the game
        setModal(false);
    }

    @Override
    public Dialog show(Stage stage) {
        super.show(stage);
        // Size and position the dialog after it's added to the stage
        pack();
        setPosition(
            (stage.getWidth() - getWidth()) / 2,
            (stage.getHeight() - getHeight()) / 2
        );
        return this;
    }
} 