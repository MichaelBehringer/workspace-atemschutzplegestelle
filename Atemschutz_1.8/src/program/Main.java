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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.DateTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

public class Main extends Shell {
	public String pers_NO = "";
	public List<String> allPersList = new ArrayList<String>();
	public List<String> allCitiesList = new ArrayList<String>();
	public List<String> allCitiesListName = new ArrayList<String>();
	public List<String> allCitiesListNameSort = new ArrayList<String>();
	public ResultSet result = null;
	public String connectionString = "";
	public Connection dbConnect = null;
	private Text txtFlaschenFuellen;
	private Text txtMaskenPruefen;
	private Text txtGeraetePruefen;
	private Table tblAnzeige;
	TableItem item = null;
	public boolean yesEdit = false;
	private Text txtArbeitszeit;
	private int currentDay = 0;
	private int currentMonth = 0;
	private int currentYear = 0;
	public String myKorrekturName = "";
	public String xmlPdfPfad = "";
	public String headerPfad = "";
	public static boolean sonstigesStatus = false;
	private Text txtNrFlaschenFuellen;
	private Text txtNrMaskenPruefen;
	private Text txtNrGeraetePruefen;
	public static String publicDbName = "";
	private Text txtFlaschenTUEV;
	private Text txtNrFlaschenTUEV;
	private Text txtMaskenReinigen;
	private Text txtNrMaskenReinigen;
	private Text txtLaPruefen;
	private Text txtNrLaPruefen;
	private Text txtLaReinigen;
	private Text txtNrLaReinigen;
	private Text txtGeraeteReinigen;
	private Text txtNrGeraeteReinigen;
	private Combo cmbLogin;
	private Label lblLogin;
	private Combo cmbCities;
	private Label lblCities;
	private Label lblFlaschen;
	private Label lblMaskenPruefen;
	private Label lblGeraetePruefen;
	private Button btnLogout;
	private Button btnHinzufuegen;
	private Button btnAbbrechen;
	private Button btnEintragBearbeiten;
	private Button btnUpdaten;
	private Label lblArbeitszeit;
	private Label lblDatum;
	private Button btnSpeichern;
	private Button btnKorrektur;
	private Button btnSonstiges;
	private Label lblStunden;
	private Button btnNeuerEintrag;
	private Button btnEintragLoeschen;
	private Button btnLogin;
	private Label lblMenge;
	private Label lblNummern;
	private Label lblFlaschenTUEV;
	private Label lblMaskenReinigen;
	private Label lblLaPruefen;
	private Label lblLaReinigen;
	private Label lblGeraeteReinigen;
	private DateTime dateTime;

	TableColumn clmFeuerwehr;
	TableColumn clmFlaschenFuellen;
	TableColumn clmFlaschenTUEV;
	TableColumn clmMaskenPruefen;
	TableColumn clmMaskenReinigen;
	TableColumn clmLaPruefen;
	TableColumn clmLaReinigen;
	TableColumn clmGeraetePruefen;
	TableColumn clmGeraeteReinigen;
	TableColumn clmArbeitszeit;
	TableColumn clmNrFlascheFuellen;
	TableColumn clmNrFlascheTuev;
	TableColumn clmNrMaskePruefen;
	TableColumn clmNrMaskeReinigen;
	TableColumn clmNrLaPruefen;
	TableColumn clmNrLaReinigen;
	TableColumn clmNrGeraetePruefen;
	TableColumn clmNrGeraeteReinigen;

