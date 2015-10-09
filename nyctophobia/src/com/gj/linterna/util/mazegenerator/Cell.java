package com.gj.linterna.util.mazegenerator;

public class Cell {
	
	public int column;
	public int row;
	public Set set; // conjunto al que pertenece
	public int setCount; // cantidad de elementos en este set
	
	public Cell(int row, int column, int set){
		this.row = row;
		this.column = column;
		this.set = new Set(set);
		this.set.addCell(this);
	}

}
