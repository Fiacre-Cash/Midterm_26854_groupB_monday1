# Car Rental Management System

## Overview

This is a Spring Boot application for managing car rental operations in Rwanda. The system demonstrates various database relationships, RESTful API design, and hierarchical location management using Spring Data JPA and PostgreSQL.

### Academic Information

- **Project**: Midterm Assessment - Advanced Database Systems
- **Group**: Group B
- **Student ID**: 26854
- **Repository**: https://github.com/Fiacre-Cash/Midterm_26854_groupB_monday1.git


## Table of Contents

- [Key Features](#key-features)
- [System Architecture](#system-architecture)
- [Database Design](#database-design)
- [Advanced Implementations](#advanced-implementations)
- [Technology Stack](#technology-stack)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Assessment Coverage](#assessment-criteria-coverage)

---

## Key Features

- Multi-entity management with 8 interconnected entities
- Hierarchical location system for Rwanda's administrative structure (Province → District → Sector → Cell → Village)
- Pagination and sorting on all endpoints
- Multiple relationship types: One-to-One, One-to-Many, Many-to-One, and Self-Referencing
- Custom query methods using JPQL and derived queries
- RESTful API with complete CRUD operations
- Performance optimizations with lazy loading and cascade operations


## System Architecture

### Core Entities

The system has 8 main entities:

| Entity | Purpose | Key Relationships |
|--------|---------|-------------------|
| Admin | System administrators | Manages bookings |
| Customer | Rental service users | Creates bookings |
| Location | Geographic data | Self-referencing hierarchy |
| ManageCar | Vehicle inventory | Linked to bookings |
| Booking | Rental reservations | Connects customers, cars, payments |
| Rent | Active rental records | One-to-one with bookings |
| Payment | Transaction records | One-to-one with bookings |
| Return | Vehicle returns | One-to-one with rentals |

### Entity Relationships

```
Admin (1) -------- manages -------- (*) Booking
Customer (1) ------ makes ---------- (*) Booking
ManageCar (1) ----- booked in ------ (*) Booking
Booking (1) ------- has ------------ (1) Rent
Booking (1) ------- has ------------ (1) Payment
Rent (1) ---------- has ------------ (1) Return
Location (1) ------ parent of ------ (*) Location (Self-referencing)
```

### Relationship Types

| Type | Implementation | Example |
|------|----------------|---------|
| One-to-Many | Customer → Bookings | One customer can have multiple bookings |
| Many-to-One | Booking → Customer | Many bookings belong to one customer |
| One-to-One | Booking → Payment | Each booking has exactly one payment |
| Self-Referencing | Location → Parent | Locations can have parent-child relationships |


## Database Design

### Relationship Implementations

#### 1. One-to-Many: Customer → Bookings

A customer can make multiple bookings over time.

```java
// Customer.java
@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
private List<Booking> bookings = new ArrayList<>();
```

Key points:
- `mappedBy = "customer"` means Booking owns the relationship
- `cascade = CascadeType.ALL` means operations on Customer cascade to bookings
- Foreign key `customer_id` is stored in the `bookings` table

#### 2. Many-to-One: Booking → Customer

Multiple bookings can belong to a single customer.

```java
// Booking.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "customer_id")
private Customer customer;
```

Key points:
- `@JoinColumn` creates the foreign key column
- `FetchType.LAZY` loads customer data only when needed
- This is the owning side of the relationship

#### 3. One-to-One: Booking ↔ Payment

Each booking has exactly one payment transaction.

```java
// Booking.java (inverse side)
@OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
private Payment payment;

// Payment.java (owning side)
@OneToOne
@JoinColumn(name = "booking_id", unique = true)
private Booking booking;
```

Key points:
- `unique = true` enforces the one-to-one constraint
- Payment owns the relationship
- Allows navigation from both sides

#### 4. Self-Referencing: Location Hierarchy

Locations form a tree structure (Province → District → Sector → Cell → Village).

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

Key points:
- `parent_location_id` references the same table
- `orphanRemoval = true` automatically deletes orphaned children
- `@JsonIgnoreProperties` prevents circular reference issues
- Supports unlimited hierarchy depth

### Database Schema

```sql
-- Core tables
CREATE TABLE customers (
    cust_nic VARCHAR(16) PRIMARY KEY,
    cust_name VARCHAR(100) NOT NULL,
    cust_email VARCHAR(100) UNIQUE,
    province VARCHAR(50),
    district VARCHAR(50)
);

CREATE TABLE locations (
    location_id BIGSERIAL PRIMARY KEY,
    province VARCHAR(50),
    district VARCHAR(50),
    sector VARCHAR(100),
    cell VARCHAR(100),
    village VARCHAR(100),
    parent_location_id BIGINT REFERENCES locations(location_id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    booking_id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(16) REFERENCES customers(cust_nic),
    car_platenum VARCHAR(20) REFERENCES manage_car(car_platenum),
    admin_id BIGINT REFERENCES admins(admin_id),
    booking_status VARCHAR(20),
    booking_date TIMESTAMP
);

CREATE TABLE payments (
    payment_id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT UNIQUE REFERENCES bookings(booking_id),
    payment_amount DECIMAL(10,2),
    payment_status VARCHAR(20)
);
```


## Advanced Implementations

### Hierarchical Location Management

#### Rwanda's Administrative Structure

Rwanda has a 5-level administrative hierarchy:
- Province (5 total)
- District (30 total)
- Sector (416 total)
- Cell (2,148 total)
- Village (14,837 total)

#### How Location Data is Stored

Root location (no parent):
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

Child location (with parent):
```json
{
  "locationId": 2,
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL Branch Office",
  "parentLocation": { "locationId": 1 }
}
```

#### Relationship Handling

```java
public Location createLocation(Location location) {
    if (location.getParentLocation() != null && 
        location.getParentLocation().getLocationId() != null) {
        Optional<Location> parentLocation = locationRepository
            .findById(location.getParentLocation().getLocationId());
        parentLocation.ifPresent(location::setParentLocation);
    }
    return locationRepository.save(location);
}
```

Features:
- Validates parent location exists before creating relationship
- Supports unlimited hierarchy depth
- Cascade delete removes all child locations
- Lazy loading prevents performance issues

---

### Pagination & Sorting

#### Implementation

```java
@GetMapping("/paginated")
public ResponseEntity<Page<Location>> getLocationsWithPagination(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "locationId") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection) {
    
    Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
        ? Sort.Direction.DESC : Sort.Direction.ASC;
    
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    Page<Location> locations = locationService.getLocationsWithPagination(pageable);
    
    return ResponseEntity.ok(locations);
}
```

#### How It Works

1. Request Parameters:
   - `page` - Page number (starts from 0)
   - `size` - Number of records per page
   - `sortBy` - Field name to sort by
   - `sortDirection` - ASC or DESC

2. SQL Translation:
   ```sql
   SELECT * FROM locations 
   ORDER BY location_id ASC 
   LIMIT 10 OFFSET 0
   ```

3. Response Structure:
   ```json
   {
     "content": [...],
     "totalPages": 5,
     "totalElements": 50,
     "size": 10,
     "number": 0,
     "first": true,
     "last": false
   }
   ```

#### Performance Benefits

- Reduced memory usage - only loads requested page
- Faster response times - database returns limited results
- Efficient sorting - done at database level
- Handles large datasets well

#### Usage Examples

```bash
# First page, 10 items, sorted by province
GET /api/locations/paginated?page=0&size=10&sortBy=province&sortDirection=ASC

# Third page, 20 items, sorted by price descending
GET /api/cars/paginated?page=2&size=20&sortBy=carPrice&sortDirection=DESC
```

---

### Custom Query Methods

#### 1. Existence Checking with existsBy()

```java
// Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByProvinceAndDistrict(Province province, District district);
}

// Service
public boolean locationExists(Province province, District district) {
    return locationRepository.existsByProvinceAndDistrict(province, district);
}
```

How it works:
- Spring Data JPA parses the method name
- Generates SQL: `SELECT COUNT(*) > 0 FROM locations WHERE province = ? AND district = ?`
- Returns boolean instead of fetching entire entity
- More efficient than loading full data

Usage:
```java
if (!locationService.locationExists(Province.KIGALI, District.GASABO)) {
    locationService.createLocation(newLocation);
}
```

#### 2. Province-Based Queries

```java
// Derived Query Method
List<Customer> findByProvince(Province province);

// Custom JPQL Query
@Query("SELECT c FROM Customer c WHERE c.province = :province")
List<Customer> findCustomersByProvince(@Param("province") Province province);
```

API Endpoints:
```bash
# Get all customers in Kigali
GET /api/customers/province/KIGALI

# Get all customers in Eastern province
GET /api/customers/province/EASTERN
```

Response Example:
```json
[
  {
    "custName": "Uwase Marie",
    "custAddress": "KG 15 Ave, Bibare, Gasabo",
    "custPhone": "0788123456",
    "custEmail": "uwase.marie@example.com",
    "province": "KIGALI",
    "district": "GASABO"
  }
]
```

Additional Query Methods:
```java
// Find by district
List<Customer> findByDistrict(District district);

// Find by province and district
List<Customer> findByProvinceAndDistrict(Province province, District district);

// Count customers by province
long countByProvince(Province province);
```


## Technology Stack

### Backend
- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- Hibernate ORM

### Database
- PostgreSQL 12+
- HikariCP connection pool

### Build Tools
- Maven 3.6+

### Other
- Jackson for JSON processing
- Bean Validation for input validation


## Setup Instructions

### Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

### Installation Steps

#### 1. Clone the Repository

```bash
git clone https://github.com/Fiacre-Cash/-midterm_26854_groupB_Monday.git
cd -midterm_26854_groupB_Monday
```

#### 2. Database Setup

Create the database in PostgreSQL:

```sql
CREATE DATABASE car_rental_system;
```

#### 3. Configure Application

Edit `backend/src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/car_rental_system
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080
```

#### 4. Build the Project

```bash
cd backend
./mvnw clean install
```

For Windows:
```bash
mvnw.cmd clean install
```

#### 5. Run the Application

```bash
./mvnw spring-boot:run
```

For Windows:
```bash
mvnw.cmd spring-boot:run
```

#### 6. Test the Application

The application runs on `http://localhost:8080`

Test an endpoint:
```bash
curl http://localhost:8080/api/locations
```

### Database Tables

The application automatically creates these tables:
- admins
- customers
- locations
- manage_car
- bookings
- rents
- payments
- returns


## API Documentation

### Base URL

```
http://localhost:8080/api
```

### Available Endpoints

| Resource | Endpoint | Methods |
|----------|----------|---------|
| Admins | `/api/admins` | GET, POST, PUT, DELETE |
| Customers | `/api/customers` | GET, POST, PUT, DELETE |
| Locations | `/api/locations` | GET, POST, PUT, DELETE |
| Cars | `/api/cars` | GET, POST, PUT, DELETE |
| Bookings | `/api/bookings` | GET, POST, PUT, DELETE |
| Rents | `/api/rents` | GET, POST, PUT, DELETE |
| Payments | `/api/payments` | GET, POST, PUT, DELETE |
| Returns | `/api/returns` | GET, POST, PUT, DELETE |

### Special Endpoints

Location Hierarchy:
```bash
GET /api/locations/roots              # Get root locations
GET /api/locations/{id}/children      # Get child locations
GET /api/locations/province/{province} # Filter by province
```

Province Queries:
```bash
GET /api/customers/province/{province}       # Get customers by province
GET /api/customers/province/query/{province} # Custom query method
```

Pagination (works on all entities):
```bash
GET /api/{entity}/paginated?page={page}&size={size}&sortBy={field}&sortDirection={ASC|DESC}
```

Example:
```bash
GET /api/customers/paginated?page=0&size=10&sortBy=custName&sortDirection=ASC
```

### Sample Requests

Create a Customer:
```bash
POST /api/customers
Content-Type: application/json

{
  "custNic": "1199012345678901",
  "custName": "Uwase Marie",
  "custAddress": "KG 15 Ave, Bibare, Gasabo",
  "custPhone": "0788123456",
  "custEmail": "uwase.marie@example.com",
  "custDrivingLicense": "RW2024001234",
  "province": "KIGALI",
  "district": "GASABO"
}
```

Create a Location:
```bash
POST /api/locations
Content-Type: application/json

{
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL",
  "parentLocation": {
    "locationId": 1
  }
}
```

### Response Formats

Success Response:
```json
{
  "status": "success",
  "data": { ... }
}
```

Paginated Response:
```json
{
  "content": [...],
  "totalPages": 5,
  "totalElements": 50,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

Error Response:
```json
{
  "status": "error",
  "message": "Resource not found",
  "timestamp": "2026-03-13T10:30:00"
}
```

### More Documentation

See these files for more details:
- API_ENDPOINTS.md - Complete API reference
- POSTMAN_EXAMPLES_RWANDA.md - Postman collection examples
- LOCATION_HIERARCHY_SUMMARY.md - Location feature details


## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/carRentalSystem/backend/
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── CustomerController.java
│   │   │   │   ├── LocationController.java
│   │   │   │   ├── ManageCarController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   ├── RentController.java
│   │   │   │   └── ReturnController.java
│   │   │   │
│   │   │   ├── model/                # JPA Entities
│   │   │   │   ├── Admin.java
│   │   │   │   ├── Booking.java
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Location.java
│   │   │   │   ├── ManageCar.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── Rent.java
│   │   │   │   └── Return.java
│   │   │   │
│   │   │   ├── repository/           # Data Access
│   │   │   │   ├── AdminRepository.java
│   │   │   │   ├── BookingRepository.java
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── LocationRepository.java
│   │   │   │   ├── ManageCarRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   ├── RentRepository.java
│   │   │   │   └── ReturnRepository.java
│   │   │   │
│   │   │   ├── services/             # Business Logic
│   │   │   │   ├── AdminService.java
│   │   │   │   ├── BookingService.java
│   │   │   │   ├── CustomerService.java
│   │   │   │   ├── LocationService.java
│   │   │   │   ├── ManageCarService.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   ├── RentService.java
│   │   │   │   └── ReturnService.java
│   │   │   │
│   │   │   ├── enums/                # Enumerations
│   │   │   │   ├── AdminRole.java
│   │   │   │   ├── BookingStatus.java
│   │   │   │   ├── CarStatus.java
│   │   │   │   ├── CarType.java
│   │   │   │   ├── District.java
│   │   │   │   ├── PaymentMethod.java
│   │   │   │   ├── PaymentStatus.java
│   │   │   │   └── Province.java
│   │   │   │
│   │   │   └── CarRentalManagementSystemApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   │
│   └── test/
│       └── java/
│
├── target/
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### Layer Responsibilities

| Layer | Purpose |
|-------|---------|
| Controller | Handles HTTP requests and responses |
| Service | Contains business logic |
| Repository | Database operations |
| Model | Entity definitions |
| Enums | Constants and types |


## Assessment Criteria Coverage

This project covers all the required assessment criteria:

| Requirement | Implementation | Status |
|-------------|----------------|--------|
| ERD with 5+ tables | 8 entities with documented relationships | Complete |
| Location Implementation | Hierarchical structure with parent-child relationships | Complete |
| Sorting & Pagination | All 8 controllers support Pageable and Sort | Complete |
| Many-to-Many | Example implementation provided | Complete |
| One-to-Many | Customer→Bookings, Car→Bookings, Admin→Bookings | Complete |
| One-to-One | Booking→Payment, Booking→Rent, Rent→Return | Complete |
| Self-Referencing | Location parent-child hierarchy | Complete |
| existsBy() Method | Implemented in multiple repositories | Complete |
| Province Query | Multiple implementations with custom JPQL | Complete |

### Summary

- 8 entities (exceeds minimum of 5)
- All relationship types implemented
- Pagination on all controllers
- Complete Rwanda province/district support
- Custom queries with JPQL and derived methods
- Performance optimizations (lazy loading, cascades)


## Rwanda Location Data

### Administrative Structure

Rwanda has 5 levels of administrative divisions:
- Province (5)
- District (30)
- Sector (416)
- Cell (2,148)
- Village (14,837)

### Provinces and Districts

**Kigali City (3 districts)**
- GASABO, KICUKIRO, NYARUGENGE

**Eastern Province (7 districts)**
- BUGESERA, GATSIBO, KAYONZA, KIREHE, NGOMA, NYAGATARE, RWAMAGANA

**Northern Province (5 districts)**
- BURERA, GAKENKE, GICUMBI, MUSANZE, RULINDO

**Southern Province (8 districts)**
- GISAGARA, HUYE, KAMONYI, MUHANGA, NYAMAGABE, NYANZA, NYARUGURU, RUHANGO

**Western Province (7 districts)**
- KARONGI, NGORORERO, NYABIHU, NYAMASHEKE, RUBAVU, RUSIZI, RUTSIRO

### Usage

The location enums are used for:
- Customer address validation
- Location-based queries
- Hierarchical location management
- Geographic filtering


## Additional Documentation

More detailed documentation is available in these files:

- API_ENDPOINTS.md - Complete API reference
- POSTMAN_EXAMPLES_RWANDA.md - Postman collection with examples
- LOCATION_HIERARCHY_SUMMARY.md - Location feature details
- DATABASE_CONNECTION_CHECK.md - Database setup guide

---

## License

This project is submitted as part of academic coursework.

---

## Contact

For questions, please contact through the university portal.

Group B - Student ID: 26854
