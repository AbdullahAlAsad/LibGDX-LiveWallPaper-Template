package com.gs.livewallpaper;

import com.badlogic.gdx.Game;

public class LWP extends Game {
	public Resolver resolver = null;

	@Override
	public void create() {
		setScreen(new Main(this, resolver));
	}

	@Override
	public void dispose () {
		super.dispose();
		resolver = null;
		getScreen().dispose();
	}
}
