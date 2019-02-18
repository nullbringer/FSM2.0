package edu.buffalo.cse.jive.finiteStateMachine.views;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/*
 * Program: FiniteStateMachine.java
 * Author: Swaminathan J, Amrita University, India
 * Description: This is a Eclipse plug-in that constructs finite state
 * 				machine given execution trace and key variables. The user
 * 				can load the trace and select the key variables of his
 * 				interest. Rendering of the diagram by PlantUML.
 * Execution: Run As ... Eclipse Application
 * Updated By Shashank Raghunath
 * Added Property Validation and Refactored the code
 */

/*
 * Draw little diagram, when you find an error. Till then disable. 
 */

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Event;
import edu.buffalo.cse.jive.finiteStateMachine.models.InputFileParser;
import edu.buffalo.cse.jive.finiteStateMachine.models.TransitionBuilder;
import edu.buffalo.cse.jive.finiteStateMachine.monitor.Monitor;
import edu.buffalo.cse.jive.finiteStateMachine.monitor.OfflineMonitor;
import edu.buffalo.cse.jive.finiteStateMachine.monitor.OnlineMonitor;
import edu.buffalo.cse.jive.finiteStateMachine.parser.Parser;
import edu.buffalo.cse.jive.finiteStateMachine.parser.ParserImpl;
import net.sourceforge.plantuml.SourceStringReader;

