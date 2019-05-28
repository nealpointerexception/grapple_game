package com.nealpointer.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nealpointer.game.screens.PlayScreen;

public class MainTester extends Game {
	SpriteBatch batch;
	AssetManager assetManager;





	@Override
	public void create () {
		assetManager = new AssetManager();
		assetManager.load("badlogic.jpg", Texture.class);
		batch = new SpriteBatch();

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
