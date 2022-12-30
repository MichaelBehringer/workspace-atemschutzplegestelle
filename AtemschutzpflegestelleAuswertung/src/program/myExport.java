package program;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Text;

public class myExport extends Dialog {

	protected Object result;
	protected Shell shell;
	private static String pdfname = "";
	Document document;
	private String userKlicked = "";
	private String cityKlicked = "";
	private int usingYear = 0;
	private String sqlStatement = "";
	private String connectionString = "";
	public Connection dbConnect = null;
	public ResultSet myResult = null;
	PdfPTable tablepdf;
	private Text txtPfad;
	public static String headerPfad;
	public String xmlPdfPfad = "";

	
	static BaseColor clrHeadline = new BaseColor(0x00, 0x61, 0x71);
	static BaseColor clrnormal = new BaseColor(0x00, 0x00, 0x00);
	static BaseColor whiteColor = new BaseColor(0xFF, 0xFF, 0xFF);
	static Font fntDatenBold2 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, clrHeadline);
	static Font ffw = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, clrnormal);
	static Font frei = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, clrnormal);
	static Font frei2 = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD, whiteColor);
	
	public String getUserKlicked() {
		return userKlicked;
	}
	public void setUserKlicked(String UserKlicked) {
		userKlicked = UserKlicked;
	}
	public String getCityKlicked() {
		return cityKlicked;
	}
	public void setCityKlicked(String CityKlicked) {
		cityKlicked = CityKlicked;
	}
	public int getUsingYear() {
		return usingYear;
	}
	public void setUsingYear(int UsingYear) {
		usingYear = UsingYear;
	}
	public String getSQLStatement() {
		return sqlStatement;
	}
	public void setSQLStatement(String SQLStatement) {
		sqlStatement = SQLStatement;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String ConnectionString) {
		connectionString = ConnectionString;
	}
	public String getXmlPdfPfad() {
		return xmlPdfPfad;
	}
	public void setXmlPdfPfad(String XmlPdfPfad) {
		xmlPdfPfad = XmlPdfPfad;
	}
	public static String getHeaderPfad() {
		return headerPfad;
	}
	public void setHeaderPfad(String HeaderPfad) {
		headerPfad = HeaderPfad;
	}
	public static double rundeAufZweiStellen(double zahl) 
	{   
	      return Math.round( zahl * 100 ) / 100.0;
	}

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public myExport(Shell parent, int style) {
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
		shell.setSize(490, 426);
		shell.setText(getText());
		
		Group grpExportmodus = new Group(shell, SWT.NONE);
		grpExportmodus.setText("Export-Modus");
		grpExportmodus.setBounds(10, 10, 201, 55);
		
		Button radPDF = new Button(grpExportmodus, SWT.RADIO);
		radPDF.setBounds(10, 29, 90, 16);
		radPDF.setText("PDF");
		radPDF.setSelection(true);
		
		Browser myBrowser = new Browser(shell, SWT.NONE);
		myBrowser.setBounds(10, 71, 454, 285);
		
		txtPfad = new Text(shell, SWT.BORDER);
		txtPfad.setEditable(false);
		txtPfad.setBounds(10, 361, 454, 21);
		
		Button btnExport = new Button(shell, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Date datumZeit = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String aktDatumZeit = sdf.format(datumZeit);
				pdfname = getXmlPdfPfad()+"Atemschutzpflegestelle"+aktDatumZeit+".pdf";
				if(getCityKlicked().length()<1&&getUserKlicked().length()<1)
				{
					createPDF(pdfname, userKlicked, connectionString, 
							 sqlStatement, cityKlicked,usingYear,1);
				}
				else if(getCityKlicked().length()>=1&&getUserKlicked().length()<1)
				{
					createPDF(pdfname, userKlicked, connectionString, 
							 sqlStatement, cityKlicked,usingYear,2);
				}
				else if(getCityKlicked().length()<1&&getUserKlicked().length()>=1)
				{
					createPDF(pdfname, userKlicked, connectionString, 
							 sqlStatement, cityKlicked,usingYear,3);
				}
				else
				{
					createPDF(pdfname, userKlicked, connectionString, 
							 sqlStatement, cityKlicked,usingYear,4);
				}
				txtPfad.setText(pdfname);
				myBrowser.setUrl(pdfname);
			}
		});
		btnExport.setBounds(389, 10, 75, 25);
		btnExport.setText("Export");
		
		Button btnAbbrechen = new Button(shell, SWT.NONE);
		btnAbbrechen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.close();
			}
		});
		btnAbbrechen.setBounds(389, 40, 75, 25);
		btnAbbrechen.setText("Abbrechen");

	}
	
	public static void messageBox(String msg, String title, Shell myShell)
	{
		MessageBox msgBox = new MessageBox(myShell);
		msgBox.setMessage(msg);
		msgBox.setText(title);
		msgBox.open();
	}
	
	public static String createPDF(String mypdfname, String UserKlicked, String connectionString, 
								   String sqlStatement, String CityKlicked, int usingYear, int mode)
	{
		Connection dbConnect;
		ResultSet myResult;
		int gesamtFlaschenF = 0;
		int gesamtFlaschenT = 0;
		int gesamtMaskenP = 0;
		int gesamtMaskenR = 0;
		int gesamtLAP = 0;
		int gesamtLAR = 0;
		int gesamtGeraeteP = 0;
		int gesamtGeraeteR = 0;
		//PDF erstellen
		com.itextpdf.text.Image headerImg = null;
		String fullHeaderPfad = getHeaderPfad()+"atemschutzHeader.png";
		
		try {
			headerImg = Image.getInstance(fullHeaderPfad);
		} catch (BadElementException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		

		Document document;
		document = new Document(PageSize.A4);
		try
		{
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(mypdfname));
			Background event = new Background(headerImg);
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
			writer.setPageEvent(event);
			
			HeaderFooter hf = new HeaderFooter();
			
			
			if(mode==1)
			{
				hf.setKopfzeile("Kompletter Ausdruck");
				hf.setText("Abrechnungsjahr " + usingYear);
			}
			else if(mode==2)
			{
				hf.setKopfzeile(CityKlicked);
				hf.setText("Abrechnungsjahr " + usingYear);
			}
			else if(mode==3)
			{
				hf.setKopfzeile(UserKlicked);
				hf.setText("Abrechnungsjahr " + usingYear);
			}
			else if(mode==4)
			{
				hf.setKopfzeile(""+CityKlicked + " / " + UserKlicked);
				hf.setText("Abrechnungsjahr " + usingYear);
			}
			else if(mode==5)
			{
				hf.setKopfzeile("Atemschutzger�tewarte Abrechnung");
				hf.setText("Abrechnungsjahr " + usingYear);
			}
			

			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		    writer.setPageEvent(hf);
		    
		    document.open();
			document.addAuthor("FFWemding");
			document.addTitle("Atemschutzpflegestelle_Abrechnung");
			document.addCreator("FFWemding");
			document.addSubject("FFWemding");
			document.addKeywords("FFWemding");
			
			if(mode==5)
			{
				document.add(new Paragraph(" "));

				PdfPTable tablepdf;
				tablepdf = new PdfPTable(2);
				tablepdf.setWidthPercentage(93);
				tablepdf.setSpacingAfter(10f);
				tablepdf.setSpacingBefore(80f);
				tablepdf.setWidths(new float[] {5f, 5f});
				
				PdfPCell headerCell = null;
				
				headerCell = new PdfPCell(new Phrase("Name", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				tablepdf.addCell(headerCell);
				
				headerCell = new PdfPCell(new Phrase("Arbeitszeit in Stunden", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				tablepdf.addCell(headerCell);

				Double sumHours = 0.0;
				
				try
				{
					sqlStatement = "SELECT p.PERS_NO, CONCAT(p.FIRSTNAME,' ',p.LASTNAME) FROM PERS p ORDER BY p.LASTNAME, p.FIRSTNAME";
					dbConnect = DriverManager.getConnection(connectionString);
					Statement statement = dbConnect.createStatement();
					myResult = statement.executeQuery(sqlStatement);
					while (myResult.next())
					{
						String sqlStatement2 = "SELECT COALESCE(SUM(d.TIME_WORK),0) FROM ATEMSCHUTZPFLEGESTELLE_DATA d WHERE d.PERS_NO = "+myResult.getObject(1);
						sqlStatement2 = sqlStatement2 + " AND d.DATE_WORK >= '"+usingYear+"-1-1' ";
						sqlStatement2 = sqlStatement2 + " AND d.DATE_WORK <= '"+usingYear+"-12-31' ";
						Statement statement2 = dbConnect.createStatement();
						ResultSet myResult2 = statement2.executeQuery(sqlStatement2);
						while (myResult2.next())
						{
							tablepdf.addCell(myResult.getObject(2).toString());
							String arbZeit = myResult2.getObject(1).toString();
							Double myArbZeit = Double.parseDouble(arbZeit);
							arbZeit = rundeAufZweiStellen(myArbZeit)+"";
							tablepdf.addCell(String.valueOf(arbZeit).replace('.', ','));
							
							sumHours += myArbZeit;
						}
					}
					
					tablepdf.addCell("");
					tablepdf.addCell("");
					
					headerCell = new PdfPCell(new Phrase("Summe", fntDatenBold2));
					headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					headerCell.setPaddingBottom(5f);
					headerCell.setPaddingTop(5f);
					headerCell.setPaddingLeft(2f);
					headerCell.setPaddingRight(5f);
					tablepdf.addCell(headerCell);
					
					headerCell = new PdfPCell(new Phrase("", fntDatenBold2));
					headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					headerCell.setPaddingBottom(5f);
					headerCell.setPaddingTop(5f);
					headerCell.setPaddingLeft(2f);
					headerCell.setPaddingRight(5f);
					
					tablepdf.addCell(headerCell);
					tablepdf.addCell("Stunden");
					tablepdf.addCell(String.valueOf(rundeAufZweiStellen(sumHours)).replace('.', ','));					
					
					String sqlStatement3 = "SELECT SUM(flaschen_fuellen), SUM(flaschen_tuev), SUM(Masken_pruefen), SUM(Masken_reinigen), SUM(LA_pruefen), SUM(LA_reinigen), SUM(Geraete_pruefen), SUM(Geraete_reinigen) FROM atemschutzpflegestelle_data WHERE Date_WORk BETWEEN '"+usingYear+"-01-01' AND '"+usingYear+"-12-31'";
					Statement statement3 = dbConnect.createStatement();
					ResultSet myResult3 = statement3.executeQuery(sqlStatement3);
					while (myResult3.next())
					{
						tablepdf.addCell("Flaschen gef�llt");
						tablepdf.addCell(myResult3.getObject(1).toString());
						tablepdf.addCell("Flaschen TUEV");
						tablepdf.addCell(myResult3.getObject(2).toString());
						
						tablepdf.addCell("Masken gepr�ft");
						tablepdf.addCell(myResult3.getObject(3).toString());
						tablepdf.addCell("Masken gereinigt");
						tablepdf.addCell(myResult3.getObject(4).toString());
						
						tablepdf.addCell("LA gepr�ft");
						tablepdf.addCell(myResult3.getObject(5).toString());
						tablepdf.addCell("LA gereinigt");
						tablepdf.addCell(myResult3.getObject(6).toString());

						tablepdf.addCell("Ger�te gepr�ft");
						tablepdf.addCell(myResult3.getObject(7).toString());
						tablepdf.addCell("Ger�te gereinigt");
						tablepdf.addCell(myResult3.getObject(8).toString());
					}
					
					
					document.add(tablepdf);
				}
				catch (SQLException sqlexc)
				{
					sqlexc.printStackTrace();
				}
			}
			else
			{
				PdfPTable tablepdf;
				tablepdf = new PdfPTable(10);
				tablepdf.setWidthPercentage(100);
				tablepdf.setSpacingAfter(10f);
				tablepdf.setSpacingBefore(0f);
				tablepdf.setWidths(new float[] {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 2f, 3f});
				
				PdfPCell headerCell = null;
				PdfPCell dataCell = null;

				headerCell = new PdfPCell(new Phrase("\n\n\n", frei2));
				headerCell.setBorder(Rectangle.NO_BORDER);
				tablepdf.addCell(headerCell);
				tablepdf.setHeaderRows(2);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				tablepdf.addCell(headerCell);
				
				
				headerCell = new PdfPCell(new Phrase("Flaschen\nf�llen", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);
				
				headerCell = new PdfPCell(new Phrase("Flaschen\nT�V", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);
				
				headerCell = new PdfPCell(new Phrase("Masken\npr�fen", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);
				
				headerCell = new PdfPCell(new Phrase("Masken\nreinigen", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);
				
				headerCell = new PdfPCell(new Phrase("LA\npr�fen", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);
				
				headerCell = new PdfPCell(new Phrase("LA\nreinigen", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);
		

				headerCell = new PdfPCell(new Phrase("Ger�te\npr�fen", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);
		

				headerCell = new PdfPCell(new Phrase("Ger�te\nreinigen", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				headerCell.setRotation(90);
				tablepdf.addCell(headerCell);

				headerCell = new PdfPCell(new Phrase("Datum", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				tablepdf.addCell(headerCell);
				
				headerCell = new PdfPCell(new Phrase("Name", fntDatenBold2));
				headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				headerCell.setPaddingBottom(5f);
				headerCell.setPaddingTop(5f);
				headerCell.setPaddingLeft(2f);
				headerCell.setPaddingRight(5f);
				tablepdf.addCell(headerCell);

//				tablepdf.setHeaderRows(1);
				//tablepdf.setSkipFirstHeader(true);
				
				try
				{
					dbConnect = DriverManager.getConnection(connectionString);
					Statement statement = dbConnect.createStatement();
					myResult = statement.executeQuery(sqlStatement);
					while (myResult.next())
					{
						tablepdf.addCell(myResult.getObject(3).toString());
						tablepdf.addCell(myResult.getObject(4).toString());
						tablepdf.addCell(myResult.getObject(5).toString());
						tablepdf.addCell(myResult.getObject(6).toString());
						tablepdf.addCell(myResult.getObject(7).toString());
						tablepdf.addCell(myResult.getObject(8).toString());
						tablepdf.addCell(myResult.getObject(9).toString());
						tablepdf.addCell(myResult.getObject(10).toString());
						tablepdf.addCell(myResult.getObject(12).toString().substring(8,10)+"."+
										 myResult.getObject(12).toString().substring(5,7)+"."+
										 myResult.getObject(12).toString().substring(0,4));
						tablepdf.addCell(myResult.getObject(1).toString());
						
						gesamtFlaschenF = gesamtFlaschenF + Integer.parseInt(myResult.getObject(3).toString());
						gesamtFlaschenT = gesamtFlaschenT + Integer.parseInt(myResult.getObject(4).toString());
						gesamtMaskenP = gesamtMaskenP + Integer.parseInt(myResult.getObject(5).toString());
						gesamtMaskenR = gesamtMaskenR + Integer.parseInt(myResult.getObject(6).toString());
						gesamtLAP = gesamtLAP + Integer.parseInt(myResult.getObject(7).toString());
						gesamtLAR = gesamtLAR + Integer.parseInt(myResult.getObject(8).toString());
						gesamtGeraeteP = gesamtGeraeteP + Integer.parseInt(myResult.getObject(9).toString());
						gesamtGeraeteR = gesamtGeraeteR + Integer.parseInt(myResult.getObject(10).toString());
					}
					
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					tablepdf.addCell("");
					
//					dataCell = new PdfPCell(new Phrase("Gesamt",fntDatenBold2));
//					tablepdf.addCell(dataCell);
					tablepdf.addCell(gesamtFlaschenF+"");
					tablepdf.addCell(gesamtFlaschenT+"");
					tablepdf.addCell(gesamtMaskenP+"");
					tablepdf.addCell(gesamtMaskenR+"");
					tablepdf.addCell(gesamtLAP+"");
					tablepdf.addCell(gesamtLAR+"");
					tablepdf.addCell(gesamtGeraeteP+"");
					tablepdf.addCell(gesamtGeraeteR+"");
					
					tablepdf.addCell("");
					tablepdf.addCell("");
					
					document.add(tablepdf);
				}
				catch (SQLException sqlexc)
				{
					sqlexc.printStackTrace();
				}
			}


		
			}
			catch (DocumentException de)
			{
			} 
			catch (FileNotFoundException e1) 
			{
			}
		finally
		{
			document.close();
		}
		return mypdfname;
	}
}





/****************************************************
 * 
 * iText Kopf- und Fu�zeilen-Klasse
 * 
 ******************************************************/

class HeaderFooter extends PdfPageEventHelper 
{
	static BaseColor clrHeadline = new BaseColor(0x00, 0x61, 0x71);
	static BaseColor clrnormal = new BaseColor(0x00, 0x00, 0x00);
	static BaseColor whiteColor = new BaseColor(0xFF, 0xFF, 0xFF);
	static Font fntDatenBold2 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, clrHeadline);
	static Font ffw = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, clrnormal);
	static Font frei = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, clrnormal);
	static Font frei2 = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD, whiteColor);
	
	
	/** Alternating phrase for the header. */
	Phrase[] header = new Phrase[2];
	/** Current page number (will be reset for every chapter). */
	int pagenumber;
	String kopfzeile = "";
	String text = "";

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}
	
	public String getKopfzeile() 
	{
		return kopfzeile;
	}

	public void setKopfzeile(String kopfzeile) 
	{
		this.kopfzeile = kopfzeile;
	}

	/**
	 * Initialize one of the headers.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) 
	{
		header[0] = new Phrase("");
	}

	/**
	 * Initialize one of the headers, based on the chapter title; reset the
	 * page number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document, float, com.itextpdf.text.Paragraph)
	 */
	public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) 
	{
		pagenumber = 1;
	}

	/**
	 * Increase the page number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) 
	{
		pagenumber++;
		
		Rectangle rect = writer.getBoxSize("art");
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_LEFT,
				new Phrase(String.format(kopfzeile, pagenumber), ffw), rect
						.getLeft(), rect.getTop() - 10, 0);
		
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_LEFT,
				new Phrase(String.format(text, pagenumber), frei), rect
						.getLeft(), rect.getTop() - 30, 0);
	}

	/**
	 * Adds the header and the footer.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) 
	{
		Rectangle rect = writer.getBoxSize("art");

		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_RIGHT,
				new Phrase(String.format("Seite %d", pagenumber), new Font(
						Font.FontFamily.HELVETICA, 8, Font.BOLD)), rect
						.getRight(), rect.getBottom() - 20, 0);
	}
	
}
