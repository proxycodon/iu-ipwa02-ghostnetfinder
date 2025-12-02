package de.ghostnet;

import de.ghostnet.domain.model.*;
import de.ghostnet.domain.repo.GhostNetRepository;
import de.ghostnet.domain.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/**
 * Einstiegspunkt der Spring-Boot-Anwendung.
 * Enthält zusätzlich eine einfache Seed-Logik für Demo-Accounts und ein Beispielnetz.
 */
@SpringBootApplication
public class GhostNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhostNetApplication.class, args);
    }

    /**
     * Initialisiert Demo-Benutzer und ein Beispiel-Geisternetz,
     * falls die Datenbank noch leer ist.
     *
     * Achtung: Nur für Entwicklung und lokale Tests gedacht,
     * in Produktion sollten andere Seed-Mechanismen verwendet werden.
     */
    @Bean
    CommandLineRunner seed(UserRepository users, GhostNetRepository nets, PasswordEncoder enc) {
        return args -> {
            // Demo-Benutzer nur anlegen, wenn noch kein "admin" existiert
            if (users.findByUsername("admin").isEmpty()) {
                users.save(new User(null, "admin", enc.encode("admin123"), Role.ROLE_ADMIN, true));
                users.save(new User(null, "salv", enc.encode("salv123"), Role.ROLE_SALVAGER, true));
                users.save(new User(null, "rep", enc.encode("rep123"), Role.ROLE_REPORTER, true));
            }

            // Ein Beispielnetz für die Startansicht anlegen, wenn noch keine Netze vorhanden sind
            if (nets.count() == 0) {
                GhostNet n = new GhostNet();
                n.setTitle("Netz bei Tonne 7");
                n.setDescription("Schätzung 20–30 m²");
                n.setLatitude(new BigDecimal("53.550000"));
                n.setLongitude(new BigDecimal("9.990000"));
                n.setEstimatedSizeM2(new BigDecimal("25.0"));
                // Status bleibt hier über Default-Wert REPORTED gesetzt
                nets.save(n);
            }
        };
    }
}
