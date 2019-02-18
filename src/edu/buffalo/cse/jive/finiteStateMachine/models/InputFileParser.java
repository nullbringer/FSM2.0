package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InputFileParser {

	private Set<String> allFields;
	private BlockingQueue<Event> events;

	public InputFileParser(String fileName) {
		this.allFields = new TreeSet<String>();
		this.events = new LinkedBlockingQueue<Event>();
		parseFile(fileName);
	}

	private void parseFile(String fileName) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("Field Write")) {
					String[] tokens = line.split(",");
					String object = tokens[4].substring(tokens[4].indexOf("=") + 1).replace("\"", "").trim();
					String field = tokens[5].substring(0, tokens[5].indexOf("=")).replace("\"", "").trim();
					String value = tokens[5].substring(tokens[5].indexOf("=") + 1).replace("\"", "").trim();
					String fld = object.replace("/", ".") + "." + field;
					events.add(new Event(fld, value));
					allFields.add(fld);
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<String> getAllFields() {
		return allFields;
	}

	public BlockingQueue<Event> getEvents() {
		return events;
	}

}