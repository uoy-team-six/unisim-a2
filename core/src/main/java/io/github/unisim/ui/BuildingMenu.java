package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.Point;
import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingType;
import io.github.unisim.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu used to place buildings in the world by clicking and dragging them
 * from the list onto the map.
 */
@SuppressWarnings({"MemberName", "AbbreviationAsWordInName"})
public class BuildingMenu {
    private final World world;
    private final GameLogic gameLogic;
    private final ShapeActor bar = new ShapeActor(GlobalState.UISecondaryColour);
    private final Table table;
    private final List<Building> buildings = new ArrayList<>();
    private final List<Image> buildingImages = new ArrayList<>();
    private final Label buildingInfoLabel = new Label(
        "", new Skin(Gdx.files.internal("ui/uiskin.json"))
    );
    private final Table buildingInfoTable = new Table();

    /**
     * Create a Building Menu and attach its actors and components to the provided stage.
     * Also handles drawing buildings and their flipped variants
     *
     * @param stage - The stage on which to draw the menu.
     */
    public BuildingMenu(Stage stage, World world, GameLogic gameLogic) {
        this.world = world;
        this.gameLogic = gameLogic;
        // Set building images and sizes
        buildings.add(new Building(
            new Texture(Gdx.files.internal("buildings/restaurant.png")),
            0.005f,
            new Vector2(1.1f, -0.2f),
            new Point(),
            new Point(6, 6),
            false,
            BuildingType.EATING,
            "Canteen",
            15000,
            0
        ));
        buildings.add(new Building(
            new Texture(Gdx.files.internal("buildings/library.png")),
            0.0075f,
            new Vector2(1.8f, -4.6f),
            new Point(),
            new Point(20, 12),
            false,
            BuildingType.LEARNING,
            "Library",
            30000,
            0
        ));
        buildings.add(new Building(
            new Texture(Gdx.files.internal("buildings/basketballCourt.png")),
            0.0025f,
            new Vector2(1f, -2.4f),
            new Point(),
            new Point(6, 9),
            false,
            BuildingType.RECREATION,
            "Basketball Court",
            10000,
            0
        ));
        buildings.add(new Building(
            new Texture(Gdx.files.internal("buildings/studentHousing.png")),
            0.108f,
            new Vector2(1.4f, -2.8f),
            new Point(),
            new Point(11, 11),
            false,
            BuildingType.SLEEPING,
            "Student Accommodation",
            12500,
            0
        ));
        buildings.add(new Building(
            new Texture(Gdx.files.internal("buildings/club.png")),
            0.005f,
            new Vector2(0.5f, 1.25f),
            new Point(),
            new Point(10, 10),
            false,
            BuildingType.RECREATION,
            "Club",
            15000,
            25000
        ));

        table = new Table();
        // Add buldings to the table
        for (int i = 0; i < buildings.size(); i++) {
            buildingImages.add(new Image(buildings.get(i).texture));
            final int buildingIndex = i;
            buildingImages.get(i).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    if (world.selectedBuilding == buildings.get(buildingIndex)) {
                        world.selectedBuilding = null;
                    } else {
                        world.selectedBuilding = buildings.get(buildingIndex);
                        int buildingPrice = gameLogic.getBuildingPrice(world.selectedBuilding);
                        buildingInfoLabel.setText(String.format("%s (£%d) - Press 'R' to rotate, 'Esc' to cancel",
                            world.selectedBuilding.name,
                            buildingPrice));
                        if (world.selectedBuilding.flipped) {
                            world.selectedBuilding.flipped = false;
                            int temp = world.selectedBuilding.size.x;
                            world.selectedBuilding.size.x = world.selectedBuilding.size.y;
                            world.selectedBuilding.size.y = temp;
                            world.selectedBuildingUpdated = true;
                        }
                    }
                }
            });
            table.add(buildingImages.get(i));
        }

        buildingInfoTable.add(buildingInfoLabel).expandX().align(Align.center);

        stage.addActor(bar);
        stage.addActor(table);
        stage.addActor(buildingInfoTable);
    }

    /**
     * Called when the window is resized, scales the building menu images with the window size.
     *
     * @param width  - The new width of the window in pixels
     * @param height - The new height of the window in pixels
     */
    @SuppressWarnings("unchecked")
    public void resize(int width, int height) {
        table.setBounds(0, 0, width, height * 0.1f);
        bar.setBounds(0, 0, width, height * 0.1f);
        buildingInfoTable.setBounds(0, height * 0.1f, width, height * 0.025f);

        // we must perform an unchecked type conversion here
        // this is acceptable as we know our table only contains instances of Actors
        for (Cell<Actor> cell : table.getCells()) {
            Image buildingImage = (Image) (cell.getActor());
            Vector2 textureSize = new Vector2(buildingImage.getWidth(), buildingImage.getHeight());
            cell.width(
                height * 0.1f * (textureSize.x < textureSize.y ? textureSize.x / textureSize.y : 1)
            ).height(
                height * 0.1f * (textureSize.y < textureSize.x ? textureSize.y / textureSize.x : 1)
            );
        }

        buildingInfoLabel.setFontScale(height * 0.0015f);
    }

    /**
     * Called when the building menu needs to be redrawn with new values in the labels.
     */
    public void update() {
        if (world.selectedBuilding == null) {
            buildingInfoLabel.setText("");
        }
    }
}
