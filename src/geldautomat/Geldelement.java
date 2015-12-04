package geldautomat;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Diese Klasse stellt einen Button dar, mit welchem man einen bestimmten Geldwert (Muenze oder Schein) einzahlt.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Geldelement extends JPanel {
	
	/**Button zum Bezahlen*/
	private JButton einzahlen = new JButton();
	/**Wert des Geldelements*/
	private double wert;
	
	public Geldelement(double wert) {
		this.wert = wert;
		einzahlen.setText(String.valueOf(wert));
		this.add(einzahlen);
	}

	public double getWert() {
		return wert;
	}

	public JButton getEinzahlen() {
		return einzahlen;
	}
}