package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.Random;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	ShapeRenderer sr;
	Texture img;
	float shapeX;
	float shapeY;
	float charX = 100;
	float height = 100;
	int speed = 400;
	int speed1 = 400;
	boolean upPressed = false;
	float counterTrue = 0;
	float counterFalse = 0;
	float counter = 0;
	float maxHeight = 199;
	int obLength = 50;
	int obHeight = 50;
	float health = 10;
	Texture jungle;
	boolean hasDied = false;
	boolean hasColided = false;
	boolean leftAlready = false;
	boolean rightAlready = false;
	long colTime;
	float charH = 100;
	float barLength = 100;
	float curFloorLev = 100;
	long healthUp = System.currentTimeMillis();
	BitmapFont font;

	@Override
	public void create() {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		font = new BitmapFont();
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		font.draw(batch, "Health remaining:", 10, 470);
		batch.end();

		checkLevel();

		if (((healthUp + 4000) < System.currentTimeMillis()) && health < 9.75) {
			health += 0.25;
			healthUp = System.currentTimeMillis();
		}

		barLength = health * 10;
		sr.setColor(0, 0, 1, 0);
		sr.begin(ShapeType.Filled);
		sr.rect(10, 445, barLength, 10);
		sr.end();

		sr.begin(ShapeType.Filled);
		sr.rect(350, 300, 100, 20);
		sr.end();
		
		sr.begin(ShapeType.Filled);
		sr.rect(100, 200, 100, 20);
		sr.end();
		
		sr.begin(ShapeType.Filled);
		sr.rect(100, 400, 100, 20);
		sr.end();

		double delta = Gdx.graphics.getDeltaTime();

		System.out.println(height);

		if (shapeY >= 600) {
			shapeY = 0;

		}

		shapeX -= speed * delta;
		if (shapeX < -400) {
			shapeX = 750;
			hasColided = false;

			obLength = ((getInt() + 1) * 30);
			obHeight = ((getInt() + 1) * 25);
			speed = ((getInt() + 1) * 250);

		}

		if ((System.currentTimeMillis() < (colTime + 500)) && (getInt() < 3)) {
			sr.setColor(1, 0, 0, 0);
		} else {

			sr.setColor(1, 1, 0, 0);

		}
		sr.begin(ShapeType.Filled);
		sr.rect(charX, height, 100, charH);
		sr.end();

		if (height == curFloorLev) {
			leftAlready = false;
			rightAlready = false;
		}

		left();
		right();
		up();
		down();

		if (checkCol() == true) {
			health -= 1;
			checkHealth();

		}

		if (hasDied == true) {
			death();
			batch.begin();
			font.draw(batch, "YOU DIED!", 10, 450);
			batch.end();
		}

		sr.setColor(1, 1, 1, 0);
		sr.begin(ShapeType.Line);
		sr.line(0, 100, 20000, 100);
		sr.end();

		
		
		sr.setColor(1, 0, 0, 0);
		sr.begin(ShapeType.Filled);
		sr.rect(shapeX, 100, obLength, obHeight);

		sr.end();

		if (height < curFloorLev) {
			height = curFloorLev;
		}

		if (shapeX == 750 || shapeX == -50) {
			counter = 0;
		}

		if (height == curFloorLev) {
			counter = 0;
		}

		counterTrue += 0.3;
		counterFalse += 11;
		if (height == curFloorLev) {
			counterFalse = 0;

		}

		if (height > maxHeight) {
			maxHeight = height;
		}

	}

	@Override
	public void dispose() {
		batch.dispose();

	}

	public void up() {
		upPressed = false;
		double delta = Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.W) && height >= curFloorLev) {
			counter += 1;
			counterTrue = 0;

			upPressed = true;
			height += (speed1 - counterFalse) * delta;

		}
	}

	public void down() {
		double delta = Gdx.graphics.getDeltaTime();
		if (height > curFloorLev && upPressed == false) {
			counterFalse = 0;
			height -= speed1 * counterTrue * 0.1 * delta;

		}

	}

	public void left() {

		if (((Gdx.input.isKeyPressed(Keys.A) && (height == curFloorLev)) || (leftAlready == true)) && charX > 0) {
			charX -= 5;
			leftAlready = true;
		}

	}

	public void right() {
		if (((Gdx.input.isKeyPressed(Keys.D) && (height == curFloorLev)) || (rightAlready == true)) && charX < 540) {
			charX += 5;
			rightAlready = true;
		}
	}

	public boolean checkCol() {
		if ((height < (100 + obHeight)) && (shapeX < (charX + 100)) && (shapeX > (charX - obLength))
				&& hasColided == false) {
			hasColided = true;
			colTime = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}

	}

	public void checkLevel() {
		if (((charX + 100) > 350) && (charX < 450) && (height >= 300) && (height <= 320)) {
			curFloorLev = 320;
		} else {
			curFloorLev = 100;
		}
		if (((charX + 100) > 100) && (charX < 200) && (height >= 200) && (height <= 220)) {
			curFloorLev = 220;
		}
		if (((charX + 100) > 100) && (charX < 200) && (height >= 400) && (height <= 420)) {
			curFloorLev = 420;
		}
		if (curFloorLev != 100) {
			counterTrue = 0;
		} else {
			curFloorLev = 100;
		}
	}

	public void checkHealth() {
		if (health < 5) {
		}
		if (health <= 0) {
			hasDied = true;
			barLength = 0;
		}
	}

	static int getInt() {
		Random direction = new Random();
		int n = direction.nextInt(4);
		return n;
	}

	public void death() {
		shapeX = 800;
		charH -= (3);
		if (charH < 0) {
			charH = 0;
		}
	}

}
