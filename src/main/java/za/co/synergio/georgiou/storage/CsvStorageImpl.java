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
import java.time.temporal.ChronoUnit;

@Component
public class CsvStorageImpl implements CsvStorage {
	
	@Value("${synergeio.index.path}") // <-- Spring injects the value here
    private String indexCounterPath;

    private final Path csvFilePath;
    private final Path backupFilePath;

    private static final String CSV_HEADER = String.join(",",
            "index",
            "transactionDate",
            "serviceDate",
            "recurringDate",
            "customerName",
            "cellphone",
            "vehicleRegNumber",
            "odometerReading",
            "vinNumber",
            "documentType",
            "requirementCategory",
            "interval",
            "daysLeft",
            "materialsRequired",
            "labourHours",
            "amount",
            "breakdown",
            "state"
    );

    public CsvStorageImpl(
            @Value("${synergeio.csv.path:data/service_records.csv}") String csvPath,
            @Value("${synergeio.back.path:data/service_records.backup}") String backupPath
    ) throws IOException {

        this.csvFilePath = Path.of(csvPath);
        this.backupFilePath = Path.of(backupPath);

        Files.createDirectories(csvFilePath.getParent());
        Files.createDirectories(backupFilePath.getParent());

        // Create file with header if missing
        if (!Files.exists(csvFilePath)) {
            try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath)) {
                writer.write(CSV_HEADER);
                writer.newLine();
            }
        }

        // Ensure backup file exists
        if (!Files.exists(backupFilePath)) {
            Files.createFile(backupFilePath);
        }
    }

    @Override
    public synchronized void save(ServiceRecord record) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath, StandardOpenOption.APPEND)) {
        	record.setIndex(getNextIndex());
            writer.write(toCsvSave(record));
            writer.newLine();
        }

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
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                ServiceRecord r = fromCsvRead(line);
                if (r != null) records.add(r);
            }
        }

        return records;
    }

    @Override
    public synchronized List<ServiceRecord> readAllDueIn40Days() throws IOException {
        return readAll().stream()
                .filter(r -> r.getDaysLeft() < 41)
                .sorted((a, b) -> Integer.compare(a.getDaysLeft(), b.getDaysLeft()))
                .toList();
    }

    // ========================================================================
    // CSV SERIALIZATION
    // ========================================================================

    private String toCsvSave(ServiceRecord r) {
        return String.join(",",
                String.valueOf(r.getIndex()),
                quote(r.getDate()),
                quote(r.getServiceDate()),
                quote(r.getRecurringDate()),
                quote(r.getCustomerName()),
                quote(r.getCellphone()),
                quote(r.getVehicleRegNumber()),
                quote(r.getOdometerReading()),
                quote(r.getVinNumber()),
                quote(r.getDocumentType()),
                quote(r.getRequirementCategory()),
                String.valueOf(r.getInterval()),
                String.valueOf(r.getDaysLeft()),
                quote(r.getMaterialsRequired()),
                String.valueOf(r.getLabourHours()),
                r.getAmount() != null ? r.getAmount().toPlainString() : "0",
                quote(r.getBreakdown()),
                String.valueOf(r.getState())
        );
    }

    

    private ServiceRecord fromCsvRead(String line) {
        try {
            String[] parts = parseCsvLine(line);
            if (parts.length < 15) return null;

            ServiceRecord r = new ServiceRecord();
 
            int i = 0;

            r.setIndex(parseInt(parts[0]));
            r.setDate(parseDate(parts[1]));
            r.setServiceDate(parseDate(parts[2]));
            r.setRecurringDate(parseDate(parts[3]));
            r.setCustomerName(parts[4]);
            r.setCellphone(parts[5]);
            r.setVehicleRegNumber(parts[6]);
            r.setOdometerReading(parts[7]);
            r.setVinNumber(parts[8]);
            r.setDocumentType(parts[9]);
            r.setRequirementCategory(parts[10]);
            r.setInterval(parseInt(parts[11]));
            r.setDaysLeft(parseInt(parts[12]));
            r.setMaterialsRequired(parts[13]);
            r.setLabourHours(parseDouble(parts[14]));
            r.setAmount(parseBigDecimal(parts[15]));
            r.setBreakdown(parts[16]);
            r.setStatel(parseInt(parts[17]));    // your model's actual method name                    // FIXED: correct setter
            LocalDate serviceDate = r.getServiceDate();            
            LocalDate today = LocalDate.now();

            if (serviceDate != null) {
                long daysLeft = ChronoUnit.DAYS.between(today, serviceDate); // future date - today
                r.setDaysLeft((int) daysLeft); // cast to int if your model uses int
            } else {
                r.setDaysLeft(0);
            }
            

            return r;

        } catch (Exception e) {
            System.err.println("Failed to parse CSV line: " + e.getMessage());
            return null;
        }
    }

    // ========================================================================
    // HELPERS
    // ========================================================================

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
        try { return Integer.parseInt(value.replace("\"", "")); }
        catch (Exception e) { return 0; }
    }

    private double parseDouble(String value) {
        try { return Double.parseDouble(value.replace("\"", "")); }
        catch (Exception e) { return 0.0; }
    }

    private BigDecimal parseBigDecimal(String value) {
        try { return new BigDecimal(value.replace("\"", "")); }
        catch (Exception e) { return BigDecimal.ZERO; }
    }

    /**
     * Handles commas and quotes correctly in CSV.
     */
    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"')
                inQuotes = !inQuotes;
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
    
    private int getNextIndex() throws IOException {

        Path indexPath = Path.of(indexCounterPath); 

        // Ensure parent folder exists
        if (indexPath.getParent() != null) {
            Files.createDirectories(indexPath.getParent());
        }

        // If file doesn't exist → create it with initial value 1
        if (!Files.exists(indexPath)) {
            Files.writeString(indexPath, "1", StandardOpenOption.CREATE);
            return 1;
        }

        // Read current index
        String content = Files.readString(indexPath).trim();

        int current;
        try {
            current = Integer.parseInt(content);
        } catch (NumberFormatException e) {
            current = 1; // fallback if file is damaged
        }

        int next = current + 1;

        // Write incremented value back
        Files.writeString(indexPath, String.valueOf(next), StandardOpenOption.TRUNCATE_EXISTING);

        return next;
    }

}
