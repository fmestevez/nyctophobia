package com.gj.linterna;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

	public static boolean step = false;
	public static boolean heart = false;
	public static boolean grito = false;
	public static boolean click = false;

	static long heartId, doorId, gritoId, stepId, clickId;

	static Music music;
	static Sound soundDoor;
	static Music musicIntro;
	static Sound soundGrito;
	static Sound soundHeart;
	static Sound soundStep1;
	static Sound soundClick;

	public static void initInGame() {
		music = Gdx.audio.newMusic(Gdx.files.getFileHandle(
				"data/sounds/music.mp3", FileType.Internal));
		soundDoor = Gdx.audio.newSound(Gdx.files.getFileHandle(
				"data/sounds/outro.mp3", FileType.Internal));
		soundGrito = Gdx.audio.newSound(Gdx.files.getFileHandle(
				"data/sounds/grito.mp3", FileType.Internal));
		soundHeart = Gdx.audio.newSound(Gdx.files.getFileHandle(
				"data/sounds/heart.mp3", FileType.Internal));
		soundStep1 = Gdx.audio.newSound(Gdx.files.getFileHandle(
				"data/sounds/pasos.mp3", FileType.Internal));
		soundClick = Gdx.audio.newSound(Gdx.files.getFileHandle(
				"data/sounds/click.mp3", FileType.Internal));
	}
	
	public static void initMenu() {
		musicIntro = Gdx.audio.newMusic(Gdx.files.getFileHandle(
				"data/sounds/intro.mp3", FileType.Internal));
	}

	public static void playMusic() {
		music.setVolume(0.3f);
		music.play();
		music.setLooping(true);
	}

	public static void stopMusic() {
		music.stop();
	}

	public static void playDoor() {
		soundDoor.setVolume(0, 1);
		doorId = soundDoor.play();
	}

	public static void stopDoor() {
		soundDoor.stop(doorId);
	}

	public static void playStepOne() {
		if (!step) {
			stepId = soundStep1.play();
			soundStep1.setLooping(stepId, true);
			soundStep1.setVolume(stepId, 0.6f);
			step = true;
		}
	}

	public static void stopStepOne() {
		if (step) {
			soundStep1.stop(stepId);
			step = false;
		}
	}

	public static void playHeart() {
		if (!heart) {
			soundHeart.setVolume(3, 3.5f);
			heartId = soundHeart.loop();
			heart = true;
		}
	}

	public static void stopHeart() {
		if (heart) {
			soundHeart.stop(heartId);
			heart = false;
		}
	}

	public static void playGrito() {
		if (!grito) {
			soundGrito.setVolume(4, 3.5f);
			gritoId = soundGrito.play();
			grito = true;
		}
	}

	public static void stopGrito() {
		if (grito) {
			soundGrito.stop(gritoId);
			grito = false;
		}
	}

	public static void playClick() {
		if (!click) {
			soundClick.setVolume(4, 2.5f);
			clickId = soundClick.play();
			click = true;
		}
	}

	public static void stopClick() {
		if (click) {
			soundClick.stop(clickId);
			click = false;
		}
	}

	public static void playIntro() {
		musicIntro.play();
		musicIntro.setLooping(true);
		musicIntro.setVolume(1);
	}

	public static void stopIntro() {
		musicIntro.stop();
	}

	public static void setHeartPitch(float pitch) {
		soundHeart.setPitch(heartId, pitch);
	}
	
	public static void disposeInGame(){
		music.dispose();
		soundDoor.dispose();
		soundGrito.dispose();
		soundHeart.dispose();
		soundStep1.dispose();
		soundClick.dispose();
	}
	
	public static void disposeMenu(){
		musicIntro.dispose();
	}
}
