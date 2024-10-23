package lk.auroraskincare.clinic.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "dermatologist")
public class DermatologistEntity extends UserEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dermatologistId")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConsultationTime consultationTime;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dermatologistEntity", cascade = CascadeType.ALL)
    private List<AppointmentEntity> appointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConsultationTime getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(ConsultationTime consultationTime) {
        this.consultationTime = consultationTime;
    }

    

}
