package model;
//Estudiante: Christian Orlando Garzon Sierra
//Codigo: 202311008
//projecto: MediSched
public class Appointment {
	private String patientName;
    private String date;  
    private String notes;  
    private String medicalData;  
    private int duration;  
    
    public Appointment(String patientName, String date, String notes) {
        this.patientName = patientName;
        this.date = date;
        this.notes = notes;
        this.medicalData = ""; 
    }


    
    public String getMedicalData() {
        return medicalData;
    }

    public void setMedicalData(String medicalData) {
        this.medicalData = medicalData;
    }

   
    public int getDuration() {
        return duration;
    }

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

    
    

}
