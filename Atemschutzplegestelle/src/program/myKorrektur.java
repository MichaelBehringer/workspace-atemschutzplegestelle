package program;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

public class myKorrektur extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table myTableAnzeige;
	public ResultSet myResult = null;
	public String connectionString = "";
	public Connection dbConnect = null;
	TableItem item = null;
	public String pers_NO = "";
	private Text txtFlaschen;
	private Text txtMasken;
	private Text txtGeraete;
	private Text txtArbeitszeit;
	public List<String> allDataList = new ArrayList<String>();
	public String data_NO = "";
	public List<String> allCitiesList = new ArrayList<String>();
	public List<String> allCitiesListName = new ArrayList<String>();
	public List<String> allCitiesListNameSort = new ArrayList<String>();

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public myKorrektur(Shell parent, int style) {
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
		Button btnZurueck = new Button(shell, SWT.NONE);
		Combo cmbFeuerwehr = new Combo(shell, SWT.READ_ONLY);
		Label lblFeuerwehr = new Label(shell, SWT.NONE);

		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if(myTableAnzeige!=null) {
					Point point = new Point(shell.getSize().x-35, shell.getSize().y-95);
					myTableAnzeige.setSize(point);
					
					btnZurueck.setBounds(shell.getSize().x-100, shell.getSize().y-75, 75, 25);
					lblFeuerwehr.setBounds(10, shell.getSize().y-72, 59, 15);
					cmbFeuerwehr.setBounds(75, shell.getSize().y-75, 185, 23);
				}
			}
		});
		shell.setSize(564, 319);
		shell.setText(getText());
		
		myTableAnzeige = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		myTableAnzeige.setBounds(10, 10, 527, 223);
		myTableAnzeige.setHeaderVisible(true);
		myTableAnzeige.setLinesVisible(true);
		
		
		TableColumn clmFeuerwehr = new TableColumn(myTableAnzeige, SWT.NONE);
		clmFeuerwehr.setWidth(100);
		clmFeuerwehr.setText("Feuerwehr");
		
		TableColumn clmFlaschen = new TableColumn(myTableAnzeige, SWT.NONE);
		clmFlaschen.setWidth(105);
		clmFlaschen.setText("Flaschen F\u00FCllen");
		
		TableColumn clmFlaschenTuev = new TableColumn(myTableAnzeige, SWT.NONE);
		clmFlaschenTuev.setWidth(105);
		clmFlaschenTuev.setText("Flaschen T\u00DCV");
		
		TableColumn clmMasken = new TableColumn(myTableAnzeige, SWT.NONE);
		clmMasken.setWidth(105);
		clmMasken.setText("Masken Pr\u00FCfen");
		
		TableColumn clmMaskenReinigen = new TableColumn(myTableAnzeige, SWT.NONE);
		clmMaskenReinigen.setWidth(105);
		clmMaskenReinigen.setText("Masken Reinigen");
		
		TableColumn clmLaPruefen = new TableColumn(myTableAnzeige, SWT.NONE);
		clmLaPruefen.setWidth(105);
		clmLaPruefen.setText("LA Pr\u00FCfen");
		
		TableColumn clmLaReinigen = new TableColumn(myTableAnzeige, SWT.NONE);
		clmLaReinigen.setWidth(105);
		clmLaReinigen.setText("LA Reinigen");
		
		TableColumn clmGeraete = new TableColumn(myTableAnzeige, SWT.NONE);
		clmGeraete.setWidth(105);
		clmGeraete.setText("Ger\u00E4te Pr\u00FCfen");
		
		TableColumn clmGeraeteReinigen = new TableColumn(myTableAnzeige, SWT.NONE);
		clmGeraeteReinigen.setWidth(105);
		clmGeraeteReinigen.setText("Ger\u00E4te Reinigen");
		
		TableColumn clmArbeitszeit = new TableColumn(myTableAnzeige, SWT.NONE);
		clmArbeitszeit.setWidth(75);
		clmArbeitszeit.setText("Arbeitszeit");
		
		TableColumn clmDatum = new TableColumn(myTableAnzeige, SWT.NONE);
		clmDatum.setWidth(86);
		clmDatum.setText("Datum");
		
		TableColumn clmBemerkung = new TableColumn(myTableAnzeige, SWT.NONE);
		clmBemerkung.setWidth(200);
		clmBemerkung.setText("Bemerkung");

		cmbFeuerwehr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				int Feuerwehr_NO = 0;
				String Feuerwehr = cmbFeuerwehr.getItem(cmbFeuerwehr.getSelectionIndex());
				if(Feuerwehr.length()==0)
				{
					refreshTable("");
				}
				else
				{
					for(int m=0; m<allCitiesList.size(); m++)
					{
						String randomCity = allCitiesListName.get(m).toString().trim();
						if(randomCity.equals(Feuerwehr))
						{
							Feuerwehr_NO = m+1;
						}
					}
					refreshTable(Feuerwehr_NO+"");
				}
			}
		});
		
		Button btnUpdaten = new Button(shell, SWT.NONE);
		btnUpdaten.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(myTableAnzeige.getSelectionIndex()<0) {messageBox("Bitte nur gewünschten Datensatz anwählen", "Fehlermeldung", shell);}
				else
				{
					data_NO = allDataList.get(myTableAnzeige.getSelectionIndex());
					String Feuerwehr = cmbFeuerwehr.getText().toString();
					int Feuerwehr_NO = 0;
					for(int m=0; m<allCitiesList.size(); m++)
					{
						String randomCity = allCitiesListName.get(m).toString().trim();
						if(randomCity.equals(Feuerwehr))
						{
							Feuerwehr_NO = m+1;
						}
					}
					
					try {
						dbConnect = DriverManager.getConnection(connectionString);
						Statement statement = dbConnect.createStatement();
						statement.executeUpdate("UPDATE ATEMSCHUTZPFLEGESTELLE_DATA SET CITY_NO="+Feuerwehr_NO+", FLASCHEN="+txtFlaschen.getText()+", MASKEN="+txtMasken.getText()+", GERAETE="+txtGeraete.getText()+", TIME_WORK="+txtArbeitszeit.getText()+" WHERE DATA_NO="+data_NO);
						statement.executeUpdate("commit");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					refreshTable("");
				}
			}
		});
		btnUpdaten.setBounds(462, 242, 75, 25);
		btnUpdaten.setText("Updaten");
		btnUpdaten.setVisible(false);
		
		Button btnLoeschen = new Button(shell, SWT.NONE);
		btnLoeschen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(myTableAnzeige.getSelectionIndex()<0) {messageBox("Bitte nur gewünschten Datensatz anwählen", "Fehlermeldung", shell);}
				else
				{
					data_NO = allDataList.get(myTableAnzeige.getSelectionIndex());
					
					try {
						dbConnect = DriverManager.getConnection(connectionString);
						Statement statement = dbConnect.createStatement();
						statement.executeUpdate("DELETE FROM ATEMSCHUTZPFLEGESTELLE_DATA WHERE DATA_NO="+data_NO);
						statement.executeUpdate("commit");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					refreshTable("");
				}	
			}
		});
		btnLoeschen.setBounds(462, 270, 75, 25);
		btnLoeschen.setText("L\u00F6schen");
		btnLoeschen.setVisible(false);
		
		
		btnZurueck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
		btnZurueck.setBounds(465, 242, 75, 25);
		btnZurueck.setText("Zur\u00FCck");
		
		txtFlaschen = new Text(shell, SWT.BORDER);
		txtFlaschen.setBounds(75, 275, 185, 21);
		txtFlaschen.setVisible(false);
		
		txtMasken = new Text(shell, SWT.BORDER);
		txtMasken.setBounds(75, 300, 185, 21);
		txtMasken.setVisible(false);
		
		txtGeraete = new Text(shell, SWT.BORDER);
		txtGeraete.setBounds(75, 325, 185, 21);
		txtGeraete.setVisible(false);
		
		lblFeuerwehr.setText("Feuerwehr");
		
		Label lblFlaschen = new Label(shell, SWT.NONE);
		lblFlaschen.setBounds(10, 275, 55, 15);
		lblFlaschen.setText("Flaschen");
		lblFlaschen.setVisible(false);
		
		Label lblMasken = new Label(shell, SWT.NONE);
		lblMasken.setBounds(10, 300, 55, 15);
		lblMasken.setText("Masken");
		lblMasken.setVisible(false);
		
		Label lblGeraete = new Label(shell, SWT.NONE);
		lblGeraete.setBounds(10, 325, 55, 15);
		lblGeraete.setText("Ger\u00E4te");
		lblGeraete.setVisible(false);
		
		Label lblArbeitszeit = new Label(shell, SWT.NONE);
		lblArbeitszeit.setBounds(10, 350, 55, 15);
		lblArbeitszeit.setText("Arbeitszeit");
		lblArbeitszeit.setVisible(false);
		
		txtArbeitszeit = new Text(shell, SWT.BORDER);
		txtArbeitszeit.setBounds(75, 350, 135, 21);
		txtArbeitszeit.setVisible(false);
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("Stunden");
		label.setBounds(216, 353, 44, 15);
		label.setVisible(false);
		
		/*myTableAnzeige.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				int selektierterIndex = myTableAnzeige.getSelectionIndex();
				if (selektierterIndex == -1) {}
				else
				{
					txtFlaschen.setText(myTableAnzeige.getItems()[selektierterIndex].getText(1));
					txtMasken.setText(myTableAnzeige.getItems()[selektierterIndex].getText(2));
					txtGeraete.setText(myTableAnzeige.getItems()[selektierterIndex].getText(3));
					txtArbeitszeit.setText(myTableAnzeige.getItems()[selektierterIndex].getText(4));
					
					for(int i=0; i<allCitiesList.size(); i++)
					{
						String randomCity = allCitiesListName.get(i).toString().trim();
						String searchedCity = myTableAnzeige.getItems()[selektierterIndex].getText(0).toString().trim();
						if(randomCity.equals(searchedCity))
						{
							cmbFeuerwehr.select(i);
						}
					}
				}
			}
		});*/
		

		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) 
			{
				refreshTable("");
			}
		});
		
		try
		{
			dbConnect = DriverManager.getConnection(connectionString);
			Statement statement = dbConnect.createStatement();
			
			cmbFeuerwehr.add("");
			
			myResult = statement.executeQuery("SELECT c.CITY_NAME, c.CITY_NO FROM ATEMSCHUTZPFLEGESTELLE_CITIES c  ORDER BY c.CITY_NAME");
			while (myResult.next())
			{
				cmbFeuerwehr.add(myResult.getObject(1).toString());
				allCitiesList.add(myResult.getObject(2).toString());
				allCitiesListNameSort.add(myResult.getObject(1).toString());
			}
			myResult = statement.executeQuery("SELECT c.CITY_NAME, c.CITY_NO FROM ATEMSCHUTZPFLEGESTELLE_CITIES c");
			while (myResult.next())
			{
				allCitiesListName.add(myResult.getObject(1).toString());
			}
			
		}
		catch (SQLException sqlexc)
		{
			sqlexc.printStackTrace();
			messageBox("SQL-Fehler", "Fehlermeldung", shell);
		}

	}
	
	public void refreshTable(String myCityNo) 
	{
		myTableAnzeige.removeAll();
		try
		{
			dbConnect = DriverManager.getConnection(connectionString);
			Statement statement = dbConnect.createStatement();
			String sqlStatement = "SELECT  CASE  WHEN c.CITY_NAME is null then '-' ELSE c.CITY_NAME END, d.FLASCHEN_FUELLEN, d.FLASCHEN_TUEV, d.MASKEN_PRUEFEN, d.MASKEN_REINIGEN, d.LA_PRUEFEN, d.LA_REINIGEN, d.GERAETE_PRUEFEN, d.GERAETE_REINIGEN, d.TIME_WORK, d.DATE_WORK, CASE  WHEN d.BEMERKUNG is null then '-' ELSE d.BEMERKUNG END FROM ATEMSCHUTZPFLEGESTELLE_DATA d JOIN  PERS p ON p.PERS_NO=d.PERS_NO LEFT JOIN ATEMSCHUTZPFLEGESTELLE_CITIES c ON c.CITY_NO = d.CITY_NO WHERE ";
			sqlStatement = sqlStatement + " p.PERS_NO = "+getPers_NO()+" ";
			if(myCityNo.length()==0) {}
			else
			{
				sqlStatement = sqlStatement + " AND c.CITY_NO = "+myCityNo+" ";
				
			}
			sqlStatement = sqlStatement + " ORDER BY d.DATE_WORK DESC, p.LASTNAME, c.CITY_NAME";
			
			myResult = statement.executeQuery(sqlStatement);
			
			while (myResult.next())
			{
				item = new TableItem(myTableAnzeige, SWT.BORDER);
				item.setText(0, myResult.getObject(1).toString());
				item.setText(1, myResult.getObject(2).toString());
				item.setText(2, myResult.getObject(3).toString());
				item.setText(3, myResult.getObject(4).toString());
				item.setText(4, myResult.getObject(5).toString());
				item.setText(5, myResult.getObject(6).toString());
				item.setText(6, myResult.getObject(7).toString());
				item.setText(7, myResult.getObject(8).toString());
				item.setText(8, myResult.getObject(9).toString());
				item.setText(9, myResult.getObject(10).toString());
				item.setText(10, myResult.getObject(11).toString().substring(8,10)+"."+
								myResult.getObject(11).toString().substring(5,7)+"."+
								myResult.getObject(11).toString().substring(0,4));
				item.setText(11, myResult.getObject(12).toString());
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
