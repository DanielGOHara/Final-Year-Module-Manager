 package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Module;

public class SelectModulesPane extends VBox {
	
	private ListView<Module> Term1UnselectedList, Term1SelectedList, Term2UnselectedList, Term2SelectedList, YearLongModules;
	private Button btnBack, btnReset, btnSubmit;
	private Label lblTerm1Credits, lblTerm2Credits;
	private int creditCountTerm1 = 0, creditCountTerm2 = 0, submitCounter = 0;
	
	public SelectModulesPane() {
			
		//Creates labels for the ListViews and apply a font and size to them
		Label lblTerm1Unselected = new Label("Unselected Term 1 Modules");
		Label lblTerm2Unselected = new Label("Unselected Term 2 Modules");
		Label lblTerm1Selected = new Label("Selected Term 1 Modules");
		Label lblTerm2Selected = new Label("Selected Term 2 Modules");
		Label lblYearLongModules = new Label("Year long modules");		
		lblTerm1Credits = new Label("Current Term 1 Credits: " + creditCountTerm1);
		lblTerm2Credits = new Label("Current Term 2 Credits: " + creditCountTerm2);
		
		//Creates and adds each term credit counter to its own HBox
		HBox Term1TFContainer = new HBox(lblTerm1Credits);
		HBox Term2TFContainer = new HBox(lblTerm2Credits);	
		Term1TFContainer.setAlignment(Pos.CENTER);
		Term2TFContainer.setAlignment(Pos.CENTER);
		
		//Create Add and Remove Buttons, Adding them to there own HBox containers, And positioning them in the centre of the HBox
		Button btnTerm1Add = new Button("Add ↓");
		Button btnTerm2Add = new Button("Add ↓");
		Button btnTerm1Remove = new Button("↑ Remove");
		Button btnTerm2Remove = new Button("↑ Remove");
		
		//Mouse click events for the Add and Remove buttons
		btnTerm1Add.setOnMouseClicked(e -> addSelectedModule("Term 1"));
		btnTerm2Add.setOnMouseClicked(e -> addSelectedModule("Term 2"));
		btnTerm1Remove.setOnMouseClicked(e -> removeSelectedModule("Term 1"));
		btnTerm2Remove.setOnMouseClicked(e -> removeSelectedModule("Term 2"));
		
		HBox btnTerm1BtnContainer = new HBox(15, btnTerm1Add, btnTerm1Remove);
		HBox btnTerm2BtnContainer = new HBox(15, btnTerm2Add, btnTerm2Remove);
		
		//Set properties for the button containers	
		btnTerm1BtnContainer.setAlignment(Pos.CENTER);
		btnTerm2BtnContainer.setAlignment(Pos.CENTER);
		
		//Initialise all of the ListViews
		Term1UnselectedList = new ListView<Module>();
		Term2UnselectedList = new ListView<Module>();
		Term1SelectedList = new ListView<Module>();
		Term2SelectedList = new ListView<Module>();
		YearLongModules = new ListView<Module>();
		
		YearLongModules.setMinHeight(40);
		YearLongModules.setMouseTransparent(true);
		
		//Binds each button to a ListView, to ensure only the necessary button is active when on a ListView. 
		btnTerm1Add.disableProperty().bind(Term1UnselectedList.getSelectionModel().selectedItemProperty().isNull());
		btnTerm2Add.disableProperty().bind(Term2UnselectedList.getSelectionModel().selectedItemProperty().isNull());
		btnTerm1Remove.disableProperty().bind(Term1SelectedList.getSelectionModel().selectedItemProperty().isNull());
		btnTerm2Remove.disableProperty().bind(Term2SelectedList.getSelectionModel().selectedItemProperty().isNull());
		
		//Mouse click events for the ListViews
		Term1UnselectedList.setOnMouseClicked(e -> clearListViewSelections("Term 1 Unselected"));
		Term2UnselectedList.setOnMouseClicked(e -> clearListViewSelections("Term 2 Unselected"));
		Term1SelectedList.setOnMouseClicked(e -> clearListViewSelections("Term 1 Selected"));
		Term2SelectedList.setOnMouseClicked(e -> clearListViewSelections("Term 2 Selected"));
		
		//Creates two VBox's one for each adding the appropriate items and sets the spacing for both
		VBox Term1VB = new VBox(15, lblTerm1Unselected, Term1UnselectedList, btnTerm1BtnContainer, lblTerm1Selected, Term1SelectedList, Term1TFContainer);
		VBox Term2VB = new VBox(15, lblTerm2Unselected, Term2UnselectedList, btnTerm2BtnContainer, lblTerm2Selected, Term2SelectedList, Term2TFContainer);
		Term1VB.setPrefWidth(2000);
		Term2VB.setPrefWidth(2000);;
		
		//Creates a new HBox container and adds both term VBox's to it
		HBox VBoxContainer = new HBox(15, Term1VB, Term2VB);
		VBoxContainer.setAlignment(Pos.CENTER);
		VBoxContainer.setPrefWidth(2000);
		
		//Creates the buttons at the bottom of the tab
		this.btnBack = new Button("Back");
		this.btnReset = new Button("Reset");
		this.btnSubmit = new Button("Submit");		
		btnSubmit.setDisable(true);
		
		//Creates a HBox container and adds the buttons for the bottom of the tab
		HBox BRSContainer = new HBox(60, btnBack, btnReset, btnSubmit);
		BRSContainer.setAlignment(Pos.CENTER);
		
		//Adds the VBox Container as well as the YearLong ListView and the remaining buttons
		this.getChildren().addAll(VBoxContainer, lblYearLongModules, YearLongModules, BRSContainer);
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setSpacing(15);
		this.setAlignment(Pos.CENTER);
		this.setPrefWidth(2000);		
	}
	
