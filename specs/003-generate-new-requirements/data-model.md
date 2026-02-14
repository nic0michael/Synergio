# Data Model

## Entities

### Customer (Existing)

Represents a client of the service station.

| Field | Type | Description |
|-------|------|-------------|
| index | int | Unique ID |
| customerName | String | Full Name |
| ... | ... | Other existing fields |

## Controller Models

### MvcController

#### GET /customers

**Parameters**: None
**Model Attributes**:
- `customers`: `List<Customer>` - All available customers.

**View**: `selectacustomer`
