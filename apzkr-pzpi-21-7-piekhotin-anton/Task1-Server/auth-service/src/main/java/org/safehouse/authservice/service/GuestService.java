package org.safehouse.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.safehouse.authservice.model.dto.AuthRequestDto;
import org.safehouse.authservice.model.dto.GuestInfoDto;
import org.safehouse.authservice.model.entity.Guest;
import org.safehouse.authservice.model.entity.GuestRole;
import org.safehouse.authservice.model.exception.AuthException;
import org.safehouse.authservice.repository.GuestRepository;
import org.safehouse.authservice.service.security.UserDetailsImpl;
import org.safehouse.authservice.service.util.RequestMetaInfoHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    public Guest createGuest(AuthRequestDto signupRequestDTO) {
        var clientMetaInfo = RequestMetaInfoHolder.getCurrentClientRequestMetaInfo();

        if (isGuestWithSameEmailExists(signupRequestDTO.getEmail())) {
            String msg = String.format("[%s] Guest with email: %s already exists", clientMetaInfo.getCorrelationId(), signupRequestDTO.getEmail());
            log.error(msg);
            throw new AuthException(msg, HttpStatus.BAD_REQUEST.name());
        }

        Guest newGuest = Guest.builder()
                .email(signupRequestDTO.getEmail())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .role(GuestRole.CLIENT)
                .created(LocalDateTime.now())
                .isEnabled(true)
                .build();

        Guest savedGuest = guestRepository.save(newGuest);

        log.info("[{}]: New guest with email: {} was created", clientMetaInfo.getCorrelationId(), signupRequestDTO.getEmail());
        return savedGuest;
    }

    public boolean isGuestWithSameEmailExists(String email) {
        return guestRepository.existsByEmail(email);
    }

    public Guest findByEmail(String email) {
        return guestRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email {} not found", email);
            return null;
        });
    }

    public Guest getAuthenticatedGuestFromSecurityContext() {
        String guestEmail = getUserDetailsFromSecurityContext().getUsername();
        return findByEmail(guestEmail);
    }

    // Before taking data about an authenticated client, check whether it is authenticated.
    // Using the isClientAuthenticated() method.
    public UserDetailsImpl getUserDetailsFromSecurityContext() {
        return (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
    }

    public boolean isClientAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public boolean existsById(Long id) {
        return guestRepository.existsById(id);
    }

    public boolean editUser(Long id, GuestInfoDto guestInfoDto) {
        if (existsById(id)) {
            Guest guestToEdit = guestRepository.findById(id).orElse(null);
            if (guestToEdit != null) {
                guestToEdit.setEmail(guestInfoDto.getEmail());
                guestToEdit.setName(guestInfoDto.getName());
                guestToEdit.setIsEnabled(guestInfoDto.isEnabled());
                guestToEdit.setRole(guestInfoDto.getRole());
                guestRepository.save(guestToEdit);
                return true;
            }
        }
        return false;
    }

    public GuestInfoDto getUserInfoById(Long id) {
        Guest guest = guestRepository.findById(id).orElse(null);
        if (guest == null) {
            return null;
        }
        return GuestInfoDto.builder()
                .email(guest.getEmail())
                .name(guest.getName())
                .isEnabled(guest.getIsEnabled())
                .role(guest.getRole())
                .build();
    }

    public List<Guest> getAllUsers() {
        return guestRepository.findAll();
    }

    public boolean deleteUserById(Long id) {
        if (!existsById(id))
            return false;
        guestRepository.deleteById(id);
        return true;
    }

    /**
     * Код експорту даних користувачів.
     */
    public String exportUsers() {
        List<Guest> users = getAllUsers();
        //Створення папки
        File directory = new File("C:\\SafeHouse");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //Створення файлу
        File csvFile = new File("C:\\SafeHouse\\users.csv");
        try (FileWriter writer = new FileWriter(csvFile)) {
            //Запис першого рядку даних
            writer.write("id,email,name,password,isEnabled,roles,creationDate\n");
            for (Guest user : users) {
                //Запис даних кожного користувача
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getPassword(),
                        user.getIsEnabled(),
                        user.getRole(),
                        user.getCreated()));
            }
            return "Successful export users to file users.csv";
        } catch (IOException e) {
            e.printStackTrace();
            return "Export failed";
        }
    }

    public String backupDatabase() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();

            processBuilder.command("docker exec user-microservice-db pg_dump -d user-db -U postgres");

            File outputFile = new File("C:\\SafeHouse\\backup.sql");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            processBuilder.redirectOutput(outputFile);

            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println(7);
                return "Backup completed successfully.";
            } else {
                System.out.println(8);
                return "Error during backup.";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error during backup.";
        }
    }
}