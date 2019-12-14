package edu.buffalo.cse.jive.finiteStateMachine.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

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

import edu.buffalo.cse.jive.finiteStateMachine.models.Event;
import edu.buffalo.cse.jive.finiteStateMachine.models.InputFileParser;
import edu.buffalo.cse.jive.finiteStateMachine.models.TransitionBuilder;
import edu.buffalo.cse.jive.finiteStateMachine.monitor.Monitor;
import edu.buffalo.cse.jive.finiteStateMachine.monitor.OfflineMonitor;
import edu.buffalo.cse.jive.finiteStateMachine.parser.Parser;
import edu.buffalo.cse.jive.finiteStateMachine.parser.TopDownParser;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.util.TemporaryDataTransporter;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
/**
 * The view of the FSM.
 *
 */
public class FSMPropertyChecker extends ViewPart {

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
	private Button validateButton;
	private Button exportButton;
	private Composite imageComposite;
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

	private Browser browser; // For svg support
	private Label canvasLabel;
	private Label byLabel;
	private Label statusLabel;
	private Text hcanvasText;
	private Text vcanvasText;
	private Label propertyLabel;
	private Text propertyText;
	private BlockingQueue<Event> incomingEvents;
	private SvgGenerator svgGenerator;
	private TransitionBuilder transitionBuilder;
	private Label errorText;
	private Monitor monitor;
	
	private Button[] granularity;
	private Label grLabel;

	public FSMPropertyChecker() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 * It's Eclipse SWT 3.x! Deal with it. As I did :D
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

		drawButton = new Button(evComposite, SWT.PUSH);
		drawButton.setText("Draw");
		drawButton.setToolTipText("Draws the state diagram");

		validateButton = new Button(evComposite, SWT.PUSH);
		validateButton.setText("Validate");
		validateButton.setToolTipText("Validates and draws the state diagram");

		resetButton = new Button(evComposite, SWT.PUSH);
		resetButton.setText("Reset");
		resetButton.setToolTipText("Clears the key attributes");

		exportButton = new Button(evComposite, SWT.PUSH);
		exportButton.setText("Export");
		exportButton.setToolTipText("Exports the state diagram");

		// Granularity composite
		Composite grComposite = new Composite(mainComposite, SWT.NONE);
		grComposite.setLayout(new GridLayout(10, false));
		grComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
				
		grLabel = new Label(grComposite, SWT.FILL);
		grLabel.setText("Granularity");
		
		granularity = new Button[2];
		
		granularity[0] = new Button(grComposite, SWT.RADIO);
		granularity[0].setSelection(true);
		granularity[0].setText("Field");
		
		granularity[1] = new Button(grComposite, SWT.RADIO);
		granularity[1].setSelection(false);
		granularity[1].setText("Method");

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

		// Error Composite
		Composite errorComposite = new Composite(mainComposite, SWT.NONE);
		errorComposite.setLayout(new GridLayout(1, false));
		errorText = new Label(errorComposite, SWT.NONE);
		errorText.setText("                                                                ");

		// Image composite

		imageComposite = new Composite(mainComposite, SWT.NONE);
		imageComposite.setLayout(new GridLayout(1, false));
		imageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		browser = new Browser(imageComposite, SWT.NONE);

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

		// Initialize the SVGGenerator
		svgGenerator = new SvgGenerator(hcanvasText, vcanvasText, browser, imageComposite, rootScrollComposite,
				mainComposite, display);

		addButton.setEnabled(false);
		drawButton.setEnabled(false);
		validateButton.setEnabled(false);
		exportButton.setEnabled(false);

		validateButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				validateButtonAction(e);
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
	}

	/** Phew... */

	/**
	 * Reads Key Attributes and Abbreviations if present
	 * 
	 * @param attributes
	 * @param abbreviations
	 * @return set of keyAttributes
	 * 
	 */
	private Set<String> readKeyAttributes(Text attributes, Text abbreviations) {
		if (attributes != null && attributes.getText().length() > 0) {
			Set<String> keyAttributes = new LinkedHashSet<String>();
			String selected = attributes.getText();
			for (String attribute : selected.split(",")) {
				keyAttributes.add(attribute.trim());
			}

			if (abbreviations != null && abbreviations.getText().length() > 0) {
				Event.abbreviations.clear();
				String[] tokens = abbreviations.getText().split(",");
				if (tokens == null || tokens.length == 0)
					throw new IllegalArgumentException("Invalid Abbreviations");
				for (String abbreviation : tokens) {
					String[] tks = abbreviation.split("=");
					if (tks == null || tks.length != 2)
						throw new IllegalArgumentException("Invalid Abbreviations");
					String attribute = tks[0].trim();
					if (keyAttributes.contains(attribute)) {
						Event.abbreviations.put(attribute, tks[1].trim());
					} else {
						throw new IllegalArgumentException("Invalid Abbreviations");
					}
				}
			}
			return keyAttributes;
		}
		throw new IllegalArgumentException("Please add atleast one attribute");
	}

	/**
	 * Reads property text and parses them into a list of expressions
	 * 
	 * @param propertyText
	 * @return List of expressions
	 * @throws Exception
	 */
	private List<Expression> parseExpressions(Text propertyText) throws Exception {
		if (propertyText != null && propertyText.getText().length() > 0) {
			Parser parser = new TopDownParser();
			String properties = propertyText.getText().trim();
			return parser.parse(properties.split(";"));
		}
		throw new IllegalArgumentException("Please enter properties to validate");
	}

	/**
	 * Validates and Draws the State Machine
	 * 
	 * @param e
	 */
	private void validateButtonAction(SelectionEvent e) {
		errorText.setText("                                                                ");
		try {
			TemporaryDataTransporter.shouldHighlight = false;
			Set<String> keyAttributes = readKeyAttributes(kvText, paText);
			List<Expression> expressions = null;
			try {
				expressions = parseExpressions(propertyText);
			} catch (Exception e3) {
				errorText.setText(e3.getMessage());
				e3.printStackTrace();
			}
			if (expressions != null && expressions.size() > 0) {
				monitor = new OfflineMonitor(keyAttributes, incomingEvents, granularity[1].getSelection());
				monitor.run();
				if (monitor.validate(expressions)) {
					errorText.setText("All properties satisfied.                                 ");
				}
				transitionBuilder = new TransitionBuilder(monitor.getRootState(), monitor.getStates());
				transitionBuilder.build();
				svgGenerator.generate(transitionBuilder.getTransitions());
				exportButton.setEnabled(true);
			}
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
			errorText.setText(e1.getMessage());
		} catch (ClassCastException e2) {
			errorText.setText("Type mismatch in properties");
			e2.printStackTrace();
		} catch (Exception e2) {
			errorText.setText("Unexpected error parsing properties");
			e2.printStackTrace();
		}
	}

	/**
	 * Reads input file and processes all attributes and events
	 * 
	 * @param e
	 */
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
		paText.setText("");
		propertyText.setText("");
		errorText.setText("                                                                ");
		monitor = null;
		InputFileParser inputFileParser = new InputFileParser(fileName);
		Set<String> allAttributes = inputFileParser.getAllFields();
		this.incomingEvents = inputFileParser.getEvents();
		for (String attribute : allAttributes) {
			attributeList.add(attribute);
		}
		addButton.setEnabled(true);
		drawButton.setEnabled(true);
		validateButton.setEnabled(true);
		statusLineManager.setMessage("Loaded " + fileName);
	}

	/**
	 * Exports the state machine into an svg file
	 * 
	 * @param e
	 */
	private void exportButtonAction(SelectionEvent e) {
		SourceStringReader reader = new SourceStringReader(transitionBuilder.getTransitions());
		FileDialog fd = new FileDialog(new Shell(Display.getCurrent()), SWT.SAVE);
		fd.setText("Export As");
		String[] filterExtensions = { "*.svg" };
		fd.setFilterExtensions(filterExtensions);
		String fileName = fd.open();
		if (fileName != null) {
			try {
				reader.outputImage(new File(fd.open()));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void keyAttributeAction(SelectionEvent e) {
		String keyVar = attributeList.getText();
		System.out.println(keyVar);
		statusLineManager.setMessage("Selected key attribute: " + keyVar);
	}

	/**
	 * Adds attribute to key attributes
	 * 
	 * @param e
	 */
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

	/**
	 * Builds and draws the State Machine
	 * 
	 * @param e
	 */
	private void drawButtonAction(SelectionEvent e) {
		errorText.setText("                                                                ");
		try {
			Set<String> keyAttributes = readKeyAttributes(kvText, paText);
			monitor = new OfflineMonitor(keyAttributes, incomingEvents, granularity[1].getSelection());
			monitor.run();
			transitionBuilder = new TransitionBuilder(monitor.getRootState(), monitor.getStates());
			transitionBuilder.build();
			svgGenerator.generate(transitionBuilder.getTransitions());
			statusLineManager.setMessage("Finite State Model for " + kvText.getText());
			exportButton.setEnabled(true);
		} catch (IllegalArgumentException e1) {
			errorText.setText(e1.getMessage());
		}
	}

	/**
	 * Resets key attributes, abbreviations, properties and errors
	 * 
	 * @param e
	 */
	private void resetButtonAction(SelectionEvent e) {
		kvText.setText("");
		paText.setText("");
		propertyText.setText("");
		errorText.setText("                                                                ");
	}

	@Override
	public void setFocus() {

	}
}