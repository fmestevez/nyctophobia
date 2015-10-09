package com.gj.linterna.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gj.linterna.Battery;
import com.gj.linterna.Exit;
import com.gj.linterna.Player;

public class WorldCreator {

	private World world;
	private Texture floor;
	private List<Sprite> floors = new ArrayList<Sprite>();
	private ArrayList<Battery> batteries = new ArrayList<Battery>();
	private Exit exit;
	
	private Player player; // para las luces, si, si se que no deberia

	public void createWorld(World world, int[][] maze, Player player) {
		this.world = world;
		this.player = player;
		
		floor = new Texture(Gdx.files.internal("data/floortile.jpg"));
		
		int exitX = (int)(Math.random() * (maze.length-1) * 0.5) * 2;
//		this.exit = new Exit(Vector2(xPosition * 8, maze.length * 4));
		
		for (int i = 0; i < maze.length; i++) {
			createWall(i, -1);
			createWall(-1, i);
			if (i == exitX) {
				createExit(maze.length, i);
			}else{
				createWall(i, maze.length);
			}
			createWall(maze.length, i);
		}
		
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				final float size = 4f;
				final float offset = 2.2f;
				switch(maze[i][j]){
				case 1:
					createWall(i, j);
					break;
				case 0:
					Sprite sprite = new Sprite(floor);
					sprite.setSize(size, size);
//					sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
					sprite.setPosition((i * size)-offset, (j * size)-offset);
					floors.add(sprite);
					break;
				case 12:
					Sprite sp = new Sprite(floor);
					sp.setSize(size, size);
//					sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
					sp.setPosition((i * size)-offset, (j * size)-offset);
					floors.add(sp);
					Battery bat = new Battery(new Vector2((i * size)-(offset/2), (j * size)-(offset/2)), world);
					batteries.add(bat);
				}
			}
		}
	}

	/**
	 * Crea un cuadrado del laberinto
	 */
	private Body createWall(int row, int column) {
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		Body wall = this.world.createBody(def);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(2f, 2f);
		wall.createFixture(poly, 0);
		poly.dispose();

		wall.setTransform(new Vector2(row * 4, column * 4), 0);

		return wall;
	}
	
	/**
	 * Crea el exit y una lampara
	 * @return
	 */
	public void createExit(int row, int column){
		this.exit = new Exit(new Vector2(column * 4, row * 4));
		this.player.createLight(column * 4, row * 4 + 8, -90);
	}

	public List<Sprite> getFloors() {
		return floors;
	}
	
	public ArrayList<Battery> getBatteries(){
		return batteries;
	}
	
	public Exit getExit(){
		return this.exit;
	}

}