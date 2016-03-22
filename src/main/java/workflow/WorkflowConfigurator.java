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

import components.connect.connection.FourSideConnection;
import components.connect.connection.IConnection;
import components.connect.connector.Connector;
import components.connect.connector.ConnectorPointer;
import javafx.scene.Node;
import javafx.scene.image.Image;
import workflow.elements.WorkflowConnection;
import workflow.elements.WorkflowItem;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class WorkflowConfigurator {



	private Function<WorkflowItem, Object> workflowItemKeyExtractor = Object::hashCode;

	public WorkflowConfigurator setWorkflowItemKeyExtractor(Function<WorkflowItem, Object> extractor){
		this.workflowItemKeyExtractor = extractor;
		return this;
	}

	public Function<WorkflowItem, Object> getWorkflowItemKeyExtractor(){
		return workflowItemKeyExtractor;
	}



	private Function<WorkflowConnection, Object> workflowConnectionKeyExtractor = Object::hashCode;

	public WorkflowConfigurator setWorkflowConnectionKeyExtractor(Function<WorkflowConnection, Object> extractor){
		this.workflowConnectionKeyExtractor = extractor;
		return this;
	}

	public Function<WorkflowConnection, Object> getWorkflowConnectionKeyExtractor(){
		return workflowConnectionKeyExtractor;
	}



	private Function<WorkflowItem, IConnection<?>> connectionFactory = (wfi)->new FourSideConnection<>(wfi.getRoot());

	public WorkflowConfigurator setConnectionFactory(Function<WorkflowItem, IConnection<?>> factory){
		this.connectionFactory = factory;
		return this;
	}

	public Function<WorkflowItem, IConnection<?>> getConnectionFactory(){
		return connectionFactory;
	}



	private Supplier<Object> defaultItemStateSupplier = ()->null;

	public WorkflowConfigurator setDefaultItemStateSupplier(Supplier<Object> supplier){
		this.defaultItemStateSupplier = supplier;
		return this;
	}

	public Supplier<Object> getDefaultItemStateSupplier(){
		return defaultItemStateSupplier;
	}



	private Supplier<Object> defaultConnectionStateSupplier = ()->null;

	public WorkflowConfigurator setDefaultConnectionStateSupplier(Supplier<Object> supplier){
		this.defaultConnectionStateSupplier = supplier;
		return this;
	}

	public Supplier<Object> getDefaultConnectionStateSupplier(){
		return defaultConnectionStateSupplier;
	}



	private Supplier<Connector> defaultConnectorSupplier = ()->{
		Connector connector = new ConnectorPointer();
		connector.mount();
		return connector;
	};

	public WorkflowConfigurator setDefaultConnectorSupplier(Supplier<Connector> supplier){
		this.defaultConnectorSupplier = supplier;
		return this;
	}

	public Supplier<Connector> getDefaultConnectorSupplier(){
		return defaultConnectorSupplier;
	}



	private Supplier<Node> defaultItemGraphicSupplier = ()->null;

	public WorkflowConfigurator setDefaultItemGraphicSupplier(Supplier<Node> supplier){
		this.defaultItemGraphicSupplier = supplier;
		return this;
	}

	public Supplier<Node> getDefaultItemGraphicSupplier(){
		return defaultItemGraphicSupplier;
	}



	private Image defaultConnectImage = new Image("connect.png");

	public WorkflowConfigurator setDefaultConnectImage(Image image){
		this.defaultConnectImage = image;
		return this;
	}

	public Image getDefaultConnectImage(){
		return defaultConnectImage;
	}



	public BiFunction<WorkflowItem, WorkflowItem, WorkflowConnection> defaultConnectionFactory;

	public WorkflowConfigurator setDefaultConnectionFactory(BiFunction<WorkflowItem, WorkflowItem, WorkflowConnection> factory){
		this.defaultConnectionFactory = factory;
		return this;
	}

	public BiFunction<WorkflowItem, WorkflowItem, WorkflowConnection> getDefaultConnectionFactory(){
		return defaultConnectionFactory;
	}



	public Function<WorkflowItem, Boolean> itemAdditionValidator = (item)->true;
	
	public Function<WorkflowItem, Boolean> getItemAdditionValidator() {
		return itemAdditionValidator;
	}

	public WorkflowConfigurator setItemAdditionValidator(Function<WorkflowItem, Boolean> itemAdditionValidator) {
		this.itemAdditionValidator = itemAdditionValidator;
		return this;
	}



	public Function<WorkflowItem, Boolean> itemRemovalValidator = (item)->true;
	
	public Function<WorkflowItem, Boolean> getItemRemovalValidator() {
		return itemRemovalValidator;
	}

	public WorkflowConfigurator setItemRemovalValidator(Function<WorkflowItem, Boolean> itemRemovalValidator) {
		this.itemRemovalValidator = itemRemovalValidator;
		return this;
	}



	public Function<WorkflowConnection, Boolean> connectionAdditionValidator = (connection)->true;
	
	public Function<WorkflowConnection, Boolean> getConnectionAdditionValidator() {
		return connectionAdditionValidator;
	}

	public WorkflowConfigurator setConnectionAdditionValidator(Function<WorkflowConnection, Boolean> connectionAdditionValidator) {
		this.connectionAdditionValidator = connectionAdditionValidator;
		return this;
	}



	public Function<WorkflowConnection, Boolean> connectionRemovalValidator = (connection)->true;
	
	public Function<WorkflowConnection, Boolean> getConnectionRemovalValidator() {
		return connectionRemovalValidator;
	}

	public WorkflowConfigurator setConnectionRemovalValidator(Function<WorkflowConnection, Boolean> connectionRemovalValidator) {
		this.connectionRemovalValidator = connectionRemovalValidator;
		return this;
	}
	
	
	
	public Function<Object, WorkflowItem> dragInfoConverter = (info)->(WorkflowItem)info;
	
	public Function<Object, WorkflowItem> getDragInfoConverter() {
		return dragInfoConverter;
	}

	public WorkflowConfigurator setDragInfoConverter(Function<Object, WorkflowItem> dragInfoConverter) {
		this.dragInfoConverter = dragInfoConverter;
		return this;
	}	
	
	
	// PERMISSIONS

	private boolean permitItemAddition = true;
	
	public boolean getPermitItemAddition() {
		return permitItemAddition;
	}

	public WorkflowConfigurator setPermitItemAddition(boolean permitItemAddition) {
		this.permitItemAddition = permitItemAddition;
		return this;
	}
	
	

	private boolean permitItemRemoval = true;

	public boolean getPermitItemRemoval() {
		return permitItemRemoval;
	}

	public WorkflowConfigurator setPermitItemRemoval(boolean permitItemRemoval) {
		this.permitItemRemoval = permitItemRemoval;
		return this;
	}

	

	private boolean permitConnectionRemoval = true;

	public boolean getPermitConnectionRemoval() {
		return permitConnectionRemoval;
	}

	public WorkflowConfigurator setPermitConnectionRemoval(boolean permitConnectionRemoval) {
		this.permitConnectionRemoval = permitConnectionRemoval;
		return this;
	}


	
	private boolean permitConnectionAddition = true;
	
	public boolean getPermitConnectionAddition() {
		return permitConnectionAddition;
	}

	public WorkflowConfigurator setPermitConnectionAddition(boolean permitConnectionAddition) {
		this.permitConnectionAddition = permitConnectionAddition;
		return this;
	}


	
	private boolean permitRemoveItemShortcut = true;

	public WorkflowConfigurator setPermitRemoveItemShortcut(boolean permit){
		this.permitRemoveItemShortcut = permit;
		return this;
	}

	public boolean getPermitRemoveItemShortcut(){
		return permitRemoveItemShortcut;
	}



	private boolean permitDisconnectShortcut = true;

	public WorkflowConfigurator setPermitDisconnectShortcut(boolean permit){
		this.permitDisconnectShortcut = permit;
		return this;
	}

	public boolean getPermitDisconnectShortcut(){
		return permitDisconnectShortcut;
	}



	private boolean permitMovableItems = true;

	public WorkflowConfigurator setPermitMovableItems(boolean permit){
		this.permitMovableItems = permit;
		return this;
	}

	public boolean getPermitMovableItems(){
		return permitMovableItems;
	}



	private boolean permitConnectibleItems = true;

	public WorkflowConfigurator setPermitConnectibleItems(boolean permit){
		this.permitConnectibleItems = permit;
		return this;
	}

	public boolean getPermitConnectibleItems(){
		return permitConnectibleItems;
	}



	private boolean permitItemSelfConnection = true;

	public WorkflowConfigurator setPermitItemSelfConnection(boolean permit){
		this.permitItemSelfConnection = permit;
		return this;
	}

	public boolean getPermitItemSelfConnection(){
		return permitItemSelfConnection;
	}



	private boolean permitMultipleConnectionsToItem = true;

	public WorkflowConfigurator setPermitMultipleConnectionsToItem(boolean permit){
		this.permitMultipleConnectionsToItem = permit;
		return this;
	}

	public boolean getPermitMultipleConnectionsToItem(){
		return permitMultipleConnectionsToItem;
	}

	
	
	private boolean permitMultipleConnectionsFromItem = true;

	public WorkflowConfigurator setPermitMultipleConnectionsFromItem(boolean permit){
		this.permitMultipleConnectionsFromItem = permit;
		return this;
	}

	public boolean getPermitMultipleConnectionsFromItem(){
		return permitMultipleConnectionsFromItem;
	}



	private boolean permitCircularConnection = true;

	public WorkflowConfigurator setPermitCircularConnection(boolean permit){
		this.permitCircularConnection = permit;
		return this;
	}

	public boolean getPermitCircularConnection(){
		return permitCircularConnection;
	}

	

	private boolean permitSimilarConnections = true;

	public WorkflowConfigurator setPermitSimilarConnections(boolean permit){
		this.permitSimilarConnections = permit;
		return this;
	}

	public boolean getPermitSimilarConnections(){
		return permitSimilarConnections;
	}



	private boolean permitUndo = true;

	public WorkflowConfigurator setPermitUndo(boolean permit){
		this.permitUndo = permit;
		return this;
	}

	public boolean getPermitUndo(){
		return permitUndo;
	}



	private boolean permitRedo = true;

	public WorkflowConfigurator setPermitRedo(boolean permit){
		this.permitRedo = permit;
		return this;
	}

	public boolean getPermitRedo(){
		return permitRedo;
	}



	private boolean permitUndoShortCut = true;

	public WorkflowConfigurator setPermitUndoShortcut(boolean permit){
		this.permitUndoShortCut = permit;
		return this;
	}

	public boolean getPermitUndoShortcut(){
		return permitUndoShortCut;
	}



	private boolean permitRedoShortcut = true;

	public WorkflowConfigurator setPermitRedoShortcut(boolean permit){
		this.permitRedoShortcut = permit;
		return this;
	}

	public boolean getPermitRedoShortcut(){
		return permitRedoShortcut;
	}

	
	
	private boolean permitDragItem = true;
	
	public boolean getPermitDragItem() {
		return permitDragItem;
	}

	public WorkflowConfigurator setPermitDragItem(boolean permitDragItem) {
		this.permitDragItem = permitDragItem;
		return this;
	}
	

	
}
