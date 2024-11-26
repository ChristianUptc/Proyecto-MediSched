package control;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Appointment;
import model.Doctor;
import view.IoManager;
//Estudiante: Christian Orlando Garzon Sierra
//Codigo: 202311008
//projecto: MediSched
public class Control {
	private IoManager view;
	private Doctor doctor;
	private Appointment appointment;
	private static ArrayList<Doctor> doctors = new ArrayList<>();
	private String message;
	private String option;
	private Map<String, List<String>> schedule = new HashMap();

	public Control() {
		view = new IoManager();
		doctor = new Doctor("", "", "");
		appointment = new Appointment("", "", "");
		message = "";
		option = "";
	}

	public String showMessage() {
		return view.showMessage(message);
	}

	public void registerDC() {

		message = "Escriba su nombre";
		String name = view.getData(message);
		message = "Elija un nombre de usuario";
		String nameUs = view.getData(message);
		message = "Escoja una contraseña";
		String password = view.getData(message);

		boolean exists = doctors.stream().anyMatch(d -> d.getUsername().equals(nameUs));
		if (!exists) {
			doctors.add(new Doctor(name, nameUs, password));
			message = "Registro exitoso!";
		} else {
			message = "El nombre de usuario ya existe.";
		}
		view.showMessage(message);
		menuDoctor();
	}

	public Doctor login() {
		message = "Digite su nombre de usuario";
		String username = view.getData(message);
		message = "Digite su contraseña";
		String password = view.getData(message);

		for (Doctor doctor : doctors) {
			if (doctor.getUsername().equals(username) && doctor.getPassword().equals(password)) {
				message = "Inicio de sesión exitoso. Bienvenido, " + doctor.getName() + "!";
				view.showMessage(message);
				menuDC();
				return doctor;
			}
		}
		message = "Credenciales incorrectas. Intente nuevamente.";
		view.showMessage(message);
		menuDoctor();
		return doctor;

	}

	private void initializeSchedule() {
		schedule = new HashMap<>();
		String[] days = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes" };
		String startTime = "08:00";
		String endTime = "17:00";
		int slotDuration = 30;

		for (String day : days) {
			schedule.put(day, generateAvailableSlots(startTime, endTime, slotDuration));
		}
	}

	private List<String> generateAvailableSlots(String startTime, String endTime, int slotDuration) {
		List<String> slots = new ArrayList<>();
		LocalTime start = LocalTime.parse(startTime);
		LocalTime end = LocalTime.parse(endTime);

		while (start.isBefore(end)) {
			slots.add(start.toString());
			start = start.plusMinutes(slotDuration);
		}

		return slots;
	}

