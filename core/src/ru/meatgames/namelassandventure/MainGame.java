package ru.meatgames.namelassandventure;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

public class MainGame extends ApplicationAdapter implements InputProcessor {

	private MapObjectsDB mMapObjectsDB_;
	private CreaturesDB mCreatureDB_;
	private CreaturesController mCreatureController_;
	private Map mMap_;
	private ArrayList<Model> mModels_;
	private ArrayList<ModelInstance> mModelInstances_;

	public PerspectiveCamera cam;
	public ModelBatch modelBatch;
	public Environment environment;
	private Quaternion mQuaternionLeft, mQuaternionRight;

	private Timer mTimer;
	private float gridStep = 5.0f;
	private float angle = 0;
	private float walkingDistance = gridStep;
	private float walkingAnimationSteps = 20.0f;
	private float walkingAnimationDistance = walkingDistance / walkingAnimationSteps;
	private float turningAngle = 90.0f;
	private float turningAnimationSteps = 18.0f;
	private float turningAnimationAngle = turningAngle / turningAnimationSteps;
	private float delay = 0;
	private float animationTime = 0.3f;
	private float walkingAnimationInterval = animationTime / walkingAnimationSteps;
	private float turningAnimationInterval = animationTime / turningAnimationSteps;

	private int playerX = 3;
	private int playerY = 3;

	Stage stage;
	ImageButton forwardButton, backwardButton, turnLeftButton, turnRightButton, strafeLeftButton, strafeRightButton;
	BitmapFont font;
	Skin skin;
	private TextureAtlas buttonAtlas;

	private void loadResources() {
		mMapObjectsDB_ = MapObjectsDB.getInstance();
		mMapObjectsDB_.addObject(new MapObject(0, false), new Texture("wall.png"));

		mCreatureDB_ = CreaturesDB.getInstance();
		mCreatureController_ = new CreaturesController();
	}

	private void newGame() {
		mMap_ = new Map(7, 7);
		mMap_.fillArea(0, 0, 7, 7, mMapObjectsDB_.WALL);
		mMap_.fillArea(1, 1, 1, 5, mMapObjectsDB_.EMPTY);
		mMap_.fillArea(3, 1, 1, 5, mMapObjectsDB_.EMPTY);
		mMap_.fillArea(5, 1, 1, 5, mMapObjectsDB_.EMPTY);
		mMap_.fillArea(1, 1, 5, 1, mMapObjectsDB_.EMPTY);
		mMap_.fillArea(1, 3, 5, 1, mMapObjectsDB_.EMPTY);
		mMap_.fillArea(1, 5, 5, 1, mMapObjectsDB_.EMPTY);

		mModels_ = new ArrayList<Model>();
		mModelInstances_ = new ArrayList<ModelInstance>();
		Vector3 b = new Vector3(0, 1, 0);
		ModelBuilder modelBuilder = new ModelBuilder();

		for (int i=0;i<mMap_.getSize();i++) {
			if (mMap_.getObject(i) != null) {
				mModels_.add(modelBuilder.createBox(gridStep, gridStep, gridStep,
						new Material(),
						VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates));
				mModelInstances_.add(new ModelInstance(mModels_.get(mModels_.size() - 1)));
				mModelInstances_.get(mModelInstances_.size() - 1).transform.translate(gridStep * (i % mMap_.getWidth()), gridStep * (i / mMap_.getHeight()), 0f).rotate(b, 90);
				mModelInstances_.get(mModelInstances_.size() - 1).materials.first().set(TextureAttribute.createDiffuse(mMapObjectsDB_.getTexture(0)));
			}
		}
	}

