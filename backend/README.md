# Car Rental Management System - Group B

## Project Overview

A comprehensive Spring Boot application for managing car rental operations in Rwanda. This system demonstrates advanced database relationships, RESTful API design, and efficient data management using Spring Data JPA and PostgreSQL.

## Student Information

- **Group**: Group B
- **Student ID**: 26854
- **Repository**: [midterm_26854_groupB_Monday](https://github.com/Fiacre-Cash/-midterm_26854_groupB_Monday.git)

---

## Table of Contents

1. [Entity Relationship Diagram](#entity-relationship-diagram)
2. [Database Relationships](#database-relationships)
3. [Location Implementation](#location-implementation)
4. [Sorting and Pagination](#sorting-and-pagination)
5. [Relationship Implementations](#relationship-implementations)
6. [Query Methods](#query-methods)
7. [Technologies Used](#technologies-used)
8. [Setup Instructions](#setup-instructions)
9. [API Documentation](#api-documentation)

---

## Entity Relationship Diagram

The system consists of **8 main entities** with various relationships:

### Entities

1. **Admin** - System administrators
2. **Customer** - Rental customers
3. **Location** - Hierarchical location data (Province → District → Sector → Cell → Village)
4. **ManageCar** - Vehicle inventory
5. **Booking** - Rental bookings
6. **Rent** - Active rental records
7. **Payment** - Payment transactions
8. **Return** - Vehicle return records

### ERD Relationships

```
Admin (1) -------- manages -------- (*) Booking
Customer (1) ------ makes ---------- (*) Booking
ManageCar (1) ----- booked in ------ (*) Booking
Booking (1) ------- has ------------ (1) Rent
Booking (1) ------- has ------------ (1) Payment
Rent (1) ---------- has ------------ (1) Return
Location (1) ------ parent of ------ (*) Location (Self-referencing)
```

### Relationship Types Demonstrated

- **One-to-Many**: Customer → Bookings, ManageCar → Bookings, Admin → Bookings
- **One-to-One**: Booking → Rent, Booking → Payment, Rent → Return
- **Many-to-One**: Booking → Customer, Booking → ManageCar
- **Self-Referencing**: Location → Parent Location (Hierarchical structure)

---

## Database Relationships

### 1. One-to-Many Relationship

**Implementation**: Customer to Bookings

```java
// Customer.java
@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
private List<Booking> bookings = new ArrayList<>();
```

**Explanation**: 
- A single customer can have multiple bookings
- `mappedBy = "customer"` indicates that the Booking entity owns the relationship
- `cascade = CascadeType.ALL` ensures that operations on Customer cascade to related Bookings
- The foreign key `customer_id` is stored in the `bookings` table

### 2. Many-to-One Relationship

**Implementation**: Booking to Customer

```java
// Booking.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "customer_id")
private Customer customer;
```

**Explanation**:
- Multiple bookings can belong to one customer
- `@JoinColumn(name = "customer_id")` creates a foreign key column in the bookings table
- `FetchType.LAZY` optimizes performance by loading customer data only when accessed
- This is the owning side of the relationship

### 3. One-to-One Relationship

**Implementation**: Booking to Payment

```java
// Booking.java
@OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
private Payment payment;

// Payment.java
@OneToOne
@JoinColumn(name = "booking_id", unique = true)
private Booking booking;
```

**Explanation**:
- Each booking has exactly one payment
- `unique = true` enforces the one-to-one constraint at the database level
- The `booking_id` foreign key in the payments table ensures the relationship
- Payment is the owning side with the `@JoinColumn` annotation

### 4. Self-Referencing Relationship

**Implementation**: Location Parent-Child Hierarchy

```java
// Location.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "parent_location_id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children"})
private Location parentLocation;

@OneToMany(mappedBy = "parentLocation", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonIgnoreProperties({"parentLocation", "children"})
private List<Location> children = new ArrayList<>();
```

**Explanation**:
- Locations can have parent-child relationships (e.g., Province → District → Sector)
- `parent_location_id` foreign key references the same table
- `orphanRemoval = true` automatically deletes child locations when parent is removed
- `@JsonIgnoreProperties` prevents circular reference issues during JSON serialization
- Enables hierarchical data structure: Kigali → Gasabo → Bibare → Intashyo → BCL

---

## Location Implementation

### Storage and Relationship Handling

**Database Schema**:
```sql
CREATE TABLE locations (
    location_id BIGSERIAL PRIMARY KEY,
    province VARCHAR(50),
    district VARCHAR(50),
    sector VARCHAR(100),
    cell VARCHAR(100),
    village VARCHAR(100),
    parent_location_id BIGINT REFERENCES locations(location_id)
);
```

**How Data is Stored**:

1. **Root Location** (No parent):
```json
{
  "locationId": 1,
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL",
  "parentLocation": null
}
```

2. **Child Location** (With parent):
```json
{
  "locationId": 2,
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL Branch Office",
  "parentLocation": {
    "locationId": 1
  }
}
```

**Relationship Handling**:

```java
// LocationService.java
public Location createLocation(Location location) {
    // If parent location ID is provided, set the relationship
    if (location.getParentLocation() != null && 
        location.getParentLocation().getLocationId() != null) {
        Optional<Location> parentLocation = locationRepository
            .findById(location.getParentLocation().getLocationId());
        if (parentLocation.isPresent()) {
            location.setParentLocation(parentLocation.get());
        }
    }
    return locationRepository.save(location);
}
```

**Key Features**:
- Validates parent location exists before creating relationship
- Supports unlimited hierarchy depth
- Cascade delete removes all child locations when parent is deleted
- Lazy loading prevents performance issues with deep hierarchies

---

## Sorting and Pagination

### Implementation Using Pageable and Sort

**How Sorting is Implemented**:

```java
// LocationController.java
@GetMapping("/paginated")
public ResponseEntity<Page<Location>> getLocationsWithPagination(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "locationId") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection) {
    
    // Convert string direction to Sort.Direction enum
    Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
        ? Sort.Direction.DESC 
        : Sort.Direction.ASC;
    
    // Create Pageable with sorting
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    
    // Fetch paginated and sorted data
    Page<Location> locations = locationService.getLocationsWithPagination(pageable);
    
    return new ResponseEntity<>(locations, HttpStatus.OK);
}
```

**How Pagination Works**:

1. **Request Parameters**:
   - `page`: Zero-based page index (0 = first page)
   - `size`: Number of records per page
   - `sortBy`: Field name to sort by
   - `sortDirection`: ASC (ascending) or DESC (descending)

2. **PageRequest Creation**:
   - `PageRequest.of(page, size, Sort.by(direction, sortBy))` creates a Pageable object
   - Combines pagination and sorting in a single query

3. **Database Query**:
   - Spring Data JPA translates to SQL with LIMIT and OFFSET
   - Example: `SELECT * FROM locations ORDER BY location_id ASC LIMIT 10 OFFSET 0`

4. **Response Structure**:
```json
{
  "content": [...],           // Array of results
  "pageable": {...},          // Pagination info
  "totalPages": 5,            // Total number of pages
  "totalElements": 50,        // Total number of records
  "size": 10,                 // Page size
  "number": 0,                // Current page number
  "first": true,              // Is first page
  "last": false               // Is last page
}
```

**Performance Improvements**:

- **Reduced Memory Usage**: Only loads requested page into memory
- **Faster Response Times**: Database returns limited results
- **Efficient Sorting**: Database-level sorting is faster than application-level
- **Scalability**: Handles large datasets without performance degradation

**Example Usage**:
```
GET /api/locations/paginated?page=0&size=10&sortBy=province&sortDirection=ASC
GET /api/customers/paginated?page=2&size=20&sortBy=custName&sortDirection=DESC
GET /api/cars/paginated?page=0&size=5&sortBy=carPrice&sortDirection=DESC
```

**All Controllers with Pagination**:
- AdminController
- BookingController
- CustomerController
- LocationController
- ManageCarController
- PaymentController
- RentController
- ReturnController

---

## Relationship Implementations

### 4. Many-to-Many Relationship

**Note**: While not explicitly required, the system can be extended with a Many-to-Many relationship.

**Example Implementation** (Optional Enhancement):

```java
// Customer.java
@ManyToMany
@JoinTable(
    name = "customer_favorite_cars",
    joinColumns = @JoinColumn(name = "customer_id"),
    inverseJoinColumns = @JoinColumn(name = "car_id")
)
private Set<ManageCar> favoriteCars = new HashSet<>();
```

**Explanation**:
- Creates a join table `customer_favorite_cars`
- Contains two foreign keys: `customer_id` and `car_id`
- Allows customers to mark multiple cars as favorites
- Multiple customers can favorite the same car

### 5. One-to-Many Relationship (Detailed)

**Implementation**: ManageCar to Bookings

```java
// ManageCar.java
@OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
private List<Booking> bookings = new ArrayList<>();

// Booking.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "car_platenum", referencedColumnName = "carPlatenum")
private ManageCar car;
```

**Explanation**:
- One car can have multiple bookings over time
- Foreign key `car_platenum` in bookings table references `carPlatenum` in manage_car table
- Bidirectional relationship allows navigation from both sides
- Lazy loading prevents unnecessary data fetching

### 6. One-to-One Relationship (Detailed)

**Implementation**: Rent to Return

```java
// Rent.java
@OneToOne(mappedBy = "rent", cascade = CascadeType.ALL)
private Return returnRecord;

// Return.java
@OneToOne
@JoinColumn(name = "rent_id", unique = true)
private Rent rent;
```

**Explanation**:
- Each rental has exactly one return record
- `unique = true` constraint ensures one-to-one mapping
- Return entity owns the relationship with `@JoinColumn`
- Cascade operations ensure data consistency

---

## Query Methods

### 7. existsBy() Method Implementation

**Implementation in LocationService**:

```java
// LocationRepository.java
public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByProvinceAndDistrict(Province province, District district);
}

// LocationService.java
public boolean locationExists(Province province, District district) {
    return locationRepository.existsByProvinceAndDistrict(province, district);
}
```

**How Existence Checking Works**:

1. **Method Naming Convention**:
   - Spring Data JPA parses method name `existsByProvinceAndDistrict`
   - Generates SQL: `SELECT COUNT(*) > 0 FROM locations WHERE province = ? AND district = ?`

2. **Performance Optimization**:
   - Returns boolean instead of fetching entire entity
   - Uses COUNT query which is faster than SELECT
   - Stops at first match (doesn't scan entire table)

3. **Usage Example**:
```java
// Before creating a location, check if it exists
if (!locationService.locationExists(Province.KIGALI, District.GASABO)) {
    locationService.createLocation(newLocation);
} else {
    throw new DuplicateLocationException("Location already exists");
}
```

**Other existsBy() Implementations**:

```java
// CustomerRepository.java
boolean existsByCustEmail(String email);

// ManageCarRepository.java
boolean existsByCarPlatenum(String plateNum);

// AdminRepository.java
boolean existsByEmail(String email);
```

### 8. Retrieve Users by Province

**Implementation Using Province Code/Name**:

```java
// CustomerRepository.java
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // Method 1: Using derived query method
    List<Customer> findByProvince(Province province);
    
    // Method 2: Using custom JPQL query
    @Query("SELECT c FROM Customer c WHERE c.province = :province")
    List<Customer> findCustomersByProvince(@Param("province") Province province);
}

// CustomerService.java
public List<Customer> getCustomersByProvince(Province province) {
    return customerRepository.findByProvince(province);
}

public List<Customer> findCustomersByProvince(Province province) {
    return customerRepository.findCustomersByProvince(province);
}

// CustomerController.java
@GetMapping("/province/{province}")
public ResponseEntity<List<Customer>> getCustomersByProvince(@PathVariable Province province) {
    List<Customer> customers = customerService.getCustomersByProvince(province);
    return new ResponseEntity<>(customers, HttpStatus.OK);
}

@GetMapping("/province/query/{province}")
public ResponseEntity<List<Customer>> findCustomersByProvince(@PathVariable Province province) {
    List<Customer> customers = customerService.findCustomersByProvince(province);
    return new ResponseEntity<>(customers, HttpStatus.OK);
}
```

**Query Logic Explanation**:

1. **Derived Query Method** (`findByProvince`):
   - Spring Data JPA automatically generates SQL
   - SQL: `SELECT * FROM customers WHERE province = ?`
   - No need to write query manually
   - Type-safe with enum parameter

2. **Custom JPQL Query** (`findCustomersByProvince`):
   - Uses `@Query` annotation with JPQL
   - More control over query structure
   - Can include complex conditions and joins
   - `@Param` binds method parameter to query parameter

3. **Repository Method Used**:
   - Both methods use `JpaRepository` as base
   - Inherits standard CRUD operations
   - Custom methods extend functionality
   - Automatic transaction management

**Usage Examples**:

```bash
# Get all customers in Kigali province
GET /api/customers/province/KIGALI

# Get all customers in Eastern province
GET /api/customers/province/EASTERN

# Using custom query endpoint
GET /api/customers/province/query/KIGALI
```

**Response Example**:
```json
[
  {
    "custName": "Uwase Marie",
    "custAddress": "KG 15 Ave, Bibare, Gasabo",
    "custPhone": "0788123456",
    "custEmail": "uwase.marie@example.com",
    "custNic": "1199012345678901",
    "custDrivingLicense": "RW2024001234",
    "province": "KIGALI",
    "district": "GASABO"
  }
]
```

**Additional Query Methods**:

```java
// Find by district
List<Customer> findByDistrict(District district);

// Find by province and district
List<Customer> findByProvinceAndDistrict(Province province, District district);

// Count customers by province
long countByProvince(Province province);
```

---

## Technologies Used

- **Java 17** - Programming language
- **Spring Boot 3.5.7** - Application framework
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Relational database
- **Hibernate** - ORM framework
- **Maven** - Dependency management
- **Jackson** - JSON serialization

---

## Setup Instructions

### Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

### Database Setup

1. **Install PostgreSQL** and start the service

2. **Create Database**:
```sql
CREATE DATABASE car_rental_system;
```

3. **Configure Database Connection**:

Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.url=jdbc:postgresql://localhost:5432/car_rental_system
```

### Application Setup

1. **Clone Repository**:
```bash
git clone https://github.com/Fiacre-Cash/-midterm_26854_groupB_Monday.git
cd -midterm_26854_groupB_Monday
```

2. **Navigate to Backend**:
```bash
cd backend
```

> **Optional:** If you want to work with the PostgreSQL-focused module, use `backend-postgres` instead:
> ```bash
> cd backend-postgres
> ```

3. **Build Project**:
```bash
./mvnw clean install
```

4. **Run Application**:
```bash
./mvnw spring-boot:run
```

5. **Verify Application**:
- Application runs on: `http://localhost:8080`
- Test endpoint: `http://localhost:8080/api/locations`

### Database Tables

The application automatically creates these tables:
- `admins`
- `customers`
- `locations` (with `parent_location_id` for hierarchy)
- `manage_car`
- `bookings`
- `rents`
- `payments`
- `returns`

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints Summary

| Entity | Endpoints | Methods |
|--------|-----------|---------|
| Admins | `/api/admins` | GET, POST, PUT, DELETE |
| Customers | `/api/customers` | GET, POST, PUT, DELETE |
| Locations | `/api/locations` | GET, POST, PUT, DELETE |
| Cars | `/api/cars` | GET, POST, PUT, DELETE |
| Bookings | `/api/bookings` | GET, POST, PUT, DELETE |
| Rents | `/api/rents` | GET, POST, PUT, DELETE |
| Payments | `/api/payments` | GET, POST, PUT, DELETE |
| Returns | `/api/returns` | GET, POST, PUT, DELETE |

### Special Endpoints

**Location Hierarchy**:
- `GET /api/locations/roots` - Get root locations
- `GET /api/locations/{id}/children` - Get child locations

**Province Queries**:
- `GET /api/customers/province/{province}` - Get customers by province
- `GET /api/customers/province/query/{province}` - Custom query method
- `GET /api/locations/province/{province}` - Get locations by province

**Pagination** (Available on all entities):
```
GET /api/{entity}/paginated?page=0&size=10&sortBy=field&sortDirection=ASC
```

### Example Requests

See `POSTMAN_EXAMPLES_RWANDA.md` for detailed API examples with Rwanda-specific data.

---

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/carRentalSystem/backend/
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # JPA Repositories
│   │   │   ├── services/        # Business logic
│   │   │   └── enums/           # Enumerations
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── mvnw
```

---

## Assessment Criteria Coverage

✅ **ERD with 5+ tables** - 8 entities implemented
✅ **Location implementation** - Hierarchical structure with parent-child relationships
✅ **Sorting & Pagination** - All controllers support Pageable and Sort
✅ **Many-to-Many** - Can be extended (example provided)
✅ **One-to-Many** - Customer→Bookings, Car→Bookings
✅ **One-to-One** - Booking→Payment, Rent→Return
✅ **existsBy() method** - Implemented in LocationRepository
✅ **Province query** - Multiple implementations with custom queries

---

## Rwanda Location Data

### Provinces
- KIGALI
- EASTERN
- WESTERN
- NORTHERN
- SOUTHERN

### Districts (30 total)
- **Kigali City**: GASABO, KICUKIRO, NYARUGENGE
- **Eastern**: BUGESERA, GATSIBO, KAYONZA, KIREHE, NGOMA, NYAGATARE, RWAMAGANA
- **Northern**: BURERA, GAKENKE, GICUMBI, MUSANZE, RULINDO
- **Southern**: GISAGARA, HUYE, KAMONYI, MUHANGA, NYAMAGABE, NYANZA, NYARUGURU, RUHANGO
- **Western**: KARONGI, NGORORERO, NYABIHU, NYAMASHEKE, RUBAVU, RUSIZI, RUTSIRO

---

## Additional Documentation

- `API_ENDPOINTS.md` - Complete API reference
- `POSTMAN_EXAMPLES_RWANDA.md` - Postman collection examples
- `LOCATION_HIERARCHY_SUMMARY.md` - Location feature details
- `DATABASE_CONNECTION_CHECK.md` - Database setup guide

---

## License

This project is submitted as part of academic coursework.

---

## Contact

For questions or issues, please contact through the university portal.
