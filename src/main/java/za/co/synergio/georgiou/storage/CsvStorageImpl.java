package za.co.synergio.georgiou.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import za.co.synergio.georgiou.model.Customer;
import za.co.synergio.georgiou.model.CustomerVehicle;
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
	
	@Value("${synergeio.customers.file}")
	private String customersFileName;
	
	@Value("${synergeio.customer_vehicles.file}")
	private String customerVehiclesFileName;
	
	private Path csvFilePath;
	private Path backupFilePath;
	private Path publishedPath;
	private Path indexCounterPath;
	private Path customersFilePath;
	private Path customerVehiclesFilePath;


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
            "colour",               
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

	private static final String CUST_CSV_HEADER = String.join(",",
            "index",
            "date",
            "customerName",
            "cellphone",
            "customerAddress",
            "state"
    );

	private static final String VEH_CSV_HEADER = String.join(",",
            "index",
            "customerId",
            "date",
            "customerName",
            "cellphone",
            "customerAddress",
            "vehicleRegNumber",
            "vehicleMakeAnModel",
            "colour",
            "vinNumber",
            "state"
    );


    

    public CsvStorageImpl(
            @Value("${synergeio.folder.path}") String folderPath,
            @Value("${synergeio.csv.file}") String csvFileName,
            @Value("${synergeio.back.file}") String backupFileName,
            @Value("${synergeio.published.file}") String publishFileName,
            @Value("${synergeio.index.file}") String indexFileName,
            @Value("${synergeio.customers.file}") String customersFileName,
            @Value("${synergeio.customer_vehicles.file}") String customerVehiclesFileName
            
    ) throws IOException {


        this.folderPath = folderPath;
        this.csvFileName = csvFileName;
        this.backupFileName = backupFileName;
        this.indexFileName = indexFileName;
        this.customersFileName = customersFileName;
        this.customerVehiclesFileName = customerVehiclesFileName;

        csvFilePath = Path.of(folderPath + csvFileName);
        backupFilePath = Path.of(folderPath + backupFileName);
        indexCounterPath = Path.of(folderPath + indexFileName);
        publishedPath = Path.of(folderPath + publishFileName);
        customersFilePath = Path.of(folderPath + customersFileName);
        customerVehiclesFilePath = Path.of(folderPath + customerVehiclesFileName);

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

        // Create customers CSV file with header if missing
        if (!Files.exists(customersFilePath)) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    customersFilePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            )) {
                writer.write(CUST_CSV_HEADER);
                writer.newLine();
            }
        }

        // Create customer_vehicles CSV file with header if missing
        if (!Files.exists(customerVehiclesFilePath)) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    customerVehiclesFilePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            )) {
                writer.write(VEH_CSV_HEADER);
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
    public synchronized List<ServiceRecord> readAllDueIn10Days() throws IOException {
        return readAll().stream()
                .filter(r -> r.getDaysLeft() < 11)
                .sorted((a, b) -> Integer.compare(a.getDaysLeft(), b.getDaysLeft()))
                .toList();
    }

    @Override
    public synchronized List<ServiceRecord> readAllDueIn40Days() throws IOException {
        return readAll().stream()
                .filter(r -> r.getDaysLeft() < 41)
                .sorted((a, b) -> Integer.compare(a.getDaysLeft(), b.getDaysLeft()))
                .toList();
    }

    @Override
    public synchronized List<Customer> readAllCustomers() throws IOException {
        log.info("Reading file " + customersFilePath);
        List<Customer> customers = new ArrayList<>();
        if (!Files.exists(customersFilePath)) return customers;

        try (BufferedReader reader = Files.newBufferedReader(customersFilePath)) {
            String line;
            boolean skipHeader = true;

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                Customer c = fromCsvReadCustomer(line);
                if (c != null) customers.add(c);
            }
        }

        return customers;
    }

    @Override
    public synchronized List<CustomerVehicle> readAllVehicles() throws IOException {
        log.info("Reading file " + customerVehiclesFilePath);
        List<CustomerVehicle> vehicles = new ArrayList<>();
        if (!Files.exists(customerVehiclesFilePath)) return vehicles;

        try (BufferedReader reader = Files.newBufferedReader(customerVehiclesFilePath)) {
            String line;
            boolean skipHeader = true;

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                CustomerVehicle v = fromCsvReadVehicle(line);
                if (v != null) vehicles.add(v);
            }
        }

        return vehicles;
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
                quote(r.getColour()),           
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

    public String toCsvSaveCustomer(Customer c) throws IOException {
        log.info("CSV serialization completed for customer");
        return String.join(",",
                String.valueOf(c.getIndex()),
                quote(c.getDate()),
                quote(c.getCustomerName()),
                quote(c.getCellphone()),
                quote(c.getCustomerAddress()),
                String.valueOf(c.getState())
        );
    }

    public String toCsvSaveVehicle(CustomerVehicle v) throws IOException {
        log.info("CSV serialization completed for vehicle");
        return String.join(",",
                String.valueOf(v.getIndex()),
                String.valueOf(v.getCustomerId()),
                quote(v.getDate()),
                quote(v.getCustomerName()),
                quote(v.getCellphone()),
                quote(v.getCustomerAddress()),
                quote(v.getVehicleRegNumber()),
                quote(v.getVehicleMakeAnModel()),
                quote(v.getColour()),
                quote(v.getVinNumber()),
                String.valueOf(v.getState())
        );
    }

    @Override
    public synchronized void saveCustomer(Customer c) throws IOException {
        log.info("Writing customer to " + customersFilePath);

        try (BufferedWriter writer = Files.newBufferedWriter(
                customersFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            c.setIndex(getNextIndex());
            writer.write(toCsvSaveCustomer(c));
            writer.newLine();
            log.info("Customer saved with index: " + c.getIndex());
        }
    }

    @Override
    public synchronized void saveVehicle(CustomerVehicle v) throws IOException {
        log.info("Writing vehicle to " + customerVehiclesFilePath);

        try (BufferedWriter writer = Files.newBufferedWriter(
                customerVehiclesFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            v.setIndex(getNextIndex());
            writer.write(toCsvSaveVehicle(v));
            writer.newLine();
            log.info("Vehicle saved with index: " + v.getIndex());
        }
    }

    @Override
    public synchronized void updateCustomer(Customer c) throws IOException {
        log.info("called method updateCustomer with customer: \n" + c);

        List<Customer> customers = readAllCustomers();
        List<Customer> newCustomers = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getIndex() == c.getIndex()) {
                log.info("Found customer index: " + c.getIndex());
                newCustomers.add(c);
            } else {
                newCustomers.add(customer);
            }
        }

        Files.copy(customersFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
        log.info("Made file backup");

        try (BufferedWriter writer = Files.newBufferedWriter(customersFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(CUST_CSV_HEADER);
            writer.newLine();

            for (Customer customer : newCustomers) {
                log.info("Customer: " + customer.getIndex() + " State: " + customer.getState());
                writer.write(toCsvSaveCustomer(customer));
                writer.newLine();
            }
            log.info("saved customers");
        }
    }

    @Override
    public synchronized void updateVehicle(CustomerVehicle v) throws IOException {
        log.info("called method updateVehicle with vehicle: \n" + v);

        List<CustomerVehicle> vehicles = readAllVehicles();
        List<CustomerVehicle> newVehicles = new ArrayList<>();

        for (CustomerVehicle vehicle : vehicles) {
            if (vehicle.getIndex() == v.getIndex()) {
                log.info("Found vehicle index: " + v.getIndex());
                newVehicles.add(v);
            } else {
                newVehicles.add(vehicle);
            }
        }

        Files.copy(customerVehiclesFilePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
        log.info("Made file backup");

        try (BufferedWriter writer = Files.newBufferedWriter(customerVehiclesFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(VEH_CSV_HEADER);
            writer.newLine();

            for (CustomerVehicle vehicle : newVehicles) {
                log.info("Vehicle: " + vehicle.getIndex() + " State: " + vehicle.getState());
                writer.write(toCsvSaveVehicle(vehicle));
                writer.newLine();
            }
            log.info("saved vehicles");
        }
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
            r.setColour(part[i++]);  
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

    private Customer fromCsvReadCustomer(String line) {
        try {
            Customer c = new Customer();
            String[] part = parseCsvLine(line);
            if (part.length < 6) return null;

            int i = 0;
            c.setIndex(parseInt(part[i++]));
            c.setDate(parseDate(part[i++]));
            c.setCustomerName(part[i++]);
            c.setCellphone(part[i++]);
            c.setCustomerAddress(part[i++]);
            c.setStatel(parseInt(part[i++]));

            return c;
        } catch (Exception e) {
            System.err.println("Failed to parse Customer CSV line: " + e.getMessage());
            return null;
        }
    }

    private CustomerVehicle fromCsvReadVehicle(String line) {
        try {
            CustomerVehicle v = new CustomerVehicle();
            String[] part = parseCsvLine(line);
            if (part.length < 10) return null;

            int i = 0;
            v.setIndex(parseInt(part[i++]));

            if (part.length >= 11) {
                v.setCustomerId(parseInt(part[i++]));
            } else {
                v.setCustomerId(0);
            }

            v.setDate(parseDate(part[i++]));
            v.setCustomerName(part[i++]);
            v.setCellphone(part[i++]);
            v.setCustomerAddress(part[i++]);
            v.setVehicleRegNumber(part[i++]);
            v.setVehicleMakeAnModel(part[i++]);
            v.setColour(part[i++]);
            v.setVinNumber(part[i++]);
            v.setStatel(parseInt(part[i++]));

            return v;
        } catch (Exception e) {
            System.err.println("Failed to parse CustomerVehicle CSV line: " + e.getMessage());
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

    @Override
    public synchronized List<CustomerVehicle> getCustomerVehicles(int customerId) throws IOException {
        return readAllVehicles().stream()
                .filter(v -> v.getCustomerId() == customerId)
                .toList();
    }

}
