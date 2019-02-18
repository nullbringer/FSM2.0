package edu.buffalo.cse.jive.finiteStateMachine.models;

public class TransitionBuilder {

	private StringBuilder transitions;
	private volatile boolean updated = false;

	public TransitionBuilder() {
		transitions = new StringBuilder();
		transitions.append("@startuml\n");
		updated = false;
	}

	public void addInitialState(State state, boolean result) {
		if (result)
			this.transitions.append("(*) --> " + "\"" + state.toString() + "\"");
		else
			this.transitions.append("(*) --> " + "\"" + state.toString() + "\"" + " #red");
		addNewLine();
	}

	public String getTransitions() {
		return new StringBuilder(transitions).append("@enduml\n").toString();
	}

	public void addTransition(State state1, State state2) {
		String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"";
		this.transitions.append(s);
		addNewLine();
	}

	public void addTransition(State state1, State state2, boolean result) {
		if (!result) {
			addColorTransition(state1, state2, "red");
		} else {
			String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"";
			this.transitions.append(s);
			addNewLine();
		}
	}

	public void addColorTransition(State state1, State state2, String color) {
		String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"" + " #" + color;
		this.transitions.append(s);
		addNewLine();
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	private void addNewLine() {
		this.transitions.append("\n");
	}

	public synchronized boolean isUpdated() {
		while (!updated) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notify();
		return true;
	}
}
