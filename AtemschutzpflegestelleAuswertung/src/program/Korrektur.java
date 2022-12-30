package program;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

public class Korrektur extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table myTable;
	private Text txtFlaschenFuellen;
	private Text txtMaskenPruefen;
	private Text txtGeraetePruefen;
	private Text txtArbeitszeit;
	private Text txtDatum;
	private Text txtBemerkung;
	public ResultSet result2 = null;
	public String connectionString = "";
	public Connection dbConnect = null;
	public List<String> allPersList = new ArrayList<String>();
	public List<String> allPersListName = new ArrayList<String>();
	public List<String> allCitiesList = new ArrayList<String>();
	public List<String> allCitiesListName = new ArrayList<String>();
	private int dataNo = 0;
	private Text txtFlaschenTUEV;
	private Text txtMaskenReinigen;
	private Text txtGeraeteReinigen;
	private Text txtLaPruefen;
	private Text txtLaReinigen;
	

	public void setTable(Table MyTable) {
		myTable = MyTable;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String ConnectionString) {
		connectionString = ConnectionString;
	}

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Korrektur(Shell parent, int style) {
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setSize(509, 365);
		shell.setText(getText());
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setBounds(10, 16, 55, 15);
		lblName.setText("Name:");
		
		Label lblFeuerwehr = new Label(shell, SWT.NONE);
		lblFeuerwehr.setBounds(10, 46, 73, 15);
		lblFeuerwehr.setText("Feuerwehr:");
		
		Label lblFlaschen = new Label(shell, SWT.NONE);
		lblFlaschen.setBounds(10, 76, 150, 15);
		lblFlaschen.setText("Flaschen Pr\u00FCfen/T\u00DCV:");
		
		Label lblMasken = new Label(shell, SWT.NONE);
		lblMasken.setBounds(10, 106, 150, 15);
		lblMasken.setText("Masken Pr\u00FCfen/Reinigen:");
		
		Label lblGeraete = new Label(shell, SWT.NONE);
		lblGeraete.setBounds(10, 166, 150, 15);
		lblGeraete.setText("Ger\u00E4te Pr\u00FCfen/Reinigen:");
		
		Label lblArbeitszeit = new Label(shell, SWT.NONE);
		lblArbeitszeit.setBounds(10, 196, 73, 15);
		lblArbeitszeit.setText("Arbeitszeit:");
		
		Label lblDatum = new Label(shell, SWT.NONE);
		lblDatum.setBounds(10, 226, 55, 15);
		lblDatum.setText("Datum:");
		
		Label lblBemerkung = new Label(shell, SWT.NONE);
		lblBemerkung.setBounds(10, 256, 73, 15);
		lblBemerkung.setText("Bemerkung:");
		
		txtFlaschenFuellen = new Text(shell, SWT.BORDER);
		txtFlaschenFuellen.setBounds(157, 76, 150, 21);
		
		txtMaskenPruefen = new Text(shell, SWT.BORDER);
		txtMaskenPruefen.setBounds(157, 106, 150, 21);
		
		txtGeraetePruefen = new Text(shell, SWT.BORDER);
		txtGeraetePruefen.setBounds(157, 166, 150, 21);
		
		txtArbeitszeit = new Text(shell, SWT.BORDER);
		txtArbeitszeit.setBounds(157, 196, 324, 21);
		
		txtDatum = new Text(shell, SWT.BORDER);
		txtDatum.setBounds(157, 226, 324, 21);
		
		txtBemerkung = new Text(shell, SWT.BORDER);
		txtBemerkung.setBounds(157, 256, 324, 21);

		
		txtFlaschenTUEV = new Text(shell, SWT.BORDER);
		txtFlaschenTUEV.setBounds(331, 76, 150, 21);
		
		txtMaskenReinigen = new Text(shell, SWT.BORDER);
		txtMaskenReinigen.setBounds(331, 106, 150, 21);
		
		txtGeraeteReinigen = new Text(shell, SWT.BORDER);
		txtGeraeteReinigen.setBounds(331, 166, 150, 21);
		
		Label lblLA = new Label(shell, SWT.NONE);
		lblLA.setText("LA Pr\u00FCfen/Reinigen");
		lblLA.setBounds(10, 137, 150, 15);
		
		txtLaPruefen = new Text(shell, SWT.BORDER);
		txtLaPruefen.setBounds(157, 136, 150, 21);
		
		txtLaReinigen = new Text(shell, SWT.BORDER);
		txtLaReinigen.setBounds(331, 133, 150, 21);
		
		Combo cmbName = new Combo(shell, SWT.NONE);
		cmbName.setBounds(157, 16, 324, 23);
		
		Combo cmbFeuerwehr = new Combo(shell, SWT.NONE);
		cmbFeuerwehr.setBounds(157, 46, 324, 23);
		
		Button btnSpeichern = new Button(shell, SWT.NONE);
		btnSpeichern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					dbConnect = DriverManager.getConnection(connectionString);
					Statement statement = dbConnect.createStatement();

					int cityNo = 0;
					int persNo = 0;
					
					for(int i=0; i<allCitiesList.size(); i++)
					{
						String randomCity = allCitiesListName.get(i).toString().trim();
						if(randomCity.equals(cmbFeuerwehr.getText()))
						{
							cityNo = Integer.parseInt(allCitiesList.get(i));
						}
					}
					
					for(int i=0; i<allPersList.size(); i++)
					{
						String randomCity = allPersListName.get(i).toString().trim();;
						if(randomCity.equals(cmbName.getText()))
						{
							persNo =Integer.parseInt(allPersList.get(i));
						}
					}
					
					String day = txtDatum.getText().substring(0, 2);
					String month = txtDatum.getText().substring(3, 5);
					String year = txtDatum.getText().substring(6, 10);
					
					statement.executeUpdate("UPDATE atemschutzpflegestelle_data set CITY_NO = "+cityNo+", FLASCHEN_FUELLEN = "+txtFlaschenFuellen.getText()+", FLASCHEN_TUEV = "+txtFlaschenTUEV.getText()+", LA_PRUEFEN = "+txtLaPruefen.getText()+", LA_REINIGEN = "+txtLaReinigen.getText()+", MASKEN_PRUEFEN = "+txtMaskenPruefen.getText()+", MASKEN_REINIGEN = "+txtMaskenReinigen.getText()+", GERAETE_PRUEFEN = "+txtGeraetePruefen.getText()+", GERAETE_REINIGEN = "+txtGeraeteReinigen.getText()+", PERS_NO = "+persNo+", TIME_WORK = "+txtArbeitszeit.getText().replace(',', '.')+", DATE_WORK = \""+year+"-"+month+"-"+day+"\", BEMERKUNG = '"+txtBemerkung.getText()+"' Where DATA_NO = "+dataNo);
				}
				catch (SQLException sqlexc)
				{
					sqlexc.printStackTrace();
				}
				
				shell.close();
			}
		});
		btnSpeichern.setBounds(262, 294, 138, 25);
		btnSpeichern.setText("Speichern und Zur\u00FCck");
		
		Button btnAbbrechen = new Button(shell, SWT.NONE);
		btnAbbrechen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
		btnAbbrechen.setBounds(406, 294, 75, 25);
		btnAbbrechen.setText("Abbrechen");
		try
		{
			dbConnect = DriverManager.getConnection(connectionString);
			Statement statement = dbConnect.createStatement();
			result2 = statement.executeQuery("SELECT p.FIRSTNAME, p.LASTNAME, p.PERS_NO FROM PERS p, FUNCTION f WHERE p.FUNCTION_NO = f.FUNCTION_NO and p.FUNCTION_NO = 1 ORDER BY p.LASTNAME, p.FIRSTNAME");
			while (result2.next())
			{
				cmbName.add(result2.getObject(1).toString()+" "+result2.getObject(2).toString());
				allPersList.add(result2.getObject(3).toString());
				allPersListName.add(result2.getObject(1).toString()+" "+result2.getObject(2).toString());
			}
			
			result2 = statement.executeQuery("SELECT c.CITY_NAME, c.CITY_NO FROM ATEMSCHUTZPFLEGESTELLE_CITIES c ORDER BY c.CITY_NAME");
			while (result2.next())
			{
				cmbFeuerwehr.add(result2.getObject(1).toString());
				allCitiesList.add(result2.getObject(2).toString());
				allCitiesListName.add(result2.getObject(1).toString());
			}
		}
		catch (SQLException sqlexc)
		{
			sqlexc.printStackTrace();
		}
		
		int selektierterIndex = myTable.getSelectionIndex();
		txtFlaschenFuellen.setText(myTable.getItems()[selektierterIndex].getText(2));
		txtFlaschenTUEV.setText(myTable.getItems()[selektierterIndex].getText(3));
		txtMaskenPruefen.setText(myTable.getItems()[selektierterIndex].getText(4));
		txtMaskenReinigen.setText(myTable.getItems()[selektierterIndex].getText(5));
		txtLaPruefen.setText(myTable.getItems()[selektierterIndex].getText(6));
		txtLaReinigen.setText(myTable.getItems()[selektierterIndex].getText(7));
		txtGeraetePruefen.setText(myTable.getItems()[selektierterIndex].getText(8));
		txtGeraeteReinigen.setText(myTable.getItems()[selektierterIndex].getText(9));
		txtArbeitszeit.setText(myTable.getItems()[selektierterIndex].getText(10));
		txtDatum.setText(myTable.getItems()[selektierterIndex].getText(11));
		txtBemerkung.setText(myTable.getItems()[selektierterIndex].getText(12));
		dataNo = Integer.parseInt(myTable.getItems()[selektierterIndex].getText(13));
		
		
		for(int i=0; i<allCitiesList.size(); i++)
		{
			String randomCity = allCitiesListName.get(i).toString().trim();
			String searchedCity = myTable.getItems()[selektierterIndex].getText(1).toString().trim();
			if(randomCity.equals(searchedCity))
			{
				cmbFeuerwehr.select(i);
			}
		}
		
		for(int i=0; i<allPersList.size(); i++)
		{
			String randomCity = allPersListName.get(i).toString().trim();
			String searchedCity = myTable.getItems()[selektierterIndex].getText(0).toString().trim();
			if(randomCity.equals(searchedCity))
			{
				cmbName.select(i);
			}
		}

	}
}
