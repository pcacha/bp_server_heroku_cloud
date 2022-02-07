package cz.zcu.students.cacha.bp_server;

import com.google.zxing.qrcode.QRCodeWriter;
import cz.zcu.students.cacha.bp_server.domain.Language;
import cz.zcu.students.cacha.bp_server.domain.Role;
import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.repositories.LanguageRepository;
import cz.zcu.students.cacha.bp_server.repositories.RoleRepository;
import cz.zcu.students.cacha.bp_server.repositories.UserRepository;
import cz.zcu.students.cacha.bp_server.security.JwtAuthenticationFilter;
import org.apache.tika.Tika;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static cz.zcu.students.cacha.bp_server.shared.RolesConstants.*;

/**
 * Start point of the program
 */
@SpringBootApplication
public class BpServerApplication {

    /**
     * Starts the program
     * @param args cmd arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BpServerApplication.class, args);
    }

    /**
     * Gets the bean of password encoder
     * @return bean of password encoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Gets bean of JwtAuthenticationFilter
     * @return bean of JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Gets bean of Tika
     * @return bean of Tika
     */
    @Bean
    public Tika tika() {
        return new Tika();
    }

    /**
     * Gets bean of QRCodeWriter
     * @return bean of QRCodeWriter
     */
    @Bean
    public QRCodeWriter getQRCodeWriter() {
        return new QRCodeWriter();
    }

    /**
     * Inserts roles and languages into db
     * @param roleRepository role repository
     * @param languageRepository language repository
     * @return runner
     */
    @Bean
    CommandLineRunner run(RoleRepository roleRepository, LanguageRepository languageRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return args -> {
            // if there are no roles in db insert them
            if(roleRepository.count() == 0) {
                // add translator role
                Role author = new Role(ROLE_TRANSLATOR);
                roleRepository.save(author);

                // add institution owner role
                Role reviewer = new Role(ROLE_INSTITUTION_OWNER);
                roleRepository.save(reviewer);

                // add admin role
                Role admin = new Role(ROLE_ADMIN);
                roleRepository.save(admin);
            }

            // if there are no languages in db insert them
            if(languageRepository.count() == 0) {
                try(InputStream is =  getClass().getClassLoader().getResourceAsStream("static/language_codes.csv")) {
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                        // read languages file
                        while(reader.ready()) {
                            // read line and extract values
                            String line = reader.readLine();
                            String[] languageValues = line.split(",");

                            // add language into db
                            Language language = new Language(languageValues[1], languageValues[0]);
                            languageRepository.save(language);
                        }
                    }
                }
            }

            // add admin
            if(userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@cts.com");
                admin.setPassword(bCryptPasswordEncoder.encode("P4ssword"));
                admin.getRoles().add(roleRepository.findByName(ROLE_TRANSLATOR).get());
                admin.getRoles().add(roleRepository.findByName(ROLE_ADMIN).get());
                userRepository.save(admin);
            }
        };
    }
}
