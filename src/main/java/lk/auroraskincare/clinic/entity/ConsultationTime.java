package lk.auroraskincare.clinic.entity;

public enum ConsultationTime {
    MONDAY("Monday: 10:00am - 01:00pm"),
    WEDNESDAY("Wednesday: 02:00pm - 05:00pm"),
    FRIDAY("Friday: 04:00pm - 08:00pm"),
    SATURDAY("Saturday: 09:00am - 01:00pm");

    private String timeSlot;

    ConsultationTime(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getTimeSlot() {
        return timeSlot;
    }
}

