package com.spacewar.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;



public class SpaceWar extends ApplicationAdapter {
	
	SpriteBatch batch;
	Texture img, tShip, tMissile, tEnemy;
	private Sprite ship, missile;
	private float posX, posY, velocity, xMissile, yMissile;
	private boolean attack;
	private Array<Rectangle> enemies;
	private long lastEnemyTime;
	
	@Override
	public void create () {	
		batch = new SpriteBatch();
		img = new Texture("bg.png");
		tShip = new Texture("spaceship.png");
		ship = new Sprite(tShip);
		
		tMissile = new Texture("missile.png");
		missile = new Sprite(tMissile);
		
		tEnemy = new Texture("enemy.png");
		enemies = new Array<Rectangle>();
		
		posX = 0;
		posY = 0;
		velocity = 10;
		
		xMissile = posX;
		yMissile = posY;
		
		attack = false;
		
		lastEnemyTime = 0;
	}

	@Override
	public void render () {
		
		this.moveShip();
		this.moveMissile();
		this.moveEnemies();
		
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		
		if(attack) {
			batch.draw(missile, xMissile + ship.getWidth()/2, yMissile + ship.getHeight()/2 - 12);
		}

		batch.draw(ship, posX, posY);
		
		for(Rectangle enemy : enemies) {
			batch.draw(tEnemy, enemy.x, enemy.y);
		}
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tShip.dispose();
	}
	
	private void moveShip() {
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if(posX < Gdx.graphics.getWidth() - ship.getWidth()) {
				posX += velocity;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if(posX > 0) {
				posX -= velocity;				
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if(posY < Gdx.graphics.getHeight() - ship.getHeight()) {
				posY += velocity;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if(posY > 0) {
				posY -= velocity;
			}
		}
	}
	
	private void moveMissile() {
		
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !attack) {
			attack = true;
			yMissile = posY;
		} 
		
		if(attack) {
			if(xMissile < Gdx.graphics.getWidth()) {
				xMissile += 40     ;
			}else {
				xMissile = posX;
				attack = false;
			}
		}else {
			xMissile = posX;
			yMissile = posY;
		}
		
	}
	
	private void spawnEnemies() {
		Rectangle enemy = new Rectangle(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight() - tEnemy.getHeight()), tEnemy.getWidth(), tEnemy.getHeight());
		enemies.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}
	
	private void moveEnemies() {
		
		if(TimeUtils.nanoTime() - lastEnemyTime > 1000000000) {
			this.spawnEnemies();
		}
		
		for(Iterator<Rectangle> iter = enemies.iterator(); iter.hasNext();) {
			Rectangle enemy = iter.next();
			enemy.x -= 10;
			if(enemy.x + tEnemy.getWidth() < 0) {
				iter.remove();
			}
		}
	}
}
