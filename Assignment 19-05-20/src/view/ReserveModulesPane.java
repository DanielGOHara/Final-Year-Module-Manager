package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import model.Module;

public class ReserveModulesPane extends VBox {
	
	private ListView<Module> Term1UnselectedList, Term1ReservedList, Term2UnselectedList, Term2ReservedList;
	private Label lblTerm1Credits, lblTerm2Credits;
	private Button btnTerm1Back, btnTerm1Confirm,  btnTerm2Confirm;
	private int creditCountTerm1 = 0, creditCountTerm2 = 0;
	
	public ReserveModulesPane() {
		
		Accordion accordion = new Accordion();
		
		//Initialise all of the Label's
		Label lblTerm1Uns = new Label("Unselected Term 1 Modules");
		Label lblTerm2Uns = new Label("Unselected Term 2 Modules");
		Label lblTerm1Res = new Label("Reserved Term 1 Modules");
		Label lblTerm2Res = new Label("Reserved Term 2 Modules");
		lblTerm1Credits = new Label("Reserve 30 credits worth of Term 1 modules: " + creditCountTerm1);
		lblTerm2Credits = new Label("Reserve 30 credits worth of Term 2 modules: " + creditCountTerm2);
		
		//Initialise all of the Button's		
		Button btnTerm1Add = new Button("Add");
		Button btnTerm2Add = new Button("Add");
		Button btnTerm1Remove = new Button("Remove");
		Button btnTerm2Remove = new Button("Remove");
		btnTerm1Confirm = new Button("Confirm");
		btnTerm2Confirm = new Button("Confirm");
		btnTerm1Back = new Button("Back");
		
		//Mouse click events for the Add and Remove button's
		btnTerm1Add.setOnMouseClicked(e -> addSelectedModule("Term 1"));
		btnTerm2Add.setOnMouseClicked(e -> addSelectedModule("Term 2"));
		btnTerm1Remove.setOnMouseClicked(e -> removeSelectedModule("Term 1"));
		btnTerm2Remove.setOnMouseClicked(e -> removeSelectedModule("Term 2"));		
		btnTerm1Confirm.setDisable(true);
		btnTerm2Confirm.setDisable(true);
		
		Button btnTerm2Back = new Button("Back");	
		
		//Initialise all of the ListView's
		Term1UnselectedList = new ListView<Module>();
		Term2UnselectedList = new ListView<Module>();
		Term1ReservedList = new ListView<Module>();
		Term2ReservedList = new ListView<Module>();
		
		//Binds each Button to a separate ListView so if you haven't selected an appropriate selection in a ListView the Button's are disabled
		btnTerm1Add.disableProperty().bind(Term1UnselectedList.getSelectionModel().selectedItemProperty().isNull());
		btnTerm2Add.disableProperty().bind(Term2UnselectedList.getSelectionModel().selectedItemProperty().isNull());
		btnTerm1Remove.disableProperty().bind(Term1ReservedList.getSelectionModel().selectedItemProperty().isNull());
		btnTerm2Remove.disableProperty().bind(Term2ReservedList.getSelectionModel().selectedItemProperty().isNull());
		
		//Mouse click events for the ListViews
		Term1UnselectedList.setOnMouseClicked(e -> clearListViewSelections("Term 1 Unselected"));
		Term2UnselectedList.setOnMouseClicked(e -> clearListViewSelections("Term 2 Unselected"));
		Term1ReservedList.setOnMouseClicked(e -> clearListViewSelections("Term 1 Reserved"));
		Term2ReservedList.setOnMouseClicked(e -> clearListViewSelections("Term 2 Reserved"));
		
		//Set the properties for the ListViews
		Term1UnselectedList.setPrefHeight(2000);
		Term2UnselectedList.setPrefHeight(2000);
		Term1ReservedList.setPrefHeight(2000);
		Term2ReservedList.setPrefHeight(2000);
		
		//Creation of all the necessary H & VBox's for Term 1
		HBox btnTerm1Container = new HBox(50, btnTerm1Back, btnTerm1Add, btnTerm1Remove, btnTerm1Confirm);
		HBox Term1CreditContainer = new HBox(15, lblTerm1Credits);
		VBox Term1UnsContainer = new VBox(15, lblTerm1Uns, Term1UnselectedList, Term1CreditContainer);
		VBox Term1ResContainer = new VBox(15, lblTerm1Res, Term1ReservedList, btnTerm1Container);
		HBox Term1VBox = new HBox(15, Term1UnsContainer, Term1ResContainer);
		TitledPane Term1TP = new TitledPane("Term 1 Modules", Term1VBox);
		
		//Set the properties for these H & VBox's
		Term1CreditContainer.setAlignment(Pos.CENTER);
		Term1UnsContainer.setAlignment(Pos.CENTER);
		Term1ResContainer.setAlignment(Pos.CENTER);
		btnTerm1Container.setAlignment(Pos.CENTER);
		Term1VBox.setAlignment(Pos.CENTER);

		//Set the Preferred sizes for each VBox
		Term1UnsContainer.setPrefSize(2000, 2000);
		Term1ResContainer.setPrefSize(2000, 2000);
		Term1VBox.setPrefHeight(2000);
		
		//Creation of all the necessary H & VBox's for Term 2
		HBox btnTerm2Container = new HBox(50, btnTerm2Back, btnTerm2Add, btnTerm2Remove, btnTerm2Confirm);
		HBox Term2CreditContainer = new HBox(15, lblTerm2Credits);
		VBox Term2UnsContainer = new VBox(15, lblTerm2Uns, Term2UnselectedList, Term2CreditContainer);
		VBox Term2ResContainer = new VBox(15, lblTerm2Res, Term2ReservedList, btnTerm2Container);
		HBox Term2VBox = new HBox(15, Term2UnsContainer, Term2ResContainer);
		TitledPane Term2TP = new TitledPane("Term 2 Modules",Term2VBox);
		
		//Set the properties for these H & VBox's
		Term2CreditContainer.setAlignment(Pos.CENTER);
		Term2UnsContainer.setAlignment(Pos.CENTER);
		Term2ResContainer.setAlignment(Pos.CENTER);
		btnTerm2Container.setAlignment(Pos.CENTER);
		Term2VBox.setAlignment(Pos.CENTER);

		//Set the Preferred sizes for each VBox
		Term2UnsContainer.setPrefSize(2000, 2000);
		Term2ResContainer.setPrefSize(2000, 2000);
		Term2VBox.setPrefHeight(2000);
		
		//Adds both TitledPanes to the Accordion
		accordion.getPanes().add(Term1TP);
		accordion.getPanes().add(Term2TP);
		accordion.setExpandedPane(Term1TP);
		Term2TP.setDisable(true);
		
		//Mouse click events for the accordions
		btnTerm1Confirm.setOnAction((ActionEvent e) -> { Term2TP.setDisable(false); Term1TP.setDisable(true); accordion.setExpandedPane(Term2TP); });
		btnTerm2Back.setOnAction((ActionEvent e) -> { Term1TP.setDisable(false); Term2TP.setDisable(true); accordion.setExpandedPane(Term1TP); });
		
		this.getChildren().addAll(accordion);
		this.setPadding(new Insets(20, 20, 20, 20));
		
	}
	
