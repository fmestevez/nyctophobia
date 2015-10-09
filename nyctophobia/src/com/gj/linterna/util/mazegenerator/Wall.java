package com.gj.linterna.util.mazegenerator;

public class Wall {
	
	public int column;
	public int row;
	public boolean open; // indica si es trancitable o no
	public Cell adjacent1; // celdas adyacentes que unen la pared
	public Cell adjacent2; // celdas adyacentes que unen la pared
	
	public Wall(int row, int column, Cell adj1, Cell adj2){
		this.row = row;
		this.column = column;
		this.adjacent1 = adj1;
		this.adjacent2 = adj2;
	}

}
