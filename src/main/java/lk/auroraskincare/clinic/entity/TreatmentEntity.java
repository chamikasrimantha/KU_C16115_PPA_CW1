package lk.auroraskincare.clinic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "treatment")
public class TreatmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatmentId")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TreatmentType treatmentType;

    // @JsonIgnore
    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "treatmentEntity", cascade = CascadeType.ALL)
    // private List<AppointmentEntity> appointments;

    @OneToOne
    @JoinColumn(name = "appointmentId")
    private AppointmentEntity appointmentEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TreatmentType getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(TreatmentType treatmentType) {
        this.treatmentType = treatmentType;
    }
    
}
