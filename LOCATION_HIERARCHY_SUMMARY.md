# Location Parent-Child Relationship Implementation

## Changes Made

### 1. Location Model Enhancement
**File**: `backend/src/main/java/com/carRentalSystem/backend/model/Location.java`

Added parent-child relationship support:
- `@ManyToOne` relationship for parent location
- `@OneToMany` relationship for child locations
- Helper methods: `addChild()` and `removeChild()`
- JSON serialization handling to prevent circular references

### 2. Location Repository Updates
**File**: `backend/src/main/java/com/carRentalSystem/backend/repository/LocationRepository.java`

Added new query methods:
- `findByParentLocationLocationId(Long parentId)` - Find all children of a parent
- `findByParentLocationIsNull()` - Find root locations (no parent)

### 3. Location Service Updates
**File**: `backend/src/main/java/com/carRentalSystem/backend/services/LocationService.java`

Enhanced methods:
- `createLocation()` - Now handles parent location assignment
- `updateLocation()` - Can update parent relationships
- `getChildLocations(Long parentId)` - Get all children of a location
- `getRootLocations()` - Get all root locations

### 4. Location Controller Updates
**File**: `backend/src/main/java/com/carRentalSystem/backend/controller/LocationController.java`

New endpoints:
- `GET /api/locations/roots` - Get all root locations
- `GET /api/locations/{id}/children` - Get child locations
- Enhanced pagination with sorting direction control

## Usage Examples

### Creating a Root Location
```json
POST /api/locations
{
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL"
}
```

### Creating a Child Location
```json
POST /api/locations
{
  "province": "KIGALI",
  "district": "KICUKIRO",
  "sector": "Gikondo",
  "cell": "Gikondo",
  "village": "Gikondo I",
  "parentLocation": {
    "locationId": 1
  }
}
```

### Getting Location Hierarchy
1. Get all root locations: `GET /api/locations/roots`
2. Get children of a location: `GET /api/locations/1/children`

### Updating Parent Relationship
```json
PUT /api/locations/2
{
  "province": "KIGALI",
  "district": "KICUKIRO",
  "sector": "Gikondo",
  "cell": "Gikondo",
  "village": "Gikondo I",
  "parentLocation": {
    "locationId": 3
  }
}
```

### Removing Parent Relationship
```json
PUT /api/locations/2
{
  "province": "KIGALI",
  "district": "KICUKIRO",
  "sector": "Gikondo",
  "cell": "Gikondo",
  "village": "Gikondo I",
  "parentLocation": null
}
```

## Pagination & Sorting - All Controllers

All controllers now support full pagination and sorting:

### Parameters
- `page`: Page number (default: 0)
- `size`: Items per page (default: 10)
- `sortBy`: Field name to sort by
- `sortDirection`: ASC or DESC

### Examples
- Admins: `/api/admins/paginated?page=0&size=10&sortBy=adminName&sortDirection=ASC`
- Bookings: `/api/bookings/paginated?page=0&size=10&sortBy=bookingId&sortDirection=DESC`
- Customers: `/api/customers/paginated?page=0&size=10&sortBy=custName&sortDirection=ASC`
- Locations: `/api/locations/paginated?page=0&size=10&sortBy=locationId&sortDirection=ASC`
- Cars: `/api/cars/paginated?page=0&size=10&sortBy=carPlatenum&sortDirection=ASC`
- Payments: `/api/payments/paginated?page=0&size=10&sortBy=paymentDate&sortDirection=DESC`
- Rents: `/api/rents/paginated?page=0&size=10&sortBy=rentDate&sortDirection=DESC`
- Returns: `/api/returns/paginated?page=0&size=10&sortBy=returnDate&sortDirection=DESC`

## Important Notes

1. **Cascade Delete**: Deleting a parent location will automatically delete all child locations
2. **JSON Serialization**: Circular references are prevented using `@JsonIgnoreProperties`
3. **Lazy Loading**: Parent location uses lazy loading for performance
4. **Validation**: Ensure parent location exists before assigning it to a child

## Rwanda Administrative Structure

The system uses Rwanda's administrative divisions:

**Province → District → Sector → Cell → Village**

Example hierarchy:
- Province: KIGALI
  - District: GASABO
    - Sector: Bibare
      - Cell: Intashyo
        - Village: BCL

### Available Provinces
- KIGALI
- EASTERN
- WESTERN
- NORTHERN
- SOUTHERN

### Available Districts (30 total)
**Kigali City:** GASABO, KICUKIRO, NYARUGENGE

**Eastern Province:** BUGESERA, GATSIBO, KAYONZA, KIREHE, NGOMA, NYAGATARE, RWAMAGANA

**Northern Province:** BURERA, GAKENKE, GICUMBI, MUSANZE, RULINDO

**Southern Province:** GISAGARA, HUYE, KAMONYI, MUHANGA, NYAMAGABE, NYANZA, NYARUGURU, RUHANGO

**Western Province:** KARONGI, NGORORERO, NYABIHU, NYAMASHEKE, RUBAVU, RUSIZI, RUTSIRO
