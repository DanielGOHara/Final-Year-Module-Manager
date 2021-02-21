package view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class ModuleSelectionRootPane extends BorderPane {

	private CreateProfilePane cpp;
	private ModuleSelectionMenuBar msmb;
	private SelectModulesPane smp;
	private ReserveModulesPane rmp;
	private OverviewPane op;
	private TabPane tp;
	
	public ModuleSelectionRootPane() {	
		
		//Create tab pane and disable tabs from being closed
		tp = new TabPane();
		tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		//Create panes
		cpp = new CreateProfilePane();
		msmb = new ModuleSelectionMenuBar();
		smp = new SelectModulesPane();
		rmp  = new ReserveModulesPane();
		op = new OverviewPane();
		
		//Create tabs with panes added
		Tab Profile = new Tab("Create Profile", cpp);
		Tab SelectModules = new Tab("Select Modules", smp);
		Tab ReserveModules = new Tab("Reserve Modules", rmp);
		Tab Overview = new Tab("Overview", op);
		
		//Add tabs to tab pane
		tp.getTabs().addAll(Profile, SelectModules, ReserveModules, Overview);
		tp.setPrefSize(1000, 1000);
		
		//Add menu bar and tab pane to this root pane
		this.setTop(msmb);
		this.setCenter(tp);
		this.setPrefHeight(1000);	
	}

	//Methods allowing sub-containers to be accessed by the controller.
	public CreateProfilePane getCreateProfilePane() {
		return cpp;
	}
	
	public ModuleSelectionMenuBar getModuleSelectionMenuBar() {
		return msmb;
	}
	
	public SelectModulesPane getSelectModulesPane() {
		return smp;
	}
	
	public ReserveModulesPane getReserveModulesPane() {
		return rmp;
	}
	
	public OverviewPane getOverviewPane() {
		return op;
	}
	
	//Method to allow the controller to change tabs and enables/disables tabs
	public void changeTab(int index) {	
		tp.getSelectionModel().select(index);
	}		
}	

