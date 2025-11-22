package za.co.synergio.georgiou.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import za.co.synergio.georgiou.controller.MvcController;
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
	
	private static final Logger log = LoggerFactory.getLogger(CsvStorageImpl.class);
	
	@Value("${synergeio.folder.path}")
	private String folderPath;

	@Value("${synergeio.csv.file}")
	private String csvFileName;

	@Value("${synergeio.back.file}")
	private String backupFileName;

	@Value("${synergeio.index.file}")
	private String indexFileName;
	
	@Value("${synergeio.published.file}")
	private String publishFileName;
	
	private Path csvFilePath;
	private Path backupFilePath;
	private Path publishedPath;
	private Path indexCounterPath;


    private static final String CSV_HEADER = String.join(",",
            "index",
            "transactionDate",
            "serviceDate",
            "recurringDate",
            "customerName",
            "cellphone",
            "customerAddress",
            "vehicleMakeAndModel",
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
            "jobsToDo",
            "state"
    );

    

    public CsvStorageImpl(
            @Value("${synergeio.folder.path}") String folderPath,
            @Value("${synergeio.csv.file}") String csvFileName,
            @Value("${synergeio.back.file}") String backupFileName,
            @Value("${synergeio.published.file}") String publishFileName,
            @Value("${synergeio.index.file}") String indexFileName
            
    ) throws IOException {


        this.folderPath = folderPath;
        this.csvFileName = csvFileName;
        this.backupFileName = backupFileName;
        this.indexFileName = indexFileName;        

        csvFilePath = Path.of(folderPath + csvFileName);
        backupFilePath = Path.of(folderPath + backupFileName);
        indexCounterPath = Path.of(folderPath + indexFileName);
        publishedPath = Path.of(folderPath + publishFileName);

        // Ensure parent folders exist
        Files.createDirectories(csvFilePath.getParent());
        Files.createDirectories(backupFilePath.getParent());

        // Create CSV file with header if missing
        if (!Files.exists(csvFilePath)) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    csvFilePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            )) {
                writer.write(CSV_HEADER);
                writer.newLine();
            }
        }

        // Ensure backup file exists
        if (!Files.exists(backupFilePath)) {
            Files.createFile(backupFilePath);
        }
    }
