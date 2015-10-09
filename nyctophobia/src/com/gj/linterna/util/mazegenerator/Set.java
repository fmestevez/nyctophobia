package com.gj.linterna.util.mazegenerator;

import java.util.Vector;

public class Set {
	
	public int id;
	public Vector<Cell> cells; // cantidad de elementos que tiene
	
	public Set(int id){
		this.cells = new Vector<Cell>();
		this.id = id;
	}
	
	/**
	 * Agrega una celda al conjunto
	 * @param cell
	 */
	public void addCell(Cell cell){
		this.cells.add(cell);
		cell.set = this;
	}
	
	/**
	 * Agrega un conjunto entero al conjunto
	 * @param set
	 */
	public void addSet(Set set){
		for (Cell cell : set.cells){
			this.addCell(cell);
		}
	}
	
	/**
	 * retorna la cantidad de elementos que tiene el conjunto
	 * @return
	 */
	public int getCount(){
		return this.cells.size();
	}

}
