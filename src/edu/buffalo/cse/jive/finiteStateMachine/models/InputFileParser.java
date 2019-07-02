package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 *@author Amlan Gupta
 *@email amlangup@buffalo.edu
 */
/**
 * Parser the Input CSV file and extracts all attributes and Field Write Events
 *
 */
public class InputFileParser {

	private Set<String> allFields;
	private BlockingQueue<Event> events;

	public InputFileParser(String fileName) {
		this.allFields = new TreeSet<String>();
		this.events = new LinkedBlockingQueue<Event>();
		parseFile(fileName);
	}

	/**
	 * File should be in the same format as that of JIVE's execution trace. Update
	 * this method if you expect other formats.
	 * 
	 * @param fileName
	 */
	private void parseFile(String fileName) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			String line;
			int counter = 0;
			String lastMethod = null;
			
			while ((line = bufferedReader.readLine()) != null) {
				
				// Logic for method consolidation
				if (line.contains("Method Call")) {
					lastMethod = line.substring(line.indexOf("target=")+7);
					lastMethod = lastMethod.substring(lastMethod.indexOf('#')+1).replace("\"","").trim();
					lastMethod = lastMethod + ":" + counter++;
					
				}
				if (line.contains("Field Write")) {
					String[] tokens = line.split(",");
					
					String object = tokens[4].substring(tokens[4].indexOf("=") + 1).replace("\"", "").trim();
					String field = tokens[5].substring(0, tokens[5].indexOf("=")).replace("\"", "").trim();
					String value = tokens[5].substring(tokens[5].indexOf("=") + 1).replace("\"", "").trim();
					String fld = object.replace("/", ".") + "." + field;
					events.add(new Event(fld, value, lastMethod));
					
					allFields.add(fld);
				}
//				if (line.contains("Method Returned")) {
//					//operations for this method ended
//				}
				
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