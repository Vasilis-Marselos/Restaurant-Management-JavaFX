package com.example.RestaurantManagement;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


// Διαχείριση κρατήσεων τραπεζιών και αποστολή email επιβεβαίωσης
public class BookingManager {
    private final Map<Integer, Map<LocalDateTime, BookingInfo>> bookings = new HashMap<>();
    private final File saveFile = new File("bookings.csv");
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    // Στοιχεία email αποστολέα
    private final String fromEmail = System.getenv("SMTP_USERNAME");
    private final String emailPassword = System.getenv("SMTP_PASSWORD");

    public BookingManager() {
        loadBookingsFromFile();
        removePastBookings();
    }


    // Έλεγχος αν είναι ήδη κλεισμένο τραπέζι
    public boolean isBooked(int tableNumber, LocalDateTime when) {
        return bookings
                .getOrDefault(tableNumber, Collections.emptyMap())
                .containsKey(when);
    }

    // Λίστα διαθέσιμων τραπεζιών για συγκεκριμένη ώρα
    public List<Integer> getAvailableTables(LocalDateTime when) {
        List<Integer> avail = new ArrayList<>();
        for (int table = 1; table <= 20; table++) {
            if (!isBooked(table, when)) {
                avail.add(table);
            }
        }
        return avail;
    }


    // Καταχώρηση νέας κράτησης με option αποστολής email
    public void addBooking(int tableNumber, LocalDateTime when, int people, String name, String email, boolean sendEmail) {
        var dayMap = bookings.computeIfAbsent(tableNumber, t -> new HashMap<>());
        if (dayMap.containsKey(when)) {
            System.out.printf("WARN: Table %d already booked at %s%n", tableNumber, when);
            return;
        }
        dayMap.put(when, new BookingInfo(people, name, email));
        saveBookingsToFile();

        if (sendEmail) {
            String subject = "Επιβεβαίωση Κράτησης - Maison De Gout";
            String body = "Αγαπητέ/ή " + name + ",\n\n" +
                    "Με μεγάλη χαρά σας ενημερώνουμε πως η κράτησή σας στο Maison De Gout επιβεβαιώθηκε:\n\n" +
                    "📍 Τραπέζι: " + tableNumber + "\n" +
                    "📅 Ημερομηνία & Ώρα: " + when + "\n" +
                    "👥 Άτομα: " + people + "\n\n" +
                    "Ένα μοναδικό γαστρονομικό ταξίδι σας περιμένει στον χώρο μας, γεμάτο αρώματα, γεύσεις και ξεχωριστές στιγμές.\n\n" +
                    "Σας ευχαριστούμε που μας επιλέξατε και ανυπομονούμε να σας υποδεχθούμε!\n\n" +
                    "Με εκτίμηση,\nMaison De Gout";

        sendEmail(email, subject, body);
        }
    }

    // Ακύρωση κράτησης
    public void cancelBooking(int tableNumber, LocalDateTime when) {
        var m = bookings.get(tableNumber);
        if (m != null && m.remove(when) != null) {
            saveBookingsToFile();
        }
    }

    // Ενημέρωση υπάρχουσας κράτησης
    public void updateBooking(int tableNumber, LocalDateTime oldWhen, LocalDateTime newWhen, int newSize, String newName, String newEmail) {
        cancelBooking(tableNumber, oldWhen);
        addBooking(tableNumber, newWhen, newSize, newName, newEmail, false);
    }

    // Επιστροφή όλων των κρατήσεων
    public Map<Integer, Map<LocalDateTime, BookingInfo>> getAllBookings() {
        return bookings;
    }

    // Αποθήκευση όλων των κρατήσεων σε αρχείο CSV
    private void saveBookingsToFile() {
        try (var writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (var entry : bookings.entrySet()) {
                int table = entry.getKey();
                for (var dtEntry : entry.getValue().entrySet()) {
                    BookingInfo info = dtEntry.getValue();
                    String line = String.join(",",
                            Integer.toString(table),
                            dtEntry.getKey().format(formatter),
                            Integer.toString(info.getPeople()),
                            info.getName(),
                            info.getEmail()
                    );
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Φόρτωση κρατήσεων από το αρχείο CSV
    private void loadBookingsFromFile() {
        if (!saveFile.exists()) return;
        try (var reader = new BufferedReader(new FileReader(saveFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5) continue;
                try {
                    int table = Integer.parseInt(parts[0]);
                    LocalDateTime dt = LocalDateTime.parse(parts[1], formatter);
                    int people = Integer.parseInt(parts[2]);
                    String name = parts[3];
                    String email = parts[4];
                    if (table >= 1 && table <= 20) {
                        bookings
                                .computeIfAbsent(table, t -> new HashMap<>())
                                .put(dt, new BookingInfo(people, name, email));

                        System.out.printf("Loaded booking: Table %d at %s for %s (%d people)%n",
                                table, dt, name, people);
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Διαγραφή κρατήσεων που έχουν περάσει
    public void removePastBookings() {
        LocalDateTime now = LocalDateTime.now();
        boolean changed = false;

        for (var entry : bookings.entrySet()) {
            Map<LocalDateTime, BookingInfo> dayMap = entry.getValue();
            Iterator<Map.Entry<LocalDateTime, BookingInfo>> iterator = dayMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<LocalDateTime, BookingInfo> booking = iterator.next();
                if (booking.getKey().toLocalDate().isBefore(now.toLocalDate())) {
                    iterator.remove();
                    changed = true;
                }
            }
        }

        bookings.entrySet().removeIf(e -> e.getValue().isEmpty());

        if (changed) {
            saveBookingsToFile();
        }
    }

    // Αποστολή προσαρμοσμένου email
    public void sendCustomEmail(String toEmail, String subject, String body) {
        sendEmail(toEmail, subject, body);
    }

    // Αποστολή email μέσω SMTP
    private void sendEmail(String toEmail, String subject, String body) {
        if (fromEmail == null || fromEmail.isBlank() || emailPassword == null || emailPassword.isBlank()) {
            System.out.println("Email not sent: configure SMTP_USERNAME and SMTP_PASSWORD.");
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}