	public void patientMenu() {
		if (schedule == null) {
			initializeSchedule();
		}

		StringBuilder daysMessage = new StringBuilder("Días disponibles:\n");
		String[] days = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes" };

		for (int i = 0; i < days.length; i++) {
			daysMessage.append(i + 1).append(". ").append(days[i]).append("\n");
		}
		view.showMessage(daysMessage.toString());

		String input = "Seleccione un día (número):";
		int dayChoice = -1;
		try {
			input = view.getData(input);
			dayChoice = Integer.parseInt(input) - 1;
		} catch (NumberFormatException e) {
			view.showMessage("Entrada no válida.");
			menu1();
			return;
		}

		if (dayChoice < 0 || dayChoice >= days.length) {
			view.showMessage("Selección no válida.");
			menu1();
			return;
		}

		String selectedDay = days[dayChoice];

		if (!schedule.containsKey(selectedDay)) {
			List<String> availableSlots = new ArrayList<>();
			for (int hour = 7; hour <= 17; hour++) {
				availableSlots.add(hour + ":00");
			}
			schedule.put(selectedDay, availableSlots);
		}

		List<String> availableSlots = schedule.get(selectedDay);

		if (availableSlots == null || availableSlots.isEmpty()) {
			view.showMessage("No hay horarios disponibles para el día seleccionado.");
			return;
		}

		StringBuilder slotsMessage = new StringBuilder("Horarios disponibles para " + selectedDay + ":\n");
		for (int i = 0; i < availableSlots.size(); i++) {
			slotsMessage.append(i + 1).append(". ").append(availableSlots.get(i)).append("\n");
		}
		view.showMessage(slotsMessage.toString());

		input = "Seleccione un horario (número):";
		int slotChoice = -1;
		try {
			input = view.getData(input);
			slotChoice = Integer.parseInt(input) - 1;
		} catch (NumberFormatException e) {
			view.showMessage("Entrada no válida.");
			patientMenu();
			return;
		}

		if (slotChoice < 0 || slotChoice >= availableSlots.size()) {
			view.showMessage("Selección no válida.");
			patientMenu();
			return;
		}

		String selectedSlot = availableSlots.get(slotChoice);

		availableSlots.remove(slotChoice);

		String patientName = view.getData("Ingrese el nombre del paciente:");
		String notes = view.getData("Notas adicionales (opcional):");

		doctor.getAppointments().add(new Appointment(patientName, selectedDay + " " + selectedSlot, notes));

		view.showMessage("Cita asignada exitosamente para " + selectedDay + " a las " + selectedSlot + ".");
		menu1();
	}

	private static List<String> getAvailableSlots(Doctor doctor, String startTime, String endTime, int slotDuration) {
		List<String> availableSlots = new ArrayList<>();
		List<String> occupiedSlots = doctor.getAppointments().stream().map(Appointment::getDate).toList();

		LocalTime current = LocalTime.parse(startTime);
		LocalTime end = LocalTime.parse(endTime);

		while (current.isBefore(end)) {
			String slot = current.toString();
			if (!occupiedSlots.contains(slot)) {
				availableSlots.add(slot);
			}
			current = current.plusMinutes(slotDuration);
		}

		return availableSlots;
	}

	public void menu1() {
		message = "Bienvenido a MediSched, organiza tu agenda en dos toques";
		view.showMessage(message);

		String option = "";

		option = view.menu(new String[] { "Paciente", "Doctor", "Salir" });
		do {
			switch (option) {
			case "Paciente":
				patientMenu();
				break;
			case "Doctor":
				menuDoctor();
				break;
			case "salir":
				System.exit(0);
				break;

			default:
			}
		} while (option.equals("salir"));
	}

	public void menuDoctor() {
		String option = "";

		option = view.menu(new String[] { "Registrarse", "Iniciar Sesion", "Regresar", "Salir" });
		do {
			switch (option) {
			case "Registrarse":
				registerDC();
				break;
			case "Iniciar Sesion":
				login();
				break;
			case "Regresar":
				menu1();
				break;
			case "salir":
				System.exit(0);
				break;

			default:
			}
		} while (option.equals("salir"));
	}

	public void menuDC() {
		String option = "";

		option = view.menu(new String[] { "Gestionar Tiempos", "Generar Reporte", "Registrar Datos Médicos",
				"Informacion del Paciente", "Agenda de la semana", "Salir" });
		do {
			switch (option) {

			case "Gestionar Tiempos":
				manageAppointmentTimes();
				break;
			case "Generar Reporte":
				generateReport();
				break;
			case "Registrar Datos Médicos":
				registerMedicalData();
				break;
			case "Informacion del Paciente":
				viewPatientInfo();
				break;
			case "Agenda de la semana":
				week();
			case "salir":
				System.exit(0);
				break;

			default:
				break;
			}
		} while (option.equals("salir"));
	}

