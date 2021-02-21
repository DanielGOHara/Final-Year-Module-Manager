package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import model.Module;

public class OverviewPane extends VBox {
	
	TextArea SelectedModules, ReservedModules, StudentProfile;
	Button btnBack, btnSave;
	
	public OverviewPane() {
		
		//Initialise the TextArea's
		SelectedModules = new TextArea();
		ReservedModules = new TextArea();
		StudentProfile = new TextArea();
		
		//Set the Preferred sizes for each TextArea
		SelectedModules.setPrefSize(2000, 900);
		ReservedModules.setPrefSize(2000, 900);
		StudentProfile.setPrefSize(2000, 400);
		
		//Sets each TextArea to be not editable
		SelectedModules.setEditable(false);
		ReservedModules.setEditable(false);
		StudentProfile.setEditable(false);
		
		//Sets specific text for each TextArea
		ReservedModules.setText("Reserved Modules:\n");
		ReservedModules.appendText("============\n");
		
		SelectedModules.setText("Selected Modules:\n");
		SelectedModules.appendText("============\n");
		
		//Initialise both Button's
		btnBack = new Button("Back");
		btnSave = new Button("Save Profile");
		
		//Initialise two HBox's one for two of the 3 TextAreas, and the second for the button's
		HBox ModulesContainer = new HBox(15, SelectedModules, ReservedModules);
		HBox ButtonContainer = new HBox(15, btnBack, btnSave);
		
		//Set alignment for the containers
		ModulesContainer.setAlignment(Pos.CENTER);
		ButtonContainer.setAlignment(Pos.CENTER);
		
		//Sets the properties for the class VBox
		this.getChildren().addAll(StudentProfile, ModulesContainer, ButtonContainer);
		this.setPadding(new Insets(50, 50, 50, 50));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(15);
		
	}
	
	//Getters and Setters : ----------------------------------------------------------
	
	public void setProfileDetails(String Firstname, String Surname, String PNumber, String Email, String Date, String Course) {
		
		//Sets the profile details for the student profile TextArea
		StudentProfile.appendText("Name: " + Firstname + " " + Surname + "\n");
		StudentProfile.appendText("P-Number: " + PNumber + "\n");
		StudentProfile.appendText("Email: " + Email + "\n");
		StudentProfile.appendText("Date: " + Date + "\n");
		StudentProfile.appendText("Course: " + Course + "\n");		
	}
	
	public void setSelectedModules(Module m) {
		
		//Creates a string variable to store if the module is compulsory
		String Compulsory = "No";

			//If the module is compulsory the string variable is changed to yes
			if(m.isMandatory() == true) {
				Compulsory = "Yes";
			}		
			//Adds each module to the selected TextArea
			SelectedModules.appendText("\n");
			SelectedModules.appendText("Module Code: " + m.getModuleCode() + "\n");
			SelectedModules.appendText("Module Name: " + m.getModuleName() + "\n");
			SelectedModules.appendText("Module Credit: " + Integer.toString(m.getCredits()) + " | Delivery: " + m.getRunPlan() + "\n");
			SelectedModules.appendText("Compulsory Module: " + Compulsory + "\n");
	}
	
	public void setReservedModules(Module m) {

			//Adds each module to the reserved TextArea
			ReservedModules.appendText("\n");
			ReservedModules.appendText("Module Code: " + m.getModuleCode() + "\n");
			ReservedModules.appendText("Module Name: " + m.getModuleName() + "\n");
			ReservedModules.appendText("Module Credit: " + Integer.toString(m.getCredits()) + " | Delivery: " + m.getRunPlan() + "\n");
	}
	
	public void resetOverviewPane() {
		
			//Resets all of the TextAreas and adds the original text
			SelectedModules.setText(null);
			ReservedModules.setText(null);
		    StudentProfile.setText(null);
		    
		    ReservedModules.setText("Reserved Modules:\n");
			ReservedModules.appendText("============\n");
			
			SelectedModules.setText("Selected Modules:\n");
			SelectedModules.appendText("============\n");		   
	}
	
	//EventHandlers : -----------------------------------------------------------------
	
	public void returnToReserveModulesPane(EventHandler<ActionEvent> handler) {
		btnBack.setOnAction(handler);
	}
	
	public void saveProfileOverview(EventHandler<ActionEvent> handler) {
		btnSave.setOnAction(handler);
	}

}
