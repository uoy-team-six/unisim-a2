package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Game {
  private World world = new World();
  private Timer timer = new Timer(10_000);
  private SpriteBatch uiBatch = new SpriteBatch();
  private BitmapFont font = new BitmapFont();
  private Camera uiCamera = new OrthographicCamera();
  private Viewport uiViewport = new ScreenViewport(uiCamera);
  private InputProcessor uiInputProcessor = new UIInputProcessor();
  private InputProcessor worldInputProcessor = new WorldInputProcessor(world);
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  public Game() {
    inputMultiplexer.addProcessor(uiInputProcessor);
    inputMultiplexer.addProcessor(worldInputProcessor);
    Gdx.input.setInputProcessor(inputMultiplexer);
    uiCamera.position.set(uiCamera.viewportWidth / 2, uiCamera.viewportHeight / 2, 0);
  }

  public void dispose() {
    uiBatch.dispose();
    world.dispose();
  }

  public void render() {
    timer.tick(Gdx.graphics.getDeltaTime() * 1000);

    world.render();

    uiViewport.apply();
    uiCamera.update();
    uiBatch.begin();
    if (timer.isRunning()) {
      font.draw(uiBatch, timer.getRemainingTime(), 10, 20);
    } else {
      font.draw(uiBatch, "Game Over!", 10, 20);
    }
    uiBatch.end();
  }

  public void resize(int width, int height) {
    world.resize(width, height);
    uiViewport.update(width, height);
    uiCamera.position.set(uiCamera.viewportWidth / 2, uiCamera.viewportHeight / 2, 0);
    uiBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
  }
}