	private void manageAppointmentTimes() {
		message = "Ingrese la cita a reprogramar (formato: paciente, dia, horario):";
		String data = view.getData(message);

		Pattern pattern = Pattern.compile("^\\s*(.+?)\\s*,\\s*(.+?)\\s*,\\s*(.+?)\\s*$");
		Matcher matcher = pattern.matcher(data);

		if (!matcher.matches()) {
			message = "Formato inválido. Use: paciente, dia, horario";
			view.showMessage(message);
			return;
		}

		String patientName = matcher.group(1);
		String newDay = matcher.group(2);
		String newTime = matcher.group(3);

		Appointment appointmentToReschedule = doctor.getAppointments().stream()
				.filter(a -> a.getPatientName().equals(patientName)).findFirst().orElse(null);

		if (appointmentToReschedule != null) {
			appointmentToReschedule.setDate(newDay + " " + newTime);
			message = "Cita reprogramada para " + patientName + " el " + newDay + " a las " + newTime;
			view.showMessage(message);
		} else {
			message = "Cita no encontrada.";
			view.showMessage(message);
		}
		menuDC();
	}

	private void generateReport() {
		int totalPatients = doctor.getAppointments().size();
		double totalTime = doctor.getAppointments().stream().mapToInt(appointment -> appointment.getDuration()).sum();
		double avgTime = 30 * totalPatients;

		message = "Total pacientes:  " + totalPatients;
		view.showMessage(message);
		message = "Tiempo estimado de atención de la semana: " + avgTime + " minutos.";
		view.showMessage(message);
		menuDC();
	}

	private void registerMedicalData() {
		message = "Ingrese el nombre del paciente:";
		String patientName = view.getData(message);
		message = "Ingrese los datos médicos (ejemplo: presión arterial, glucosa):";
		String medicalData = view.getData(message);

		Appointment appointment = doctor.getAppointments().stream().filter(a -> a.getPatientName().equals(patientName))
				.findFirst().orElse(null);

		if (appointment != null) {
			appointment.setMedicalData(medicalData);
			message = "Datos médicos registrados para " + patientName;
			view.showMessage(message);
		} else {
			message = "Paciente no encontrado.";
			view.showMessage(message);
		}
		menuDC();
	}

	private void week() {
		StringBuilder allAppointments = new StringBuilder();

		for (int i = 0; i < doctor.getAppointments().size(); i++) {
			String opt = doctor.getAppointments().get(i).getDate() + doctor.getAppointments().get(i).getPatientName()
					+ " " + "\n";
			allAppointments.append(opt);
		}

		view.showMessage(allAppointments.toString());
		menuDC();
	}

	private void viewPatientInfo() {
		if (doctor.getAppointments().isEmpty()) {
			message = "No hay pacientes registrados.";
			view.showMessage(message);
			menuDC();
			return;
		}

		StringBuilder patientList = new StringBuilder("Pacientes:\n");
		List<Appointment> appointmen = doctor.getAppointments();
		for (int i = 0; i < appointmen.size(); i++) {
			patientList.append(i + 1).append(". ").append(appointmen.get(i).getPatientName()).append("\n");
		}
		view.showMessage(patientList.toString());

		message = "Seleccione un paciente (número):";
		String input = view.getData(message);
		int patientChoice;
		try {
			patientChoice = Integer.parseInt(input) - 1;
		} catch (NumberFormatException e) {
			view.showMessage("Entrada no válida.");
			return;
		}

		if (patientChoice < 0 || patientChoice >= appointmen.size()) {
			view.showMessage("Selección no válida.");
			return;
		}

		Appointment selectedAppointment = appointmen.get(patientChoice);

		String patientInfo = "Información del paciente:\n" + "Nombre: " + selectedAppointment.getPatientName() + "\n"
				+ "Notas: " + (selectedAppointment.getNotes().isEmpty() ? "Ninguna" : selectedAppointment.getNotes())
				+ "\n" + "Datos médicos: "
				+ (selectedAppointment.getMedicalData().isEmpty() ? "Ninguno" : selectedAppointment.getMedicalData());
		view.showMessage(patientInfo);
		menuDC();
	}

	public void init() {
		menu1();
	}

}
