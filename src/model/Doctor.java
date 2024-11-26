package model;

import java.util.ArrayList;
import java.util.List;
//Estudiante: Christian Orlando Garzon Sierra
//Codigo: 202311008
//projecto: MediSched
public class Doctor {
	private String name;
	private String username;
	private String password;
	private List<Appointment> appointments;

	public Doctor(String name, String username, String password) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.appointments = new ArrayList<>(); 
	}

	public int getTotalPatients() {
		return appointments.size();
	}

	public double getTotalTime() {
		return appointments.stream().mapToInt(Appointment::getDuration).sum();
	}

	public double getAverageTime() {
		return getTotalTime() / getTotalPatients();
	}

	public double getTotalWorkHours() {

		return getTotalTime() / 60;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
