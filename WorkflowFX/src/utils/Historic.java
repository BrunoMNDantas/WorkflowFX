/*-
 * Copyright (c) 2016, NGSPipes Team <ngspipes@gmail.com>
 * All rights reserved.
 *
 * This file is part of NGSPipes <http://ngspipes.github.io/>.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package utils;

import java.util.LinkedList;

public class Historic {

	public static class Operation{
		public final Runnable undo;
		public final Runnable redo;

		public Operation(Runnable undo, Runnable redo){
			this.undo = undo;
			this.redo = redo;
		}
	}

	private final LinkedList<Operation> undos = new LinkedList<>();
	private final LinkedList<Operation> redos = new LinkedList<>();
	private final boolean permitsRedo;
	
	public Historic(boolean permitsRedo){
		this.permitsRedo = permitsRedo;
	}
	
	public Historic(){
		this(true);
	}
	

	public void undo() {
		if(undos.size() == 0)
			return;

		Operation operation = undos.removeFirst();
		operation.undo.run();
		
		if(permitsRedo)
			redos.addFirst(operation);
	}

	public void redo() {
		if(redos.size() == 0)
			return;

		Operation operation = redos.removeFirst();
		operation.redo.run();
		undos.addFirst(operation);
	}

	public void add(Operation operation){
		undos.addFirst(operation);
		redos.clear();
	}

	public void clear() {
		undos.clear();
		redos.clear();
	}
	
}