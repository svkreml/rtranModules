package gui.window.makery.address.model;

import Other.R_machine;
import Other.StopType;
import Other.Tape;
import Other.WorkExchange;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Created by Admin on 18.02.2017.
 */
public class DebuggerWindow extends Application{
	static TextField outputNode, outputCondition, outputStatement, tapeField;
	static TextArea outputMemories;
	static Button nextCond,nextStat,nextNode,toEnd;

	static R_machine r_machine;
	static WorkExchange workExchange;
	static  Stage primaryStage;
	static Stage currentStage;

	public DebuggerWindow(WorkExchange workExchange, Stage primaryStage) {
		DebuggerWindow.workExchange = workExchange;
		DebuggerWindow.primaryStage = primaryStage;

	}

	public DebuggerWindow() {
	}

	@Override
	public synchronized void start(Stage primaryStage1) throws Exception {
		Platform.setImplicitExit(false);
		outputNode = new TextField();
		outputNode.setEditable(false);
		outputCondition = new TextField();
		outputCondition.setEditable(false);
		outputStatement = new TextField();
		outputStatement.setEditable(false);
		outputMemories = new TextArea();
		outputMemories.setMinHeight(50);
		outputMemories.setEditable(false);
		tapeField = new TextField();
		tapeField.setEditable(false);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setHgap(5);
		grid.setVgap(5);

		nextCond = new Button("Следующее условие");
		nextStat = new Button("Следующее выражение");
		nextNode = new Button("Следующая вершина");
		toEnd = new Button("В конец программы");
		Button exit = new Button("Выход");

		nextCond.setOnAction(event -> {
			disableButtons();
			try {
				r_machine=workExchange.sendWork(StopType.CONDITION);
				enableButtons();
				if(r_machine.endOfAlgorythm()){
					disableButtons();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setFields();

		});

		nextStat.setOnAction(event -> {
			disableButtons();
			try {
				r_machine=workExchange.sendWork(StopType.STATEMENT);
				enableButtons();
				if(r_machine.endOfAlgorythm()){
					disableButtons();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setFields();


		});

		nextNode.setOnAction(event -> {
			disableButtons();
			try {
				this.r_machine=workExchange.sendWork(StopType.NODE);
				enableButtons();
				if(r_machine.endOfAlgorythm()){
					disableButtons();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setFields();

		});

		toEnd.setOnAction(event -> {
			disableButtons();
			try {
				r_machine=workExchange.sendWork(StopType.END);
				enableButtons();
				if(r_machine.endOfAlgorythm()){
					disableButtons();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setFields();

		});

		exit.setOnAction(event -> {
			currentStage.close();
		});
		grid.addRow(0, new Label("Вершина:"), outputNode);
		grid.addRow(1, new Label("Условие:"), outputCondition);
		grid.addRow(2, new Label("Выражение:"), outputStatement);
		grid.addRow(3, new Label("Памяти:"), outputMemories);
		grid.addRow(4, new Label("Лента:"), tapeField);
		grid.addColumn(2, nextCond,nextStat, nextNode, toEnd,exit);
		GridPane.setHgrow(outputStatement, Priority.ALWAYS);
		GridPane.setHgrow(outputCondition, Priority.ALWAYS);
		GridPane.setHgrow(outputNode, Priority.ALWAYS);
		GridPane.setHgrow(outputMemories, Priority.ALWAYS);

		Scene sceneFirst = new Scene(grid);
//		primaryStage.setTitle("Обмен");
//		primaryStage.setScene(sceneFirst);
		currentStage = new Stage();
		currentStage.setScene(sceneFirst);
//		primaryStage.initModality(Modality.WINDOW_MODAL);
		currentStage.show();


//		primaryStage.setTitle("Обмен");
//		primaryStage.setScene(sceneFirst);
//		primaryStage.show();
	}
	private static void disableButtons(){
		nextCond.setDisable(true);
		nextStat.setDisable(true);
		nextNode.setDisable(true);
		toEnd.setDisable(true);
	}
	private static void  enableButtons(){
		nextCond.setDisable(false);
		nextStat.setDisable(false);
		nextNode.setDisable(false);
		toEnd.setDisable(false);
	}

	public void setFields(){
		String currentNumber = r_machine.getCurrentNumber();
		if(currentNumber==null){
			outputNode.setText("null");
		}else {
			outputNode.setText(currentNumber);
		}
		if(r_machine.getCurrentCondition()==null){
			outputCondition.setText("null");
		}else {
			outputCondition.setText(r_machine.getCurrentCondition().toString());
		}
		if(r_machine.getCurrenntStatement()==null){
			outputStatement.setText("null");
		}else {
			outputStatement.setText(r_machine.getCurrenntStatement().toString());
		}
		if(r_machine.getTape()==null){
			tapeField.setText("null");
		}else {
			tapeField.setText(readTape());
		}
		outputMemories.setText(r_machine.stringMemories());
	}
	private String readTape(){
		Tape tape = r_machine.getTape();
		String tapeString = tape.toString();
		int number=tape.counter;
		return tapeString.substring(0,number)+"["+tapeString.charAt(number)+"]"+tapeString.substring(number+1,tapeString.length());
	}
//	@Override
//	public void run() {
//		launch(DebuggerWindow.class);
//	}

}