	/**
	 * Launch the application.
	 * 
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

	public static boolean getSonstigesStatus() {
		return sonstigesStatus;
	}

	public static void setSonstigesStatus(boolean SonstigesStatus) {
		sonstigesStatus = SonstigesStatus;
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public Main(Display display) {
		super(display, SWT.SHELL_TRIM);
		addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if(tblAnzeige!=null) {
					Point point = new Point(display.getActiveShell().getSize().x-40, tblAnzeige.getSize().y);
					tblAnzeige.setSize(point);
				}
			}
		});

		final Shell shell = new Shell(display);

		String filepath = "Atemschutzpflegestelle_Config.xml";
		File f = new File(filepath);
		if (!f.exists()) {
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

			messageBox("Bitte XML-Datei befüllen und danach Programm neustarten", "Info-Fehler", shell);
		} else {
			try {

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
				connectionString = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + user
						+ "&password=" + password;
				publicDbName = dbName;
			} catch (ParserConfigurationException e1) {
			} catch (IOException e2) {
			} catch (SAXException e3) {
			}
		}
		

		createContents();

		txtFlaschenFuellen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtFlaschenFuellen.getText().length() == 0) {
					txtFlaschenFuellen.setText("0");
				} else {
					try {
						Integer.parseInt(txtFlaschenFuellen.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtFlaschenFuellen.setText("");
						txtFlaschenFuellen.setFocus();
					}
				}
			}
		});

		txtMaskenPruefen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtMaskenPruefen.getText().length() == 0) {
					txtMaskenPruefen.setText("0");
				} else {
					try {
						Integer.parseInt(txtMaskenPruefen.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtMaskenPruefen.setText("");
						txtMaskenPruefen.setFocus();
					}
				}
			}
		});

		txtGeraetePruefen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtGeraetePruefen.getText().length() == 0) {
					txtGeraetePruefen.setText("0");
				} else {
					try {
						Integer.parseInt(txtGeraetePruefen.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtGeraetePruefen.setText("");
						txtGeraetePruefen.setFocus();
					}
				}
			}
		});
		
		txtLaPruefen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtLaPruefen.getText().length() == 0) {
					txtLaPruefen.setText("0");
				} else {
					try {
						Integer.parseInt(txtLaPruefen.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtLaPruefen.setText("");
						txtLaPruefen.setFocus();
					}
				}
			}
		});
		
		txtFlaschenTUEV.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtFlaschenTUEV.getText().length() == 0) {
					txtFlaschenTUEV.setText("0");
				} else {
					try {
						Integer.parseInt(txtFlaschenTUEV.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtFlaschenTUEV.setText("");
						txtFlaschenTUEV.setFocus();
					}
				}
			}
		});

		txtMaskenReinigen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtMaskenReinigen.getText().length() == 0) {
					txtMaskenReinigen.setText("0");
				} else {
					try {
						Integer.parseInt(txtMaskenReinigen.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtMaskenReinigen.setText("");
						txtMaskenReinigen.setFocus();
					}
				}
			}
		});

		txtGeraeteReinigen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtGeraeteReinigen.getText().length() == 0) {
					txtGeraeteReinigen.setText("0");
				} else {
					try {
						Integer.parseInt(txtGeraeteReinigen.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtGeraeteReinigen.setText("");
						txtGeraeteReinigen.setFocus();
					}
				}
			}
		});
		
		txtLaReinigen.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtLaReinigen.getText().length() == 0) {
					txtLaReinigen.setText("0");
				} else {
					try {
						Integer.parseInt(txtLaReinigen.getText());
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtLaReinigen.setText("");
						txtLaReinigen.setFocus();
					}
				}
			}
		});

		tblAnzeige.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (yesEdit) {
					int selektierterIndex = tblAnzeige.getSelectionIndex();
					if (selektierterIndex == -1) {
					} else {
						fillEditFieldsFromTable(selektierterIndex);
					}
				}
			}
		});

		txtArbeitszeit.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtArbeitszeit.getText().length() == 0) {
					txtArbeitszeit.setText("0");
				} else {
					try {
						Double.parseDouble(txtArbeitszeit.getText().replace(',', '.'));
					} catch (NumberFormatException exc) {
						messageBox("Bitte nur Zahlen eingeben", "Eingabe-Fehler", shell);
						txtArbeitszeit.setText("");
						txtArbeitszeit.setFocus();
					}
				}
			}
		});

		btnNeuerEintrag.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnNeuerEintrag.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				btnEintragBearbeiten.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));

				yesEdit = false;
				
				setEditFields(true, false);
			}
		});

		btnEintragBearbeiten.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnEintragBearbeiten.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				btnNeuerEintrag.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));

				yesEdit = true;

				setEditFields(true, true);

				int selektierterIndex = tblAnzeige.getSelectionIndex();
				if (selektierterIndex == -1) {
				} else {
					fillEditFieldsFromTable(selektierterIndex);
				}
			}
		});

		btnEintragLoeschen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tblAnzeige.remove(tblAnzeige.getSelectionIndices());
			}
		});

		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbLogin.getSelectionIndex() == -1) {
					messageBox("Bitte einen User auswählen", "Ungültige Eingabe", shell);
				} else {
					pers_NO = allPersList.get(cmbLogin.getSelectionIndex());
					workView();

					setSize(1053, 571);

					try {
						dbConnect = DriverManager.getConnection(connectionString);
						Statement statement = dbConnect.createStatement();
						result = statement.executeQuery(
								"select p.FIRSTNAME, p.LASTNAME, f.FUNCTION_NAME from PERS p, FUNCTION f where p.FUNCTION_NO = f.FUNCTION_NO AND p.FUNCTION_NO = 1 AND p.PERS_NO = "
										+ pers_NO);
						while (result.next()) {
							setText("Atemschutzpflegestelle - " + result.getObject(1).toString() + " "
									+ result.getObject(2).toString());
							myKorrekturName = "Beiträge - " + result.getObject(1).toString() + " "
									+ result.getObject(2).toString();
						}
					} catch (SQLException sqlexc) {
						sqlexc.printStackTrace();
						messageBox("SQL-Feheler", "Fehlermeldung", shell);
					}
				}
			}
		});

		btnLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loginView();
			}
		});

		btnHinzufuegen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmbCities.getSelectionIndex() == -1) {
					messageBox("Bitte die Feuerwehr auswählen", "Unvollständige Eingabe", shell);
				} else {
					if (Integer.parseInt(txtFlaschenFuellen.getText()) == 0
							&& Integer.parseInt(txtFlaschenTUEV.getText()) == 0
							&& Integer.parseInt(txtMaskenPruefen.getText()) == 0
							&& Integer.parseInt(txtMaskenReinigen.getText()) == 0
							&& Integer.parseInt(txtLaPruefen.getText()) == 0
							&& Integer.parseInt(txtLaReinigen.getText()) == 0
							&& Integer.parseInt(txtGeraetePruefen.getText()) == 0
							&& Integer.parseInt(txtGeraeteReinigen.getText()) == 0) {
						messageBox("Bitte mindestens eine Flasche, Masken, LA oder ein Gerät hinzufügen",
								"Unvollständige Eingabe", shell);
					} else {
						if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.')) == 0.0) {
							messageBox("0 Stunden Arbeitszeit ist nicht zulässig", "Unvollständige Eingabe", shell);
						} else {
							if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.')) > 10) {
								messageBox("Die maxmimale Arbeitszeit beträgt 10 Stunden", "Falsche Eingabe", shell);
							} else {

								if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.')) < 0) {
									messageBox("Bitte eine positive Arbeitszeit eingeben", "Falsche Eingabe", shell);
								} else {
									if (Integer.parseInt(txtFlaschenFuellen.getText()) > 20
											|| Integer.parseInt(txtFlaschenTUEV.getText()) > 20
											|| Integer.parseInt(txtMaskenPruefen.getText()) > 20
											|| Integer.parseInt(txtMaskenReinigen.getText()) > 20
											|| Integer.parseInt(txtLaPruefen.getText()) > 20
											|| Integer.parseInt(txtLaReinigen.getText()) > 20
											|| Integer.parseInt(txtGeraetePruefen.getText()) > 20
											|| Integer.parseInt(txtGeraeteReinigen.getText()) > 20) {
										messageBox("Die maximale Menge von Flaschen, Masken, LA oder Geräte beträgt 20",
												"Falsche Eingabe", shell);
									} else {
										item = new TableItem(tblAnzeige, SWT.BORDER);
										item.setText(0, cmbCities.getText().toString());
										item.setText(1, txtFlaschenFuellen.getText());
										item.setText(2, txtFlaschenTUEV.getText());
										item.setText(3, txtMaskenPruefen.getText());
										item.setText(4, txtMaskenReinigen.getText());
										item.setText(5, txtLaPruefen.getText());
										item.setText(6, txtLaReinigen.getText());
										item.setText(7, txtGeraetePruefen.getText());
										item.setText(8, txtGeraeteReinigen.getText());
										item.setText(9, txtArbeitszeit.getText());
										item.setText(10, txtNrFlaschenFuellen.getText());
										item.setText(11, txtNrFlaschenTUEV.getText());
										item.setText(12, txtNrMaskenPruefen.getText());
										item.setText(13, txtNrMaskenReinigen.getText());
										item.setText(14, txtNrLaPruefen.getText());
										item.setText(15, txtNrLaReinigen.getText());
										item.setText(16, txtNrGeraetePruefen.getText());
										item.setText(17, txtNrGeraeteReinigen.getText());
										// item.setText(10,
										// txtNrFlaschenFuellen.getText()+"~#"+txtNrFlaschenTUEV+"~#"+txtNrMaskenPruefen.getText()+"~#"+txtNrMaskenReinigen+"~#"+txtNrLaPruefen+"~#"+txtNrLaReinigen+"~#"+txtNrGeraetePruefen.getText()+"~#"+txtNrGeraeteReinigen+"~");

										txtFlaschenFuellen.setText("0");
										txtFlaschenTUEV.setText("0");
										txtMaskenPruefen.setText("0");
										txtMaskenReinigen.setText("0");
										txtLaPruefen.setText("0");
										txtLaReinigen.setText("0");
										txtGeraetePruefen.setText("0");
										txtGeraeteReinigen.setText("0");
										cmbCities.deselectAll();
										txtArbeitszeit.setText("0");
										txtNrFlaschenFuellen.setText("");
										txtNrFlaschenTUEV.setText("");
										txtNrMaskenPruefen.setText("");
										txtNrMaskenReinigen.setText("");
										txtNrLaPruefen.setText("");
										txtNrLaReinigen.setText("");
										txtNrGeraetePruefen.setText("");
										txtNrGeraeteReinigen.setText("");
									}
								}
							}
						}
					}
				}
			}
		});

		btnAbbrechen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				yesEdit = false;

				setEditFields(false, false);

				btnNeuerEintrag.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				btnEintragBearbeiten.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			}
		});

		btnUpdaten.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selektierterIndex = tblAnzeige.getSelectionIndex();
				if (selektierterIndex == -1) {
					messageBox("Bitte den Eintrag zum ändern Tabelle anwählen", "Fehler", shell);
				} else {
					if (Integer.parseInt(txtFlaschenFuellen.getText()) == 0
							&& Integer.parseInt(txtFlaschenTUEV.getText()) == 0
							&& Integer.parseInt(txtMaskenPruefen.getText()) == 0
							&& Integer.parseInt(txtMaskenReinigen.getText()) == 0
							&& Integer.parseInt(txtLaPruefen.getText()) == 0
							&& Integer.parseInt(txtLaReinigen.getText()) == 0
							&& Integer.parseInt(txtGeraetePruefen.getText()) == 0
							&& Integer.parseInt(txtGeraeteReinigen.getText()) == 0) {
						messageBox("Bitte mindestens eine Flasche, Masken, LA oder ein Gerät hinzufügen",
								"Unvollständige Eingabe", shell);
					} else {
						if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.')) == 0.0) {
							messageBox("0 Stunden Arbeitszeit ist nicht zulässig", "Unvollständige Eingabe", shell);
						} else {
							if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.')) > 10) {
								messageBox("Die maxmimale Arbeitszeit beträgt 10 Stunden", "Falsche Eingabe", shell);
							} else {

								if (Double.parseDouble(txtArbeitszeit.getText().replace(',', '.')) < 0) {
									messageBox("Bitte eine positive Arbeitszeit eingeben", "Falsche Eingabe", shell);
								} else {
									if (Integer.parseInt(txtFlaschenFuellen.getText()) > 20
											|| Integer.parseInt(txtFlaschenTUEV.getText()) > 20
											|| Integer.parseInt(txtMaskenPruefen.getText()) > 20
											|| Integer.parseInt(txtMaskenReinigen.getText()) > 20
											|| Integer.parseInt(txtLaPruefen.getText()) > 20
											|| Integer.parseInt(txtLaReinigen.getText()) > 20
											|| Integer.parseInt(txtGeraetePruefen.getText()) > 20
											|| Integer.parseInt(txtGeraeteReinigen.getText()) > 20) {
										messageBox("Die maximale Menge von Flaschen, Masken, LA oder Geräte beträgt 20",
												"Falsche Eingabe", shell);
									} else {
										tblAnzeige.remove(tblAnzeige.getSelectionIndices());
										item = new TableItem(tblAnzeige, SWT.BORDER);
										item.setText(0, cmbCities.getText().toString());
										item.setText(1, txtFlaschenFuellen.getText());
										item.setText(2, txtFlaschenTUEV.getText());
										item.setText(3, txtMaskenPruefen.getText());
										item.setText(4, txtMaskenReinigen.getText());
										item.setText(5, txtLaPruefen.getText());
										item.setText(6, txtLaReinigen.getText());
										item.setText(7, txtGeraetePruefen.getText());
										item.setText(8, txtGeraeteReinigen.getText());
										item.setText(9, txtArbeitszeit.getText());
										item.setText(10, txtNrFlaschenFuellen.getText());
										item.setText(11, txtFlaschenTUEV.getText());
										item.setText(12, txtMaskenPruefen.getText());
										item.setText(13, txtMaskenReinigen.getText());
										item.setText(14, txtLaPruefen.getText());
										item.setText(15, txtLaReinigen.getText());
										item.setText(16, txtGeraetePruefen.getText());
										item.setText(17, txtGeraeteReinigen.getText());
										// item.setText(10,
										// txtNrFlaschenFuellen.getText()+"~#"+txtNrFlaschenTUEV+"~#"+txtNrMaskenPruefen.getText()+"~#"+txtNrMaskenReinigen+"~#"+txtNrLaPruefen+"~#"+txtNrLaReinigen+"~#"+txtNrGeraetePruefen.getText()+"~#"+txtNrGeraeteReinigen+"~");

										txtFlaschenFuellen.setText("0");
										txtFlaschenTUEV.setText("0");
										txtMaskenPruefen.setText("0");
										txtMaskenReinigen.setText("0");
										txtLaPruefen.setText("0");
										txtLaReinigen.setText("0");
										txtGeraetePruefen.setText("0");
										txtGeraeteReinigen.setText("0");
										cmbCities.deselectAll();
										txtArbeitszeit.setText("0");
										txtNrFlaschenFuellen.setText("");
										txtNrFlaschenTUEV.setText("");
										txtNrMaskenPruefen.setText("");
										txtNrMaskenReinigen.setText("");
										txtNrLaPruefen.setText("");
										txtNrLaReinigen.setText("");
										txtNrGeraetePruefen.setText("");
										txtNrGeraeteReinigen.setText("");
									}
								}
							}
						}
					}
				}
			}
		});

		btnSpeichern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int myRC = 0;
				if (tblAnzeige.getItems().length == 0) {
					MessageBox msgBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					msgBox.setMessage(
							"Möchten Sie sich wirklich ausloggen?\nSie haben noch nichts in die Tabelle hinzugefügt, deswegen wird auch nichts gespeichert");
					msgBox.setText("Wirklich verlassen?");
					myRC = msgBox.open();
				}
				if (myRC == SWT.YES) {
					loginView();
				} else if (myRC == SWT.NO) {
				} else {
					for (int i = 0; i < tblAnzeige.getItems().length; i++) {
						String Feuerwehr = tblAnzeige.getItems()[i].getText(0).toString().trim();
						String FlaschenFuellen = tblAnzeige.getItems()[i].getText(1);
						String FlaschenTUEV = tblAnzeige.getItems()[i].getText(2);
						String MaskenPruefen = tblAnzeige.getItems()[i].getText(3);
						String MaskenReinigen = tblAnzeige.getItems()[i].getText(4);
						String LaPruefen = tblAnzeige.getItems()[i].getText(5);
						String LaReinigen = tblAnzeige.getItems()[i].getText(6);
						String GeraetePruefen = tblAnzeige.getItems()[i].getText(7);
						String GeraeteReinigen = tblAnzeige.getItems()[i].getText(8);
						String FlaschenFuellenNr = tblAnzeige.getItems()[i].getText(10);
						String FlaschenTUEVNr = tblAnzeige.getItems()[i].getText(11);
						String MaskenPruefenNr = tblAnzeige.getItems()[i].getText(12);
						String MaskenReinigenNr = tblAnzeige.getItems()[i].getText(13);
						String LaPruefenNr = tblAnzeige.getItems()[i].getText(14);
						String LaReinigenNr = tblAnzeige.getItems()[i].getText(15);
						String GeraetePruefenNr = tblAnzeige.getItems()[i].getText(16);
						String GeraeteReinigenNr = tblAnzeige.getItems()[i].getText(17);

						int Feuerwehr_NO = 0;
						String Arbeitszeit = tblAnzeige.getItems()[i].getText(9).toString().trim().replace(",", ".");
						int Arbeits_Tag = dateTime.getDay();
						int Arbeits_Monat = dateTime.getMonth() + 1;
						int Arbeits_Jahr = dateTime.getYear();
						String Arbeits_Datum = Arbeits_Jahr + "-" + Arbeits_Monat + "-" + Arbeits_Tag;

						for (int m = 0; m < allCitiesList.size(); m++) {
							String randomCity = allCitiesListName.get(m).toString().trim();
							if (randomCity.equals(Feuerwehr)) {
								Feuerwehr_NO = m + 1;
							}
						}

						try {
							dbConnect = DriverManager.getConnection(connectionString);
							Statement statement = dbConnect.createStatement();
							statement.executeUpdate(
									"INSERT INTO ATEMSCHUTZPFLEGESTELLE_DATA (CITY_NO, FLASCHEN_FUELLEN, FLASCHEN_TUEV, MASKEN_PRUEFEN, MASKEN_REINIGEN, LA_PRUEFEN, LA_REINIGEN, GERAETE_PRUEFEN, GERAETE_REINIGEN, PERS_NO, TIME_WORK, DATE_WORK) "
											+ "VALUES (" + Feuerwehr_NO + "," + FlaschenFuellen + "," + FlaschenTUEV
											+ "," + MaskenPruefen + "," + MaskenReinigen + "," + LaPruefen + ","
											+ LaReinigen + "," + GeraetePruefen + "," + GeraeteReinigen + "," + pers_NO
											+ "," + Arbeitszeit + ",'" + Arbeits_Datum + "')");

							result = statement.executeQuery("SELECT MAX(DATA_NO) FROM ATEMSCHUTZPFLEGESTELLE_DATA");
							result.next();
							String data_id = result.getObject(1).toString();

							statement.executeUpdate(
									"INSERT INTO ATEMSCHUTZPFLEGESTELLE_NR (DATA_NO, FLASCHEN_FUELLEN_NR, FLASCHEN_TUEV_NR, MASKEN_PRUEFEN_NR, MASKEN_REINIGEN_NR, LA_PRUEFEN_NR, LA_REINIGEN_NR, GERAETE_PRUEFEN_NR, GERAETE_REINIGEN_NR) "
											+ "VALUES (" + data_id + ",'" + FlaschenFuellenNr + "','" + FlaschenTUEVNr
											+ "','" + MaskenPruefenNr + "','" + MaskenReinigenNr + "','" + LaPruefenNr
											+ "','" + LaReinigenNr + "','" + GeraetePruefenNr + "','"
											+ GeraeteReinigenNr + "')");

							statement.executeUpdate("commit");
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					tblAnzeige.removeAll();

					yesEdit = false;

					setEditFields(false, false);
					dateTime.setDay(currentDay);
					dateTime.setMonth(currentMonth);
					dateTime.setYear(currentYear);

					messageBox("Die Daten wurden erfolgreich gespeichert\nSie werden nun ausgeloggt", "Projekt-Ausgabe",
							shell);

					loginView();
				}
			}
		});

		btnKorrektur.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myKorrektur myKo = new myKorrektur(shell, SWT.OPEN);
				myKo.setText(myKorrekturName);
				myKo.setConnectionString(connectionString);
				myKo.setPers_NO(pers_NO);
				myKo.open();
			}
		});

		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		MenuItem mntmHilfe = new MenuItem(menu, SWT.NONE);
		mntmHilfe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myHilfe myHi = new myHilfe(shell, SWT.OPEN);
				myHi.setText("Hilfe");
				myHi.setHeaderPfad(headerPfad);
				myHi.open();
			}
		});
		mntmHilfe.setText("Hilfe");

		btnSonstiges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mySonstiges mySo = new mySonstiges(shell, SWT.OPEN);
				mySo.setConnectionString(connectionString);
				mySo.setPers_NO(pers_NO);
				mySo.open();
				if (getSonstigesStatus()) {
					setEditFields(false, false);
					loginView();
				}

			}
		});


		try {
			dbConnect = DriverManager.getConnection(connectionString);
			Statement statement = dbConnect.createStatement();
			result = statement.executeQuery(
					"SELECT p.FIRSTNAME, p.LASTNAME, p.PERS_NO FROM PERS p, FUNCTION f WHERE p.FUNCTION_NO = f.FUNCTION_NO and p.FUNCTION_NO = 1 ORDER BY p.LASTNAME, p.FIRSTNAME");
			while (result.next()) {
				cmbLogin.add(result.getObject(1).toString() + " " + result.getObject(2).toString());
				allPersList.add(result.getObject(3).toString());
			}

			result = statement.executeQuery(
					"SELECT c.CITY_NAME, c.CITY_NO FROM ATEMSCHUTZPFLEGESTELLE_CITIES c ORDER BY c.CITY_NAME");
			while (result.next()) {
				cmbCities.add(result.getObject(1).toString());
				allCitiesList.add(result.getObject(2).toString());
				allCitiesListNameSort.add(result.getObject(1).toString());
			}
			result = statement.executeQuery("SELECT c.CITY_NAME, c.CITY_NO FROM ATEMSCHUTZPFLEGESTELLE_CITIES c");
			while (result.next()) {
				allCitiesListName.add(result.getObject(1).toString());
			}

		} catch (SQLException sqlexc) {
			sqlexc.printStackTrace();
			messageBox("SQL-Fehler", "Fehlermeldung", shell);
		}
		loginView();
		//workView();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Atemschutzpflegestelle");
		setSize(1053, 571);

		cmbLogin = new Combo(this, SWT.READ_ONLY);
		cmbLogin.setBounds(69, 12, 185, 23);

		lblLogin = new Label(this, SWT.NONE);
		lblLogin.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblLogin.setBounds(10, 15, 31, 15);
		lblLogin.setText("User");

		lblCities = new Label(this, SWT.NONE);
		lblCities.setBounds(10, 153, 58, 15);
		lblCities.setText("Feuerwehr:");

		cmbCities = new Combo(this, SWT.READ_ONLY);
		cmbCities.setBounds(74, 150, 301, 23);

		txtArbeitszeit = new Text(this, SWT.BORDER | SWT.RIGHT);

		txtFlaschenFuellen = new Text(this, SWT.BORDER);
		txtFlaschenFuellen.setBounds(110, 200, 100, 21);

		txtMaskenPruefen = new Text(this, SWT.BORDER);
		txtMaskenPruefen.setBounds(110, 260, 100, 21);

		txtGeraetePruefen = new Text(this, SWT.BORDER);
		txtGeraetePruefen.setBounds(110, 380, 100, 21);

		lblFlaschen = new Label(this, SWT.NONE);
		lblFlaschen.setBounds(10, 200, 100, 19);
		lblFlaschen.setText("Flaschen f\u00FCllen:");

		lblMaskenPruefen = new Label(this, SWT.NONE);
		lblMaskenPruefen.setBounds(10, 260, 100, 21);
		lblMaskenPruefen.setText("Masken pr\u00FCfen:");

		lblGeraetePruefen = new Label(this, SWT.NONE);
		lblGeraetePruefen.setBounds(10, 380, 94, 21);
		lblGeraetePruefen.setText("Ger\u00E4te pr\u00FCfen:");

		btnLogout = new Button(this, SWT.NONE);

		tblAnzeige = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);

		tblAnzeige.setBounds(10, 38, 1013, 101);
		tblAnzeige.setHeaderVisible(true);
		tblAnzeige.setLinesVisible(true);

		clmFeuerwehr = new TableColumn(tblAnzeige, SWT.NONE);
		clmFeuerwehr.setWidth(100);
		clmFeuerwehr.setText("Feuerwehr");

		clmFlaschenFuellen = new TableColumn(tblAnzeige, SWT.NONE);
		clmFlaschenFuellen.setWidth(100);
		clmFlaschenFuellen.setText("Flaschen f\u00FCllen");

		clmFlaschenTUEV = new TableColumn(tblAnzeige, SWT.NONE);
		clmFlaschenTUEV.setWidth(100);
		clmFlaschenTUEV.setText("Flaschen T\u00DCV");

		clmMaskenPruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmMaskenPruefen.setWidth(100);
		clmMaskenPruefen.setText("Masken pr\u00FCfen");

		clmMaskenReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmMaskenReinigen.setWidth(100);
		clmMaskenReinigen.setText("Masken reinigen");

		clmLaPruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmLaPruefen.setWidth(100);
		clmLaPruefen.setText("LA pr\u00FCfen");

		clmLaReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmLaReinigen.setWidth(100);
		clmLaReinigen.setText("LA Reinigen");

		clmGeraetePruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmGeraetePruefen.setWidth(100);
		clmGeraetePruefen.setText("Ger\u00E4te pr\u00FCfen");

		clmGeraeteReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmGeraeteReinigen.setWidth(100);
		clmGeraeteReinigen.setText("Ger\u00E4te reinigen");

		clmArbeitszeit = new TableColumn(tblAnzeige, SWT.NONE);
		clmArbeitszeit.setWidth(100);
		clmArbeitszeit.setText("Arbeitszeit in h");

		clmNrFlascheFuellen = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrFlascheFuellen.setWidth(100);
		clmNrFlascheFuellen.setText("ff nr");

		clmNrFlascheTuev = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrFlascheTuev.setWidth(100);
		clmNrFlascheTuev.setText("ft nr");

		clmNrMaskePruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrMaskePruefen.setWidth(100);
		clmNrMaskePruefen.setText("mp nr");

		clmNrMaskeReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrMaskeReinigen.setWidth(100);
		clmNrMaskeReinigen.setText("mr nr");

		clmNrLaPruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrLaPruefen.setWidth(100);
		clmNrLaPruefen.setText("lp nr");

		clmNrLaReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrLaReinigen.setWidth(100);
		clmNrLaReinigen.setText("lr nr");

		clmNrGeraetePruefen = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrGeraetePruefen.setWidth(100);
		clmNrGeraetePruefen.setText("gp nr");

		clmNrGeraeteReinigen = new TableColumn(tblAnzeige, SWT.NONE);
		clmNrGeraeteReinigen.setWidth(100);
		clmNrGeraeteReinigen.setText("gr nr");
		
		clmNrFlascheFuellen.setWidth(0);
		clmNrFlascheTuev.setWidth(0);
		clmNrGeraetePruefen.setWidth(0);
		clmNrGeraeteReinigen.setWidth(0);
		clmNrLaPruefen.setWidth(0);
		clmNrLaReinigen.setWidth(0);
		clmNrMaskePruefen.setWidth(0);
		clmNrMaskeReinigen.setWidth(0);

		btnHinzufuegen = new Button(this, SWT.NONE);

		btnAbbrechen = new Button(this, SWT.NONE);

		btnEintragBearbeiten = new Button(this, SWT.NONE);
		btnEintragBearbeiten.setToolTipText("Gew\u00FCnschten Eintrag in der Tabelle anw\u00E4hlen");

		btnUpdaten = new Button(this, SWT.NONE);

		lblArbeitszeit = new Label(this, SWT.NONE);

		lblDatum = new Label(this, SWT.NONE);

		dateTime = new DateTime(this, SWT.BORDER);

		btnSpeichern = new Button(this, SWT.NONE);

		btnKorrektur = new Button(this, SWT.NONE);

		btnSonstiges = new Button(this, SWT.NONE);

		lblStunden = new Label(this, SWT.NONE);

		btnNeuerEintrag = new Button(this, SWT.NONE);
		btnNeuerEintrag.setBounds(432, 172, 124, 25);
		btnNeuerEintrag.setText("Neuer Eintrag");

		btnEintragBearbeiten.setBounds(432, 203, 124, 25);
		btnEintragBearbeiten.setText("Eintrag bearbeiten");

		btnEintragLoeschen = new Button(this, SWT.NONE);
		btnEintragLoeschen.setBounds(432, 234, 124, 25);
		btnEintragLoeschen.setText("Eintrag l\u00F6schen");

		btnLogin = new Button(this, SWT.NONE);
		btnLogin.setBounds(260, 10, 75, 25);
		btnLogin.setText("Login");

		btnLogout.setBounds(849, 424, 174, 25);
		btnLogout.setText("Abbrechen und Ausloggen");
		btnHinzufuegen.setBounds(10, 474, 100, 25);
		btnHinzufuegen.setText("Hinzuf\u00FCgen");

		btnAbbrechen.setBounds(160, 474, 100, 25);
		btnAbbrechen.setText("Abbrechen");

		btnUpdaten.setBounds(10, 474, 100, 25);
		btnUpdaten.setText("Updaten");

		lblArbeitszeit.setBounds(10, 440, 100, 21);
		lblArbeitszeit.setText("Arbeitszeit:");

		lblDatum.setBounds(854, 384, 39, 15);
		lblDatum.setText("Datum:");

		dateTime.setBounds(899, 380, 124, 24);
		currentDay = dateTime.getDay();
		currentMonth = dateTime.getMonth();
		currentYear = dateTime.getYear();

		txtArbeitszeit.setBounds(110, 440, 100, 21);

		lblStunden.setBounds(216, 443, 44, 15);
		lblStunden.setText("Stunden");

		btnSpeichern.setBounds(849, 455, 174, 25);
		btnSpeichern.setText("Speichern und Ausloggen");

		btnKorrektur.setBounds(432, 296, 124, 25);
		btnKorrektur.setText("Bisherige Eintr\u00E4ge");

		btnSonstiges.setBounds(432, 265, 124, 25);
		btnSonstiges.setText("Sonstige Aufgabe");

		txtNrFlaschenFuellen = new Text(this, SWT.BORDER);
		txtNrFlaschenFuellen.setEnabled(false);
		txtNrFlaschenFuellen.setBounds(250, 200, 125, 21);

		txtNrMaskenPruefen = new Text(this, SWT.BORDER);
		txtNrMaskenPruefen.setBounds(250, 260, 125, 21);
		txtNrMaskenPruefen.setEnabled(false);

		txtNrGeraetePruefen = new Text(this, SWT.BORDER);
		txtNrGeraetePruefen.setBounds(250, 380, 125, 21);
		txtNrGeraetePruefen.setEnabled(false);

		lblMenge = new Label(this, SWT.NONE);
		lblMenge.setBounds(131, 179, 55, 15);
		lblMenge.setText("Menge");

		lblNummern = new Label(this, SWT.NONE);
		lblNummern.setBounds(281, 179, 55, 15);
		lblNummern.setText("Nummern");

		lblFlaschenTUEV = new Label(this, SWT.NONE);
		lblFlaschenTUEV.setBounds(10, 225, 100, 19);
		lblFlaschenTUEV.setText("Flaschen T\u00DCV:");

		txtFlaschenTUEV = new Text(this, SWT.BORDER);
		txtFlaschenTUEV.setBounds(110, 225, 100, 21);

		txtNrFlaschenTUEV = new Text(this, SWT.BORDER);
		txtNrFlaschenTUEV.setEnabled(false);
		txtNrFlaschenTUEV.setBounds(250, 225, 125, 21);

		lblMaskenReinigen = new Label(this, SWT.NONE);
		lblMaskenReinigen.setBounds(10, 285, 100, 21);
		lblMaskenReinigen.setText("Masken reinigen:");

		txtMaskenReinigen = new Text(this, SWT.BORDER);
		txtMaskenReinigen.setBounds(110, 285, 100, 21);

		txtNrMaskenReinigen = new Text(this, SWT.BORDER);
		txtNrMaskenReinigen.setEnabled(false);
		txtNrMaskenReinigen.setBounds(250, 285, 125, 21);

		lblLaPruefen = new Label(this, SWT.NONE);
		lblLaPruefen.setBounds(10, 320, 100, 21);
		lblLaPruefen.setText("LA pr\u00FCfen:");

		txtLaPruefen = new Text(this, SWT.BORDER);
		txtLaPruefen.setBounds(110, 320, 100, 21);

		txtNrLaPruefen = new Text(this, SWT.BORDER);
		txtNrLaPruefen.setEnabled(false);
		txtNrLaPruefen.setBounds(250, 320, 125, 21);

		lblLaReinigen = new Label(this, SWT.NONE);
		lblLaReinigen.setBounds(10, 345, 100, 21);
		lblLaReinigen.setText("LA reinigen:");

		txtLaReinigen = new Text(this, SWT.BORDER);
		txtLaReinigen.setBounds(110, 345, 100, 21);

		txtNrLaReinigen = new Text(this, SWT.BORDER);
		txtNrLaReinigen.setEnabled(false);
		txtNrLaReinigen.setBounds(250, 345, 125, 21);

		lblGeraeteReinigen = new Label(this, SWT.NONE);
		lblGeraeteReinigen.setBounds(10, 405, 94, 21);
		lblGeraeteReinigen.setText("Ger\u00E4te reinigen:");

		txtGeraeteReinigen = new Text(this, SWT.BORDER);
		txtGeraeteReinigen.setBounds(110, 405, 100, 21);

		txtNrGeraeteReinigen = new Text(this, SWT.BORDER);
		txtNrGeraeteReinigen.setEnabled(false);
		txtNrGeraeteReinigen.setBounds(250, 405, 125, 21);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public static void messageBox(String msg, String title, Shell myShell) {
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

	private void setAllInvisible() {
		txtFlaschenFuellen.setVisible(false);
		txtMaskenPruefen.setVisible(false);
		txtGeraetePruefen.setVisible(false);
		tblAnzeige.setVisible(false);
		txtArbeitszeit.setVisible(false);
		txtNrFlaschenFuellen.setVisible(false);
		txtNrMaskenPruefen.setVisible(false);
		txtNrGeraetePruefen.setVisible(false);
		txtFlaschenTUEV.setVisible(false);
		txtNrFlaschenTUEV.setVisible(false);
		txtMaskenReinigen.setVisible(false);
		txtNrMaskenReinigen.setVisible(false);
		txtLaPruefen.setVisible(false);
		txtNrLaPruefen.setVisible(false);
		txtLaReinigen.setVisible(false);
		txtNrLaReinigen.setVisible(false);
		txtGeraeteReinigen.setVisible(false);
		txtNrGeraeteReinigen.setVisible(false);
		cmbLogin.setVisible(false);
		lblLogin.setVisible(false);
		cmbCities.setVisible(false);
		lblCities.setVisible(false);
		lblFlaschen.setVisible(false);
		lblMaskenPruefen.setVisible(false);
		lblGeraetePruefen.setVisible(false);
		btnLogout.setVisible(false);
		btnHinzufuegen.setVisible(false);
		btnAbbrechen.setVisible(false);
		btnEintragBearbeiten.setVisible(false);
		btnUpdaten.setVisible(false);
		lblArbeitszeit.setVisible(false);
		lblDatum.setVisible(false);
		btnSpeichern.setVisible(false);
		btnKorrektur.setVisible(false);
		btnSonstiges.setVisible(false);
		lblStunden.setVisible(false);
		btnNeuerEintrag.setVisible(false);
		btnEintragLoeschen.setVisible(false);
		btnLogin.setVisible(false);
		lblMenge.setVisible(false);
		lblNummern.setVisible(false);
		lblFlaschenTUEV.setVisible(false);
		lblMaskenReinigen.setVisible(false);
		lblLaPruefen.setVisible(false);
		lblLaReinigen.setVisible(false);
		lblGeraeteReinigen.setVisible(false);
		dateTime.setVisible(false);
		

		btnNeuerEintrag.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		btnEintragBearbeiten.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
	}

	private void loginView() {
		setAllInvisible();

		btnLogin.setVisible(true);
		lblLogin.setVisible(true);
		cmbLogin.setVisible(true);

	}
	
	private void setEditFields(boolean bool, boolean isUpdate) {
		String empty = "";
		String text = bool ? "0" : empty;
		
		txtFlaschenFuellen.setEnabled(bool);
		txtNrFlaschenFuellen.setEnabled(bool);
		txtFlaschenTUEV.setEnabled(bool);
		txtNrFlaschenTUEV.setEnabled(bool);
		
		txtGeraetePruefen.setEnabled(bool);
		txtNrGeraetePruefen.setEnabled(bool);
		txtGeraeteReinigen.setEnabled(bool);
		txtNrGeraeteReinigen.setEnabled(bool);
		
		txtLaPruefen.setEnabled(bool);
		txtNrLaPruefen.setEnabled(bool);
		txtLaReinigen.setEnabled(bool);
		txtNrLaReinigen.setEnabled(bool);
		
		txtMaskenPruefen.setEnabled(bool);
		txtNrMaskenPruefen.setEnabled(bool);
		txtMaskenReinigen.setEnabled(bool);
		txtNrMaskenReinigen.setEnabled(bool);
		
		txtArbeitszeit.setEnabled(bool);
		cmbCities.setEnabled(bool);
		
		txtFlaschenFuellen.setText(text);
		txtNrFlaschenFuellen.setText(empty);
		txtFlaschenTUEV.setText(text);
		txtNrFlaschenTUEV.setText(empty);
		
		txtGeraetePruefen.setText(text);
		txtNrGeraetePruefen.setText(empty);
		txtGeraeteReinigen.setText(text);
		txtNrGeraeteReinigen.setText(empty);
		
		txtLaPruefen.setText(text);
		txtNrLaPruefen.setText(empty);
		txtLaReinigen.setText(text);
		txtNrLaReinigen.setText(empty);
		
		txtMaskenPruefen.setText(text);
		txtNrMaskenPruefen.setText(empty);
		txtMaskenReinigen.setText(text);
		txtNrMaskenReinigen.setText(empty);

		btnHinzufuegen.setVisible(bool ? !isUpdate : false);
		btnUpdaten.setVisible(bool ? isUpdate : false);
		btnAbbrechen.setVisible(bool);
		
		txtArbeitszeit.setText(text);
		cmbCities.deselectAll();
	}
	
	private void workView() {
		setAllInvisible();
		
		cmbCities.setVisible(true);
		lblCities.setVisible(true);
		txtFlaschenFuellen.setVisible(true);
		txtMaskenPruefen.setVisible(true);
		txtGeraetePruefen.setVisible(true);
		lblFlaschen.setVisible(true);
		lblMaskenPruefen.setVisible(true);
		lblGeraetePruefen.setVisible(true);
		btnLogout.setVisible(true);
		tblAnzeige.setVisible(true);
		btnNeuerEintrag.setVisible(true);
		btnEintragBearbeiten.setVisible(true);
		btnEintragLoeschen.setVisible(true);
		txtFlaschenFuellen.setEnabled(false);
		txtMaskenPruefen.setEnabled(false);
		txtGeraetePruefen.setEnabled(false);
		cmbCities.setEnabled(false);
		btnAbbrechen.setVisible(false);
		btnHinzufuegen.setVisible(false);
		lblArbeitszeit.setVisible(true);
		lblDatum.setVisible(true);
		dateTime.setVisible(true);
		txtArbeitszeit.setVisible(true);
		txtArbeitszeit.setEnabled(false);
		lblStunden.setVisible(true);
		btnSpeichern.setVisible(true);
		txtArbeitszeit.setText("");
		btnKorrektur.setVisible(true);
		btnSonstiges.setVisible(true);
		tblAnzeige.removeAll();
		cmbCities.setEnabled(true);
		cmbCities.deselectAll();
		cmbCities.setEnabled(false);
		lblNummern.setVisible(true);
		lblMenge.setVisible(true);

		txtNrFlaschenFuellen.setVisible(true);
		txtNrMaskenPruefen.setVisible(true);
		txtNrGeraetePruefen.setVisible(true);
		
		lblFlaschenTUEV.setVisible(true);
		lblMaskenReinigen.setVisible(true);
		lblLaPruefen.setVisible(true);
		lblLaReinigen.setVisible(true);
		lblGeraeteReinigen.setVisible(true);
		
		txtFlaschenTUEV.setVisible(true);
		txtNrFlaschenTUEV.setVisible(true);
		txtLaPruefen.setVisible(true);
		txtNrLaPruefen.setVisible(true);
		txtLaReinigen.setVisible(true);
		txtNrLaReinigen.setVisible(true);
		txtGeraeteReinigen.setVisible(true);
		txtNrGeraeteReinigen.setVisible(true);
		txtMaskenReinigen.setVisible(true);
		txtNrMaskenReinigen.setVisible(true);
		
		setEditFields(false, false);
	}
	
	private void fillEditFieldsFromTable(int selektierterIndex) {
		TableItem tableItem = tblAnzeige.getItems()[selektierterIndex];
		txtFlaschenFuellen.setText(tableItem.getText(1));
		txtFlaschenTUEV.setText(tableItem.getText(2));
		txtMaskenPruefen.setText(tableItem.getText(3));
		txtMaskenReinigen.setText(tableItem.getText(4));
		txtLaPruefen.setText(tableItem.getText(5));
		txtLaReinigen.setText(tableItem.getText(6));
		txtGeraetePruefen.setText(tableItem.getText(7));
		txtGeraeteReinigen.setText(tableItem.getText(8));
		txtArbeitszeit.setText(tableItem.getText(9));

		// String[] tableNummern = tableItem.getText(10).split("#");
		txtNrFlaschenFuellen.setText(tableItem.getText(10));
		txtNrFlaschenTUEV.setText(tableItem.getText(11));
		txtNrMaskenPruefen.setText(tableItem.getText(12));
		txtNrMaskenReinigen.setText(tableItem.getText(13));
		txtNrLaPruefen.setText(tableItem.getText(14));
		txtNrLaReinigen.setText(tableItem.getText(15));
		txtNrGeraetePruefen.setText(tableItem.getText(16));
		txtNrGeraeteReinigen.setText(tableItem.getText(17));

		for (int i = 0; i < allCitiesList.size(); i++) {
			String randomCity = allCitiesListNameSort.get(i).toString().trim();
			String searchedCity = tableItem.getText(0).toString().trim();
			if (randomCity.equals(searchedCity)) {
				cmbCities.select(i);
			}
		}
	}
}