	//Getters and Setters : ----------------------------------------------------------
	
	public void setCredits(String Term) {
		
		//Sets the credits for the term provided and updates the creditCount for that given term, disabling the submit button if the total isn't 120
		if(Term == "Term 1") {
			for(Module m : Term1SelectedList.getItems()) {
				creditCountTerm1 += m.getCredits(); }
				for(Module m : YearLongModules.getItems()) {
					creditCountTerm1 += m.getCredits()/2; }
				lblTerm1Credits.setText("Select 60 Term 1 Credits: " + creditCountTerm1);			
 		} else if(Term == "Term 2") {
 			for(Module m : Term2SelectedList.getItems()) {
 				creditCountTerm2 += m.getCredits(); }
 				for(Module m : YearLongModules.getItems()) {
 					creditCountTerm2 += m.getCredits()/2; }
 				    lblTerm2Credits.setText("Select 60 Term 2 Credits: " + creditCountTerm2);
 		} 	
		if(creditCountTerm1 + creditCountTerm2 == 120) {
			btnSubmit.setDisable(false);
		}
 	}
	
	public void setModules(String Term, Module m) {
		
		//If statement that split up the modules first by if it is compulsory and its term, then solely by its term
		if (m.isMandatory() == true && Term == "Term 1") {
			Term1SelectedList.getItems().add(m);
			
		} else if (m.isMandatory() == true && Term == "Term 2"){
			Term2SelectedList.getItems().add(m);
			
		} else if (Term == "Term 1") {
			Term1UnselectedList.getItems().add(m);
			
		} else if (Term == "Term 2") {
			Term2UnselectedList.getItems().add(m);
			
		} else if (Term == "Year Long") {
			YearLongModules.getItems().add(m);
			
		}
	  }
		
	public void setSelectedModules(String Term, Module m) {
		
		//If states that split up selected modules into their terms
		if(Term == "Term 1") {
		   Term1SelectedList.getItems().add(m);
		   
		} else if(Term == "Term 2") {
		   Term2SelectedList.getItems().add(m);
		   
		} else if(Term == "Year Long") {
		   YearLongModules.getItems().add(m);
		   
		}
	}
	
	public ListView<Module> getSelectedModules() {
		
		//Creates a temporary ListView and merges all of the selected term lists into it
		ListView<Module> MergedList = new ListView<Module>();
		for(Module m : Term1SelectedList.getItems()) {
			MergedList.getItems().add(m);
		}
		for(Module m : Term2SelectedList.getItems()) {
			MergedList.getItems().add(m);
		}
		for(Module m : YearLongModules.getItems()) {
			MergedList.getItems().add(m);
		}
		return MergedList;
	}

	public ObservableList<Module> getUnselectedModules(String Term) {
		
		//Returns the ListView associated with the Term supplied
		if (Term == "Term 1") {
			return Term1UnselectedList.getItems();
		} else if (Term == "Term 2") {
			return Term2UnselectedList.getItems();
		} else {
			return null;
		}
	}
	
	public int getSubmitCounter() {
		return submitCounter;
	}

