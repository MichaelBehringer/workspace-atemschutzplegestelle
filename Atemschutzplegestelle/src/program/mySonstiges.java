package program;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Combo;

public class mySonstiges extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text txtBeschreibung;
	private Text txtArbeitszeit;
	public String connectionString = "";
	public Connection dbConnect = null;
	public String pers_NO = "";

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public mySonstiges(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}
	
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String ConnectionString) {
		connectionString = ConnectionString;
	}

	public String getPers_NO() {
		return pers_NO;
	}
	public void setPers_NO(String Pers_NO) {
		pers_NO = Pers_NO;
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
		shell.setSize(369, 191);
		shell.setText("Sonstige Aufgabe");
		
		Button btnAbbrechen = new Button(shell, SWT.NONE);
		btnAbbrechen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Main.setSonstigesStatus(false);
				shell.close();
			}
		});
		btnAbbrechen.setBounds(10, 118, 75, 25);
		btnAbbrechen.setText("Abbrechen");
		
		Button btnSpeichernUndAusloggen = new Button(shell, SWT.NONE);
		btnSpeichernUndAusloggen.setBounds(195, 118, 148, 25);
		btnSpeichernUndAusloggen.setText("Speichern und Ausloggen");
		
		txtBeschreibung = new Text(shell, SWT.BORDER);
		txtBeschreibung.setBounds(91, 7, 252, 21);
		txtBeschreibung.setVisible(false);

		
		Combo cmbBeschreibung = new Combo(shell, SWT.NONE);
		cmbBeschreibung.setBounds(91, 7, 252, 23);
		cmbBeschreibung.add("Monatliche Kurzprüfung");
		cmbBeschreibung.add("Sonstiges");
		
		Label lblBeschreibung = new Label(shell, SWT.NONE);
		lblBeschreibung.setBounds(10, 10, 75, 15);
		lblBeschreibung.setText("Beschreibung");
		
		txtArbeitszeit = new Text(shell, SWT.BORDER | SWT.RIGHT);
		txtArbeitszeit.setText("0");
		txtArbeitszeit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) 
			{
				if(txtArbeitszeit.getText().length()==0) {txtArbeitszeit.setText("0");}
				else
				{
					try
					{
						Double.parseDouble(txtArbeitszeit.getText().replace(',', '.'));
					}
					catch (NumberFormatException exc)
					{
						messageBox("Bitte nur Zahlen eingeben","Eingabe-Fehler",shell);
						txtArbeitszeit.setText("");
						txtArbeitszeit.setFocus();
					}
				}
			}
		});
		txtArbeitszeit.setBounds(91, 34, 202, 21);
		
		Label lblArbeitszeit = new Label(shell, SWT.NONE);
		lblArbeitszeit.setBounds(10, 37, 75, 15);
		lblArbeitszeit.setText("Arbeitszeit");
		
		Label lblStunden = new Label(shell, SWT.NONE);
		lblStunden.setBounds(299, 37, 44, 15);
		lblStunden.setText("Stunden");
		
		Label lblDatum = new Label(shell, SWT.NONE);
		lblDatum.setBounds(10, 64, 55, 15);
		lblDatum.setText("Datum");
		
		DateTime dateTime = new DateTime(shell, SWT.BORDER);
		dateTime.setBounds(91, 61, 252, 24);
		
		btnSpeichernUndAusloggen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(cmbBeschreibung.getText().length()==0) {messageBox("Bitte eine Beschreibung eingeben","Eingabe-Fehler",shell);}
				else
				{
					if(cmbBeschreibung.getText().length()>=255) {messageBox("Bitte eine kürzere Beschreibung eingeben\nMax: 255 Zeichen","Eingabe-Fehler",shell);}
					else
					{
						if(txtArbeitszeit.getText().equals("0")) {messageBox("Eine Arbrbeitzeit von 0 ist nicht zulässig","Eingabe-Fehler",shell);}
						else
						{
							if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.'))>10) {messageBox("Die maxmimale Arbeitszeit beträgt 10 Stunden", "Falsche Eingabe", shell);}
							else
							{
								if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.'))<0) {messageBox("Bitte eine positive Arbeitszeit eingeben", "Falsche Eingabe", shell);}
								else
								{
									int Arbeits_Tag = dateTime.getDay();
									int Arbeits_Monat = dateTime.getMonth()+1;
									int Arbeits_Jahr = dateTime.getYear();
									String Arbeits_Datum = Arbeits_Jahr + "-" + Arbeits_Monat + "-" + Arbeits_Tag;
									try {
										dbConnect = DriverManager.getConnection(connectionString);
										Statement statement = dbConnect.createStatement();
										statement.executeUpdate("INSERT INTO ATEMSCHUTZPFLEGESTELLE_DATA (CITY_NO, FLASCHEN_FUELLEN, MASKEN_PRUEFEN, GERAETE_PRUEFEN, FLASCHEN_TUEV, MASKEN_REINIGEN, LA_PRUEFEN, LA_REINIGEN, GERAETE_REINIGEN, PERS_NO, TIME_WORK, DATE_WORK, BEMERKUNG) "
												+ "VALUES (0,0,0,0,0,0,0,0,0,"+pers_NO+","+txtArbeitszeit.getText().replace(',', '.')+",'"+Arbeits_Datum+"','"+cmbBeschreibung.getText()+"')");
										
										ResultSet resultSet = statement.executeQuery("SELECT MAX(DATA_NO) FROM ATEMSCHUTZPFLEGESTELLE_DATA");
										resultSet.next();
										String data_id = resultSet.getObject(1).toString();
										statement.executeUpdate("INSERT INTO ATEMSCHUTZPFLEGESTELLE_NR (DATA_NO) VALUES (" + data_id + ")");
										
										statement.executeUpdate("commit");
									} catch (SQLException e1) {
										e1.printStackTrace();
										messageBox("SQL-Fehler", "Projekt-Ausgabe", shell);
									}

									messageBox("Die Daten wurden erfolgreich gespeichert\nSie werden nun ausgeloggt", "Projekt-Ausgabe", shell);
									Main.setSonstigesStatus(true);
									shell.close();
								}
							}
						}
					}
				}
				
			}
		});

	}
	
	public static void messageBox(String msg, String title, Shell myShell)
	{
		MessageBox msgBox = new MessageBox(myShell);
		msgBox.setMessage(msg);
		msgBox.setText(title);
		msgBox.open();
	}
}
