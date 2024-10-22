package lk.auroraskincare.clinic;

import lk.auroraskincare.clinic.config.AppointmentApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ClinicApplication implements CommandLineRunner {

    @Autowired
    private AppointmentApp appointmentApp;

    public static void main(String[] args) {
        SpringApplication.run(ClinicApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        appointmentApp.start();  // Start AppointmentApp when the application starts
    }
}