//    
//    public CsvStorageImpl(
//            @Value("${synergeio.csv.path:data/service_records.csv}") String csvPath,
//            @Value("${synergeio.back.path:data/service_records.backup}") String backupPath
//    ) throws IOException {
//
//        this.csvFilePath = Path.of(csvPath);
//        this.backupFilePath = Path.of(backupPath);
//
//        Files.createDirectories(csvFilePath.getParent());
//        Files.createDirectories(backupFilePath.getParent());
//
//        // Create file with header if missing
//        if (!Files.exists(csvFilePath)) {
//            try (BufferedWriter writer = Files.newBufferedWriter(
//                    csvFilePath,
//                    StandardOpenOption.CREATE,
//                    StandardOpenOption.WRITE
//            )) {
//                writer.write(CSV_HEADER);
//                writer.newLine();
//            }
//        }
//
//
//        // Ensure backup file exists
//        if (!Files.exists(backupFilePath)) {
//            Files.createFile(backupFilePath);
//        }
//    }

    @Override
    public synchronized void save(ServiceRecord record) throws IOException {
    	log.info("Writing file to " + csvFilePath);

    	try (BufferedWriter writer = Files.newBufferedWriter(
    	        csvFilePath,
    	        StandardOpenOption.CREATE,   // create if missing
    	        StandardOpenOption.APPEND    // append if exists
    	)) {
    	    record.setIndex(getNextIndex());
    	    writer.write(toCsvSave(record));
    	    writer.newLine();
    	}
//        try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath, StandardOpenOption.APPEND)) {
//        	record.setIndex(getNextIndex());
//            writer.write(toCsvSave(record));
//            writer.newLine();
//        }
//
//        Files.copy(csvFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
    }
    
    @Override
    public synchronized void update(ServiceRecord record) throws IOException {
    	log.info("called method update with record: \n" + record);

        List<ServiceRecord> records = readAll();
        List<ServiceRecord> newRecords = new ArrayList<>();

        for (ServiceRecord serviceRecord : records) {	
            if (serviceRecord.getIndex() == record.getIndex()) {
                // Replace the record with the updated one
                log.info("Found file index:"+ record.getIndex());
                newRecords.add(record);
            } else {
            	newRecords.add(serviceRecord);
            }            
        }


        Files.copy(csvFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
        log.info("Made file backup");

        try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            // Write header first
            writer.write(CSV_HEADER);
            writer.newLine();

            // Write all records
            for (ServiceRecord r : newRecords) {
            	log.info("Record:"+r.getIndex()+" State" +r.getState());
                writer.write(toCsvSave(r));
                writer.newLine();
            }
            log.info("saved records");
        }
    }
    

	@Override
	public void publish() throws IOException {
        log.info("To Publish Records: "+ publishedPath);
        

        try (BufferedWriter writer = Files.newBufferedWriter(publishedPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            
        	List<ServiceRecord> records = readAll();
            writer.write(CSV_HEADER);
            writer.newLine();

            for (ServiceRecord r : records) {
            	log.info("Record:"+r.getIndex()+" State" +r.getState());
                writer.write(toCsvSave(r));
                writer.newLine();
            }
            log.info("Published records");
        }
	}


    @Override
    public synchronized List<ServiceRecord> readAll() throws IOException {

    	log.info("Reading file " + csvFilePath);
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
                quote(r.getCustomerAddress()),
                quote(r.getVehicleMakeAnModel()),
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
                quote(String.join(";", 
                        r.getJobsToDo() == null ? List.of() : r.getJobsToDo()
                )),
                String.valueOf(r.getState())
        );
    }

    

    private ServiceRecord fromCsvRead(String line) {
        try {

            ServiceRecord r = new ServiceRecord();
 

            String[] part = parseCsvLine(line);
            if (part.length < 20) return null;


            int i = 0;
            r.setIndex(parseInt(part[i++]));
            r.setDate(parseDate(part[i++]));
            r.setServiceDate(parseDate(part[i++]));
            r.setRecurringDate(parseDate(part[i++]));
            r.setCustomerName(part[i++]);
            r.setCellphone(part[i++]);
            r.setCustomerAddress(part[i++]);
            r.setVehicleMakeAnModel(part[i++]);
            r.setVehicleRegNumber(part[i++]);
            r.setOdometerReading(part[i++]);
            r.setVinNumber(part[i++]);
            r.setDocumentType(part[i++]);
            r.setRequirementCategory(part[i++]);
            r.setInterval(parseInt(part[i++]));
            r.setDaysLeft(parseInt(part[i++]));
            r.setMaterialsRequired(part[i++]);
            r.setLabourHours(parseDouble(part[i++]));
            r.setAmount(parseBigDecimal(part[i++]));
            r.setBreakdown(part[i++]);
            

            
            if (i < part.length) {
                String raw = part[i++];
                if (raw != null && !raw.isBlank()) {
                    String[] items = raw.split(";");
                    r.setJobsToDo(List.of(items));
                } else {
                    r.setJobsToDo(new ArrayList<>());
                }
            }
            
            LocalDate serviceDate = r.getServiceDate();            
            LocalDate today = LocalDate.now();

            if (serviceDate != null) {
                long daysLeft = ChronoUnit.DAYS.between(today, serviceDate); // future date - today
                r.setDaysLeft((int) daysLeft); // cast to int if your model uses int
            } else {
                r.setDaysLeft(0);
            }

            r.setStatel(parseInt(part[i++]));

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

        // Escape newlines
        s = s.replace("\n", "\\n");

        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
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

    	Path indexPath = indexCounterPath;

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
