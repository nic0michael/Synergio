package za.co.synergio.georgiou.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import za.co.synergio.georgiou.model.ServiceRecord;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvStorageImpl implements CsvStorage {

    private final Path csvFilePath;
    private final Path backupFilePath;

    public CsvStorageImpl(
            @Value("${synergeio.csv.path:data/service_records.csv}") String csvPath,
            @Value("${synergeio.back.path:data/service_records.backup}") String backupPath
    ) throws IOException {
        this.csvFilePath = Path.of(csvPath);
        this.backupFilePath = Path.of(backupPath);

        // Ensure parent directories exist
        Files.createDirectories(csvFilePath.getParent());
        Files.createDirectories(backupFilePath.getParent());

        // Create CSV file with header if it doesn't exist
        if (!Files.exists(csvFilePath)) {
            try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath)) {
                writer.write("date,serviceDate,recurringDate,customerName,cellphone,vehicleRegNumber,odometerReading,vinNumber," +
                        "documentType,requirementCategory,interval,materialsRequired,labourHours,amount,breakdown,daysLeft");
                writer.newLine();
            }
        }

        // Create backup file if it doesn't exist
        if (!Files.exists(backupFilePath)) {
            Files.createFile(backupFilePath);
        }
    }

    @Override
    public synchronized void save(ServiceRecord record) throws IOException {
        // Write to main CSV
        try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath, StandardOpenOption.APPEND)) {
            writer.write(toCsv(record));
            writer.newLine();
        }

        // Update backup
        Files.copy(csvFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public synchronized List<ServiceRecord> readAll() throws IOException {
        List<ServiceRecord> records = new ArrayList<>();
        if (!Files.exists(csvFilePath)) return records;

        try (BufferedReader reader = Files.newBufferedReader(csvFilePath)) {
            String line;
            boolean skipHeader = true;
            while ((line = reader.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                ServiceRecord record = fromCsv(line);
                if (record != null) records.add(record);
            }
        }
        return records;
    }

    @Override
    public synchronized List<ServiceRecord> readAllDueIn40Days() throws IOException {
        List<ServiceRecord> allRecords = readAll();
        return allRecords.stream()
                .filter(r -> r.getDaysLeft() < 41)
                .sorted((r1, r2) -> Integer.compare(r1.getDaysLeft(), r2.getDaysLeft()))
                .toList();
    }
    
    // --- CSV helpers ---
    private String toCsv(ServiceRecord r) {
        return String.join(",",
                quote(r.getDate()),
                quote(r.getServiceDate()),
                quote(r.getRecurringDate()),
                quote(r.getCustomerName()),
                quote(r.getCellphone()),
                quote(r.getVehicleRegNumber()),
                quote(r.getOdometerReading()), // fixed spelling
                quote(r.getVinNumber()),
                quote(r.getDocumentType()),
                quote(r.getRequirementCategory()),
                String.valueOf(r.getInterval()),
                quote(r.getMaterialsRequired()),
                String.valueOf(r.getLabourHours()),
                r.getAmount() != null ? r.getAmount().toPlainString() : "0",
                quote(r.getBreakdown()),
                String.valueOf(r.getDaysLeft())  
        );
    }

    private ServiceRecord fromCsv(String line) {
        try {
            String[] parts = parseCsvLine(line);
            if (parts.length < 15) return null;

            ServiceRecord r = new ServiceRecord();
            r.setDate(parseDate(parts[0]));
            r.setServiceDate(parseDate(parts[1]));
            r.setRecurringDate(parseDate(parts[2]));
            r.setCustomerName(parts[3]);
            r.setCellphone(parts[4]);
            r.setVehicleRegNumber(parts[5]);
            r.setOdometerReading(parts[6]);
            r.setVinNumber(parts[7]);
            r.setDocumentType(parts[8]);
            r.setRequirementCategory(parts[9]);
            r.setInterval(parseInt(parts[10]));
            r.setMaterialsRequired(parts[11]);
            r.setLabourHours(parseDouble(parts[12]));
            r.setAmount(parseBigDecimal(parts[13]));
            r.setBreakdown(parts[14]);            
            r.setDaysLeftl(parseInt(parts[15]));           

            return r;
        } catch (Exception e) {
            System.err.println("Failed to parse CSV line: " + e.getMessage());
            return null;
        }
    }

    private String quote(Object value) {
        if (value == null) return "";
        String s = value.toString();
        if (s.contains(",") || s.contains("\"")) {
            s = "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        return LocalDate.parse(value.replace("\"", ""));
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value.replace("\"", "")); } catch (Exception e) { return 0; }
    }

    private double parseDouble(String value) {
        try { return Double.parseDouble(value.replace("\"", "")); } catch (Exception e) { return 0.0; }
    }

    private BigDecimal parseBigDecimal(String value) {
        try { return new BigDecimal(value.replace("\"", "")); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    /**
     * Handles commas and quotes in CSV lines correctly.
     */
    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
}
