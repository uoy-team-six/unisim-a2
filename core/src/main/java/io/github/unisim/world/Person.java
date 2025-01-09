package io.github.unisim.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.unisim.building.Building;

public class Person {
    private final Vector2 position;
    private final Building targetBuilding;
    private final Matrix4 isoTransform;
    private float lifeTimer;
    private float animationTimer;
    private int spriteColumn;
    private int spriteRow;

    public Person(Vector2 position, Building targetBuilding, Matrix4 isoTransform) {
        this.position = position;
        this.targetBuilding = targetBuilding;
        this.isoTransform = isoTransform;
        lifeTimer = 60.0f;
    }

    public boolean update(float deltaTime) {
        lifeTimer -= deltaTime;
        if (lifeTimer <= 0.0f) {
            // Reached the maximum lifetime, signal deletion.
            return true;
        }

        Vector2 direction = targetBuilding.location.toVector().sub(position);

        // Check distance to target building. Signal deletion if close enough.
        if (direction.len() < 1.0f) {
            return true;
        }

        // Normalise vector to get a direction.
        direction.nor();

        // Calculate and angle in world (isometric) space.
        var worldDirection = new Vector3(direction, 0.0f).mul(isoTransform);
        float angle = new Vector2(worldDirection.x, worldDirection.y).angleDeg();

        // Round angle to nearest 45 degrees to calculate sprite index.
        spriteColumn = MathUtils.round(angle / 45.0f);

        // Move person in direction of target building.
        direction.scl(MathUtils.random(2.5f, 5.0f) * deltaTime);
        position.add(direction);

        animationTimer += deltaTime;
        if (animationTimer >= 0.5f) {
            animationTimer = 0.0f;
            spriteRow = spriteRow == 0 ? 2 : 0;
        }
        return false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getSpriteColumn() {
        return spriteColumn;
    }

    public int getSpriteRow() {
        return spriteRow;
    }
}
