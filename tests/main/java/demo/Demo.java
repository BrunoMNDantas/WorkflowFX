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
package demo;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import workflow.elements.Workflow;
import workflow.elements.WorkflowItem;


public class Demo extends Application {
	
	private static final String FXML_DOCUMENT_PATH = "resources/FXMLDocument.fxml";
	private static final String TITLE = "MultiOption";
	private static final String DESCRIPTION = "Demo to show usage of MultiOption component.\nPlease right click on button below, choose one option and observe button image changing.";
	
	private static final ImageView A_IMAGE = new ImageView(new Image("resources/A.png"));
	private static final ImageView B_IMAGE = new ImageView(new Image("resources/B.png"));
	private static final ImageView C_IMAGE = new ImageView(new Image("resources/C.png"));
	private static final ImageView D_IMAGE = new ImageView(new Image("resources/D.png"));
	
	@Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = getLoader();
        
        init(stage, loader);
        
        mount(loader.getController());
    }
	
	public static FXMLLoader getLoader(){
		URL location = ClassLoader.getSystemResource(FXML_DOCUMENT_PATH);
		return new FXMLLoader(location);
	}

	private static void init(Stage stage, FXMLLoader loader) throws Exception{
		Parent root = loader.load();
		
		Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();	
	}
	
	private static void mount(Controller controller) throws Exception{
		controller.title.setText(TITLE);
		controller.description.setText(DESCRIPTION);
		controller.description.editableProperty().set(false);
		mountDemo(controller.demoPane);
	}
	
	////////////////////////////////////////////////////////
	//                                                    //
	//                       DEMO                         //
	//                                                    //
	//    mountDemo(SubScene) -> relevant method          // 
	//                           to see Component         //
	//                           utilization              //
	//                                                    //
 	//                                                    //
	////////////////////////////////////////////////////////
	
	
	private static void mountDemo(AnchorPane demoPane) {
		Workflow wf = new Workflow(demoPane);
		
		
		wf.events.addItem.addListner((wfi)->System.out.println("Item " + wfi.getState() + " Added"));
		wf.events.removeItem.addListner((wfi)->System.out.println("Item " + wfi.getState() + " Removed"));
		wf.events.connect.addListner((wfc)->System.out.println("Connection "  + wfc.getState() + " Added"));
		wf.events.disconnect.addListner((wfc)->System.out.println("Connection " + wfc.getState() + " Removed"));
		
		
		WorkflowItem item = wf.itemFactory.create(new Label("Label"), "Label");
		WorkflowItem itemA = wf.itemFactory.create(A_IMAGE, "A");
		WorkflowItem itemB = wf.itemFactory.create(B_IMAGE, "B");
		WorkflowItem itemC = wf.itemFactory.create(C_IMAGE, "C");
		WorkflowItem itemD = wf.itemFactory.create(D_IMAGE, "D");
		
		
		itemA.operations.add("Say hello", ()->System.out.println("Hello from A!"));
		itemA.operations.add("Say Goodbey", ()->System.out.println("Goodbey from A!"));
		itemB.operations.add("Say hello", ()->System.out.println("Hello from B!"));
		itemB.operations.add("Say Goodbey", ()->System.out.println("Goodbey from B!"));
		itemC.operations.add("Say hello", ()->System.out.println("Hello from C!"));
		itemC.operations.add("Say Goodbey", ()->System.out.println("Goodbey from C!"));
		itemD.operations.add("Say hello", ()->System.out.println("Hello from D!"));
		itemD.operations.add("Say Goodbey", ()->System.out.println("Goodbey from D!"));
		
		
		wf.elements.addItem(item);
		wf.elements.addItem(itemA);
		wf.elements.addItem(itemB);
		wf.elements.addItem(itemC);
		wf.elements.addItem(itemD);
		
		
		item.setPosition(250, 150);
		itemB.setPosition(0, 300);
		itemC.setPosition(500, 300);
		itemD.setPosition(500, 0);
		
		
		wf.elements.addConnection(wf.connectionFactory.create(itemA, itemB, "A-B"));
		wf.elements.addConnection(wf.connectionFactory.create(itemB, itemC, "B-C"));
		wf.elements.addConnection(wf.connectionFactory.create(itemC, itemD, "C-D"));
		wf.elements.addConnection(wf.connectionFactory.create(itemD, itemA, "D-A"));	
	}
	
    public static void main(String[] args) {
        launch(args);
    }
    
}
