# Data Model: Generate Code from Requirements

**Feature**: Generate Code from Requirements

## Entities

No new data entities are created. The feature utilizes existing entities.

### Customer
*Existing Entity*
- **index**: `int` (Unique ID)
- **customerName**: `String`
- **customerAddress**: `String`
- **cellphone**: `String`

### CustomerVehicle
*Existing Entity*
- **index**: `int` (Unique ID)
- **vehicleRegNumber**: `String`
- **vehicleMakeAnModel**: `String`
- **colour**: `String`
- **vinNumber**: `String`
- **customerId**: `int` (Foreign Key to Customer)

## View Models (Thymeleaf Context)

### /customervehiclesbycust
- **Attribute**: `vehicles`
- **Type**: `List<CustomerVehicle>`
- **Description**: Filtered list of vehicles belonging to the requested customer.

### /customerbycust
- **Attribute**: `customer`
- **Type**: `Customer`
- **Description**: Single customer object matching the requested ID.

## Persistence
- **Read-Only**: This feature only performs read operations on `customers.csv` and `customer_vehicles.csv` via `CsvStorageImpl`.
