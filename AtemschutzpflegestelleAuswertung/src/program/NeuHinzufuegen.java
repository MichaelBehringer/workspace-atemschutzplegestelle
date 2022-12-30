package program;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NeuHinzufuegen extends Dialog {

	protected Object result;
	protected Shell shell;
	private String connectionString = "";
	private String myModus = "";
	private Table tblAnzeige;
	private Text txtEingabe;
	private Button btnZurueck;
	public Connection dbConnect = null;
	public ResultSet myResult = null;
	TableItem item = null;
	
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String ConnectionString) {
		connectionString = ConnectionString;
	}
	
	public String getModus() {
		return myModus;
	}
	public void setModus(String MyModus) {
		myModus = MyModus;
	}

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NeuHinzufuegen(Shell parent, int style) {
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
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER | SWT.SYSTEM_MODAL);
		shell.setSize(237, 357);
		shell.setText(getText());
		
		tblAnzeige = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tblAnzeige.setBounds(10, 10, 201, 242);
		tblAnzeige.setHeaderVisible(true);
		tblAnzeige.setLinesVisible(true);
		
		TableColumn clmVariable = new TableColumn(tblAnzeige, SWT.NONE);
		clmVariable.setWidth(186);
		clmVariable.setText("Variable");
		
		txtEingabe = new Text(shell, SWT.BORDER);
		txtEingabe.setBounds(10, 260, 120, 21);
		
		Button btnHinzufgen = new Button(shell, SWT.NONE);
		btnHinzufgen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(txtEingabe.getText().trim().length()==0){messageBox("Bitte das Textfeld füllen","Eingabe-Fehler",shell);}
				else
				{
					try 
					{
						dbConnect = DriverManager.getConnection(connectionString);
						Statement statement = dbConnect.createStatement();
						
						if(getModus().equals("Feuerwehr"))
						{
							statement.executeUpdate("INSERT INTO ATEMSCHUTZPFLEGESTELLE_CITIES (CITY_NAME) VALUES ('"+txtEingabe.getText()+"')");
						}
						else if(getModus().equals("Atemschutz"))
						{
							String[] Vname = txtEingabe.getText().split(" ");
							statement.executeUpdate("INSERT INTO PERS (FIRSTNAME, LASTNAME, FUNCTION_NO) VALUES ('"+Vname[0]+"','"+Vname[1]+"',1)");
						}
						statement.executeUpdate("commit");
						refreshTable();
					} 
					catch (SQLException e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});
		btnHinzufgen.setBounds(136, 258, 75, 25);
		btnHinzufgen.setText("Hinzuf\u00FCgen");
		
		btnZurueck = new Button(shell, SWT.NONE);
		btnZurueck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
		btnZurueck.setBounds(10, 289, 201, 25);
		btnZurueck.setText("Zur\u00FCck");
		
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) 
			{
				refreshTable();
			}
		});

	}
	
	public void refreshTable() 
	{
		tblAnzeige.removeAll();
		try
		{
			dbConnect = DriverManager.getConnection(connectionString);
			Statement statement = dbConnect.createStatement();
			if(getModus().equals("Feuerwehr"))
			{
				myResult = statement.executeQuery("SELECT CITY_NAME FROM ATEMSCHUTZPFLEGESTELLE_CITIES ORDER BY CITY_NAME");
			}
			else if(getModus().equals("Atemschutz"))
			{
				myResult = statement.executeQuery("SELECT CONCAT(p.FIRSTNAME,' ',p.LASTNAME) FROM PERS p ORDER BY p.LASTNAME, p.FIRSTNAME");
			}
			
			while (myResult.next())
			{
				item = new TableItem(tblAnzeige, SWT.BORDER);
				item.setText(0, myResult.getObject(1).toString());
			}
		}
		catch (SQLException sqlexc)
		{
			sqlexc.printStackTrace();
		}
	}
	
	public static void messageBox(String msg, String title, Shell myShell)
	{
		MessageBox msgBox = new MessageBox(myShell);
		msgBox.setMessage(msg);
		msgBox.setText(title);
		msgBox.open();
	}
}
