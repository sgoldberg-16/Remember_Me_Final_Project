//////////////// FILE HEADER//////////////////////////////////////////////
//
// Title: HelloFX
// Files:  MainLayout.java, ContactListGUI.java, ContactListADT.java,ContactListTest.java, ContactShallow.java, MainLayout.java, TestInputTxt.java
// Course:  CS 400, Summer, 2020
// Lecture: 002
// Author:  Sam Goldberg
// Email:   sdgoldberg@wisc.edu
// Lecturer's Name: Florian Heimerl
//
//////////////////////////////////////////////////////////////////////////////
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainLayout extends BorderPane {
	private File file;
	private Stage pStage;
	private Scanner scan;
	//ContactList
	private ContactList contacts;
	private ContactList recentsList;
	private ContactList favorites;
	//buttons
	private Button add;
	private Button remove;
	private Button close;
	private Button changeFile;
	//Labels
	private Label recents;
	private Label filterLabel;
	private Label currentFile;
	//VBox
	private VBox recent;
	private VBox fileDirect;
	//ScrollPane
	private ScrollPane recentScroll;
	//ComboBox
	private ComboBox<String> filterBy;
	//Hbox
	private HBox buttons;
	
	private Scene contactDeep;
	public MainLayout(String filename, Stage stage) throws FileNotFoundException {
		contacts = new ContactList();
		recentsList = new ContactList();
		File file = new File(filename);
		pStage = stage;
		scan = new Scanner(file);
//insert contacts from file into the list
		int count = 1;
		while (scan.hasNextLine()) {
			String infoStr = "" + scan.nextLine();
			String[] info = infoStr.split(",");
			Contact newContact = new Contact(info[1] + " " + info[0], info[2]);
			contacts.insert(newContact);
			System.out.print(count + " ");
			contacts.print();
			count++;
		}
		//create a recents tab on the left
		recent = new VBox(8);
	    recents = new Label("      Recents           ");
		recents.setFont(new Font("Times New Roman", 30));
		recent.setMargin(recents, new Insets(20));
		recent.getChildren().add(recents);
		this.setAlignment(recents, Pos.TOP_LEFT);
		recentScroll = new ScrollPane(recent);
		recentScroll.setVisible(true);
		recentScroll.setPannable(true);
		recentScroll.setFitToHeight(true);
		recentScroll.setFitToWidth(true);
		this.setLeft(recentScroll);
		//create contact shallow objects from contact list
		VBox rows = new VBox();
		HBox columns;
		int index = 0;
		while (index < contacts.size()) {
			columns = new HBox();
			int i = 0;
			while (i < 4 && index < contacts.size()) {
				ContactShallow lay = new ContactShallow(contacts.get(index), this);
				RecentsHandler rh = new RecentsHandler(lay, this);
				lay.setOnMouseClicked(rh);
				columns.getChildren().add(lay);
				index++;
				i++;
			}
			rows.getChildren().add(columns);
		}
		// Set the Buttons on the bottom of the screen
		add = new Button("Add Contact");
		remove = new Button("Remove Contact");
		close = new Button("Close Application");
		filterBy = new ComboBox<String>();
		filterLabel = new Label("Filter By");
		filterBy.getItems().addAll("Favorites", "Family", "Recent");
		buttons = new HBox(10);
		buttons.getChildren().addAll(add, remove, close, filterLabel, filterBy);
		this.setBottom(buttons);
		this.setCenter(rows);
		//Set the header of this scene
		Label header = new Label("Contacts");
		header.setFont(new Font("Arial", 40));
		this.setTop(header);
		this.setAlignment(header, Pos.TOP_CENTER);

		fileDirect = new VBox(10);
		currentFile = new Label("Current File: " + filename);
		changeFile = new Button("Select a Different Contact File");
		fileDirect.getChildren().addAll(currentFile, changeFile);
		fileDirect.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
		this.setRight(fileDirect);
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));
	}
	private class RecentsHandler implements EventHandler<MouseEvent>{
		ContactShallow contact;
		MainLayout layout;
		private RecentsHandler(ContactShallow contact, MainLayout l) {
			this.contact = contact;
			layout = l;
		}
		@Override
		public void handle(MouseEvent e) {
			contactDeep = new Scene(new ContactDeepLayout(contact.getPerson(), layout ), pStage.getWidth(), pStage.getHeight());
			pStage.setScene(contactDeep);
			recentsList.insert(contact.getPerson());
			ContactShallow newShallow = new ContactShallow(contact.getPerson(), contact.getMainlayout());
			recent.getChildren().add(newShallow);
			
		}
		
	}
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * @return the pStage
	 */
	public Stage getpStage() {
		return pStage;
	}
	/**
	 * @param pStage the pStage to set
	 */
	public void setpStage(Stage pStage) {
		this.pStage = pStage;
	}
	/**
	 * @return the scan
	 */
	public Scanner getScan() {
		return scan;
	}
	/**
	 * @param scan the scan to set
	 */
	public void setScan(Scanner scan) {
		this.scan = scan;
	}
	/**
	 * @return the contacts
	 */
	public ContactList getContacts() {
		return contacts;
	}
	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(ContactList contacts) {
		this.contacts = contacts;
	}
	/**
	 * @return the recentsList
	 */
	public ContactList getRecentsList() {
		return recentsList;
	}
	/**
	 * @param recentsList the recentsList to set
	 */
	public void setRecentsList(ContactList recentsList) {
		this.recentsList = recentsList;
	}
	/**
	 * @return the add
	 */
	public Button getAdd() {
		return add;
	}
	/**
	 * @param add the add to set
	 */
	public void setAdd(Button add) {
		this.add = add;
	}
	/**
	 * @return the remove
	 */
	public Button getRemove() {
		return remove;
	}
	/**
	 * @param remove the remove to set
	 */
	public void setRemove(Button remove) {
		this.remove = remove;
	}
	/**
	 * @return the close
	 */
	public Button getClose() {
		return close;
	}
	/**
	 * @param close the close to set
	 */
	public void setClose(Button close) {
		this.close = close;
	}
	/**
	 * @return the filterBy
	 */
	public ComboBox<String> getFilterBy() {
		return filterBy;
	}
	/**
	 * @param filterBy the filterBy to set
	 */
	public void setFilterBy(ComboBox<String> filterBy) {
		this.filterBy = filterBy;
	}
	/**
	 * @return the filterLabel
	 */
	public Label getFilterLabel() {
		return filterLabel;
	}
	/**
	 * @param filterLabel the filterLabel to set
	 */
	public void setFilterLabel(Label filterLabel) {
		this.filterLabel = filterLabel;
	}
	/**
	 * @return the buttons
	 */
	public HBox getButtons() {
		return buttons;
	}
	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(HBox buttons) {
		this.buttons = buttons;
	}
	/**
	 * @return the favorites
	 */
	public ContactList getFavorites() {
		return favorites;
	}
	/**
	 * @param favorites the favorites to set
	 */
	public void setFavorites(ContactList favorites) {
		this.favorites = favorites;
	}
	/**
	 * @return the recent
	 */
	public VBox getRecent() {
		return recent;
	}
	/**
	 * @param recent the recent to set
	 */
	public void setRecent(VBox recent) {
		this.recent = recent;
	}
}