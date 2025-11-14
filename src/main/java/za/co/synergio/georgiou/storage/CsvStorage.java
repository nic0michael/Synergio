package za.co.synergio.georgiou.storage;

import java.io.IOException;
import java.util.List;

import za.co.synergio.georgiou.model.ServiceRecord;

public interface CsvStorage {
	List<ServiceRecord> readAll() throws IOException;

	void save(ServiceRecord record) throws IOException;
	
	List<ServiceRecord> readAllDueIn40Days() throws IOException;
}