	//Getters and Setters : ----------------------------------------------------------

	public void setUnselectedModules(String Term, ObservableList<Module> tempList) {
		
		//If statement that splits up the modules based on term
		if (Term == "Term 1") {
			Term1UnselectedList.setItems(tempList);
		} else if (Term == "Term 2") {
			Term2UnselectedList.setItems(tempList);
		}
	}
	
	public void setReservedModules(String Term, Module m) {
		
		//If statement that splits up the modules based on term then updates the creditCount and enables the confirm buttons if it equals 30
		if(Term == "Term 1") {
		   Term1ReservedList.getItems().add(m);
		   creditCountTerm1 += m.getCredits();
		   lblTerm1Credits.setText("Reserve 30 credits worth of Term 1 modules: " + creditCountTerm1);
		} else if(Term == "Term 2") {
		   Term2ReservedList.getItems().add(m);
		   creditCountTerm2 += m.getCredits();
		   lblTerm2Credits.setText("Reserve 30 credits worth of Term 2 modules: " + creditCountTerm2);
		}
		if(creditCountTerm1 == 30) {
			btnTerm1Confirm.setDisable(false);
		}
		if(creditCountTerm2 == 30) {
			btnTerm2Confirm.setDisable(false);
		}
	}
	
	public ListView<Module> getReservedModules() {
		
		//Creates a temporary ListView and merges all of the reserved term lists into it
		ListView<Module> MergedList = new ListView<Module>();
		for(Module m : Term1ReservedList.getItems()) {
			MergedList.getItems().add(m);
		}
		for(Module m : Term2ReservedList.getItems()) {
			MergedList.getItems().add(m);
		}
		return MergedList;
	}
	
