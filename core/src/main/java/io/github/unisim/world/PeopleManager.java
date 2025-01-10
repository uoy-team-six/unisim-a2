package io.github.unisim.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PeopleManager {
    private final BuildingManager buildingManager;
    private final Matrix4 isoTransform;
    private final List<Person> people;
    private final Texture spritesheet;
    private float spawnTimer = 0.0f;

    public PeopleManager(BuildingManager buildingManager, Matrix4 isoTransform) {
        this.buildingManager = buildingManager;
        this.isoTransform = isoTransform;
        people = new ArrayList<>();
        spritesheet = new Texture(Gdx.files.internal("people.png"));
    }

    private int calculateMaxPeopleCount() {
        // Scale the maximum number of people as a function of the number of buildings.
        final int buildingCount = buildingManager.getBuildings().size();
        if (buildingCount < 2) {
            return 0;
        }
        return buildingCount * 5;
    }

    private float calculateSpawnRate() {
        final int buildingCount = buildingManager.getBuildings().size();
        return 3.0f / buildingCount;
    }

    private Building getRandomBuilding() {
        var building = buildingManager.getBuildings().get(ThreadLocalRandom.current().nextInt(0, buildingManager.getBuildings().size()));
        if (building == buildingManager.getPreviewBuilding()) {
            return null;
        }
        return building;
    }

    /**
     * Renders people on the world.
     *
     * @param deltaTime the delta time between the last call of render
     * @param batch     a suitable sprite batch to render with
     */
    public void render(float deltaTime, SpriteBatch batch) {
        spawnTimer += deltaTime;
        if (spawnTimer >= calculateSpawnRate() && people.size() < calculateMaxPeopleCount()) {
            spawnTimer = 0.0f;

            // Pick a random source and target building.
            final var sourceBuilding = getRandomBuilding();
            final var targetBuilding = getRandomBuilding();
            if (sourceBuilding != null && targetBuilding != null && sourceBuilding != targetBuilding) {
                people.add(new Person(sourceBuilding.location.toVector(), targetBuilding, isoTransform));
            }
        }

        var iter = people.iterator();
        while (iter.hasNext()) {
            Person person = iter.next();
            if (person.update(deltaTime)) {
                // Person has expired.
                iter.remove();
                continue;
            }

            var position = new Vector3(person.getPosition(), 0);
            position.mul(isoTransform);

            int spriteX = person.getSpriteColumn() * 16;
            int spriteY = person.getSpriteRow() * 24;
            var region = new TextureRegion(spritesheet, spriteX, spriteY, 16, 24);
            batch.draw(region, position.x, position.y, 1.0f, 1.5f);
        }
    }
}
