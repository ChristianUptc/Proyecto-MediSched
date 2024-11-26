package view;

import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import model.Appointment;

public class IoManager {
	
	public String menu(String[]menu) {
		return (String) JOptionPane.showInputDialog(null, null, "menu", 0, icono("/view/MediSched.png",120,120), menu, menu[0]);
	}
	public String showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "MediSched",0,icono("/view/MediSched.png",120,120));
		return message;
	}
	public String getData(String message) {
		return (String) JOptionPane.showInputDialog(null, message, "MediSched",0, icono("/view/MediSched.png", 100, 100), null, message);
	}
	
	
	public Icon icono(String path, int weith, int height) {
		return new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(weith, height, 0));
	}
	public List<Appointment> showMessage(List<Appointment> appointments) {
		JOptionPane.showMessageDialog(null, appointments, "MediSched",0,icono("/view/MediSched.png",120,120));
		return appointments;
	}
	public Appointment showMessage(Appointment appointment) {
		JOptionPane.showMessageDialog(null, appointment, "MediSched",0,icono("/view/MediSched.png",120,120));
		return appointment;
	}

}
