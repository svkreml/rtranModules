package gui.window.makery.address.model;

import Other.*;
import XmlReader.AlgorithmReaderNew;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Created by Admin on 28.01.2017.
 */
public class DebuggerRunner {
	String filepath;
	String tape;
	Stage primaryStage;

	public DebuggerRunner(String filepath, String tape, Stage primaryStage) {
		this.filepath = filepath;
		this.tape = tape;
		this.primaryStage=primaryStage;

	}
	public DebuggerRunner(String filepath, String tape) {
		this.filepath = filepath;
		this.tape = tape;
	}

	public void simpleStart() throws Exception{
		AlgorithmReaderNew algorithmReader = new AlgorithmReaderNew(filepath);
		algorithmReader.readMemories();
		algorithmReader.readAlgorithm();
		Tape tape = new Tape(this.tape);
		Storage storage = new Storage(algorithmReader.arms,algorithmReader.memoryHashMap,algorithmReader.alphabetHashMap);
		AllStorage allStorage = new AllStorage(storage,tape);
		WorkExchange workExchange = new WorkExchange();
		R_machine r_machine = new R_machine(allStorage,workExchange);
		r_machine.setDaemon(true);
		DebuggerWindow debugger = new DebuggerWindow(workExchange, primaryStage);
		r_machine.start();
		Application.launch(DebuggerWindow.class);
	}

	public void start () throws Exception{
		AlgorithmReaderNew algorithmReader = new AlgorithmReaderNew(filepath);
		algorithmReader.readMemories();
		algorithmReader.readAlgorithm();
		Tape tape = new Tape(this.tape);
		Storage storage = new Storage(algorithmReader.arms,algorithmReader.memoryHashMap,algorithmReader.alphabetHashMap);
		AllStorage allStorage = new AllStorage(storage,tape);
		WorkExchange workExchange = new WorkExchange();
		R_machine r_machine = new R_machine(allStorage,workExchange);
		r_machine.setDaemon(true);
		DebuggerWindow debugger = new DebuggerWindow(workExchange, primaryStage);
		r_machine.start();
		debugger.start(this.primaryStage);
	}
}