	public void addSelectedModule(String Term) {
		
		  //If statements that add selected items from either Term 1 or Term 2 depending on the string passed
		  if (Term == "Term 1" && creditCountTerm1 != 30) {		  
			  		Term1ReservedList.getItems().add(Term1UnselectedList.getSelectionModel().getSelectedItem());
			  		creditCountTerm1 += Term1UnselectedList.getSelectionModel().getSelectedItem().getCredits();
			  		lblTerm1Credits.setText("Reserve 30 credits worth of Term 1 modules: " + creditCountTerm1);	  		 
			  		Term1UnselectedList.getItems().remove(Term1UnselectedList.getSelectionModel().getSelectedIndex());		  		
			  		if(creditCountTerm1 == 30) {
			  			      btnTerm1Confirm.setDisable(false);
			  		}		  		 
		  } else if (Term == "Term 2" && creditCountTerm2 != 30) {		  
			  		Term2ReservedList.getItems().add(Term2UnselectedList.getSelectionModel().getSelectedItem());
			  		creditCountTerm2 += Term2UnselectedList.getSelectionModel().getSelectedItem().getCredits();
			  	    lblTerm2Credits.setText("Reserve 30 credits worth of Term 2 modules: " + creditCountTerm2);			
			  		Term2UnselectedList.getItems().remove(Term2UnselectedList.getSelectionModel().getSelectedIndex());  		
			  		if(creditCountTerm1 == 30 && creditCountTerm2 == 30) {
			  				  btnTerm2Confirm.setDisable(false);
			  		}
		  }			 
	}
	
	public void removeSelectedModule(String Term) {
		
		  //If statements that remove selected items from either Term 1 or Term 2 depending on the string passed
		  if (Term == "Term 1") {			  
			  		Term1UnselectedList.getItems().add(Term1ReservedList.getSelectionModel().getSelectedItem());
			  		creditCountTerm1 -= Term1ReservedList.getSelectionModel().getSelectedItem().getCredits();
			  	    lblTerm1Credits.setText("Reserve 30 credits worth of Term 1 Modules: " + creditCountTerm1);
					Term1ReservedList.getItems().remove(Term1ReservedList.getSelectionModel().getSelectedIndex());			 
					if(creditCountTerm1 != 30) {
						      btnTerm1Confirm.setDisable(true);
					}					
		  } else if (Term == "Term 2") {			  
			  		Term2UnselectedList.getItems().add(Term2ReservedList.getSelectionModel().getSelectedItem());
			  	    creditCountTerm2 -= Term2ReservedList.getSelectionModel().getSelectedItem().getCredits();
			  		lblTerm2Credits.setText("Reserve 30 credits worth of Term 2 Modules: " + creditCountTerm2);
			  		Term2ReservedList.getItems().remove(Term2ReservedList.getSelectionModel().getSelectedIndex()); 		  		
			  		if(creditCountTerm1 != 30 || creditCountTerm2 != 30) {
			  			      btnTerm2Confirm.setDisable(true);
			  		}
		  }
	}

	public void resetReserveModulesPane() {
		
			//Resets all variables, ListViews and disables the submit button
		    creditCountTerm1 = 0;
		    creditCountTerm2 = 0;
		    Term1UnselectedList.getItems().clear();
		    Term2UnselectedList.getItems().clear();
		    Term1ReservedList.getItems().clear();
		    Term2ReservedList.getItems().clear();
	}
	
	public void clearListViewSelections(String Term) {
		
		//Deselect's all other ListViews apart from the one selected
		if(Term == "Term 1 Unselected") {		
			Term1ReservedList.getSelectionModel().clearSelection();
			Term2ReservedList.getSelectionModel().clearSelection();
			Term2UnselectedList.getSelectionModel().clearSelection();			
		} else if(Term == "Term 1 Reserved") {			
			Term1UnselectedList.getSelectionModel().clearSelection();
			Term2UnselectedList.getSelectionModel().clearSelection();
			Term2ReservedList.getSelectionModel().clearSelection();			
		} else if(Term == "Term 2 Unselected") {
			Term1UnselectedList.getSelectionModel().clearSelection();
			Term1ReservedList.getSelectionModel().clearSelection();
			Term2ReservedList.getSelectionModel().clearSelection();			
		} else if(Term == "Term 2 Reserved") {			
			Term1UnselectedList.getSelectionModel().clearSelection();
			Term2UnselectedList.getSelectionModel().clearSelection();
			Term1ReservedList.getSelectionModel().clearSelection();		
		}	
	}
	
	//EventHandlers : -----------------------------------------------------------------
	
	public void returnToSelectModules(EventHandler<ActionEvent> handler) {
		btnTerm1Back.setOnAction(handler);
	}
	
	public void confirmReservedModules(EventHandler<ActionEvent> handler) {
		btnTerm2Confirm.setOnAction(handler);
	}
	
}
