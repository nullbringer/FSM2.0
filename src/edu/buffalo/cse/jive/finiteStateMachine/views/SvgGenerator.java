package edu.buffalo.cse.jive.finiteStateMachine.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class SvgGenerator {

	private Browser browser;
	private Composite imageComposite;
	private ScrolledComposite rootScrollComposite;
	private Composite mainComposite;
	private Display display;
	private Text hcanvasText;
	private Text vcanvasText;

	public SvgGenerator(Text hcanvasText, Text vcanvasText, Browser browser, Composite imageComposite,
			ScrolledComposite rootScrollComposite, Composite mainComposite, Display display) {
		this.browser = browser;
		this.imageComposite = imageComposite;
		this.rootScrollComposite = rootScrollComposite;
		this.mainComposite = mainComposite;
		this.display = display;
		this.hcanvasText = hcanvasText;
		this.vcanvasText = vcanvasText;
	}

	public void generate(String source) {
		SourceStringReader reader = new SourceStringReader(source);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			reader.outputImage(os, new FileFormatOption(FileFormat.SVG));
			os.close();
		} catch (IOException ioe) {
			System.out.println("Unable to generate SVG");
		}
		String svg = new String(os.toByteArray(), Charset.forName("UTF-8"));
		GridData browserLData = new GridData();
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				browserLData.widthHint = Integer.parseInt(hcanvasText.getText()); // 1000;
				browserLData.heightHint = Integer.parseInt(vcanvasText.getText()); // 600;
				browser.setLayoutData(browserLData);
				browser.setText(svg);
				imageComposite.pack();
				rootScrollComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
	}
}
