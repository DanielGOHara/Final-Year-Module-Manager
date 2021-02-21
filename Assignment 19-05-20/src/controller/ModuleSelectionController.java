package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import model.Course;
import model.Delivery;
import model.Module;
import model.StudentProfile;

import view.ModuleSelectionRootPane;
import view.OverviewPane;
import view.ReserveModulesPane;
import view.SelectModulesPane;
import view.CreateProfilePane;
import view.ModuleSelectionMenuBar;

public class ModuleSelectionController {

	//Fields to be used throughout class
	private StudentProfile model;
	private ModuleSelectionRootPane view;
	
	private CreateProfilePane cpp;
	private ModuleSelectionMenuBar msmb;
	private SelectModulesPane smp;
	private ReserveModulesPane rmp;
	private OverviewPane op;
	
	private Optional<ButtonType> result;

	public String path = "B:\\Programming\\IntelliJ Idea Workspace\\Object Oriented Development\\Assignment 19-05-20\\";

	public ModuleSelectionController(StudentProfile model, ModuleSelectionRootPane view) {
		
		//Initialise model and view fields
	    this.model = model;
		this.view = view;
		
		//Initialise view SubContainer fields
		cpp = view.getCreateProfilePane();
		msmb = view.getModuleSelectionMenuBar();
		smp = view.getSelectModulesPane();
		rmp  = view.getReserveModulesPane();
		op = view.getOverviewPane();

		//Populate ComboBox in create profile pane with courses using the setupAndGetCourses method below
		cpp.populateCourseComboBox(importAndSetupCourses());

		//Attach event handlers to view using private helper method
		this.attachEventHandlers();	
		
	}
	
	//A helper method used to attach event handlers
	private void attachEventHandlers() {
		
		//Attach an event handler to the create profile pane
		cpp.addCreateProfileHandler(new CreateProfileHandler());
		
		//SelectModulesPane handler attachments
		smp.returnToCreateProfile(new returnToCreateProfile());
		smp.resetSelectModulesPane(new resetSelectModulesPane());
		smp.submitModules(new submitSelectModules());
		
		//ReserveModulesPane handler attachments
		rmp.returnToSelectModules(new returnToSelectModules());
		rmp.confirmReservedModules(new confirmReservedModules());
		
		//OverviewPane handler attachments
		op.returnToReserveModulesPane(new returnToReserveModulesPane());
		op.saveProfileOverview(new saveProfileOverview());
		
		//Attach an event handler to the menu bar that closes the application
		msmb.addSaveHandler(new saveProfile());
		msmb.addLoadHandler(new loadProfile());
		msmb.addAboutHandler(new aboutProfile());
		msmb.addExitHandler(e -> System.exit(0));
		
	}
	
	//CreateProfilePane Handlers : -----------------------------------------------------------------
		
	private class CreateProfileHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
		if(inputValidation() == true) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alertDialogBuilder(alert, "Confirmation Dialog", "Overwrite Current Profile?", "If you continue "
					                    + "the current profile (If there is one) will be wiped and replaced!");
		
