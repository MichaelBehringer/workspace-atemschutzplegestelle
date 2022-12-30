package program;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class myHilfe extends Dialog {

	protected Object result;
	protected Shell shell;
	public String headerPfad = "";
	
	public String getHeaderPfad() {
		return headerPfad;
	}
	public void setHeaderPfad(String HeaderPfad) {
		headerPfad = HeaderPfad;
	}

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public myHilfe(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX | SWT.SYSTEM_MODAL);
		shell.setSize(277, 115);
		shell.setText(getText());
		setText("Hilfe");
		
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBounds(10, 10, 64, 64);
		
        canvas.setBackgroundImage(SWTResourceManager.getImage(getHeaderPfad()+"flamme.png"));
        
        Button btnZurueck = new Button(shell, SWT.NONE);
        btnZurueck.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) 
        	{
        		shell.close();
        	}
        });
        btnZurueck.setBounds(192, 49, 64, 25);
        btnZurueck.setText("Zur\u00FCck");
        
        Label lblEntwickelt = new Label(shell, SWT.NONE);
        lblEntwickelt.setBounds(80, 10, 176, 15);
        lblEntwickelt.setText("Entwickelt von Michael Behringer");
        
        Label lblVersion = new Label(shell, SWT.NONE);
        lblVersion.setBounds(80, 59, 81, 15);
        lblVersion.setText("Version: 6");
        
        Label lblEmail = new Label(shell, SWT.NONE);
        lblEmail.setBounds(80, 27, 176, 15);
        lblEmail.setText("michabehringer@gmail.com");

	}
}
