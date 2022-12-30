package program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class Main extends Shell {
	public List<String> allPersList = new ArrayList<String>();
	public List<String> allCitiesList = new ArrayList<String>();
	public List<String> allCitiesListName = new ArrayList<String>();
	public ResultSet result = null;
	public String connectionString = "";
	public Connection dbConnect = null;
	public String sqlStatement = "";
	public String oldsqlStatement = "";
	private Table tblAnzeige;
	@SuppressWarnings("unused")
	private int currentDay = 0;
	@SuppressWarnings("unused")
	private int currentMonth = 0;
	private int currentYear = 0;
	TableItem item = null;
	int anzahlTreffer = 0;
	public String xmlPdfPfad = "";
	public String headerPfad = "";

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Main shell = new Main(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public Main(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		final Shell shell = new Shell(display);
		
		String filepath = "Atemschutzpflegestelle_Config.xml";
		File f = new File(filepath);
		if(!f.exists()) 
		{ 
			xmlWrite(filepath, "<?xml version=\"1.0\"?>", true);
			xmlWrite(filepath, "<myData>", true);
			xmlWrite(filepath, "\t<Hostname></Hostname>", true);
			xmlWrite(filepath, "\t<Port></Port>", true);
			xmlWrite(filepath, "\t<dbName></dbName>", true);
			xmlWrite(filepath, "\t<user></user>", true);
			xmlWrite(filepath, "\t<password></password>", true);
			xmlWrite(filepath, "\t<pdfPfad>C:\\temp\\ffw\\atemschutzpflegestelle\\</pdfPfad>", true);
			xmlWrite(filepath, "\t<headerPfad>C:\\temp\\ffw\\pic\\</headerPfad>", true);
			xmlWrite(filepath, "</myData>", true);
			
			messageBox("Bitte XML-Datei befüllen und danach Programm neustarten","Info-Fehler",shell);
		}
		else
		{
			try
			{
			File file = new File("Atemschutzpflegestelle_Config.xml");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			String hostname = document.getElementsByTagName("Hostname").item(0).getTextContent();
			String port = document.getElementsByTagName("Port").item(0).getTextContent();
			String dbName = document.getElementsByTagName("dbName").item(0).getTextContent();
			String user = document.getElementsByTagName("user").item(0).getTextContent();
			String password = document.getElementsByTagName("password").item(0).getTextContent();
			xmlPdfPfad = document.getElementsByTagName("pdfPfad").item(0).getTextContent();
			headerPfad = document.getElementsByTagName("headerPfad").item(0).getTextContent();
			connectionString = "jdbc:mysql://"+hostname+":"+port+"/"+dbName+"?user="+user+"&password="+password;
			}
			catch (ParserConfigurationException e1) {
			}
			catch (IOException e2) {
			}
			catch (SAXException e3) {
			}
		}
		
		tblAnzeige = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		
		tblAnzeige.setBounds(10, 142, 1268, 241);
		tblAnzeige.setHeaderVisible(true);
		tblAnzeige.setLinesVisible(true);
		
		TableColumn clmName = new TableColumn(tblAnzeige, SWT.NONE);
		clmName.setWidth(129);
		clmName.setText("Name");
		
		TableColumn clmFeuerwehr = new TableColumn(tblAnzeige, SWT.NONE);
		clmFeuerwehr.setWidth(95);
		clmFeuerwehr.setText("Feuerwehr");
		
		TableColumn clmFlaschenFuellen = new TableColumn(tblAnzeige, SWT.NONE);
		clmFlaschenFuellen.setWidth(100);
		clmFlaschenFuellen.setText("Flaschen f\u00FCllen");
		
		TableColumn clmFlaschenTUEV = new TableColumn(tblAnzeige, SWT.NONE);
		clmFlaschenTUEV.setWidth(100);
		clmFlaschenTUEV.setText("Flaschen T\u00DCV");
		
		TableColumn clmMaskenPruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmMaskenPruefen.setWidth(100);
		clmMaskenPruefen.setText("Masken pr\u00FCfen");
		
		TableColumn clmMaskenReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmMaskenReinigen.setWidth(100);
		clmMaskenReinigen.setText("Masken reinigen");
		
		TableColumn clmLaPruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmLaPruefen.setWidth(100);
		clmLaPruefen.setText("LA pr\u00FCfen");
		
		TableColumn clmLaReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmLaReinigen.setWidth(100);
		clmLaReinigen.setText("LA Reinigen");
		
		TableColumn clmGeraetePruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmGeraetePruefen.setWidth(100);
		clmGeraetePruefen.setText("Ger\u00E4te pr\u00FCfen");
		
		TableColumn clmGeraeteReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmGeraeteReinigen.setWidth(100);
		clmGeraeteReinigen.setText("Ger\u00E4te reinigen");
		
		TableColumn clmArbeitszeit = new TableColumn(tblAnzeige, SWT.NONE);
		clmArbeitszeit.setWidth(69);
		clmArbeitszeit.setText("Arbeitszeit");
		
		TableColumn clmDatum = new TableColumn(tblAnzeige, SWT.NONE);
		clmDatum.setWidth(74);
		clmDatum.setText("Datum");
		
		TableColumn tblclmnBemerkung = new TableColumn(tblAnzeige, SWT.NONE);
		tblclmnBemerkung.setWidth(200);
		tblclmnBemerkung.setText("Bemerkung");
		
		TableColumn tblclmnDatano = new TableColumn(tblAnzeige, SWT.NONE);
		tblclmnDatano.setWidth(0);
		tblclmnDatano.setText("DATA_NO");
		
		Combo cmbName = new Combo(this, SWT.READ_ONLY);
		cmbName.setBounds(60, 10, 170, 23);
		
		Combo cmbFeuerwehr = new Combo(this, SWT.READ_ONLY);
		cmbFeuerwehr.setBounds(404, 10, 170, 23);
		
		DateTime dateTimeEnd = new DateTime(this, SWT.BORDER);
		dateTimeEnd.setBounds(404, 40, 170, 24);
		currentDay = dateTimeEnd.getDay();
		currentMonth = dateTimeEnd.getMonth();
		currentYear = dateTimeEnd.getYear();
		
		DateTime dateTimeBegin = new DateTime(this, SWT.BORDER);
		dateTimeBegin.setBounds(60, 40, 170, 24);
		dateTimeBegin.setDay(1);
		dateTimeBegin.setMonth(0);
		dateTimeBegin.setYear(currentYear);
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setBounds(10, 13, 35, 15);
		lblName.setText("Name");
		
		Label lblFeuerwehr = new Label(this, SWT.NONE);
		lblFeuerwehr.setBounds(337, 13, 61, 15);
		lblFeuerwehr.setText("Feuerwehr");
		
		Label lblVon = new Label(this, SWT.NONE);
		lblVon.setBounds(10, 43, 35, 15);
		lblVon.setText("Von");
		
		Label lblBis = new Label(this, SWT.NONE);
		lblBis.setBounds(337, 43, 55, 15);
		lblBis.setText("Bis");
		
		Label lblDaten = new Label(this, SWT.NONE);
		lblDaten.setBounds(10, 124, 80, 15);
		lblDaten.setText("Daten (0)");
		
		Button btnSuche = new Button(this, SWT.NONE);
		btnSuche.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				tblAnzeige.removeAll();
				
				sqlStatement = "SELECT CONCAT(p.FIRSTNAME,' ',p.LASTNAME), CASE  WHEN c.CITY_NAME is null then '-' ELSE c.CITY_NAME END,  d.FLASCHEN_FUELLEN, d.FLASCHEN_TUEV, d.MASKEN_PRUEFEN, d.MASKEN_REINIGEN, d.LA_PRUEFEN, d.LA_REINIGEN, d.GERAETE_PRUEFEN, d.GERAETE_REINIGEN, d.TIME_WORK, d.DATE_WORK, CASE  WHEN d.BEMERKUNG is null then '-' ELSE d.BEMERKUNG END, d.DATA_NO FROM ATEMSCHUTZPFLEGESTELLE_DATA d JOIN  PERS p ON p.PERS_NO=d.PERS_NO LEFT JOIN ATEMSCHUTZPFLEGESTELLE_CITIES c ON c.CITY_NO = d.CITY_NO WHERE ";
				
				if (cmbName.getSelectionIndex()!=-1&&cmbName.getSelectionIndex()!=0)
				{
					sqlStatement = sqlStatement + " p.PERS_NO = "+allPersList.get(cmbName.getSelectionIndex())+" AND ";
				}
				
				if (cmbFeuerwehr.getSelectionIndex()!=-1&&cmbFeuerwehr.getSelectionIndex()!=0)
				{
					sqlStatement = sqlStatement + " c.CITY_NO = "+allCitiesList.get(cmbFeuerwehr.getSelectionIndex())+" AND ";
				}
				
				int day_begin = dateTimeBegin.getDay();
				int month_begin = dateTimeBegin.getMonth()+1;
				int year_begin = dateTimeBegin.getYear();
				String date_begin = year_begin + "-" + month_begin + "-" + day_begin;
				sqlStatement = sqlStatement + " d.DATE_WORK >= '"+date_begin+"' ";
				
				int day_end = dateTimeEnd.getDay();
				int month_end = dateTimeEnd.getMonth()+1;
				int year_end = dateTimeEnd.getYear();
				String date_end = year_end + "-" + month_end + "-" + day_end;
				sqlStatement = sqlStatement + "AND d.DATE_WORK >= '"+date_begin+"' ";
				
				sqlStatement = sqlStatement + "ORDER BY d.DATE_WORK DESC, p.LASTNAME, c.CITY_NAME";
				
				//System.out.println(sqlStatement);
				
				try
				{
					anzahlTreffer = 0;
					dbConnect = DriverManager.getConnection(connectionString);
					Statement statement = dbConnect.createStatement();
					result = statement.executeQuery(sqlStatement);
					while (result.next())
					{
						anzahlTreffer=anzahlTreffer+1;
						item = new TableItem(tblAnzeige, SWT.BORDER);
						item.setText(0, result.getObject(1).toString());
						item.setText(1, result.getObject(2).toString());
						item.setText(2, result.getObject(3).toString());
						item.setText(3, result.getObject(4).toString());
						item.setText(4, result.getObject(5).toString());
						item.setText(5, result.getObject(6).toString());
						item.setText(6, result.getObject(7).toString());
						item.setText(7, result.getObject(8).toString());
						item.setText(8, result.getObject(9).toString());
						item.setText(9, result.getObject(10).toString());
						item.setText(10, result.getObject(11).toString());
						item.setText(11, result.getObject(12).toString().substring(8,10)+"."+
										result.getObject(12).toString().substring(5,7)+"."+
										result.getObject(12).toString().substring(0,4));
						item.setText(12, result.getObject(13).toString());
						item.setText(13, result.getObject(14).toString());
					}
					lblDaten.setText("Daten ("+anzahlTreffer+")");
				}
				catch (SQLException sqlexc)
				{
					sqlexc.printStackTrace();
					messageBox("SQL-Feheler", "Fehlermeldung", shell);
				}
			}
		});
		btnSuche.setBounds(499, 111, 75, 25);
		btnSuche.setText("Suche");
		
		Button btnExport = new Button(this, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (anzahlTreffer==0){messageBox("Es muss mindestens einen Datzensatz geben", "Ungültig", shell);}
				else
				{
					myExport myEx = new myExport(shell, SWT.OPEN);
					myEx.setText("Export");
					myEx.setUserKlicked(cmbName.getText());
					myEx.setCityKlicked(cmbFeuerwehr.getText());
					myEx.setUsingYear(dateTimeBegin.getYear());
					myEx.setSQLStatement(sqlStatement);
					myEx.setConnectionString(connectionString);
					myEx.setHeaderPfad(headerPfad);
					myEx.setXmlPdfPfad(xmlPdfPfad);
					myEx.open();
				}
			}
		});
		btnExport.setBounds(499, 80, 75, 25);
		btnExport.setText("Export");
		
		try
		{
			cmbName.add("");
			allPersList.add("");
			cmbFeuerwehr.add("");
			
			Menu menu = new Menu(this, SWT.BAR);
			setMenuBar(menu);
			
			MenuItem mntmAdmin = new MenuItem(menu, SWT.CASCADE);
			mntmAdmin.setText("Admin");
			
			Menu menu_1 = new Menu(mntmAdmin);
			mntmAdmin.setMenu(menu_1);
			
			MenuItem mntmFeuerwehrHinzufuegen = new MenuItem(menu_1, SWT.NONE);
			mntmFeuerwehrHinzufuegen.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) 
				{
					NeuHinzufuegen neuHin = new NeuHinzufuegen(shell, SWT.OPEN);
					neuHin.setText("Neu");
					neuHin.setModus("Feuerwehr");
					neuHin.setConnectionString(connectionString);
					neuHin.open();
				}
			});
			mntmFeuerwehrHinzufuegen.setText("Feuerwehr hinzuf\u00FCgen");
			
			MenuItem mntmAtenschutzpfegewartHinzufuegen = new MenuItem(menu_1, SWT.NONE);
			mntmAtenschutzpfegewartHinzufuegen.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) 
				{
					NeuHinzufuegen neuHin = new NeuHinzufuegen(shell, SWT.OPEN);
					neuHin.setText("Neu");
					neuHin.setModus("Atemschutz");
					neuHin.setConnectionString(connectionString);
					neuHin.open();
				}
			});
			mntmAtenschutzpfegewartHinzufuegen.setText("Atemschutzpfegewart hinzuf\u00FCgen");
			
			MenuItem mntmJahresauswertung = new MenuItem(menu, SWT.CASCADE);
			mntmJahresauswertung.setText("Jahresauswertung");
			
			Menu menu_2 = new Menu(mntmJahresauswertung);
			mntmJahresauswertung.setMenu(menu_2);
			
			MenuItem mntmFeuerwehren = new MenuItem(menu_2, SWT.NONE);
			mntmFeuerwehren.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) 
				{
					myExport.headerPfad=headerPfad;
					Jahresauswertung JaAu = new Jahresauswertung(shell, SWT.OPEN);
					JaAu.setText("Jahresauswertung - Feuerwehr");
					JaAu.setModus("Feuerwehr");
					JaAu.setConnectionString(connectionString);
					JaAu.setHeaderPfad(headerPfad);
					JaAu.setXmlPdfPfad(xmlPdfPfad);
					JaAu.open();
				}
			});
			mntmFeuerwehren.setText("Feuerwehren");
			
			MenuItem mntmGeraetewarte = new MenuItem(menu_2, SWT.NONE);
			mntmGeraetewarte.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) 
				{
					myExport.headerPfad=headerPfad;
					Jahresauswertung JaAu = new Jahresauswertung(shell, SWT.OPEN);
					JaAu.setText("Jahresauswertung - Gerätewart");
					JaAu.setModus("Geraetewart");
					JaAu.setConnectionString(connectionString);
					JaAu.setHeaderPfad(headerPfad);
					JaAu.setXmlPdfPfad(xmlPdfPfad);
					JaAu.open();
				}
			});
			mntmGeraetewarte.setText("Ger\u00E4tewarte");
			
			MenuItem mntmHilfe = new MenuItem(menu, SWT.NONE);
			mntmHilfe.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) 
				{
					myHilfe myHi = new myHilfe(shell, SWT.OPEN);
					myHi.setText("Hilfe");
					myHi.setHeaderPfad(headerPfad);
					myHi.open();
				}
			});
			mntmHilfe.setText("Hilfe");
			allCitiesList.add("");
			allCitiesListName.add("");
			
			dbConnect = DriverManager.getConnection(connectionString);
			Statement statement = dbConnect.createStatement();
			result = statement.executeQuery("SELECT p.FIRSTNAME, p.LASTNAME, p.PERS_NO FROM PERS p, FUNCTION f WHERE p.FUNCTION_NO = f.FUNCTION_NO and p.FUNCTION_NO = 1 ORDER BY p.LASTNAME, p.FIRSTNAME");
			while (result.next())
			{
				cmbName.add(result.getObject(1).toString()+" "+result.getObject(2).toString());
				allPersList.add(result.getObject(3).toString());
			}
			
			result = statement.executeQuery("SELECT c.CITY_NAME, c.CITY_NO FROM ATEMSCHUTZPFLEGESTELLE_CITIES c ORDER BY c.CITY_NAME");
			while (result.next())
			{
				cmbFeuerwehr.add(result.getObject(1).toString());
				allCitiesList.add(result.getObject(2).toString());
				allCitiesListName.add(result.getObject(1).toString());
			}
			
		}
		catch (SQLException sqlexc)
		{
			sqlexc.printStackTrace();
			messageBox("SQL-Feheler", "Fehlermeldung", shell);
		}
		
		tblAnzeige.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) 
			{
				Korrektur korr = new Korrektur(shell, SWT.OPEN);
				korr.setText("Korrektur");
				korr.setTable(tblAnzeige);
				korr.setConnectionString(connectionString);
//				korr.setAllCitiesList(allCitiesList);
//				korr.setAllCitiesListName(allCitiesListName);
//				korr.setAllPersList(allPersList);
				korr.open();


tblAnzeige.removeAll();
				
				sqlStatement = "SELECT CONCAT(p.FIRSTNAME,' ',p.LASTNAME), CASE  WHEN c.CITY_NAME is null then '-' ELSE c.CITY_NAME END,  d.FLASCHEN_FUELLEN, d.FLASCHEN_TUEV, d.MASKEN_PRUEFEN, d.MASKEN_REINIGEN, d.LA_PRUEFEN, d.LA_REINIGEN, d.GERAETE_PRUEFEN, d.GERAETE_REINIGEN, d.TIME_WORK, d.DATE_WORK, CASE  WHEN d.BEMERKUNG is null then '-' ELSE d.BEMERKUNG END, d.DATA_NO FROM ATEMSCHUTZPFLEGESTELLE_DATA d JOIN  PERS p ON p.PERS_NO=d.PERS_NO LEFT JOIN ATEMSCHUTZPFLEGESTELLE_CITIES c ON c.CITY_NO = d.CITY_NO WHERE ";
				
				if (cmbName.getSelectionIndex()!=-1&&cmbName.getSelectionIndex()!=0)
				{
					sqlStatement = sqlStatement + " p.PERS_NO = "+allPersList.get(cmbName.getSelectionIndex())+" AND ";
				}
				
				if (cmbFeuerwehr.getSelectionIndex()!=-1&&cmbFeuerwehr.getSelectionIndex()!=0)
				{
					sqlStatement = sqlStatement + " c.CITY_NO = "+allCitiesList.get(cmbFeuerwehr.getSelectionIndex())+" AND ";
				}
				
				int day_begin = dateTimeBegin.getDay();
				int month_begin = dateTimeBegin.getMonth()+1;
				int year_begin = dateTimeBegin.getYear();
				String date_begin = year_begin + "-" + month_begin + "-" + day_begin;
				sqlStatement = sqlStatement + " d.DATE_WORK >= '"+date_begin+"' ";
				
				int day_end = dateTimeEnd.getDay();
				int month_end = dateTimeEnd.getMonth()+1;
				int year_end = dateTimeEnd.getYear();
				String date_end = year_end + "-" + month_end + "-" + day_end;
				sqlStatement = sqlStatement + "AND d.DATE_WORK >= '"+date_begin+"' ";
				
				sqlStatement = sqlStatement + "ORDER BY d.DATE_WORK DESC, p.LASTNAME, c.CITY_NAME";
				
				//System.out.println(sqlStatement);
				
				try
				{
					anzahlTreffer = 0;
					dbConnect = DriverManager.getConnection(connectionString);
					Statement statement = dbConnect.createStatement();
					result = statement.executeQuery(sqlStatement);
					while (result.next())
					{
						anzahlTreffer=anzahlTreffer+1;
						item = new TableItem(tblAnzeige, SWT.BORDER);
						item.setText(0, result.getObject(1).toString());
						item.setText(1, result.getObject(2).toString());
						item.setText(2, result.getObject(3).toString());
						item.setText(3, result.getObject(4).toString());
						item.setText(4, result.getObject(5).toString());
						item.setText(5, result.getObject(6).toString());
						item.setText(6, result.getObject(7).toString());
						item.setText(7, result.getObject(8).toString());
						item.setText(8, result.getObject(9).toString());
						item.setText(9, result.getObject(10).toString());
						item.setText(10, result.getObject(11).toString());
						item.setText(11, result.getObject(12).toString().substring(8,10)+"."+
										result.getObject(12).toString().substring(5,7)+"."+
										result.getObject(12).toString().substring(0,4));
						item.setText(12, result.getObject(13).toString());
						item.setText(13, result.getObject(14).toString());
					}
					lblDaten.setText("Daten ("+anzahlTreffer+")");
				}
				catch (SQLException sqlexc)
				{
					sqlexc.printStackTrace();
					messageBox("SQL-Feheler", "Fehlermeldung", shell);
				}
			}
		});
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Auswertung");
		setSize(1412, 451);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public static void messageBox(String msg, String title, Shell myShell)
	{
		MessageBox msgBox = new MessageBox(myShell);
		msgBox.setMessage(msg);
		msgBox.setText(title);
		msgBox.open();
	}
	
	public static void xmlWrite(String filename, String text, boolean mode) {
		FileWriter outFile = null;
		PrintWriter out = null;
		try {
			outFile = new FileWriter(filename, mode);
			out = new PrintWriter(outFile);
			out.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	public static void myFileWrite(String filename, String text, boolean mode)
	{
		FileWriter outFile = null;
		PrintWriter out = null;
		try
		{
			outFile = new FileWriter(filename, mode);
			out = new PrintWriter(outFile);
			out.println(text);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (out != null)
			{
				out.flush();
				out.close();
			}
		}
	}
}
