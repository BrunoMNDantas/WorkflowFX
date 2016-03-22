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

import jfxwfutils.Historic;
import jfxwfutils.Historic.Operation;
import workflow.elements.WorkflowConnection;
import workflow.elements.WorkflowItem;

import java.util.*;

public class Elements {

	private final Map<WorkflowItem, Collection<WorkflowConnection>> connectionsFrom = new HashMap<>();
	private final Map<WorkflowItem, Collection<WorkflowConnection>> connectionsTo = new HashMap<>();
	private final Map<Object, WorkflowItem> workflowItems = new HashMap<>();
	private final Map<Object, WorkflowConnection> workflowConnections = new HashMap<>();
	private final Collection<WorkflowItem> items = new LinkedList<>();
	private final Collection<WorkflowConnection> connections = new LinkedList<>();

	public final Events events;
	private final Historic historic;
	private final WorkflowConfigurator config;



	public Elements(WorkflowConfigurator config, Historic historic){
		this.config = config;
		this.historic = historic;
		this.events = new Events();
	}



	public boolean containsItem(Object key){
		return workflowItems.containsKey(key);
	}

	public boolean containsItem(WorkflowItem item){
		return containsItem(config.getWorkflowItemKeyExtractor().apply(item));
	}

	public WorkflowItem getItem(Object key){
		return workflowItems.get(key);
	}
	
	public boolean containsConnection(Object key){
		return workflowConnections.containsKey(key);
	}

	public boolean containsConnection(WorkflowConnection connection){
		return containsConnection(config.getWorkflowConnectionKeyExtractor().apply(connection));
	}

	public WorkflowConnection getConnection(Object key){
		return workflowConnections.get(key);
	}
	
	public boolean hasConnectionsFrom(WorkflowItem item){
		return connectionsFrom.containsKey(item);
	}
	
	public boolean hasConnectionsFrom(Object key){
		return connectionsFrom.containsKey(getItem(key));
	}
	
	public boolean hasConnectionsTo(WorkflowItem item){
		return connectionsTo.containsKey(item);
	}
	
	public boolean hasConnectionsTo(Object key){
		return connectionsTo.containsKey(getItem(key));
	}
	
	public boolean hasConnections(WorkflowItem item){
		return hasConnectionsFrom(item) || hasConnectionsTo(item);
	}
	
	public boolean hasConnections(Object key){
		return hasConnectionsFrom(key) || hasConnectionsTo(key);
	}
	
	public boolean hasConnectionsFromAndTo(WorkflowItem item){
		return hasConnectionsFrom(item) && hasConnectionsTo(item);
	}
	
	public boolean hasConnectionsFromAndTo(Object key){
		return hasConnectionsFrom(key) && hasConnectionsTo(key);
	}
		
	public Collection<WorkflowItem> getItems(){
		return new LinkedList<>(items);
	}

	public Collection<WorkflowConnection> getConnections(){
		return new LinkedList<>(connections);
	}

	public Map<WorkflowItem, Collection<WorkflowConnection>> getConnectionsFrom(){
		return connectionsFrom;
	}
	 
	public Map<WorkflowItem, Collection<WorkflowConnection>> getConnectionsTo(){
		return connectionsTo;
	}

	public Collection<WorkflowConnection> getConnectionsFrom(WorkflowItem item){
		return connectionsFrom.get(item);
	}
	
	public Collection<WorkflowConnection> getConnectionsFrom(Object key){
		return connectionsFrom.get(getItem(key));
	}
	
	public Collection<WorkflowConnection> getConnectionsTo(WorkflowItem item){
		return connectionsTo.get(item);
	}
	
	public Collection<WorkflowConnection> getConnectionsTo(Object key){
		return connectionsTo.get(getItem(key));
	}
	
	public Collection<WorkflowConnection> getConnectionsOf(WorkflowItem item){
		List<WorkflowConnection> connections =  new LinkedList<>();
		connections.addAll(connectionsFrom.get(item));
		connections.addAll(connectionsTo.get(item));
		return connections;
	}
	
	public Collection<WorkflowConnection> getConnectionsOf(Object key){
		return getConnectionsOf(getItem(key));
	}
	


	
	private void addToHistoric(Operation operation){
		if(config.getPermitUndo())
			historic.add(operation);
	}


	
	// ADD

	private boolean internalAdd(WorkflowItem item){
		if(!validateAddition(item))
			return false;

		addItemDependencies(item);
		events.addItem.trigger(item);

		return true;
	}

	public boolean addItem(WorkflowItem item){
		if(!internalAdd(item))
			return false;

		Operation operation = new Operation(()->internalRemove(item), ()->internalAdd(item));
		addToHistoric(operation);
		
		return true;
	}

	private boolean validateAddition(WorkflowItem item){
		if(!config.getItemAdditionValidator().apply(item))
			return false;

		return !items.contains(item) && config.getPermitItemAddition();
	}

	private void addItemDependencies(WorkflowItem item){
		items.add(item);

		Object key = this.config.getWorkflowItemKeyExtractor().apply(item);
		workflowItems.put(key, item);

		connectionsFrom.put(item, new LinkedList<>());
		connectionsTo.put(item, new LinkedList<>());
	}
	
	

	// REMOVE

	private boolean internalRemove(WorkflowItem item){
		if(!validateRemoval(item))
			return false;

		removeItemDepedencies(item);
		events.removeItem.trigger(item);

		return true;	
	}

