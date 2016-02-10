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
package workflow;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import jfxwfutils.Event;
import jfxwfutils.Historic;
import jfxwfutils.Historic.Operation;

import components.DoubleClickable;
import components.multiOption.Menu;
import components.multiOption.Operations;

public class WorkflowBase<R extends Node> {
	
	protected final R root;
	public R getRoot(){ return root; }
	
	public final Event<Object> stateEvent;
	private Object state;
	public Object getState(){ return state; }
	public void setState(Object state){ 
		this.state = state;
		stateEvent.trigger(state);
	}
	
	public final Event<MouseEvent> doubleClickEvent;

	public final Operations operations;
	protected final WorkflowConfigurator config;
	protected final Historic historic;
	
	
	public WorkflowBase(R root, Object state, WorkflowConfigurator config, Historic historic){
		this.root = root;
		this.state = state;
		this.config = config;
		this.historic = historic;
		this.stateEvent = new Event<>();
		this.doubleClickEvent = new Event<>();
		this.operations = new Operations();
		new DoubleClickable<>(root, doubleClickEvent::trigger).mount();
		new Menu<>(root, operations).mount();
	}
	
	protected void addToHistoric(Operation operation){
		if(config.getPermitUndo())
			historic.add(operation);
	}

}
