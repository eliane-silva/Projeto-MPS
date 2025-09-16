package projetomps.view;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projetomps.business_logic.controller.RotationController;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.Taxist;
import projetomps.util.exception.RepositoryException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
@AllArgsConstructor
public class RotationView {

    private final RotationController rotationController;
    private final Scanner scanner;

    // ===== Forms =====

    public void showScheduleForm() {
        try {
            log.info("=== Schedule a New Rotation ===");

            log.info("Enter taxist ID:");
            int taxistId = Integer.parseInt(readInput());

            log.info("Enter date (YYYY-MM-DD):");
            LocalDate date = LocalDate.parse(readInput());

            log.info("Enter start time (HH:MM):");
            LocalTime start = LocalTime.parse(readInput());

            log.info("Enter end time (HH:MM) [optional, press Enter to skip]:");
            String endRaw = readInput();
            LocalTime end = (endRaw == null || endRaw.isBlank()) ? null : LocalTime.parse(endRaw);

            sendSchedule(taxistId, date, start, end);
        } catch (Exception e) {
            showError("Error while filling the form: " + e.getMessage());
        }
    }

    public void showAvailableTimes(LocalDate date) throws RepositoryException {
        log.info("=== Available times for {} ===", date);

        // Example time windows (start-only slots). If you prefer, compare ranges with start/end.
        List<LocalTime> candidateStarts = List.of(
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );

        List<Rotation> all = rotationController.getAllRotations();

        List<LocalTime> takenStarts = all.stream()
                .filter(r -> date.equals(r.getDate()))
                .map(Rotation::getStartTime)
                .filter(t -> t != null)
                .toList();

        List<LocalTime> available = new ArrayList<>(candidateStarts);
        available.removeAll(takenStarts);

        if (available.isEmpty()) {
            log.info("No available times for the selected date.");
        } else {
            available.forEach(t -> log.info("- {}", t));
        }
    }

    public void sendSchedule(int taxistId, LocalDate date, LocalTime start, LocalTime end) throws RepositoryException {
        Taxist taxist = new Taxist();
        taxist.setId(taxistId);

        Rotation rotation = new Rotation();
        rotation.setTaxist(taxist);
        rotation.setDate(date);
        rotation.setStartTime(start);
        rotation.setEndTime(end);
        rotation.setStatus("PENDING");

        Boolean created = rotationController.createRotation(rotation);
        if (Boolean.TRUE.equals(created)) {
            showMessage("Rotation scheduled successfully!");
        } else {
            showError("Failed to schedule rotation.");
        }
    }

    // ===== Actions using only existing controller methods =====

    public void confirmSchedule(int listIndex) throws RepositoryException {
        List<Rotation> list = rotationController.getAllRotations();
        if (!isValidIndex(listIndex, list)) {
            showError("Invalid rotation index.");
            return;
        }

        Rotation r = list.get(listIndex);
        r.setStatus("CONFIRMED");

        Rotation updated = rotationController.updateRotation(r);
        if (updated != null) {
            showMessage("Rotation confirmed successfully!");
        } else {
            showError("Error confirming rotation.");
        }
    }

    public void cancelSchedule(int listIndex) throws RepositoryException {
        List<Rotation> list = rotationController.getAllRotations();
        if (!isValidIndex(listIndex, list)) {
            showError("Invalid rotation index.");
            return;
        }

        Rotation r = list.get(listIndex);
        r.setStatus("CANCELLED"); // optional: mark before deletion for visibility

        Boolean deleted = rotationController.deleteRotation(r);
        if (Boolean.TRUE.equals(deleted)) {
            showMessage("Rotation cancelled successfully!");
        } else {
            showError("Error cancelling rotation.");
        }
    }

    public void listMyRotations(int taxistId) throws RepositoryException {
        List<Rotation> all = rotationController.getAllRotations();
        List<Rotation> mine = all.stream()
                .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxistId)
                .toList();

        if (mine.isEmpty()) {
            log.info("No rotations found for taxist {}.", taxistId);
            return;
        }

        log.info("=== Rotations for taxist {} ===", taxistId);
        for (int i = 0; i < mine.size(); i++) {
            Rotation r = mine.get(i);
            log.info("[{}] Date: {} | Start: {} | End: {} | Status: {}",
                    i,
                    r.getDate(),
                    r.getStartTime(),
                    r.getEndTime(),
                    r.getStatus());
        }
    }

    public void showIndividualStats(int taxistId) throws RepositoryException {
        List<Rotation> all = rotationController.getAllRotations();
        List<Rotation> mine = all.stream()
                .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxistId)
                .toList();

        long total = mine.size();
        long confirmed = mine.stream().filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
        long pending = mine.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
        long cancelled = mine.stream().filter(r -> "CANCELLED".equalsIgnoreCase(r.getStatus())).count();

        log.info("=== Stats for taxist {} ===", taxistId);
        log.info("Total: {}", total);
        log.info("Confirmed: {}", confirmed);
        log.info("Pending: {}", pending);
        log.info("Cancelled: {}", cancelled);
    }

    // ===== Messaging & helpers =====

    public void showMessage(String msg) {
        log.info(msg);
    }

    public void showError(String msg) {
        log.error("ERROR: {}", msg);
    }

    private String readInput() {
        String input = scanner.nextLine();
        return input != null ? input.trim() : "";
    }

    private boolean isValidIndex(int index, List<Rotation> list) {
        return index >= 0 && index < list.size();
    }

    public void showMainMenu() {
        log.info("\n=== Rotation Management ===");
        log.info("1. Schedule rotation");
        log.info("2. Show available times (asks for date)");
        log.info("3. Confirm rotation (by list index)");
        log.info("4. Cancel rotation (by list index)");
        log.info("5. List my rotations (by taxist ID)");
        log.info("6. Show my stats (by taxist ID)");
        log.info("0. Exit");
        log.info("Choose an option:");
    }
}