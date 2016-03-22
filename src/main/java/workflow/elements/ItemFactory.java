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
package workflow.elements;

import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import jfxwfutils.Historic;
import workflow.Elements;
import workflow.WorkflowConfigurator;

import components.ConnectArea;
import components.connect.Coordinates;
import components.shortcut.Keys;
import components.shortcut.Shortcut;

public class ItemFactory {

	private final Elements elements;
	private final WorkflowConfigurator config;
	private final Historic historic;

	
	protected ItemFactory(Elements elements, WorkflowConfigurator config, Historic historic){
		this.elements = elements;
		this.config = config;
		this.historic = historic;
	}
	
	
	
	public WorkflowItem create(Node graphic, Object state, Coordinates pos){
		WorkflowItem item = new WorkflowItem(config, historic, graphic, state, pos);
		mount(item);
		return item;
	}
	
	public WorkflowItem create(Node graphic, Coordinates pos){
		WorkflowItem item = new WorkflowItem(config, historic, graphic, pos);
		mount(item);
		return item;
	}
	
	public WorkflowItem create(Object state, Coordinates pos){
		WorkflowItem item = new WorkflowItem(config, historic, state, pos);
		mount(item);
		return item;
	}

	public WorkflowItem create(Coordinates pos) {
		WorkflowItem item = new WorkflowItem(config, historic, pos);
		mount(item);
		return item;
	}

	public WorkflowItem create(Node graphic, Object state){
		WorkflowItem item = new WorkflowItem(config, historic, graphic, state);
		mount(item);
		return item;
	}
	
	public WorkflowItem create(Node graphic){
		WorkflowItem item = new WorkflowItem(config, historic, graphic);
		mount(item);
		return item;
	}
	
	public WorkflowItem create(Object state){
		WorkflowItem item = new WorkflowItem(config, historic, state);
		mount(item);
		return item;
	}

	public WorkflowItem create() {
		WorkflowItem item = new WorkflowItem(config, historic);
		mount(item);
		return item;
	}
	
	
	
	private void mount(WorkflowItem item){
		mountRemoveShortcuts(item);
		mountConnectArea(item);
	}

	private void mountRemoveShortcuts(WorkflowItem item) {
		if(!config.getPermitRemoveItemShortcut())
			return;
		
		Keys keys = new Keys();
		keys.add(KeyCode.DELETE, ()->elements.removeItem(item));
		keys.add(KeyCode.BACK_SPACE, ()->elements.removeItem(item));
		new Shortcut<Node>(item.getRoot(), keys).mount();
	}
	
	private void mountConnectArea(WorkflowItem item) {
		if(!config.getPermitConnectibleItems())
			return;
		
		Function<WorkflowItem, Boolean> onConnectionReceived = (otherItem)->{
			return elements.addConnection(config.getDefaultConnectionFactory().apply(otherItem, item));
		}; 
		
		new ConnectArea<>(item.getRoot(), config.getDefaultConnectImage(), onConnectionReceived, null, item).mount();
	}
	
}
