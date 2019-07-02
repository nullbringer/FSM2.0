
/*
 * Program: FiniteStateMachine.java
 * Author: Swaminathan J, Amrita Vishwa Vidyapeetham, India
 * Description: This is a Ecliplse plug-in that constructs finite state
 * 				machine given execution trace and key variables. The user
 * 				can load the trace and select the key variables of his
 * 				interest. Rendering of the diagram using PlantUML.
 * Execution: Run As ... Eclipse Application
 */

package edu.buffalo.cse.jive.finiteStateMachine.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class FSMAbstractionGranularity extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "finite_state_machine.views.FiniteStateMachine";
	private TableViewer viewer;
	
	// UI Controls
	private IStatusLineManager statusLineManager;
	private Display display;
	private ScrolledComposite rootScrollComposite;
	private Composite mainComposite;
	private Label fileLabel;
	private Text fileText;
	private Combo attributeList;
	private Button browseButton;
	private Button exportButton;
	Composite imageComposite;
	Composite image2Composite;
	private Image image;
	
	public boolean horizontal;
	public boolean vertical;
	
	private Label kvLabel;
	private Text kvText;
	private Label paLabel; 	// For predicate abstraction
	private Text paText;	// For predicate abstraction
	private Button addButton;
	private Button resetButton;
	private Button drawButton;
	private Button ssChkBox; // For step-by-step construction
	private Button startButton;
	private Button prevButton;
	private Button nextButton;
	private Button endButton;
	private Button startButton2;
	private Button prevButton2;
	private Button nextButton2;
	private Button endButton2;
	private Button[] granularity;
	private Label grLabel;
	private Label repeatLabel;
	private Text repeatText;
	private Button transitionCount;
	private Button aRun;
	
	StateDiagram sd;
	private Label kvSyntax;
	private Label kvSpace;
	private Label paSpace;	// For predicate abstraction
	private Label paSyntax; // For predicate abstraction
	
	private StringBuffer sb;
	private String source;
	private int count;
	
	Browser browser; // For svg support
	private Label canvasLabel;
	private Label byLabel;
	private Text hcanvasText;
	private Text vcanvasText;
	String svg;
	
	private Label tpLabel;
	private Text tpText;
	private Button tpButton;
	
	/**
	 * The constructor.
	 */
	public FSMAbstractionGranularity() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		statusLineManager = getViewSite().getActionBars().getStatusLineManager();
		display = parent.getDisplay();
		
		GridLayout layoutParent = new GridLayout();
		layoutParent.numColumns = 1;
		parent.setLayout(layoutParent);
		
		rootScrollComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		rootScrollComposite.setLayout(new GridLayout(1,false));
		rootScrollComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rootScrollComposite.setExpandHorizontal(true);
		rootScrollComposite.setExpandVertical(true);
		
		mainComposite = new Composite(rootScrollComposite, SWT.NONE);
		rootScrollComposite.setContent(mainComposite);
		
		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true,true));
		
		Composite browseComposite = new Composite(mainComposite, SWT.NONE);
		browseComposite.setLayout(new GridLayout(5,false));
		browseComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		
		browseButton = new Button(browseComposite, SWT.PUSH);
		browseButton.setText("Browse");

		fileLabel = new Label(browseComposite, SWT.FILL);
		fileLabel.setText("CSV File : ");
		
		fileText = new Text(browseComposite, SWT.READ_ONLY);
		fileText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		GridData gd = new GridData();
		gd.widthHint = 650;
		//gd.grabExcessHorizontalSpace = true;
		//gd.horizontalAlignment = SWT.FILL;
		fileText.setLayoutData(gd);

	
		// Key Attributes Composite
		
		Composite kvComposite = new Composite(mainComposite, SWT.NONE);
		kvComposite.setLayout(new GridLayout(2, false));
		kvComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		kvLabel = new Label(kvComposite, SWT.FILL);
		kvLabel.setText("Key Attributes");
		
		kvText = new Text(kvComposite, SWT.BORDER|SWT.FILL);
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

		//attributeList = new Combo(evComposite, SWT.SIMPLE | SWT.BORDER);
		attributeList = new Combo(evComposite, SWT.DROP_DOWN | SWT.BORDER);
		
		addButton = new Button(evComposite, SWT.PUSH);
		addButton.setText("Add");
		addButton.setToolTipText("Adds the key attribute selected");
		
		resetButton = new Button(evComposite, SWT.PUSH);
		resetButton.setText("Reset");
		resetButton.setToolTipText("Clears the key attributes");

		drawButton = new Button(evComposite, SWT.PUSH);
		drawButton.setText("Draw");
		drawButton.setToolTipText("Draws the state diagram");

		exportButton = new Button(evComposite, SWT.PUSH);
		exportButton.setText("Export");
		exportButton.setToolTipText("Exports the state diagram");
		
		ssChkBox = new Button(evComposite, SWT.CHECK);
		ssChkBox.setSelection(false);
		ssChkBox.setText("Step-by-step");
		
		startButton = new Button(evComposite, SWT.PUSH);
		startButton.setText("Start");
		startButton.setToolTipText("Start state");
		startButton.setEnabled(false);
		
		prevButton = new Button(evComposite, SWT.PUSH);
		prevButton.setText("Prev");
		prevButton.setToolTipText("Previous state");
		prevButton.setEnabled(false);
		
		nextButton = new Button(evComposite, SWT.PUSH);
		nextButton.setText("Next");
		nextButton.setToolTipText("Next state");
		nextButton.setEnabled(false);

		endButton = new Button(evComposite, SWT.PUSH);
		endButton.setText(" End ");
		endButton.setToolTipText("End state");
		endButton.setEnabled(false);
		
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
		
		repeatLabel = new Label(grComposite, SWT.FILL);
		repeatLabel.setText("       Mininmum Field Updates");
				
		repeatText = new Text(grComposite, SWT.BORDER|SWT.FILL);
		GridData gd6 = new GridData();
		gd6.widthHint = 25;
		repeatText.setLayoutData(gd6);
		repeatText.setText("1");
		
		transitionCount = new Button(grComposite, SWT.CHECK);
		transitionCount.setSelection(true);
		transitionCount.setText("Count transitions");
		
		aRun = new Button(grComposite, SWT.CHECK);
		aRun.setSelection(false);
		aRun.setText("Run");
		
		// Predicate abstraction composite
		Composite paComposite = new Composite(mainComposite, SWT.NONE);
		paComposite.setLayout(new GridLayout(2, false));
		paComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		paLabel = new Label(paComposite, SWT.FILL);
		paLabel.setText("Abstraction");
		
		paText = new Text(paComposite, SWT.BORDER|SWT.FILL);
		GridData gd5b = new GridData();
		gd5b.widthHint = 400;
		paText.setLayoutData(gd5b);
		
		paSpace = new Label(paComposite, SWT.FILL);
		paSpace.setText("                     ");
		
		paSyntax = new Label(paComposite, SWT.FILL);
		paSyntax.setText("Comma-separated entries each of which may be =n, <n, >n, #n, \n"
				+ "[a:b:..:c] or left empty, e.g., =5,,>3,[2:5:8],#true,<4.17,=str");
		// Predicate abstraction changes end

			
		// Image composite
		
		imageComposite = new Composite(mainComposite, SWT.NONE);
		imageComposite.setLayout(new GridLayout(1,false));  
		imageComposite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		browser = new Browser(imageComposite, SWT.NONE);
	    GridData browserLData = new GridData();
	    browserLData.widthHint = 800; //1000;
	    browserLData.heightHint = 200; //600;
	    browser.setLayoutData(browserLData);
		browser.setText("");
		

		// Ev2 composite
		Composite ev2Composite = new Composite(mainComposite, SWT.NONE);
		ev2Composite.setLayout(new GridLayout(8, false));
		ev2Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		startButton2 = new Button(ev2Composite, SWT.PUSH);
		startButton2.setText("Start");
		startButton2.setToolTipText("Start state");
		startButton2.setEnabled(false);
		
		prevButton2 = new Button(ev2Composite, SWT.PUSH);
		prevButton2.setText("Prev");
		prevButton2.setToolTipText("Previous state");
		prevButton2.setEnabled(false);
		
		nextButton2 = new Button(ev2Composite, SWT.PUSH);
		nextButton2.setText("Next");
		nextButton2.setToolTipText("Next state");
		nextButton2.setEnabled(false);

		endButton2 = new Button(ev2Composite, SWT.PUSH);
		endButton2.setText(" End ");
		endButton2.setToolTipText("End state");
		endButton2.setEnabled(false);
		
		// Added for SVG support test
		
		canvasLabel = new Label(ev2Composite, SWT.FILL);
		canvasLabel.setText("Canvas dimension");

		hcanvasText = new Text(ev2Composite, SWT.BORDER|SWT.FILL);
		GridData hcd = new GridData();
		hcd.widthHint = 40;
		hcanvasText.setLayoutData(hcd);
		hcanvasText.setText("1000");
		
		byLabel = new Label(ev2Composite, SWT.FILL);
		byLabel.setText("   X    ");
		
		vcanvasText = new Text(ev2Composite, SWT.BORDER|SWT.FILL);
		GridData vcd = new GridData();
		vcd.widthHint = 40;
		vcanvasText.setLayoutData(vcd);
		vcanvasText.setText("600");

		rootScrollComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		statusLineManager.setMessage("Developed at Amrita Vishwa Vidyapeetham, India");

		// Action Listeners
		
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
		
		ssChkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ssChkBoxAction(e);
			}
		});
		
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startButtonAction(e);
			}
		});

		prevButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prevButtonAction(e);
			}
		});

		nextButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextButtonAction(e);
			}
		});
		
		endButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				endButtonAction(e);
			}
		});

		startButton2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startButtonAction(e);
			}
		});

		prevButton2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prevButtonAction(e);
			}
		});

		nextButton2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextButtonAction(e);
			}
		});
		
		endButton2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				endButtonAction(e);
			}
		});
	}
	
	private void browseButtonAction(SelectionEvent e) {
		
		if (image != null) { 
			if (!image.isDisposed()) {
				System.out.println("Image disposedx");
				image.dispose();	
			}	
		}
	
		FileDialog fd = new FileDialog(new Shell(Display.getCurrent(),SWT.OPEN));
		fd.setText("Open CSV File");
		
		String[] filterExtensions = {"*.csv"};
		fd.setFilterExtensions(filterExtensions);
		
		String fileName = fd.open();
		if (fileName == null)
			return;
		fileText.setText(fileName);
		
		sd = new StateDiagram();
		// First reset attributes and fwevents
		sd.attributes = new TreeSet<String>();  // Set of attributes
		sd.attributesMap = new TreeMap<String,Integer>(); // Set of attributes with count
		sd.fwevents = new ArrayList<FWEvent>(); // All field write events
		sd.keys = new ArrayList<KeyAttribute>(); // Reset key attributes
		sd.transitions = new LinkedHashMap<String,Integer>(); // Reset transitions
		sd.allTransitions = new ArrayList<String>();

		attributeList.removeAll(); // Reset dropdown box
		kvText.setText("");
		paText.setText("");
		drawButton.setEnabled(true);
		ssChkBox.setSelection(false);
		aRun.setSelection(false);
		
		
		sd.traceFile = fileName; // Get the file name
		sd.readEvents();
		sd.printAttributes();
		
		browser.setText("<html><body><p>"
				+ "Loaded " + fileName
				+ "</p></body></html>");
	}
	
	private void exportButtonAction(SelectionEvent e) {
		
		IPath path = ResourcesPlugin.getPlugin().getStateLocation();
		String from = path.toFile().getPath() + File.separator	+ "state.svg";
		
		FileDialog fd = new FileDialog(new Shell(Display.getCurrent()), SWT.SAVE);
		fd.setText("Export As");
		//String[] filterExtensions = {"*.png", "*.bmp", "*.jpg"};
		String[] filterExtensions = {"*.svg"};
		fd.setFilterExtensions(filterExtensions);
		
		String to = fd.open();
		if (to == null)
			return;
		
		File fromFile = new File(from);
		File toFile = new File(to);
		try {
			Files.copy(fromFile.toPath(), toFile.toPath());
		}
		catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	private void keyAttributeAction(SelectionEvent e) {
		String keyVar = attributeList.getText();
		System.out.println(keyVar);
		browser.setText("<html><body><p>"
				+ "Selected key attribute: " + keyVar
				+ "</p></body></html>");
	}
	
	private void addButtonAction(SelectionEvent e) {

		String keyVar = attributeList.getText();
		if (!keyVar.equals("")) {
			if (kvText.getText().equals("")) 
				kvText.setText(keyVar);
			else
				kvText.setText(kvText.getText() + "," + keyVar);
			
			System.out.println("Adding key attribute ... "  + keyVar);
			browser.setText("<html><body><p>"
					+ "Attributes added : " + kvText.getText()
					+ "</p></body></html>");

		}
		attributeList.setText("");
	}
	
	private void drawButtonAction(SelectionEvent e) {
		sd.keys = new ArrayList<KeyAttribute>(); // Reallocate key attributes
		sd.states = new ArrayList<State>();
		sd.paStates = new ArrayList<State>();
		sd.transitions = new LinkedHashMap<String,Integer>(); // Reset transitions
		
		if (kvText.getText().equals(""))
			return;
		
		sd.readKeyAttributes();
		
		sd.createStates();
		if(granularity[1].getSelection()) // If method granularity
			sd.methodConsolidation();
		
		sd.abstraction();
		
		if (aRun.getSelection()) 
			sd.generateSVG(sd.exportRun(Integer.MAX_VALUE));
		else {
			sd.createTransitions(Integer.MAX_VALUE);
			if (transitionCount.getSelection())
				sd.generateSVG(sd.exportToPlantUML(true));
			else
				sd.generateSVG(sd.exportToPlantUML(false));
		}
	}
	
	private void resetButtonAction(SelectionEvent e) {
		kvText.setText("");
		paText.setText("");
		browser.setText("<html><body><p>"
				+ "Attributes reset"
				+ "</p></body></html>");

	}
	
	private void ssChkBoxAction(SelectionEvent e) {
		if (ssChkBox.getSelection()) {

			drawButton.setEnabled(false);
			prevButton.setEnabled(false); prevButton2.setEnabled(false);
			startButton.setEnabled(true); startButton2.setEnabled(true);
		}
		else {
			drawButton.setEnabled(true);
			startButton.setEnabled(false); startButton2.setEnabled(false);
			prevButton.setEnabled(false); prevButton2.setEnabled(false);
			nextButton.setEnabled(false); nextButton2.setEnabled(false);
			endButton.setEnabled(false); endButton2.setEnabled(false);
		}
	}
	
	private void startButtonAction(SelectionEvent e) {
		sd.keys = new ArrayList<KeyAttribute>(); // Reallocate key attributes
		sd.states = new ArrayList<State>();
		sd.paStates = new ArrayList<State>();
		sd.transitions = new LinkedHashMap<String,Integer>(); // Reset transitions
		sd.allTransitions = new ArrayList<String>();
		
		sd.readKeyAttributes();
		sd.printKeyAttributes();
		
		sd.createStates();
		if(granularity[1].getSelection()) // If method granularity
			sd.methodConsolidation();
		
		sd.abstraction();
		
		count = 0;
		if (aRun.getSelection())
			sd.generateSVG(sd.exportRun(count));
		else {
			sd.createTransitions(count);
			if (transitionCount.getSelection())
				sd.generateSVG(sd.exportToPlantUML(true));
			else
				sd.generateSVG(sd.exportToPlantUML(false));
		}
		
		prevButton.setEnabled(false); prevButton2.setEnabled(false);
		nextButton.setEnabled(true); nextButton2.setEnabled(true);
		endButton.setEnabled(true); endButton2.setEnabled(true);
	}
	
	private void prevButtonAction(SelectionEvent e) {
		if (count > 0) {
			nextButton.setEnabled(true); nextButton2.setEnabled(true);
		}
		
		if (aRun.getSelection())
			sd.generateSVG(sd.exportRun(--count));
		else {
			sd.createTransitions(--count);
			if (transitionCount.getSelection())
				sd.generateSVG(sd.exportToPlantUML(true));
			else
				sd.generateSVG(sd.exportToPlantUML(false));
		}
		
		if (count <= 0){
			prevButton.setEnabled(false); prevButton2.setEnabled(false);
		}
	}
	
	private void nextButtonAction(SelectionEvent e) {
		if (count < sd.paStates.size() - 1) {
			prevButton.setEnabled(true); prevButton2.setEnabled(true);
		}
		
		if (aRun.getSelection()) {
			sd.generateSVG(sd.exportRun(++count));
		}
		else {
			sd.createTransitions(++count);
			if (transitionCount.getSelection())
				sd.generateSVG(sd.exportToPlantUML(true));
			else
				sd.generateSVG(sd.exportToPlantUML(false));
		}
		
		if (count >= sd.paStates.size() - 1) {
			nextButton.setEnabled(false); nextButton2.setEnabled(false);
		}
	}
	
	private void endButtonAction(SelectionEvent e) {
		count = sd.paStates.size() - 1;
		
		if (aRun.getSelection())
			sd.generateSVG(sd.exportRun(Integer.MAX_VALUE));
		else {
			sd.createTransitions(Integer.MAX_VALUE);
			if (transitionCount.getSelection())
				sd.generateSVG(sd.exportToPlantUML(true));
			else
				sd.generateSVG(sd.exportToPlantUML(false));
		}
		
		prevButton.setEnabled(true); prevButton2.setEnabled(true);
		nextButton.setEnabled(false); nextButton2.setEnabled(false);
	}

	class StateDiagram {

		private String traceFile;
		//private String keyAttributes; // Not necessary to set. Getting from the UI now.
		private TreeSet<String> attributes; // Set of attributes
		private TreeMap<String,Integer> attributesMap;
		private ArrayList<FWEvent> fwevents; // All field write events
		private ArrayList<KeyAttribute> keys; // Key attributes -comma separated
		ArrayList<State> states = new ArrayList<State>(); // Sequence of original states
		ArrayList<State> paStates = new ArrayList<State>(); // Sequence of predicated states
		private LinkedHashMap<String,Integer> transitions; // Transitions
		private ArrayList<String> allTransitions;
		
		public void readEvents() {

			ArrayList<String> sortedAttributes = new ArrayList<String>();
			HashMap<String,Stack<String>> callStacks = new HashMap<String,Stack<String>>();
			String lastMethod = null;
			
			String strLine = null;
			try {
				Scanner sc = new Scanner(new File(traceFile));
				int counter = 0;	// Added to distinguish method instance for Jevitha's BCI
				while (sc.hasNext()) {
					strLine = sc.nextLine().trim();
		
					// Logic for method consolidation
					if (strLine.contains("Method Call")) {
						String thrd = strLine.substring(0, strLine.indexOf(',')).replace("\"","").trim();
						lastMethod = strLine.substring(strLine.indexOf("target=")+7);
						lastMethod = lastMethod.substring(lastMethod.indexOf('#')+1).replace("\"","").trim();
						lastMethod = lastMethod + ":" + counter++;
						if (callStacks.containsKey(thrd)) {
							Stack<String> threadStack = callStacks.get(thrd);
							threadStack.push(lastMethod);
							callStacks.put(thrd, threadStack);
						}
						else {
							Stack<String> threadStack = new Stack<String>();
							threadStack.push(lastMethod);
							callStacks.put(thrd,threadStack);
							//System.out.println("lastMethod =" + lastMethod);
						}
					}
					if (strLine.contains("Field Write")) {
						String[] tokens = strLine.split(",");
						String thread = tokens[0].replace("\"","").trim();
						String object = tokens[4].substring(tokens[4].indexOf("=")+1).replace("\"","").trim();
						String field = tokens[5].substring(0,tokens[5].indexOf("=")).replace("\"","").trim();
						String value = tokens[5].substring(tokens[5].indexOf("=")+1).replace("\"","").trim();
						
						if (value.equals(""))	// Added to fix the error caused by plantuml which cannot take
							value = " ";		// strings of length 0 ("") as a state
						if (value.equals("start"))	// Plantuml has trouble drawing the state "start"
							value = "_start";

						String entry = object + "->" + field;
						fwevents.add(new FWEvent(thread, object, field, value, lastMethod));
						if ( !(attributes.contains(entry)) ) {
							attributes.add(entry);
							attributesMap.put(entry,1);
							if ( Integer.parseInt(repeatText.getText()) == 1 )
								sortedAttributes.add(entry);
						}
						else {
							int count = attributesMap.get(entry);
							attributesMap.put(entry, ++count);
							
							if (count == Integer.parseInt(repeatText.getText())) { // Add to drop-down occurrence hits 2
								sortedAttributes.add(entry);
							}
						}
					}
					if (strLine.contains("Method Returned")) {
						String thrd = strLine.substring(0,strLine.indexOf(','));
						Stack<String> threadStack = callStacks.get(thrd);
						callStacks.put(thrd, threadStack);
					}
				}
				sc.close();
				sortedAttributes.sort(null);
				for (int i=0; i<sortedAttributes.size(); i++) {
					attributeList.add(sortedAttributes.get(i));		
				}
				// Added to disable method level granularity if method call information not available
				granularity[1].setEnabled(true);
				granularity[0].setSelection(true);
				for (int e=1; e<fwevents.size(); e++) {
					if (fwevents.get(e).getMethod() == null) {
						granularity[1].setEnabled(false);
						granularity[0].setSelection(true);
						break;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void printAttributes() {
			Iterator it = attributes.iterator();
			while (it.hasNext())
				System.out.println(it.next());
		}

		public void readKeyAttributes() {

			String userChoices = "";
			userChoices = kvText.getText(); // From the UI
			if (userChoices.equals(""))
				return;
			String[] uc = userChoices.split(",");
			for (int i=0; i<uc.length; i++) {
				if ( attributes.contains(uc[i]) ) {
					String[] of = uc[i].split("->");
					keys.add(new KeyAttribute(of[0],of[1]));
				}
			}
			printKeyAttributes();
		}

		public void printKeyAttributes() {
			Iterator it = keys.iterator();
			while (it.hasNext())
				System.out.println(it.next());
		}
		
		public void createStates() {
			int time = 0;
			State currentState = new State();
			for (int s=0; s<keys.size(); s++) 
				currentState.set(s, "null");
			currentState.method = "init";
			currentState.time = time;
			states.add(currentState);
			
			State nextState = new State();
			nextState.copy(currentState);
			
			for (int i=0; i<fwevents.size(); i++) {
				for (int j=0; j<keys.size(); j++) {
					if ( fwevents.get(i).getObject().equals(keys.get(j).getObject())
						&& fwevents.get(i).getField().equals(keys.get(j).getField())
					) {
						nextState.remove(j);
						nextState.set(j,fwevents.get(i).getValue());
						nextState.setMethod(fwevents.get(i).getMethod());
						nextState.time = ++time;
						
						State st = new State();
						st.copy(nextState);
						states.add(st);
					}
				}
				currentState = new State();
				currentState.copy(nextState);
			}
			printStates();
		}
		
		public void printStates() {
			for (int s=0; s<states.size(); s++)
				states.get(s).print(1);
		}

		public void methodConsolidation() {
			int s = 0;
			while (s<states.size()-1) {
				if (states.get(s).getMethod().equals(states.get(s+1).getMethod()))
					states.remove(s);
				else
					s++;
			}
			printStates();
		}
		
		public void abstraction() {
			
			paStates = new ArrayList<State>();
			for (int s=0; s<states.size(); s++) {
				State paState = new State();
				paState.copy(states.get(s));
				paStates.add(paState);
			}
			
			String paStr = paText.getText().trim();
			if (paStr.equals(""))
				return;
			
			String[] paEntries = paStr.split(",");
			boolean reductionFlag = false;
			int s = 0;
			while (s<paStates.size()) {
				for (int k=0; k<paEntries.length && k<paStates.get(s).keyVar.size(); k++) {
					if ( !paEntries[k].trim().equals("") ) {
						String absVal = applyAbstraction(paStates.get(s).keyVar.get(k), paEntries[k]);
						if (absVal.equals("")) {
							reductionFlag = true;
							break;
						}
						else
							paStates.get(s).keyVar.set(k, absVal);
					}
				}	
				if (reductionFlag) {
					State hState = new State();
					hState.copy(paStates.get(s));
					hState.hashed = true;
					paStates.set(s, hState);
					reductionFlag = false;
					s++; // latest
				}
				else
					s++;
			}
			printPaStates();
		}
		
		public String applyAbstraction(String value, String predicate) {

			if (value.equals("null"))
				return value;
			
			int choice;
			char lhs = predicate.trim().charAt(0);
			if ( !(lhs == '=' || lhs == '>' || lhs == '<' || lhs == '[' || lhs == '#' || lhs == '~') )
				return value;	// Invalid predicate - return 'value' as it is
			
			String rhs = predicate.trim().substring(1).trim();
			if (lhs == '#')
				choice = 4;
			else if (rhs.matches("^[0-9]+$")) 
				choice = 0; // integer
			else if (rhs.matches("^[0-9]+.[0-9]+$"))
				choice = 1; // decimal
			else if (rhs.endsWith("]"))
				choice = 2; // range
			else 
				choice = 3; // string

			switch(choice) {
			case 0:	// Integer
				int n = Integer.parseInt(predicate.substring(1).trim());
				if (predicate.trim().startsWith("<")) { // <n
					if ( Integer.parseInt(value) < n ) 
						return "<" + n;
					else
						return ">=" + n;
				}
				else if (predicate.trim().startsWith(">")) { // >n
					if ( Integer.parseInt(value) > n ) 
						return ">" + n;
					else
						return "<=" + n;
				}
				else if (predicate.trim().startsWith("=")) { // =n
					if ( Integer.parseInt(value) == n ) 
						return "" + n;
					else
						return "~" + n;
				}			
				else if (predicate.trim().startsWith("~")) { // ~n
					if ( Integer.parseInt(value) != n ) 
						return "~" + n;
					else
						return "" + n;
				}			
				break;
				
			case 1:	// Double
				double d = Double.parseDouble(predicate.substring(1).trim());
				if (predicate.trim().startsWith("<")) { // <n
					if ( Double.parseDouble(value) < d ) 
						return "<" + d;
					else
						return ">=" + d;
				}
				else if (predicate.trim().startsWith(">")) { // >n
					if ( Double.parseDouble(value) > d ) 
						return ">" + d;
					else
						return "<=" + d;
				}
				else if (predicate.trim().startsWith("=")) { // =n
					if ( Double.parseDouble(value) == d ) 
						return "" + d;
					else
						return "~" + d;
				}						
				else if (predicate.trim().startsWith("~")) { // ~n
					if ( Double.parseDouble(value) != d ) 
						return "~" + d;
					else
						return "" + d;
				}						
				break;
				
			case 2: // Range abstraction
				String[] range = predicate.substring(1, predicate.indexOf(']')).split(":");
				try {
					if (Integer.parseInt(value) < Integer.parseInt(range[0]))
						return "<" + Integer.parseInt(range[0]);
					for (int i=0; i < range.length-1; i++) {
						if ( Integer.parseInt(value) >= Integer.parseInt(range[i])
							&& Integer.parseInt(value) < Integer.parseInt(range[i+1])
						)
						return Integer.parseInt(range[i]) + ":" + Integer.parseInt(range[i+1]);
					}
					if (Integer.parseInt(value) >= Integer.parseInt(range[range.length-1]))
						return ">=" + Integer.parseInt(range[range.length-1]);
				}
				catch(NumberFormatException nfe) {
					return value;
				}
				break;
				
			case 3: // String
				String s = predicate.substring(1).trim();
				if (predicate.trim().startsWith("=")) {
					if (value.equals(s))
						return s;
					else
						return "~" + s;
				}
				else if (predicate.trim().startsWith("~")) {
					if (!value.equals(s))
						return "~" + s;
					else
						return s;
				}
				
			case 4: // Reduction or Selection
				String ss = predicate.substring(1).trim();
				if (predicate.trim().startsWith("#") && value.equals(ss))
					return ss;
				else
					return "";
			}
			return value; // If nothing matches return 'value' as is - no abstraction applied
		}

		public void printPaStates() {
			/*for (int s=0; s<paStates.size(); s++)
				paStates.get(s).print(1);*/
			paStates.forEach((k) -> k.print(1)); // Lambda expression for the commented loop
		}
		
		public void createTransitions(int count) {
			transitions = new LinkedHashMap<String,Integer>();
			String transition = "(*) --> " + "\"" + paStates.get(0).toString() + "\"";
			transitions.put(transition, 1);
			int s = 0;
			String lastTransition = "";
			while (s<count && s<paStates.size()-1) {
				if (paStates.get(s).hashed && paStates.get(s+1).hashed) // skip the transition
					; // Both from and to states do not have the selection
				else if (!paStates.get(s).hashed && paStates.get(s+1).hashed) // skip the transition
					; // To node does not have the selection
				else if (paStates.get(s).hashed && !paStates.get(s+1).hashed) { // Add self-loop and make color white
					transition = new String("\"" + paStates.get(s+1).toString() + "\""
							+ " -[#white]-> "
							+ "\"" + paStates.get(s+1).toString() + "\"");

					transitions.merge(transition, 1, Integer::sum);					
				}					
				else {
					transition = new String("\"" + paStates.get(s).toString() + "\""
										+ " --> "
										+ "\"" + paStates.get(s+1).toString() + "\"");

					transitions.merge(transition, 1, Integer::sum);
					lastTransition = new String(transition);
				}
				s++;
			}
			if (s >= 1) {	// Added for animation while rendering step-by-step
				if (transitions.containsKey(lastTransition)) {
					int value = transitions.get(lastTransition);
					transitions.remove(lastTransition);
					transitions.put(lastTransition + " #red", value);
				}
			}

			sd.printTransitions();
		}
		
		public void printTransitions() { // Uses lambda
			transitions.forEach((k,v) -> System.out.println(k.replaceAll("-->", "--> [" + v + "]")));
		}	
		
		public String exportToPlantUML(boolean transitionCount) {
			sb = new StringBuffer();
			sb.append("@startuml\n");
			
			if (transitionCount)
				transitions.forEach((k,v) -> sb.append(k.replaceAll("-->", "--> [" + v + "]") + "\n"));
			else
				transitions.forEach((k,v) -> sb.append(k + "\n"));
			
			sb.append("@enduml\n");
			return sb.toString();
		}
		
		public String exportRun(int count) {
			String[] direction = {"right", "right", "right", "right", "right", "right", "right", "right",
					"down", 
					"left", "left", "left", "left", "left", "left", "left", "left",
					"down"};
			StringBuffer runsb = new StringBuffer();
			runsb.append("@startuml\n");
			
			runsb.append("(*) -right-> " + "\"t=" + paStates.get(0).time + "\\n" + paStates.get(0).toString() + "\"" + "\n");
			
			
			for (int s=0; s<count && s<paStates.size()-1; s++) {
				runsb.append("\"t=" + paStates.get(s).time + "\\n" + paStates.get(s).toString() + "\""
							+ " -" + direction[s%18] + "-> " 
							+ "\"t=" + paStates.get(s+1).time + "\\n" + paStates.get(s+1).toString() + "\"" + "\n");
			}
			
			runsb.append("@enduml\n");
			return runsb.toString();
		}
		
		public void generateSVG(String source) {
			IPath path = ResourcesPlugin.getPlugin().getStateLocation();
			System.out.println(path);
			SourceStringReader reader = new SourceStringReader(source);
			
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			OutputStream fos = null;
			try {
				fos = new FileOutputStream(path.toFile().getPath() + File.separator	+ "state.svg");
			} catch (FileNotFoundException fnfe) {
				System.out.println(fnfe);
			}
			// Write the first image to "os"
			try {
				String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
				String desc2 = reader.generateImage(fos, new FileFormatOption(FileFormat.SVG));
				os.close();			
				fos.close();
			}
			catch(IOException ioe) {
				System.out.println("Unable to generate SVG");
			}

			// The XML is stored into svg
			svg = new String(os.toByteArray(), Charset.forName("UTF-8"));

			//browser = new Browser(imageComposite, SWT.NONE);
		    GridData browserLData = new GridData();
		    browserLData.widthHint = Integer.parseInt(hcanvasText.getText()); //1000;
		    browserLData.heightHint = Integer.parseInt(vcanvasText.getText()); //600;
		    browser.setLayoutData(browserLData);
			browser.setText(svg);
			imageComposite.pack();
			rootScrollComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
	}	

	class FWEvent {

		public String thread;
		public String object;
		public String field;
		public String value;
		public String method;

		FWEvent(String t, String o, String f, String v, String m) {
			this.thread = t;
			this.object = o;
			this.field = f;
			this.value = v;
			this.method = m;
		}

		public String toString() {
			return object + "->" + field + "=" + value + "   (" + method + "," + thread + ")";
		}
		
		public String getThread() {
			return thread;
		}

		public String getObject() {
			return object;
		}

		public String getField() {
			return field;
		}

		public String getValue() {
			return value;
		}
		
		public String getMethod() {
			return method;
		}
	}

	class KeyAttribute {

		public String object;
		public String field;

		KeyAttribute(String o, String f) {
			this.object = o;
			this.field = f;
		}

		public String toString() {
			return object + "->" + field;
		}

		public String getObject() {
			return object;
		}

		public String getField() {
			return field;
		}
	}

	class State {
		ArrayList<String> keyVar = new ArrayList<String>();
		String method = null;
		boolean hashed = false;
		int time = 0;

		public int getSize() {
			return keyVar.size();
		}

		public void set(int index, String value) {
			keyVar.add(index,value);
		}

		public String get(int index) {
			return keyVar.get(index);
		}
		
		public void setMethod(String m) {
			method = m;
		}
		
		public String getMethod() {
			return method;
		}

		public void remove(int index) {
			keyVar.remove(index);
		}

		public void copy(State s) {
			for (int j=0; j<s.getSize(); j++) {
				keyVar.add(j,s.get(j));
			}
			if (s.method != null)
				method = new String(s.method);
			time = s.time;
		}
		
		public String toString() {
			StringBuffer sbKeys = new StringBuffer();
			sbKeys.append(keyVar.get(0));
			for (int j=1; j<keyVar.size(); j++) {
				sbKeys.append(",");
				sbKeys.append(keyVar.get(j));
			}
			return sbKeys.toString();
		}

		public void print(int flag) {
			if (flag == 0)
				System.out.println(toString());
			else if (flag == 1)
				System.out.println(toString() + " (" + method + ") " + hashed + " time=" + time);
		}
	}
	
	class Transition {
		String current;
		String next;
		String method;
		int count;
		
		Transition(String c, String n, String m) {
			current = c;
			next = n;
			method = m;
			count = 1;
		}
		
		public String toString() {
			return "\"" + current + "\"" 
						+ " --> "
						+ "[" + count + "]"
						+ "\"" + next + "\"";
		}
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Finite State Machine",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
}