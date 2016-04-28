package com.gs.livewallpaper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Main extends Base {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Texture texture2;
	private boolean touched;
	private Vector3 touch; // Not Vector2 because camera.unproject need Vector3 :\

	public Main(Game game, Resolver resolver) {
		super(game, resolver);

		// Never put "show" part here
	}

	@Override
	public void show() {
		Config.load();

		batch = new SpriteBatch(512);

		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		texture2 = new Texture(Gdx.files.internal("data/ic_launcher.png"));
		texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		touch = new Vector3();

		resetCamera();
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		texture2.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		resetCamera();
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isTouched()) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			touched = true;
		} else {
			touched = false;
		}

		draw(delta); // Main draw part

		//if (isAndroid)
		//	limitFPS();

		if (!isAndroid && Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();
	}

	private void draw(float delta) {
		super.render(delta);

		if (isAndroid && resolver != null) // In daydream resolver is null
			camera.position.x = (sW / 2) - resolver.getxPixelOffset();

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
			/*
			 * To see how wallpaper scroll with screen
			 * 
			 * As i said before - you can't get count of screens, so, just try use (sW * 2 or * 3)
			 * to remove empty space on the screen.
			 * It's easiest and only way.
			 */
			{
				batch.setColor(1f, 0f, 1f, 1f);
					for (int x = 0; x < (sW /* * 2/3 */ / texture2.getWidth()); x++)
						batch.draw(texture2, x * texture2.getWidth(), 0);
				batch.setColor(1f, 1f, 1f, 1f);
			}

			batch.setColor(1f, 1f, 0f, 1f);
			batch.draw(texture2, camera.position.x - texture2.getWidth() / 2, camera.position.y - texture2.getHeight() / 2);
			batch.setColor(1f, 1f, 1f, 1f);

			if (touched) {
				if (Config.checkBoxTest) {
					batch.setColor(1f, 0f, 0f, 1f);
				} else {
					batch.setColor(0f, 1f, 0f, 1f);
				}
	
				batch.draw(texture, touch.x - texture.getWidth() / 2, touch.y - texture.getHeight() / 2);
				batch.setColor(1f, 1f, 1f, 1f);
			}
		batch.end();
	}

	private void resetCamera() {
		camera = new OrthographicCamera(sW, sH);
		camera.setToOrtho(false, sW, sH);
		camera.position.set(sW / 2, sH / 2, 0);
	}
}