public class FiniteStateMachine extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "edu.buffalo.cse.jive.finiteStateMachine.views.FiniteStateMachine";

	private IStatusLineManager statusLineManager;
	private Display display;
	private ScrolledComposite rootScrollComposite;
	private Composite mainComposite;
	private Label fileLabel;
	private Text fileText;
	private Combo attributeList;
	private Button browseButton;
	private Button listenButton;
	private Button stopButton;
	private Button buildButton;
	private Button exportButton;
	Composite imageComposite;
	Composite image2Composite;
	private Image image;
	public boolean horizontal;
	public boolean vertical;

	private Label kvLabel;
	private Text kvText;
	private Label paLabel; // For predicate abstraction
	private Text paText; // For predicate abstraction
	private Button addButton;
	private Button resetButton;
	private Button drawButton;

	private Label kvSyntax;
	private Label kvSpace;

	Browser browser; // For svg support
	private Label canvasLabel;
	private Label byLabel;
	private Label statusLabel;
	private Text hcanvasText;
	private Text vcanvasText;
	String svg;
	private Label propertyLabel;
	private Text propertyText;
	public TransitionBuilder transitionBuilder;
	private BlockingQueue<Event> incomingStates;
	private SvgGenerator svgGenerator;
	private Monitor monitor;
	private boolean online = false;

	/**
	 * The constructor.
	 */
	public FiniteStateMachine() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {

		statusLineManager = getViewSite().getActionBars().getStatusLineManager();
		display = parent.getDisplay();

		GridLayout layoutParent = new GridLayout();
		layoutParent.numColumns = 1;
		parent.setLayout(layoutParent);

		rootScrollComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		rootScrollComposite.setLayout(new GridLayout(1, false));
		rootScrollComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rootScrollComposite.setExpandHorizontal(true);
		rootScrollComposite.setExpandVertical(true);

		mainComposite = new Composite(rootScrollComposite, SWT.NONE);
		rootScrollComposite.setContent(mainComposite);

		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite browseComposite = new Composite(mainComposite, SWT.NONE);
		browseComposite.setLayout(new GridLayout(5, false));
		browseComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

//		listenButton = new Button(browseComposite, SWT.PUSH);
//		listenButton.setText("Listen");
//		stopButton = new Button(browseComposite, SWT.PUSH);
//		stopButton.setText("Stop");
		browseButton = new Button(browseComposite, SWT.PUSH);
		browseButton.setText("Browse");

		fileLabel = new Label(browseComposite, SWT.FILL);
		fileLabel.setText("CSV File : ");

		fileText = new Text(browseComposite, SWT.READ_ONLY);
		fileText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		GridData gd = new GridData();
		gd.widthHint = 550;
		fileText.setLayoutData(gd);

		// Key Attributes Composite

		Composite kvComposite = new Composite(mainComposite, SWT.NONE);
		kvComposite.setLayout(new GridLayout(2, false));
		kvComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		kvLabel = new Label(kvComposite, SWT.FILL);
		kvLabel.setText("Key Attributes");

		kvText = new Text(kvComposite, SWT.BORDER | SWT.FILL);
		GridData gd5 = new GridData();
		gd5.widthHint = 650;
		kvText.setLayoutData(gd5);

		kvSpace = new Label(kvComposite, SWT.FILL);
		kvSpace.setText("                     ");

		kvSyntax = new Label(kvComposite, SWT.FILL);
		kvSyntax.setText("   class:index->field,......,class:index->field");

		// Choice composite
		Composite evComposite = new Composite(mainComposite, SWT.NONE);
		evComposite.setLayout(new GridLayout(10, false));
		evComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		// attributeList = new Combo(evComposite, SWT.SIMPLE | SWT.BORDER);
		attributeList = new Combo(evComposite, SWT.DROP_DOWN | SWT.BORDER);

		addButton = new Button(evComposite, SWT.PUSH);
		addButton.setText("Add");
		addButton.setToolTipText("Adds the key attribute selected");

		resetButton = new Button(evComposite, SWT.PUSH);
		resetButton.setText("Reset");
		resetButton.setToolTipText("Clears the key attributes");

		buildButton = new Button(evComposite, SWT.PUSH);
		buildButton.setText("Build");
		buildButton.setToolTipText("Builds the state diagram");

		drawButton = new Button(evComposite, SWT.PUSH);
		drawButton.setText("Draw");
		drawButton.setToolTipText("Draws the state diagram");

		exportButton = new Button(evComposite, SWT.PUSH);
		exportButton.setText("Export");
		exportButton.setToolTipText("Exports the state diagram");

		// Granularity composite
		Composite grComposite = new Composite(mainComposite, SWT.NONE);
		grComposite.setLayout(new GridLayout(10, false));
		grComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		// Predicate abstraction composite
		Composite paComposite = new Composite(mainComposite, SWT.NONE);
		paComposite.setLayout(new GridLayout(2, false));
		paComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		paLabel = new Label(paComposite, SWT.FILL);
		paLabel.setText("Abbreviations");

		paText = new Text(paComposite, SWT.BORDER | SWT.FILL);
		GridData gd5b = new GridData();
		gd5b.widthHint = 400;
		paText.setLayoutData(gd5b);

		Composite grammarView = new Composite(mainComposite, SWT.NONE);
		grammarView.setLayout(new GridLayout(3, false));
		grammarView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		propertyLabel = new Label(grammarView, SWT.FILL);
		propertyLabel.setText("Properties       ");

		propertyText = new Text(grammarView, SWT.V_SCROLL);
		GridData grid = new GridData();
		grid.widthHint = 400;
		grid.heightHint = 100;
		propertyText.setLayoutData(grid);
		// Image composite

		imageComposite = new Composite(mainComposite, SWT.NONE);
		imageComposite.setLayout(new GridLayout(1, false));
		imageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// imageLabel = new Label(imageComposite,SWT.NONE);
		browser = new Browser(imageComposite, SWT.NONE);
		// canvas = new Canvas(imageComposite,SWT.NO_REDRAW_RESIZE);
		// rootScrollComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT,
		// SWT.DEFAULT));

		// Ev2 composite
		Composite ev2Composite = new Composite(mainComposite, SWT.NONE);
		ev2Composite.setLayout(new GridLayout(8, false));
		ev2Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		// Added for SVG support test

		canvasLabel = new Label(ev2Composite, SWT.FILL);
		canvasLabel.setText("Canvas dimension");

		hcanvasText = new Text(ev2Composite, SWT.BORDER | SWT.FILL);
		GridData hcd = new GridData();
		hcd.widthHint = 40;
		hcanvasText.setLayoutData(hcd);
		hcanvasText.setText("1000");

		byLabel = new Label(ev2Composite, SWT.FILL);
		byLabel.setText("   X    ");

		vcanvasText = new Text(ev2Composite, SWT.BORDER | SWT.FILL);
		GridData vcd = new GridData();
		vcd.widthHint = 40;
		vcanvasText.setLayoutData(vcd);
		vcanvasText.setText("600");

		statusLabel = new Label(ev2Composite, SWT.FILL);
		statusLabel.setText("StatusUpdate:");

		svgGenerator = new SvgGenerator(hcanvasText, vcanvasText, browser, imageComposite, rootScrollComposite,
				mainComposite, display);
		listenButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				listenButtonAction(e);
			}
		});
		buildButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				buildButtonAction(e);
			}
		});
		browseButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				browseButtonAction(e);
			}
		});

		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportButtonAction(e);
			}
		});

		attributeList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				keyAttributeAction(e);
			}
		});

		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addButtonAction(e);
			}
		});

		drawButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				drawButtonAction(e);
			}
		});

		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetButtonAction(e);
			}
		});

		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Job.getJobManager().cancel("MonitorPortJob");
			}
		});
	}

	private List<String> readAttributes(Text attributes, Text abbreviations) {
		List<String> keyAttributes;
		if (attributes != null && attributes.getText().length() > 0) {
			keyAttributes = new ArrayList<String>();
			String selected = attributes.getText();
			for (String attribute : selected.split(",")) {
				keyAttributes.add(attribute.trim());
			}

			if (abbreviations != null && abbreviations.getText().length() > 0) {
				for (String abbreviation : abbreviations.getText().split(",")) {
					String attribute = abbreviation.split("=")[0].trim();
					if (keyAttributes.contains(attribute)) {
						Event.map.put(attribute, abbreviation.split("=")[1].trim());
					}
				}
			}
			return keyAttributes;
		}
		return null;
	}

	private List<Expression> parseExpressions(Text propertyText) throws IOException {
		Parser parser = new ParserImpl();
		String properties = propertyText.getText().trim();
		return parser.parse(properties.split(";"));
	}

	private void buildButtonAction(SelectionEvent e) {
		this.transitionBuilder = new TransitionBuilder();
		if (propertyText != null && propertyText.getText().length() > 0) {
			try {
				List<Expression> expressions = parseExpressions(propertyText);
				if (online) {
					monitor.resetStates();
					monitor.validate(expressions);
					monitor.buildTransitions(this.transitionBuilder);
				} else {
					monitor = new OfflineMonitor(readAttributes(kvText, paText), incomingStates);
					monitor.run();
					monitor.resetStates();
					monitor.validate(expressions);
					monitor.buildTransitions(this.transitionBuilder);
				}
			} catch (IOException e1) {
				statusLineManager.setErrorMessage("Unexpected error parsing properties");
				e1.printStackTrace();
			}
		} else {
			if (online) {
				monitor.resetStates();
				this.monitor.buildTransitions(this.transitionBuilder);
			} else {
				this.monitor = new OfflineMonitor(readAttributes(kvText, paText), incomingStates);
				monitor.resetStates();
				this.monitor.run();
				this.monitor.buildTransitions(this.transitionBuilder);
			}
		}
	}

	private void listenButtonAction(SelectionEvent e) {
		this.online = true;
		this.incomingStates = new LinkedBlockingQueue<Event>();
		Job job = new Job("MonitorPortJob") {
			ServerSocket server;
			Socket socket;

			protected IStatus run(IProgressMonitor monitor) {
				String line = "";
				try {
					server = new ServerSocket(5000);
					System.out.println("Server started at port 5000");

					socket = server.accept();
					System.out.println("Client accepted");
					DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 131072));
					while (true) {
						try {
							if (in.available() > 0) {
								incomingStates.put(getEvent(in.readUTF().replace("\"", "").trim()));
							}
						} catch (IOException | InterruptedException e) {
							System.out.println("Job Stopped");
							break;
						}
					}
					socket.close();
					server.close();
					updateUI("Socket: Client Disconnected");

				} catch (EOFException eofe) {
					System.out.println(line);
					updateUI("Error:Reached end-of-file");
					System.out.println("Reached end-of-file");
				} catch (IOException ioe) {
					System.out.println("Connection problem");
					updateUI("Error:Connection problem");
					System.out.println(line);
				}
				return Status.OK_STATUS;
			}

			@Override
			protected void canceling() {
				super.canceling();
				try {
					System.out.println("Server Closed");
					if (socket != null)
						socket.close();
					if (server != null)
						server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public boolean belongsTo(Object family) {
				return family.equals("MonitorPortJob");
			}
		};
		job.setUser(true);
		job.schedule();
		this.monitor = new OnlineMonitor(readAttributes(kvText, paText), incomingStates);
		Thread thread = new Thread(this.monitor);
		thread.start();
	}

	void updateUI(String message) {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				statusLabel.setText(message);
			}
		});
	}

	private Event getEvent(String input) {
		String[] tokens = input.split(",");
		String object = tokens[0].substring(tokens[0].indexOf("=") + 1).replace("\"", "").trim();
		String field = tokens[1].substring(0, tokens[1].indexOf("=")).replace("\"", "").trim();
		String value = tokens[1].substring(tokens[1].indexOf("=") + 1).replace("\"", "").trim();
		return new Event(object.replace("/", ".") + "." + field, value);
	}

	private void browseButtonAction(SelectionEvent e) {
		if (image != null) {
			if (!image.isDisposed()) {
				System.out.println("Image disposedx");
				image.dispose();
			}
		}

		statusLineManager.setMessage(null);
		FileDialog fd = new FileDialog(new Shell(Display.getCurrent(), SWT.OPEN));
		fd.setText("Open CSV File");
		String[] filterExtensions = { "*.csv" };
		fd.setFilterExtensions(filterExtensions);

		String fileName = fd.open();
		if (fileName == null)
			return;
		fileText.setText(fileName);
		attributeList.removeAll();
		kvText.setText("");
		InputFileParser inputFileParser = new InputFileParser(fileName);
		Set<String> allAttributes = inputFileParser.getAllFields();
		this.incomingStates = inputFileParser.getEvents();
		for (String attribute : allAttributes) {
			attributeList.add(attribute);
		}
		drawButton.setEnabled(true);
		statusLineManager.setMessage("Loaded " + fileName);
	}

	private void exportButtonAction(SelectionEvent e) {
		SourceStringReader reader = new SourceStringReader(transitionBuilder.getTransitions());
		FileDialog fd = new FileDialog(new Shell(Display.getCurrent()), SWT.SAVE);
		fd.setText("Export As");
		String[] filterExtensions = { "*.svg" };
		fd.setFilterExtensions(filterExtensions);
		try {
			reader.outputImage(new File(fd.open()));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void keyAttributeAction(SelectionEvent e) {
		String keyVar = attributeList.getText();
		System.out.println(keyVar);
		statusLineManager.setMessage("Selected key attribute: " + keyVar);
	}

	private void addButtonAction(SelectionEvent e) {

		String keyVar = attributeList.getText();
		if (!keyVar.equals("")) {
			if (kvText.getText().equals(""))
				kvText.setText(keyVar);
			else
				kvText.setText(kvText.getText() + "," + keyVar);
			System.out.println("Adding key attribute ... " + keyVar);
		}
		attributeList.setText("");
	}

	private void drawButtonAction(SelectionEvent e) {
		svgGenerator.generate(this.transitionBuilder.getTransitions());
		statusLineManager.setMessage("Finite State Model for " + kvText.getText());
	}

	private void resetButtonAction(SelectionEvent e) {
		kvText.setText("");
		paText.setText("");
	}

	@Override
	public void setFocus() {

	}
}