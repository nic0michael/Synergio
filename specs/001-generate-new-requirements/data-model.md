# Data Model & Schema: Show Customer Vehicles

## Entity: CustomerVehicle

We are modifying the existing `CustomerVehicle` class.

### New Field

| Field Name | Data Type | Description |
|------------|-----------|-------------|
| `customerId` | `int` | **Foreign Key**. Stores the `index` of the owning `Customer`. |

### CSV Structure Impact

The `toCsvSaveVehicle` and `fromCsvRead` (implied/generic) logic in `CsvStorageImpl` must be updated to read/write this new column.

**Proposed CSV Header for Vehicles**:
```csv
index,customerId,date,customerName,cellphone,customerAddress,vehicleRegNumber,vehicleMakeAnModel,colour,vinNumber,state
```
*(Position of `customerId` is flexible, but must be consistent. Placing it second is standard for FKs)*.

## Entity: Customer

No changes to schema. Used for lookup.

## API Contracts

### Controller: MvcController

#### 1. Show Search Form
- **Path**: `GET /getcustvehicles`
- **View**: `getcustvehicles.html`
- **Model**: `customerOptions` (List<Map<String, ...>>) for the dropdown.

#### 2. Get Vehicles
- **Path**: `GET /vehicleRecords`
- **Params**: `CustomerId` (int)
- **View**: `displaycustvehicles.html`
- **Model**: 
    - `vehicles`: `List<CustomerVehicle>`
    - `customerName`: `String`

### Storage: CsvStorage

```java
List<CustomerVehicle> getCustomerVehicles(int customerId);
```
