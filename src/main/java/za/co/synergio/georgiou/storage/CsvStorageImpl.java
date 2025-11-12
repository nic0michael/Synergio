package za.co.synergio.georgiou.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import za.co.synergio.georgiou.model.ServiceRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CsvStorageImpl implements CsvStorage {

    private static final String HEADER = "Date,Customer Name,Cellphone,Vehicle Registration Number,Vehicle Audometer Reading,VIN Number,Document Type,Vehicle Requirement Category,Interval,Materials Required,Labour (hrs),Amount,Breakdown";

    private final Path csvPath;
    private final Path backupPath;
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ISO_LOCAL_DATE;

    public CsvStorageImpl(
            @Value("${synergeio.csv.path}") String csvPathStr,
            @Value("${synergeio.back.path}") String backupPathStr
    ) throws IOException {
        this.csvPath = Path.of(csvPathStr);
        this.backupPath = Path.of(backupPathStr);

        createFileIfNotExist(csvPath);
        createFileIfNotExist(backupPath);
    }

    private void createFileIfNotExist(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createFile(path);
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.WRITE)) {
                writer.write(HEADER);
                writer.newLine();
            }
        } else if (Files.size(path) == 0) {
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.WRITE)) {
                writer.write(HEADER);
                writer.newLine();
            }
        }
    }

    @Override
    public synchronized List<ServiceRecord> readAll() throws IOException {
        List<ServiceRecord> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                if (line.isBlank()) continue;
                List<String> cols = parseCsvLine(line);
                ServiceRecord r = toRecord(cols);
                list.add(r);
            }
        }
        return list;
    }

    @Override
    public synchronized void save(ServiceRecord record) throws IOException {
        String line = serialize(record);

        // Write to main CSV
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        }

        // Write to backup CSV
        try (BufferedWriter writer = Files.newBufferedWriter(backupPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        }
    }

    // Serialize a ServiceRecord into a single CSV line, quoting the date
    private String serialize(ServiceRecord r) {
        String dateStr = r.getDate() != null ? "\"" + r.getDate().format(dateFmt) + "\"" : "";

        String[] parts = new String[] {
                dateStr,
                r.getCustomerName(),
                r.getCellphone(),
                r.getVehicleRegNumber(),
                r.getAudometerReading(),
                r.getVinNumber(),
                r.getDocumentType(),
                r.getRequirementCategory(),
                r.getInterval(),
                r.getMaterialsRequired(),
                String.valueOf(r.getLabourHours()),
                r.getAmount() != null ? r.getAmount().toPlainString() : "",
                r.getBreakdown()
        };

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escape(parts[i] == null ? "" : parts[i]));
        }
        return sb.toString();
    }

    // Basic CSV parser handling quoted fields
    private List<String> parseCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++; // skip escaped quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    cols.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    cur.append(c);
                }
            }
        }
        cols.add(cur.toString());
        return cols;
    }

    private ServiceRecord toRecord(List<String> cols) {
        ServiceRecord r = new ServiceRecord();
        try {
            if (cols.size() > 0 && !cols.get(0).isBlank())
                r.setDate(LocalDate.parse(cols.get(0).replace("\"", ""), dateFmt));
        } catch (Exception ignored) { }
        if (cols.size() > 1) r.setCustomerName(cols.get(1));
        if (cols.size() > 2) r.setCellphone(cols.get(2));
        if (cols.size() > 3) r.setVehicleRegNumber(cols.get(3));
        if (cols.size() > 4) r.setAudometerReading(cols.get(4));
        if (cols.size() > 5) r.setVinNumber(cols.get(5));
        if (cols.size() > 6) r.setDocumentType(cols.get(6));
        if (cols.size() > 7) r.setRequirementCategory(cols.get(7));
        if (cols.size() > 8) r.setInterval(cols.get(8));
        if (cols.size() > 9) r.setMaterialsRequired(cols.get(9));
        if (cols.size() > 10) {
            try { r.setLabourHours(Double.parseDouble(cols.get(10))); } catch (Exception e) { r.setLabourHours(0); }
        }
        if (cols.size() > 11) {
            try { r.setAmount(new BigDecimal(cols.get(11))); } catch (Exception e) { r.setAmount(BigDecimal.ZERO); }
        }
        if (cols.size() > 12) r.setBreakdown(cols.get(12));
        return r;
    }

    // Escape commas and quotes
    private String escape(String value) {
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