	@Override
	public void create () {
		loadResources();
		newGame();

		stage = new Stage();
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.txt"));
		skin.addRegions(buttonAtlas);

		forwardButton = new ImageButton(skin.getDrawable("forward"));
		forwardButton.setSize(100f, 100f);
		forwardButton.getImageCell().expand().fill();
		forwardButton.setPosition(Gdx.graphics.getWidth() / 2 - 50f, 110);
		forwardButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (mTimer.isEmpty()) {
					final int playerNextX = (int) Math.cos(Math.toRadians(angle));
					final int playerNextY = (int) Math.sin(Math.toRadians(angle));
					if (mMap_.getObject(playerX + playerNextX + (playerY + playerNextY) * mMap_.getWidth()) == null) {
						mTimer.scheduleTask(new Timer.Task() {
							@Override
							public void run() {
								cam.position.add(playerNextX * walkingAnimationDistance, playerNextY * walkingAnimationDistance, 0);
								cam.update();
							}
						}, delay, walkingAnimationInterval, (int) (walkingAnimationSteps - 1f));
						playerX += (int) Math.cos(Math.toRadians(angle));
						playerY += (int) Math.sin(Math.toRadians(angle));
					}
				}
			}
		});

		backwardButton = new ImageButton(skin.getDrawable("backward"));
		backwardButton.setSize(100f, 100f);
		backwardButton.getImageCell().expand().fill();
		backwardButton.setPosition(Gdx.graphics.getWidth() / 2 - 50f, 5);
		backwardButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (mTimer.isEmpty()) {
					final int playerNextX = (int) Math.cos(Math.toRadians(angle));
					final int playerNextY = (int) Math.sin(Math.toRadians(angle));
					if (mMap_.getObject(playerX - playerNextX + (playerY - playerNextY) * mMap_.getWidth()) == null) {
						mTimer.scheduleTask(new Timer.Task() {
							@Override
							public void run() {
								cam.position.add(playerNextX * -1 * walkingAnimationDistance, playerNextY * -1 * walkingAnimationDistance, 0);
								cam.update();
							}
						}, delay, walkingAnimationInterval, (int) (walkingAnimationSteps - 1f));
						playerX -= (int) Math.cos(Math.toRadians(angle));
						playerY -= (int) Math.sin(Math.toRadians(angle));
					}
				}
			}
		});

		strafeLeftButton = new ImageButton(skin.getDrawable("strafeLeft"));
		strafeLeftButton.setSize(100f, 100f);
		strafeLeftButton.getImageCell().expand().fill();
		strafeLeftButton.setPosition(Gdx.graphics.getWidth() / 4 - 50f, 5);
		strafeLeftButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (mTimer.isEmpty()) {
					final float tempAngle = (angle <= 0) ? 360f + angle - 90f : angle - 90f;
					final int playerNextX = (int) Math.cos(Math.toRadians(tempAngle));
					final int playerNextY = (int) Math.sin(Math.toRadians(tempAngle));
					if (mMap_.getObject(playerX + playerNextX + (playerY + playerNextY) * mMap_.getWidth()) == null) {
						mTimer.scheduleTask(new Timer.Task() {
							@Override
							public void run() {
								cam.position.add(playerNextX * walkingAnimationDistance, playerNextY * walkingAnimationDistance, 0);
								cam.update();
							}
						}, delay, walkingAnimationInterval, (int) (walkingAnimationSteps - 1f));
						playerX += (int) Math.cos(Math.toRadians(tempAngle));
						playerY += (int) Math.sin(Math.toRadians(tempAngle));
					}
				}
			}
		});

		strafeRightButton = new ImageButton(skin.getDrawable("strafeRight"));
		strafeRightButton.setSize(100f, 100f);
		strafeRightButton.getImageCell().expand().fill();
		strafeRightButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4 - 50f, 5);
		strafeRightButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (mTimer.isEmpty()) {
					final float tempAngle = (angle >= 360f) ? 360f - angle + 90f : angle + 90f;
					final int playerNextX = (int) Math.cos(Math.toRadians(tempAngle));
					final int playerNextY = (int) Math.sin(Math.toRadians(tempAngle));
					if (mMap_.getObject(playerX + playerNextX + (playerY + playerNextY) * mMap_.getWidth()) == null) {
						mTimer.scheduleTask(new Timer.Task() {
							@Override
							public void run() {
								cam.position.add(playerNextX * walkingAnimationDistance, playerNextY * walkingAnimationDistance, 0);
								cam.update();
							}
						}, delay, walkingAnimationInterval, (int) (walkingAnimationSteps - 1f));
						playerX += (int) Math.cos(Math.toRadians(tempAngle));
						playerY += (int) Math.sin(Math.toRadians(tempAngle));
					}
				}
			}
		});

		turnLeftButton = new ImageButton(skin.getDrawable("turnLeft"));
		turnLeftButton.setSize(100f, 100f);
		turnLeftButton.getImageCell().expand().fill();
		turnLeftButton.setPosition(Gdx.graphics.getWidth() / 4 - 50f, 110);
		turnLeftButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (mTimer.isEmpty()) {
					mTimer.scheduleTask(new Timer.Task() {
						@Override
						public void run() {
							cam.rotate(mQuaternionLeft);
							cam.update();
						}
					}, delay, turningAnimationInterval, (int) (turningAnimationSteps - 1f));
					angle = (angle <= 0) ? 360f + angle - 90f : angle - 90f;
				}
			}
		});

		turnRightButton = new ImageButton(skin.getDrawable("turnRight"));
		turnRightButton.setSize(100f, 100f);
		turnRightButton.getImageCell().expand().fill();
		turnRightButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4 - 50f, 110);
		turnRightButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (mTimer.isEmpty()) {
					mTimer.scheduleTask(new Timer.Task() {
						@Override
						public void run() {
							cam.rotate(mQuaternionRight);
							cam.update();
						}
					}, delay, turningAnimationInterval, (int) (turningAnimationSteps - 1f));
					angle = (angle >= 360f) ? 360f - angle + 90f : angle + 90f;
				}
			}
		});

		stage.addActor(forwardButton);
		stage.addActor(backwardButton);
		stage.addActor(strafeLeftButton);
		stage.addActor(strafeRightButton);
		stage.addActor(turnLeftButton);
		stage.addActor(turnRightButton);
		Gdx.input.setInputProcessor(stage);

		mTimer = new Timer();
		mQuaternionLeft = new Quaternion(new Vector3(0, 0, 1), (int) -turningAnimationAngle);
		mQuaternionRight = new Quaternion(new Vector3(0, 0, 1), (int) turningAnimationAngle);
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		modelBatch = new ModelBatch();
		cam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(gridStep * playerX, gridStep * playerY, 0.0f);
		cam.lookAt(gridStep * playerX + 10f, gridStep * playerY, 0);
		Vector3 a = new Vector3(1, 0, 0);
		Vector3 b = new Vector3(0, 1, 0);
		cam.rotate(a, -90);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();

		for (Model m : mModels_)
			m.dispose();
		for (Texture t : mMapObjectsDB_.getTextures())
			t.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.gl.glScissor(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glScissor(0, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);

		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		for (ModelInstance m : mModelInstances_)
			modelBatch.render(m, environment);
		modelBatch.end();

		stage.draw();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
