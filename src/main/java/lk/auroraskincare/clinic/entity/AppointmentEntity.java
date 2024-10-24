package lk.auroraskincare.clinic.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents an appointment in the system, including details about
 * the date, time, registration fee, patient, dermatologist and associated treatment. This class manages the
 * validates of available consultation times.
 */

@Entity
@Table(name = "appointment")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointmentId")
    private Long id;

    private LocalDate date;

    private LocalTime time;

    private static final double REGISTRATION_FEE = 500.00;

    private boolean registrationFeeAccepted;

    // many-to-one with patient entity
    @ManyToOne
    @JoinColumn(name = "patientId")
    private PatientEntity patientEntity;

    @ManyToOne
    @JoinColumn(name = "dermatologistId")
    private DermatologistEntity dermatologistEntity;

    @OneToOne
    @JoinColumn(name = "treatmentId")
    private TreatmentEntity treatmentEntity;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appointmentEntity", cascade = CascadeType.ALL)
    private List<InvoiceEntity> invoices = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public static double getRegistrationFee() {
        return REGISTRATION_FEE;
    }

    public boolean isRegistrationFeeAccepted() {
        return registrationFeeAccepted;
    }

    public void setRegistrationFeeAccepted(boolean registrationFeeAccepted) {
        this.registrationFeeAccepted = registrationFeeAccepted;
    }

    public PatientEntity getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(PatientEntity patientEntity) {
        this.patientEntity = patientEntity;
    }

    public DermatologistEntity getDermatologistEntity() {
        return dermatologistEntity;
    }

    public void setDermatologistEntity(DermatologistEntity dermatologistEntity) {
        this.dermatologistEntity = dermatologistEntity;
    }

    public TreatmentEntity getTreatmentEntity() {
        return treatmentEntity;
    }

    public void setTreatmentEntity(TreatmentEntity treatmentEntity) {
        this.treatmentEntity = treatmentEntity;
    }

    public List<InvoiceEntity> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceEntity> invoices) {
        this.invoices = invoices;
    }

    
    
}
