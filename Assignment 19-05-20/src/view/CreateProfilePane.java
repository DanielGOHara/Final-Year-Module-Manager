package view;

import java.time.LocalDate;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import model.Course;
import model.Name;

public class CreateProfilePane extends GridPane {

	private ComboBox<Course> cboCourse;
	private TextField txtSurname, txtFirstName, txtPnumber, txtEmail;
	private DatePicker inputDate;
	private Button btnCreate;

	public CreateProfilePane() {
		
		//Styling
		this.setVgap(15);
		this.setHgap(20);
		this.setAlignment(Pos.CENTER);

		ColumnConstraints column0 = new ColumnConstraints();
		column0.setHalignment(HPos.RIGHT);

		this.getColumnConstraints().addAll(column0);
		
		//Create labels
		Label lblTitle = new Label("Select course: ");
		Label lblPnumber = new Label("Input P number: ");
		Label lblFirstName = new Label("Input first name: ");
		Label lblSurname = new Label("Input surname: ");
		Label lblEmail = new Label("Input email: ");
		Label lblDate = new Label("Input date: ");
		
		//Setup ComboBox
		cboCourse = new ComboBox<Course>(); //will be populated via method towards end of class
		
		//Setup text fields
		txtFirstName = new TextField();
		txtSurname = new TextField();
		txtPnumber = new TextField();
		txtEmail = new TextField();	
		inputDate = new DatePicker();
		
		BooleanBinding BB = new BooleanBinding() {
							
			{
				super.bind(txtFirstName.textProperty(), txtSurname.textProperty(), txtPnumber.textProperty(), 
						   txtEmail.textProperty(), inputDate.getEditor().textProperty()); 
			}	
			
			@Override
			protected boolean computeValue() {
				return (txtFirstName.getText().isEmpty() || txtSurname.getText().isEmpty() || txtPnumber.getText().isEmpty() 
				         || txtEmail.getText().isEmpty() || inputDate.getValue() == null); 
			}	
		};
		
		//Initialise create button and binds it to BB
		btnCreate = new Button("Create Profile");
		btnCreate.disableProperty().bind(BB);

		//Add controls and labels to container
		this.add(lblTitle, 0, 0);
		this.add(cboCourse, 1, 0);

		this.add(lblPnumber, 0, 1);
		this.add(txtPnumber, 1, 1);
		
		this.add(lblFirstName, 0, 2);
		this.add(txtFirstName, 1, 2);

		this.add(lblSurname, 0, 3);
		this.add(txtSurname, 1, 3);
		
		this.add(lblEmail, 0, 4);
		this.add(txtEmail, 1, 4);
		
		this.add(lblDate, 0, 5);
		this.add(inputDate, 1, 5);
			
		this.add(new HBox(), 0, 6);
		this.add(btnCreate, 1, 6);
		
	}
	
	//Method to allow the controller to populate the course ComboBox
	public void populateCourseComboBox(Course[] courses) {		
		cboCourse.getItems().addAll(courses);
		cboCourse.getSelectionModel().select(0); //select first course by default		
	}
	
	public void setProfile(Course Course, String PNumber, String Firstname, String Surname, String Email, LocalDate Date) {
		cboCourse.getSelectionModel().select(Course);
		txtPnumber.setText(PNumber);
		txtFirstName.setText(Firstname);
		txtSurname.setText(Surname);
		txtEmail.setText(Email);
		inputDate.setValue(Date);
		
	}	
	
	public void setPNumber(String PNumber) {
		txtPnumber.setText(PNumber);
	}
	
	//Methods to retrieve the form selection/input
	public Course getSelectedCourse() {	
		return cboCourse.getSelectionModel().getSelectedItem();
	}
	
	public String getPnumberInput() {	
		return txtPnumber.getText();	
	}
	
	public Name getName() {		
		return new Name(txtFirstName.getText(), txtSurname.getText());		
	}

	public String getEmail() {		
		return txtEmail.getText();		
	}
	
	public LocalDate getDate() {		
		return inputDate.getValue();		
	}
	
	//method to attach the create profile button event handler
	public void addCreateProfileHandler(EventHandler<ActionEvent> handler) {
		btnCreate.setOnAction(handler);
	}
}
