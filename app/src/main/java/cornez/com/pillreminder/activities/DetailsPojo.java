package cornez.com.pillreminder.activities;


public class DetailsPojo {

    private String nameOfPill;
    private String dosage;
    private String frequency;
    private String date;

    public String getNameOfPill() {
        return nameOfPill;
    }

    public void setNameOfPill(String nameOfPill) {

        this.nameOfPill = nameOfPill;
    }

    public String getDosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}