	public void addSelectedModule(String Term) {
		
		  //If statements that add selected items from either Term 1 or Term 2 depending on the string passed and updates the creditCount for the term
		  if(Term == "Term 1" && creditCountTerm1 != 60) {
			  
			  		Term1SelectedList.getItems().add(Term1UnselectedList.getSelectionModel().getSelectedItem());
					creditCountTerm1 += Term1UnselectedList.getSelectionModel().getSelectedItem().getCredits();
					lblTerm1Credits.setText("Select 60 Term 1 Credits: " + creditCountTerm1);			
					Term1UnselectedList.getItems().remove(Term1UnselectedList.getSelectionModel().getSelectedIndex());
					submitCounter++;
					
		  } else if(Term == "Term 2" && creditCountTerm2 != 60) {
			  
			  		Term2SelectedList.getItems().add(Term2UnselectedList.getSelectionModel().getSelectedItem());
			  		creditCountTerm2 += Term2UnselectedList.getSelectionModel().getSelectedItem().getCredits();
			  		lblTerm2Credits.setText("Select 60 Term 2 Credits: " + creditCountTerm2);
			  		Term2UnselectedList.getItems().remove(Term2UnselectedList.getSelectionModel().getSelectedIndex()); }
		  			submitCounter++;
		  
		  if(creditCountTerm1 == 60 && creditCountTerm2 == 60) {
			  		btnSubmit.setDisable(false); }		  
	}
	
	public void removeSelectedModule(String Term) {
		
		  //If statements that removes selected items from either Term 1 or Term 2 depending on the string passed and updates the creditCount for the term
		  if(Term == "Term 1" && Term1SelectedList.getSelectionModel().getSelectedItem().isMandatory() == true) { 	  
		  } else if(Term == "Term 1") {
			  
			  		Term1UnselectedList.getItems().add(Term1SelectedList.getSelectionModel().getSelectedItem());
			  		creditCountTerm1 -= Term1SelectedList.getSelectionModel().getSelectedItem().getCredits();
			  		lblTerm1Credits.setText("Select 60 Term 1 Credits: " + creditCountTerm1);
			  		Term1SelectedList.getItems().remove(Term1SelectedList.getSelectionModel().getSelectedItem());
			  		submitCounter++;
		    
	      } else if(Term == "Term 2" && Term2SelectedList.getSelectionModel().getSelectedItem().isMandatory() == true) { 	  
	      } else if(Term == "Term 2") {
	    	  
	    	  		Term2UnselectedList.getItems().add(Term2SelectedList.getSelectionModel().getSelectedItem());	     	
	     			creditCountTerm2 -= Term2SelectedList.getSelectionModel().getSelectedItem().getCredits();
	     			lblTerm2Credits.setText("Select 60 Term 2 Credits: " + creditCountTerm2);
	     			Term2SelectedList.getItems().remove(Term2SelectedList.getSelectionModel().getSelectedItem()); 
	     			submitCounter++;
	     			
	      }
		  
		  //If either creditCount doesn't equal 60 the submit button is disabled
		  if(creditCountTerm1 != 60 || creditCountTerm2 != 60) {
			  		btnSubmit.setDisable(true); }		  
    }
	
	public void resetSubmitCounter() {
		submitCounter = 0;
	}
	
	public void resetSelectModulesPane() {
		
			//Resets all variables, ListViews and disables the submit button
			creditCountTerm1 = 0;
			creditCountTerm2 = 0;					
			Term1UnselectedList.getItems().clear();
			Term2UnselectedList.getItems().clear();
			Term1SelectedList.getItems().clear();
			Term2SelectedList.getItems().clear();
			YearLongModules.getItems().clear();		
			btnSubmit.setDisable(true);
			submitCounter = 0;
	}
	
	public void clearListViewSelections(String Term) {
		
		//Deselect's all other ListViews apart from the one selected
		if(Term == "Term 1 Unselected") {			
			Term2UnselectedList.getSelectionModel().clearSelection();
			Term1SelectedList.getSelectionModel().clearSelection();
			Term2SelectedList.getSelectionModel().clearSelection();			
		} else if(Term == "Term 1 Selected") {		
			Term1UnselectedList.getSelectionModel().clearSelection();
			Term2UnselectedList.getSelectionModel().clearSelection();
			Term2SelectedList.getSelectionModel().clearSelection();		
		} else if(Term == "Term 2 Unselected") {			
			Term1UnselectedList.getSelectionModel().clearSelection();
			Term1SelectedList.getSelectionModel().clearSelection();
			Term2SelectedList.getSelectionModel().clearSelection();			
		} else if(Term == "Term 2 Selected") {		
			Term1UnselectedList.getSelectionModel().clearSelection();
			Term2UnselectedList.getSelectionModel().clearSelection();
			Term1SelectedList.getSelectionModel().clearSelection();			
		}		
	}
	
	//EventHandlers : -----------------------------------------------------------------
	
	public void returnToCreateProfile(EventHandler<ActionEvent> handler) {
		btnBack.setOnAction(handler);
	}
	
	public void resetSelectModulesPane(EventHandler<ActionEvent> handler) {
		btnReset.setOnAction(handler);
	}
	
	public void submitModules(EventHandler<ActionEvent> handler) {
		btnSubmit.setOnAction(handler);
	}	

	
	
	
	
	
	
}