	public boolean removeItem(WorkflowItem item){
		Collection<WorkflowConnection> connectionOfItem = getConnectionsOf(item);
		
		if(!internalRemove(item))
			return false;
		
		Operation operation = new Operation(()->{
			internalAdd(item);
			connectionOfItem.forEach(this::internalConnect);
		}, ()-> internalRemove(item));
		
		addToHistoric(operation);

		return true;
	}

	public boolean removeItem(Object key){
		if(!workflowItems.containsKey(key))
			return false;

		return removeItem(workflowItems.get(key));
	}

	private boolean validateRemoval(WorkflowItem item){
		if(!config.getItemRemovalValidator().apply(item))
			return false;

		return items.contains(item) && config.getPermitItemRemoval();
	}

	private void removeItemDepedencies(WorkflowItem item){
		List<WorkflowConnection> connections =  new LinkedList<>();
		connections.addAll(connectionsFrom.get(item));
		connections.addAll(connectionsTo.get(item));
		connections.forEach(this::internalDisconnect);

		items.remove(item);

		Object key = this.config.getWorkflowItemKeyExtractor().apply(item);
		workflowItems.remove(key);

		connectionsFrom.remove(item);
		connectionsTo.remove(item);
	}



	// CONNECT

	private boolean internalConnect(WorkflowConnection connection){
		if(!validateConnection(connection))
			return false;

		addConnectionDepedencies(connection);
		events.connect.trigger(connection);

		return true;
	}

	public boolean addConnection(WorkflowConnection connection){
		if(!internalConnect(connection))
			return false;

		Operation operation = new Operation(()->internalDisconnect(connection), ()->internalConnect(connection));
		addToHistoric(operation);

		return true;
	}

	private boolean validateConnection(WorkflowConnection connection){
		if(!config.getConnectionAdditionValidator().apply(connection))
			return false;
		
		if(connections.contains(connection) || !config.getPermitConnectionAddition())
			return false;
 
		return 	validateElementsExistence(connection) &&
				validateCircularConnection(connection) &&
				validateMultipleConnections(connection) &&
				validateSimilarConnections(connection);		
	}
	
	private boolean validateElementsExistence(WorkflowConnection connection){
		WorkflowItem init = connection.getInitItem();
		WorkflowItem end = connection.getEndItem();
		
		return items.contains(init) && items.contains(end);
	}
	
	private boolean validateCircularConnection(WorkflowConnection connection){
		WorkflowItem init = connection.getInitItem();
		WorkflowItem end = connection.getEndItem();
		
		if(init==end)
			return this.config.getPermitItemSelfConnection();

		if(!config.getPermitCircularConnection() && canReach(init, end))
			return false;

		return true;
	}
	
	private boolean canReach(WorkflowItem item, WorkflowItem from){
		if(item == from)
			return true;
		
		for(WorkflowConnection connection : connectionsFrom.get(from))
			if(connection.getEndItem()!=from && canReach(item, connection.getEndItem()))
				return true;
		
		return false;
	}

	private boolean validateMultipleConnections(WorkflowConnection connection){
		Collection<WorkflowConnection> connectionsFromInit = connectionsFrom.get(connection.getInitItem());
		if(!connectionsFromInit.isEmpty() && !config.getPermitMultipleConnectionsFromItem())
			return false;
		
		Collection<WorkflowConnection> connectionsToEnd = connectionsTo.get(connection.getEndItem());

		if(!connectionsToEnd.isEmpty() && !config.getPermitMultipleConnectionsToItem())
			return false;

		return true;
	}

	private boolean validateSimilarConnections(WorkflowConnection connection){
		if(config.getPermitSimilarConnections())
			return true;
		
		WorkflowItem end = connection.getEndItem();
		for(WorkflowConnection con : connectionsFrom.get(connection.getInitItem()))
			if(con.getEndItem() == end)
				return false;
			
		return true;
	}
	
	private void addConnectionDepedencies(WorkflowConnection connection){
		connections.add(connection);

		Object key = this.config.getWorkflowConnectionKeyExtractor().apply(connection);
		workflowConnections.put(key, connection);

		connectionsFrom.get(connection.getInitItem()).add(connection);
		connectionsTo.get(connection.getEndItem()).add(connection);
	}



	// DISCONNECT

	private boolean internalDisconnect(WorkflowConnection connection){
		if(!validateDisconnection(connection))
			return false;

		removeConnectionDepedencies(connection);
		events.disconnect.trigger(connection);

		return true;
	}

	public boolean removeConnection(WorkflowConnection connection){
		if(!internalDisconnect(connection))
			return false;

		Operation operation = new Operation(()->internalConnect(connection), ()->internalDisconnect(connection));
		addToHistoric(operation);

		return true;
	}

	public boolean removeConnection(Object key){
		if(!workflowConnections.containsKey(key))
			return false;

		return removeConnection(workflowConnections.get(key));
	}

	private boolean validateDisconnection(WorkflowConnection connection){
		if(!config.getConnectionRemovalValidator().apply(connection))
			return false;

		return connections.contains(connection) && config.getPermitConnectionRemoval();
	}

	private void removeConnectionDepedencies(WorkflowConnection connection){
		connections.remove(connection);

		Object key = this.config.getWorkflowConnectionKeyExtractor().apply(connection);
		workflowConnections.remove(key);

		connectionsFrom.get(connection.getInitItem()).remove(connection);
		connectionsTo.get(connection.getEndItem()).remove(connection);
	}
	
}
