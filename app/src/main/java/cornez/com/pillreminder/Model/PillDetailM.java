package cornez.com.pillreminder.Model;



public class PillDetailM {

    boolean isMorningPending;
    boolean isNoonPending;
    boolean isNightPending;
    boolean isCompleted;
    boolean isAddedRecord;

    int id;

    String pillName;
    String doase;
    String frequency;

    public PillDetailM(boolean isMorningPending, boolean isNoonPending, boolean isNightPending,
                       boolean isCompleted, int id, String pillName, String doase, String frequency
                        ,boolean isAddedRecord) {

        this.id                 = id;
        this.isMorningPending   = isMorningPending;
        this.isNoonPending      = isNoonPending;
        this.isNightPending     = isNightPending;
        this.isCompleted        = isCompleted;
        this.pillName           = pillName;
        this.doase              = doase;
        this.frequency          = frequency;
        this.isAddedRecord      = isAddedRecord;
    }


    public boolean isMorningPending() {
        return isMorningPending;
    }

    public void setMorningPending(boolean morningPending) {
        isMorningPending = morningPending;
    }

    public boolean isNoonPending() {
        return isNoonPending;
    }

    public void setNoonPending(boolean noonPending) {
        isNoonPending = noonPending;
    }

    public boolean isNightPending() {
        return isNightPending;
    }

    public void setNightPending(boolean nightPending) {
        isNightPending = nightPending;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public String getDoase() {
        return doase;
    }

    public void setDoase(String doase) {
        this.doase = doase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isAddedRecord() {
        return isAddedRecord;
    }

    public void setAddedRecord(boolean addedRecord) {
        isAddedRecord = addedRecord;
    }
}