			if(result.get() == ButtonType.OK) {
				//Reset all of the pane's
				smp.resetSelectModulesPane();
				rmp.resetReserveModulesPane();
				op.resetOverviewPane();
			
			//Store information in model
			model.setCourse(cpp.getSelectedCourse());
			model.setpNumber(cpp.getPnumberInput());
			model.setStudentName(cpp.getName());
			model.setEmail(cpp.getEmail());
			model.setSubmissionDate(cpp.getDate());
			
			//Process modules on selected course into the ListView on the next form
			for (Module m : model.getCourse().getAllModulesOnCourse()) {
				 if (m.getRunPlan() == Delivery.TERM_1) {
					 smp.setModules("Term 1", m);
					 
				 } else if (m.getRunPlan() == Delivery.TERM_2) {
					 smp.setModules("Term 2", m);
					 
				 } else if (m.getRunPlan() == Delivery.YEAR_LONG) {
					 smp.setModules("Year Long", m);
					 
				 } 
			}
			
			//Set the credits for both Term's
		    smp.setCredits("Term 1");
		    smp.setCredits("Term 2");
		    
		    /*Retrieve the unselected ListView's in the SelectModulesPane and attach them to the unselected 
		      ListView's in the ReserveModulesPane so any changes in one pane affects the other */
		    ObservableList<Module> TempList = smp.getUnselectedModules("Term 1");
			rmp.setUnselectedModules("Term 1", TempList);
			TempList = smp.getUnselectedModules("Term 2");
			rmp.setUnselectedModules("Term 2", TempList);
			
			//Change the tab to tab index 1
		    view.changeTab(1);
		  } 
		}
	  }	
	}

	//SelectModulesPane Handlers : -----------------------------------------------------------------
	
	private class returnToCreateProfile implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {	
			//Change tab to index 0
			view.changeTab(0);
		}
	}
	
	private class resetSelectModulesPane implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
			//Reset all of the pane's
			smp.resetSelectModulesPane();
			rmp.resetReserveModulesPane();
			op.resetOverviewPane();
			
			//Process modules on selected course into the ListView on the next form
			for (Module m : model.getCourse().getAllModulesOnCourse()) {
				if (m.getRunPlan() == Delivery.TERM_1) {
					smp.setModules("Term 1", m);
					
				} else if (m.getRunPlan() == Delivery.TERM_2) {
					smp.setModules("Term 2", m);
					
				} else if (m.getRunPlan() == Delivery.YEAR_LONG) {
					smp.setModules("Year Long", m);
					
				}
			}				
			//Set the credits for both Term's
		    smp.setCredits("Term 1");
		    smp.setCredits("Term 2");		    
		}		
	}
	
	private class submitSelectModules implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {	
			
			//Clear the selected modules in the model and reset the submitCounter
			model.clearSelectedModules();		
			smp.resetSubmitCounter();
			
			//Process the selected modules and add them to the model
			for(Module m : smp.getSelectedModules().getItems()) {
				model.addSelectedModule(m);
			}		
			//Change the tab index to 2
			view.changeTab(2);			
		}
	}
	
	//ReserveModulesPane Handlers : -----------------------------------------------------------------
	
	private class returnToSelectModules implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			//Change the tab index to 1s
			view.changeTab(1);
		}
	}

	private class confirmReservedModules implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
			int TempCounter = 0;
			
			//Clear the reserved modules in the model and reset the OverviewPane
			model.clearReservedModules();
			op.resetOverviewPane();
			
			//Gives TempCounter the same value as the submitCounter
			TempCounter = smp.getSubmitCounter();
			
			//Checks the credit count on the SelectModulesPane and if it equals 120 moves to the OverviewPane
			if(TempCounter != 0) {
				Alert alert = new Alert(AlertType.WARNING);
				alertDialogBuilder(alert, "WARNING", "Missing Credits", "Please check and ensure you have" 
						                            + " the full 120 credits on the Select Modules Pane"
													+ " and have submitted them!");
				
			} else {
			//Process the reserved modules and add them to the model
			for(Module m : rmp.getReservedModules().getItems()) {
				model.addReservedModule(m);
			}				
			
			//Retrieve the selected modules from the model and add them to the OverviewPane
			for(Module m : model.getAllSelectedModules()) {
				op.setSelectedModules(m);
			}
			
			//Retrieve the reserved modules from the model and add them to the OverviewPane
			for(Module m : model.getAllReservedModules()) {
				op.setReservedModules(m);
			}	
			
			//Convert the submission date to a specific Date Format and convert it to a string
			String FormattedDate = model.getSubmissionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			
			//Add the profile details to the OverviewPane
			op.setProfileDetails(model.getStudentName().getFirstName(), model.getStudentName().getFamilyName(), 
					             model.getpNumber(), model.getEmail(), FormattedDate, model.getCourse().toString());
			
			//Change the tab index to 3
			view.changeTab(3);
			}
		}
	}
	
	//OverPane Handlers : ---------------------------------------------------------------------------
	
	private class returnToReserveModulesPane implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			//Change the tab index to 2
			view.changeTab(2);
		}
	}
		
	private class saveProfileOverview implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			try {
				
				//Format both the Date and the entire Model
				String FormattedDate = model.getSubmissionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				String FormattedModel = ("\n Course: " + model.getCourse() + "\n P-Number: " + model.getpNumber() + 
										 "\n Name: " + model.getStudentName().getFirstName() + " " + model.getStudentName().getFamilyName() +
									     "\n Email: " + model.getEmail() +
										 "\n Submission Date: " + FormattedDate + "\n Selected Module's: " + model.getAllSelectedModules() +
										 "\n Reserved Module's: " + model.getAllReservedModules());			
				
				//Save the model in text form in the file Profile.txt
				PrintWriter writer;
				File file = new File( path + "Profile.txt");
	            writer = new PrintWriter(new FileWriter(file, true));
	            writer.println(FormattedModel);
	            writer.close();
	            Alert alert = new Alert(AlertType.INFORMATION);
	            alertDialogBuilder(alert, "Information Dialog", "Save success", "Profile Overview saved to Profile.txt");
	        } catch (IOException ex) {
	        	System.out.println("Error Saving");
	        }
		}
	}
	
	//MenuBar Handlers : ----------------------------------------------------------------------------
	
	private class saveProfile implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
			//Creates a new file from the Profile.dat file
			File DeleteFile = new File(path + "Profile.dat");
			
			//Checks if the file Profile.dat exists and if it does deletes it
			if(DeleteFile.exists()) {
				DeleteFile.delete();
			}
			
			if(smp.getSelectedModules().getItems().isEmpty()) {
				Alert alert = new Alert(AlertType.WARNING);
				alertDialogBuilder(alert, "Warning", "Empty Profile", "You cannot save an empty profile, make sure you create a profile" +
										  " before attempting to save!");
			} else {
			//Clears the model of selected and reserved modules
			model.getAllSelectedModules().clear();
			model.getAllReservedModules().clear();
			
			//Retrieve and save the profile data to the model
			model.setCourse(cpp.getSelectedCourse());
			model.setpNumber(cpp.getPnumberInput());
			model.setStudentName(cpp.getName());
			model.setEmail(cpp.getEmail());
			model.setSubmissionDate(cpp.getDate());		
			
			//Retrieve the selected and reserved ListViews and add them to the model
			for(Module m : smp.getSelectedModules().getItems()) {
				model.addSelectedModule(m);
			}		
			for(Module m : rmp.getReservedModules().getItems()) {
				model.addReservedModule(m);
			}		
			
			//Use ObjectOutputStream to save the model to a .dat file in binary format
			try (ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(path + "Profile.dat"));) {
				file.writeObject(model); 
				file.flush();	
	            Alert alert = new Alert(AlertType.INFORMATION);
				alertDialogBuilder(alert, "Information Dialog", "Save success", "Profile saved to Profile.dat");
			}
			catch (IOException ioExcep){
				System.out.println("Error saving");
				ioExcep.printStackTrace();
			}
		  }
       }
	}
	
	private class loadProfile implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {	
			
			int creditCounter = 0;
			
			//Reset all of the Pane's
			smp.resetSelectModulesPane();
			rmp.resetReserveModulesPane();
			op.resetOverviewPane();
			
			//Creates a new file from the Profile.dat file
			File checkFile = new File(path + "Profile.dat");
			
			//Checks if the file Profile.dat exists
			if(checkFile.exists()) {
				//Use ObjectOutputStream to load the model from the Profile.dat file from binary form
				try(ObjectInputStream file = new ObjectInputStream(new FileInputStream(path + "Profile.dat"));) {
					model = (StudentProfile) file.readObject(); //reads the model object back from a file	
					Alert alert = new Alert(AlertType.INFORMATION);
					alertDialogBuilder(alert, "Information Dialog", "Load success", "Profile loaded from Profile.dat");							
				}
				catch(IOException ioExcep){
					  System.out.println("Error loading");
				}
				catch(ClassNotFoundException c) {
					  System.out.println("Class Not found");
				}	
			
				//Retrieve the saved profile data and display it on the CreateProfilePane
				cpp.setProfile(model.getCourse(), model.getpNumber(), model.getStudentName().getFirstName(), 
							   model.getStudentName().getFamilyName(), model.getEmail(), model.getSubmissionDate());
			
				//Initialise two List's, one to store the duplicate's to be removed and the other to store the non duplicate list
				List<Module> RemoveList = new ArrayList<Module>();
				List<Module> CourseModuleList = new ArrayList<Module>();
			
				//Retrieve all of the module's and add them to the CourseModuleList
				for(Module m : model.getCourse().getAllModulesOnCourse()) {
					CourseModuleList.add(m);
				}
			
				//Compare the saved selected and reserved module's with the main module model
				for(Module m : model.getCourse().getAllModulesOnCourse()) {
					for(Module m2 : model.getAllReservedModules()) {
						for(Module m3 : model.getAllSelectedModules()) {
							if(m.equals(m2) || m.equals(m3)) {
								RemoveList.add(m);
							}
						}
					}
				}
			
				//Remove the selected and reserved module's from the CourseModuleList
				CourseModuleList.removeAll(RemoveList);
			
				//Process the module's from the CourseModuleList and add them to the unselected ListView's
				for(Module m : CourseModuleList) {
					//If statement to duplication of compulsory modules
					if(m.isMandatory() == false) {
						if(m.getRunPlan() == Delivery.TERM_1) {
							smp.setModules("Term 1", m);
						} else if (m.getRunPlan() == Delivery.TERM_2) {
							smp.setModules("Term 2", m);
						} else if (m.getRunPlan() == Delivery.YEAR_LONG) {
							smp.setModules("Year Long", m);
						}
					}
				}
			
				//Process the module's from the selected module's model and add them to the selected ListView's
				for(Module m : model.getAllSelectedModules()) {
					if(m.getRunPlan() == Delivery.TERM_1) {
						smp.setSelectedModules("Term 1", m);
					} else if (m.getRunPlan() == Delivery.TERM_2) {
						smp.setSelectedModules("Term 2", m);
					} else if (m.getRunPlan() == Delivery.YEAR_LONG) {
						smp.setSelectedModules("Year Long", m);
					}
				}
			
				//Process the module's from the reserved module's model and add them to the reserved ListView's
				for(Module m : model.getAllReservedModules()) {
					if(m.getRunPlan() == Delivery.TERM_1) {
						rmp.setReservedModules("Term 1", m);
					} else if (m.getRunPlan() == Delivery.TERM_2) {
						rmp.setReservedModules("Term 2", m);
					}
				}			
			
				//Set the credits for both Term's
				smp.setCredits("Term 1");
				smp.setCredits("Term 2");
		    
				/*Retrieve the unselected ListView's in the SelectModulesPane and attach them to the unselected 
		      	  ListView's in the ReserveModulesPane so any changes in one pane affects the other */
				ObservableList<Module> TempList = smp.getUnselectedModules("Term 1");
				rmp.setUnselectedModules("Term 1", TempList);
				TempList = smp.getUnselectedModules("Term 2");
				rmp.setUnselectedModules("Term 2", TempList);	
			
				for(Module m : smp.getSelectedModules().getItems()) {
					creditCounter += m.getCredits();
				}
			
				for(Module m : rmp.getReservedModules().getItems()) {
					creditCounter += m.getCredits();
				}
			
				//If the creditCounter equals 180 the user is prompted if they want to create an overview in the overview pane
				if(creditCounter == 180) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alertDialogBuilder(alert, "Confirmation", "Create Overview?", "As this profile has all of its required credits" +
											  " would you like to create an overview in the overview pane?");
				
					//If the user selects OK an overview is created
					if(result.get() == ButtonType.OK) {
						//Retrieve the selected modules from the model and add them to the OverviewPane
						for(Module m : model.getAllSelectedModules()) {
							op.setSelectedModules(m);
						}
				
						//Retrieve the reserved modules from the model and add them to the OverviewPane
						for(Module m : model.getAllReservedModules()) {
							op.setReservedModules(m);
						}	
				
						//Convert the submission date to a specific Date Format and convert it to a string
						String FormattedDate = model.getSubmissionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				
						//Add the profile details to the OverviewPane
						op.setProfileDetails(model.getStudentName().getFirstName(), model.getStudentName().getFamilyName(), 
						             	 model.getpNumber(), model.getEmail(), FormattedDate, model.getCourse().toString());	
					}
				}
				
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alertDialogBuilder(alert, "Error", "Error Loading File", "An error occured trying to load the file Profile.dat" +
									  ", please double check it exists and is in the same directory as this application!");
		}
	  }
    }
	
	private class aboutProfile implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alertDialogBuilder(alert, "Application Information", "Student Profile Creator", "This application allows for the creation of profiles" +
									  " which students can select and reserve modules for the next academic year.");
		}
	}
	
	//Constructs alert dialogs
	private Optional<ButtonType> alertDialogBuilder(Alert alert, String title, String header, String content) {
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    result = alert.showAndWait();	
	    return result;
	}
	
	//Validates all inputs on the CreateProfilePane
	private boolean inputValidation() {
		
		String forename	= cpp.getName().getFirstName(), surname = cpp.getName().getFamilyName(), email = cpp.getEmail();	
		int correctInputsCounter = 0, counter = 0;
		
			//Validate the PNumber input is 8 characters long and includes a P followed by 7 numbers
		if(cpp.getPnumberInput().length() == 8) {
				
				//Creates an int to count the number of numbers and a char array to store the PNumber string	
			char[] chars = new char[8];
				
				//Splits the string into separate chars and adds them to the char array
			for(int i = 0; i < 8; i++) {
				chars[i] = cpp.getPnumberInput().toUpperCase().charAt(i);
			}
				
				//Checks if the first index in the char array is a P then enters a for loop for each char
			if(chars[0] == 'P') {
				for(char c : chars) {
				//Checks if the current indexed char is a number and increments the counter if it is
					if(Character.isDigit(c)) {
						counter++;
					}
				}
			} else if(chars[0] != 'P') {
					//If the first index in the char array isn't a P an error dialog is displayed and the counter is reset
				Alert alert = new Alert(AlertType.ERROR);
				alertDialogBuilder(alert, "Error", "P-Number Input Error", "Please check that " +
										  "the PNumber is a P followed by 7 numbers!");	
					counter = 0;
			}
				
		if(counter == 7) {
			//Increments the value in CorrectInputsCounter indicating the PNumber is correct and validated
			correctInputsCounter++;
		} else if(counter != 7 && counter != 0) {
			//Displays an error dialog if the PNumber has 6 or less numbers
			Alert alert = new Alert(AlertType.ERROR);
			alertDialogBuilder(alert, "Error", "P-Number Input Error", "Please check that " +
									  "the PNumber is a P followed by 7 numbers!");		
		}
		} else if(cpp.getPnumberInput().length() != 8) {
				//Displays an error dialog if the PNumber input isn't 8 characters in length
				Alert alert = new Alert(AlertType.ERROR);
				alertDialogBuilder(alert, "Error", "P-Number Length Error", "Please check that the " +
										  "PNumber is 8 characters long!");
		}
		
		//If statement only allows access if the first test passed
		if(correctInputsCounter == 1) {
			//Validate the name with a name regex to ensure it includes no numbers or special symbols
			Pattern namePat = Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE);
			Matcher forenameMatcher = namePat.matcher(forename);
			
			if(forenameMatcher.find() == true) {
				//Increments the value in CorrectInputsCounter indicating the forename has no numbers or illegal symbols
				correctInputsCounter++;
			} else if(forenameMatcher.find() == false) {	
				//Displays an error dialog if the forename contains any incorrect symbols or numbers
				Alert alert = new Alert(AlertType.ERROR);
				alertDialogBuilder(alert, "Error", "Forename Input Error", "Please check that there" +
										  " are no numbers or symbols in the forename!");
			}		
		
		//If statement only allows access if the second test passed
		if(correctInputsCounter == 2) {
			Matcher surnameMatcher = namePat.matcher(surname);
			
			if(surnameMatcher.find() == true) {
				//Increments the value in CorrectInputsCounter indicating the surname has no numbers or illegal symbols
				correctInputsCounter++;
			} else if(surnameMatcher.find() == false) {	
				//Displays an error dialog if the surname contains any incorrect symbols or numbers
				Alert alert = new Alert(AlertType.ERROR);
				alertDialogBuilder(alert, "Error", "Surname Input Error", "Please check that there" +
										  " are no numbers or symbols in the surname!");
			}
		  }
		}
		
		//If statement only allows access if the third test passed
		if(correctInputsCounter == 3) {
			//Validate the email address with a email regex to ensure it has at least one @ and one .
			String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
			Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
			Matcher emailMatcher = emailPat.matcher(email);	
			
			//Checks the result to see if the email is correctly formatted 
			if(emailMatcher.find() == true) {
				//Increments the value in CorrectInputsCounter indicating the email is correctly formatted
				correctInputsCounter++;
			} else if(emailMatcher.find() == false) {
				//Displays an error dialog if the email is not correctly formatted
				Alert alert = new Alert(AlertType.ERROR);
				alertDialogBuilder(alert, "Error", "Email Input Error", "Please check that the email" +
										  " is in the right format and correctly inputted!");
			}
		}
		
		//Compares the value of correctInputsCounter to 4 if it matches all validations were successful and it returns true else it returns false
		if(correctInputsCounter == 4) {
			return true;
		} else {
			return false;
		}
	}
	
	//Imports modules and sets up courses
	private Course[] importAndSetupCourses() {
		@SuppressWarnings("unused")
		String CourseName, ModuleCode, ModuleName, Credits, Mandatory, DeliveryPlan, Whitespace;

		//Creates a course array with a size of 2
		Course[] Courses = new Course[2];
		//Creates two courses Computer Science and Software Engineering
		Course compSci = new Course("Computer Science");
		Course softEng = new Course("Software Engineering");
		
		try {
			
			//Creates a scanner with the file Courses.txt with a delimiter
			Scanner sc = new Scanner(new File(path + "Courses.txt"));
			sc.useDelimiter(",");

			//Keeps looping until there is no longer another line in the txt file
			while(sc.hasNextLine()) {
				
			//Scans each line putting the data in the corresponding variable
			CourseName = sc.next();
			ModuleCode = sc.next();
			ModuleName = sc.next();
			Credits = sc.next();
			Mandatory = sc.next();
			DeliveryPlan = sc.next();
			Whitespace = sc.next();
			
			//If the module is mandatory <Compulsory> is added to it, making it easier to notice
			if(Boolean.parseBoolean(Mandatory) == true) {
				ModuleName = ModuleName + " <Compulsory>";
			}
				
			//Creates a module with the values scanned 
			Module Module = new Module(ModuleCode, ModuleName, Integer.parseInt(Credits), Boolean.parseBoolean(Mandatory), Delivery.valueOf(DeliveryPlan));

				//Splits up the modules based on the course they have and adds them to the specified course
				if(CourseName.contentEquals("Both")) {
					compSci.addModule(Module);
					softEng.addModule(Module);
				} else if(CourseName.contentEquals("Computer Science")) {
					compSci.addModule(Module);
				} else if(CourseName.contentEquals("Software Engineering")) {
					softEng.addModule(Module);
				} 
			}
		
		//Catches any errors and exceptions
		} catch (IOException ioExcep){
            System.out.println("Error loading");
            ioExcep.printStackTrace();
		}
		
		//Adds both courses to the course array
		Courses[0] = compSci;
		Courses[1] = softEng;
		
		return Courses;
	}
	
	//Generates all module and course data and returns courses within an array -- NO LONGER USED --
	@SuppressWarnings("unused")
	private Course[] setupAndGetCourses() {
		
		Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, Delivery.TERM_1);
		Module ctec3451 = new Module("CTEC3451", "Development Project", 30, true, Delivery.YEAR_LONG);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, Delivery.TERM_2);	
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, Delivery.TERM_2);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, Delivery.TERM_1);
		Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, Delivery.TERM_1);	
		Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, Delivery.TERM_2);	
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, Delivery.TERM_2);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, Delivery.TERM_2);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, Delivery.TERM_2);
		Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, Delivery.TERM_1);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, Delivery.TERM_2);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, Delivery.TERM_1);
		Module imat3611 = new Module("IMAT3611", "Computer Ethics and Privacy", 15, false, Delivery.TERM_1);
		Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, Delivery.TERM_1);
		Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, Delivery.TERM_2);
		Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, Delivery.TERM_2);

		Course compSci = new Course("Computer Science");
		compSci.addModule(imat3423);
		compSci.addModule(ctec3451);
		compSci.addModule(ctec3902_CompSci);
		compSci.addModule(ctec3110);
		compSci.addModule(ctec3605);
		compSci.addModule(ctec3606);
		compSci.addModule(ctec3410);
		compSci.addModule(ctec3904);
		compSci.addModule(ctec3905);
		compSci.addModule(ctec3906);
		compSci.addModule(imat3410);
		compSci.addModule(imat3406);
		compSci.addModule(imat3611);
		compSci.addModule(imat3613);
		compSci.addModule(imat3614);
		compSci.addModule(imat3428_CompSci);

		Course softEng = new Course("Software Engineering");
		softEng.addModule(imat3423);
		softEng.addModule(ctec3451);
		softEng.addModule(ctec3902_SoftEng);
		softEng.addModule(ctec3110);
		softEng.addModule(ctec3605);
		softEng.addModule(ctec3606);
		softEng.addModule(ctec3410);
		softEng.addModule(ctec3904);
		softEng.addModule(ctec3905);
		softEng.addModule(ctec3906);
		softEng.addModule(imat3410);
		softEng.addModule(imat3406);
		softEng.addModule(imat3611);
		softEng.addModule(imat3613);
		softEng.addModule(imat3614);

		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;
		
		return courses;
		
	}
}

