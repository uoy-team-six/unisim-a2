package io.github.unisim.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.unisim.GameLogic;
import io.github.unisim.event.GameEvent;
import io.github.unisim.world.World;

public class EventDialog extends Dialog {
    private final GameLogic gameLogic;
    private final World world;

    private TextureRegionDrawable createRoundedDrawable(int width, int height, int cornerRadius, Color fillColor, Color borderColor) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        
        // Clear the pixmap with full transparency
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();

        int border = 3;
        
        // Draw the fill
        pixmap.setColor(fillColor);
        pixmap.fillRectangle(border, border, width - 2 * border, height - 2 * border);
        
        // Draw the border
        pixmap.setColor(borderColor);
        pixmap.fillRectangle(0, 0, width, border); // Top
        pixmap.fillRectangle(0, height - border, width, border); // Bottom
        pixmap.fillRectangle(0, 0, border, height); // Left
        pixmap.fillRectangle(width - border, 0, border, height); // Right
        
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        
        return drawable;
    }

    public EventDialog(GameEvent event, GameLogic gameLogic, World world, Skin skin) {
        super("", skin);
        this.gameLogic = gameLogic;
        this.world = world;

        // Cancel any building selection when dialog appears
        world.selectedBuilding = null;

        // Create a container table with custom background
        Table container = new Table();
        Color backgroundColor = new Color(0xFAF7F5FF);
        Color borderColor = new Color(0x144E96FF);
        TextureRegionDrawable background = createRoundedDrawable(500, 300, 30, backgroundColor, borderColor);
        container.setBackground(background);
        container.pad(30);

        // Style the title based on event type
        Label titleLabel = new Label(event.getName() + "!", skin);
        titleLabel.setAlignment(Align.center);
        Color titleColor = switch (event.getType()) {
            case POSITIVE -> new Color(0.2f, 0.8f, 0.2f, 1.0f);  // Softer green
            case NEGATIVE -> new Color(0.8f, 0.2f, 0.2f, 1.0f);  // Softer red
            default -> Color.BLACK;
        };
        titleLabel.setColor(titleColor);
        titleLabel.setFontScale(2.0f);
        container.add(titleLabel).expandX().center().padBottom(20).row();

        // Add a separator line
        Image separator = new Image(skin.getDrawable("white"));
        separator.setColor(titleColor);
        container.add(separator).height(3).expandX().fillX().pad(10).row();

        // Style the description
        Label descriptionLabel = new Label(event.getDescription(), skin);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setWrap(true);
        descriptionLabel.setColor(Color.BLACK);
        descriptionLabel.setFontScale(1.5f);
        container.add(descriptionLabel).width(450).pad(20).row();

        // Style the continue button
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.getLabel().setFontScale(1.5f);
        continueButton.pad(15, 40, 15, 40);
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
