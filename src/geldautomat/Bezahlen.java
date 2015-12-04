package geldautomat;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

/**
 * Diese Klasse des Geldautomaten steuert sowohl die graphische Oberflaeche als auch die Hintergrundberechnungen.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Bezahlen {
	
	/**Programmfenster zur Darstellung*/
	private JFrame frame1 = new JFrame("Einzahlautomat");
	/**Zahlenformat des Eingabefelds*/
	private NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
	/**Zahlenformatierer des Eingabefelds*/
    private NumberFormatter formatter = new NumberFormatter(format);
    /**Textfeld zur Eingabe des Geldbetrags*/
    private JFormattedTextField eingabeFeld = new JFormattedTextField(formatter);
    /**Bestaetigung des Geldbetrags*/
    private JButton wertBestaetigen = new JButton("Einzahlen");
    /**Abbruch des Einzahlvorgangs*/
    private JButton vorgangAbbrechen = new JButton("Abbrechen");
    /**Dezimalformat des Restgelds*/
    private DecimalFormat dezFormat = new DecimalFormat("#0.00");
    /**Dezimalformat*/
    private DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    /**Array aus Geldeinzahlbuttons*/
    private Geldelement[] ges = new Geldelement[15];
    /**Double mit dem Restgeldbetrag*/
    private double restBezahlung;
	
	public Bezahlen() {
		dfs.setDecimalSeparator('.');
	    dezFormat.setDecimalFormatSymbols(dfs);
	    
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(350,250));
		frame1.setMinimumSize(new Dimension(350,250));
		frame1.setMaximumSize(new Dimension(700,500));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridBagLayout());
		//cp.setLayout(new BorderLayout());
		
		eingabeFeld.setText(dezFormat.format(0));
		eingabeFeld.setHorizontalAlignment(SwingConstants.RIGHT);
		restBezahlung = Double.valueOf(eingabeFeld.getText());
		wertBestaetigen.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				try {
					if(Double.valueOf(eingabeFeld.getText())>0) {
						restBezahlung = Double.valueOf(eingabeFeld.getText());
						eingabeFeld.setEnabled(false);
						wertBestaetigen.setEnabled(false);
						aktivieren();
					} else {
						JOptionPane.showMessageDialog(null, "Bitte zahle einen positiven Geldbetrag ein.", "Falscheingabe", JOptionPane.WARNING_MESSAGE);
					}
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null, "Du hast falsche Werte eingetragen."+System.getProperty("line.separator")+"Wenn Du dies nicht korrigierst"+System.getProperty("line.separator")+"wird Dein Bankkonto gesperrt!", "Falscheingabe", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		vorgangAbbrechen.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				eingabeFeld.setText(dezFormat.format(0));
				restBezahlung = Double.valueOf(eingabeFeld.getText());
				aktivieren();
				eingabeFeld.setEnabled(true);
				wertBestaetigen.setEnabled(true);
			}
		});
		format.setGroupingUsed(false);
		formatter.setAllowsInvalid(false);
		format.setMinimumFractionDigits(2);
		
		JPanel eingabe = new JPanel();
		eingabe.setLayout(new GridBagLayout());
		eingabe.add(eingabeFeld, new GridBagFelder(0, 0, 1, 1, 0.4, 1));
		eingabe.add(wertBestaetigen, new GridBagFelder(1, 0, 1, 1, 0.3, 1));
		eingabe.add(vorgangAbbrechen, new GridBagFelder(2, 0, 1, 1, 0.3, 1));
		
		JPanel bezahlButtons = new JPanel();
		bezahlButtons.setLayout(new GridBagLayout());
		
		double[] geldwerte = {0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1, 2, 5, 10, 20, 50, 100, 200, 500};
		for(int y=0;y<5;y++) {
			for(int x=0;x<3;x++) {
				Geldelement ge = new Geldelement(geldwerte[3*y+x]);
				ges[3*y+x] = ge;
				ge.getEinzahlen().addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						if(ge.isEnabled()) {
							restBezahlung -= ge.getWert();
							eingabeFeld.setText(dezFormat.format(restBezahlung));
							aktivieren();
							if(restBezahlung==0) {
								eingabeFeld.setEnabled(true);
								wertBestaetigen.setEnabled(true);
							}
						}
					}
				});
				bezahlButtons.add(ge, new GridBagFelder(x, y, 1, 1, 0.33, 0.2));
			}
		}
		
		cp.add(eingabe, new GridBagFelder(0, 0, 1, 1, 1, 0.1));
		cp.add(bezahlButtons, new GridBagFelder(0, 1, 1, 1, 1, 0.5));
		aktivieren();
		
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode aktiviert und deaktiviert alle Buttons zum Einzahlen je nachdem, ob sie kleiner/gleich dem Restgeldbetrag sind.
	 */
	private void aktivieren() {
		for(Geldelement gefor:ges) {
			if(gefor.getWert()>restBezahlung) {
				gefor.getEinzahlen().setEnabled(false);
			} else {
				gefor.getEinzahlen().setEnabled(true);
			}
		}
	}
	
	public static void main(String[] args) {
		new Bezahlen();
	}
}