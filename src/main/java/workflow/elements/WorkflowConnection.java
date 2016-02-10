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

import javafx.scene.shape.Line;
import jfxwfutils.Historic;
import workflow.WorkflowBase;
import workflow.WorkflowConfigurator;

import components.connect.connector.Connector;

public class WorkflowConnection extends WorkflowBase<Line> {

	private final Connector connector;
	public Connector getConnector(){ return connector; }
	
	private final WorkflowItem initItem;
	public WorkflowItem getInitItem(){ return initItem; }
	
	private final WorkflowItem endItem;
	public WorkflowItem getEndItem(){ return endItem; }
	
	
	// COSTRUCTORS
	
	protected WorkflowConnection(WorkflowConfigurator config, Historic historic, Connector connector, Object state, WorkflowItem initItem, WorkflowItem endItem){
		super(connector.getNode(), state, config, historic);
		this.connector = connector;
		this.initItem = initItem;
		this.endItem = endItem;
	}
	
	protected WorkflowConnection(WorkflowConfigurator config, Historic historic, Object state, WorkflowItem initItem, WorkflowItem endItem){
		this(config, historic, config.getDefaultConnectorSupplier().get(), state, initItem, endItem);
	}
	
	protected WorkflowConnection(WorkflowConfigurator config, Historic historic, Connector connector, WorkflowItem initItem, WorkflowItem endItem){
		this(config, historic, connector, config.getDefaultConnectionStateSupplier().get(), initItem, endItem);
	}
	
	protected WorkflowConnection(WorkflowConfigurator config, Historic historic, WorkflowItem initItem, WorkflowItem endItem){
		this(config, historic, config.getDefaultConnectorSupplier().get(), config.getDefaultConnectionStateSupplier().get(), initItem, endItem);
	}
	
}
