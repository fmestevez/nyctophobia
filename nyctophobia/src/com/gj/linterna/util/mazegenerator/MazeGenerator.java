package com.gj.linterna.util.mazegenerator;

import java.util.Vector;

public class MazeGenerator {
		
	public int[][] generateMaze(int totalRow, int totalCol, int batteries){
		int[][] ret = new int[totalRow][totalCol];
		Vector<Wall> walls = new Vector<Wall>();
		Cell[][] cells = new Cell[totalRow][totalCol];
		int set = 0; // conjuntos que se asignaran a las celdas
		
		for (int fila = 0 ; fila < totalRow ; fila++){
			for (int col = 0 ; col < totalCol ; col++){
				if (fila % 2 == 0 && col % 2 == 0){
					ret[fila][col] = 0;
					cells[fila][col] = new Cell(fila, col, set);
					set++;
				}
			}
		}
		
		for (int fila = 0 ; fila < totalRow ; fila++){
			for (int col = 0 ; col < totalCol ; col++){
				if (!(fila % 2 == 0 && col % 2 == 0)){
					ret[fila][col] = 1;
					if (fila % 2 == 0){
						walls.add(new Wall(fila, col, cells[fila][col-1], cells[fila][col+1]));
						set += 2;
					}else if (col % 2 == 0){
						walls.add(new Wall(fila, col, cells[fila-1][col], cells[fila+1][col]));
						set += 2;
					}
				}
			}
		}
		
		
		// reviso la pared en particular
		Wall wall;
		while(walls.size() > 0){
			wall = walls.get((int)(Math.random() * walls.size()));
			if (wall.adjacent1.set.id != wall.adjacent2.set.id){
				ret[wall.row][wall.column] = 0;
				wall.adjacent1.set.addSet(wall.adjacent2.set);
			}
			walls.remove(wall);
		}
		
		// agregamos baterias
				// buscamos todos los posibles lugares
				Vector<Cell> posiblesCells = new Vector<Cell>();
				
				for (int fila = 0 ; fila < totalRow ; fila++){
					for (int col = 0 ; col < totalCol ; col++){
						if (fila % 2 == 0 && col % 2 == 0){
							try{
								if (ret[fila+1][col] + ret[fila-1][col] + ret[fila][col-1] + ret[fila][col+1] == 3){
									posiblesCells.add(cells[fila][col]);
								}
							}catch(ArrayIndexOutOfBoundsException e){
								//
							}
						}
					}
				}
				
				for (int i = 0 ; i < batteries ; i++){
					Cell cell = posiblesCells.get((int)(Math.random() * posiblesCells.size()));
					ret[cell.row][cell.column] = 12;
					posiblesCells.remove(cell);
					if (posiblesCells.size() == 0) break;
				}
		
		return ret;
	}

}
