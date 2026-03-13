# Car Rental System - API Endpoints for Postman

Base URL: `http://localhost:8080`

## 1. Admin APIs (`/api/admins`)

### Create Admin
- **POST** `/api/admins`
- **Body**: 
```json
{
  "adminName": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "SUPER_ADMIN"
}
```

### Get All Admins
- **GET** `/api/admins`

### Get Admin by ID
- **GET** `/api/admins/{id}`
- **Example**: `/api/admins/1`

### Get Admin by Email
- **GET** `/api/admins/email/{email}`
- **Example**: `/api/admins/email/john@example.com`

### Get Admins by Role
- **GET** `/api/admins/role/{role}`
- **Example**: `/api/admins/role/SUPER_ADMIN`

### Get Admins with Pagination
- **GET** `/api/admins/paginated?page=0&size=10&sortBy=adminName&sortDirection=ASC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: adminName)
  - `sortDirection`: ASC or DESC (default: ASC)

### Update Admin
- **PUT** `/api/admins/{id}`
- **Body**: Same as Create Admin

### Delete Admin
- **DELETE** `/api/admins/{id}`

---

## 2. Booking APIs (`/api/bookings`)

### Create Booking
- **POST** `/api/bookings`
- **Body**:
```json
{
  "bookingDate": "2026-03-15",
  "status": "PENDING",
  "customer": { "custName": "customer_name" },
  "car": { "carPlatenum": "ABC-1234" }
}
```

### Get All Bookings
- **GET** `/api/bookings`

### Get Booking by ID
- **GET** `/api/bookings/{id}`

### Get Bookings by Status
- **GET** `/api/bookings/status/{status}`
- **Example**: `/api/bookings/status/PENDING`
- **Valid statuses**: PENDING, CONFIRMED, CANCELLED, COMPLETED

### Get Bookings with Pagination
- **GET** `/api/bookings/paginated?page=0&size=10&sortBy=bookingId&sortDirection=DESC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: bookingId)
  - `sortDirection`: ASC or DESC (default: DESC)

### Update Booking
- **PUT** `/api/bookings/{id}`
- **Body**: Same as Create Booking

### Delete Booking
- **DELETE** `/api/bookings/{id}`

---

## 3. Customer APIs (`/api/customers`)

### Create Customer
- **POST** `/api/customers`
- **Body**:
```json
{
  "custName": "Jane Smith",
  "custAddress": "KG 123 St, Kigali",
  "custPhone": "0781234567",
  "custEmail": "jane@example.com",
  "custNic": "1199512345678901",
  "custDrivingLicense": "RW1234567",
  "province": "KIGALI",
  "district": "GASABO"
}
```

### Get All Customers
- **GET** `/api/customers`

### Get Customer by Name
- **GET** `/api/customers/{name}`

### Get Customers by Province
- **GET** `/api/customers/province/{province}`
- **Example**: `/api/customers/province/KIGALI`

### Get Customers by District
- **GET** `/api/customers/district/{district}`
- **Example**: `/api/customers/district/GASABO`

### Get Customers with Pagination
- **GET** `/api/customers/paginated?page=0&size=10&sortBy=custName&sortDirection=ASC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: custName)
  - `sortDirection`: ASC or DESC (default: ASC)

### Update Customer
- **PUT** `/api/customers/{name}`
- **Body**: Same as Create Customer

### Delete Customer
- **DELETE** `/api/customers/{name}`

### Find Customers by Province (Custom Query)
- **GET** `/api/customers/province/query/{province}`

---

## 4. Location APIs (`/api/locations`)

### Create Location (with optional parent)
- **POST** `/api/locations`
- **Body (Root Location)**:
```json
{
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL"
}
```
- **Body (Child Location with Parent)**:
```json
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

### Get All Locations
- **GET** `/api/locations`

### Get Location by ID
- **GET** `/api/locations/{id}`

### Get Locations by Province
- **GET** `/api/locations/province/{province}`
- **Example**: `/api/locations/province/KIGALI`

### Get Locations by District
- **GET** `/api/locations/district/{district}`
- **Example**: `/api/locations/district/GASABO`

### Get Root Locations (No Parent)
- **GET** `/api/locations/roots`
- Returns all locations that don't have a parent location

### Get Child Locations
- **GET** `/api/locations/{id}/children`
- Returns all child locations for a specific parent location

### Get Locations with Pagination and Sorting
- **GET** `/api/locations/paginated?page=0&size=10&sortBy=locationId&sortDirection=ASC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: locationId) - Options: locationId, province, district, sector, cell, village
  - `sortDirection`: ASC or DESC (default: ASC)

### Update Location
- **PUT** `/api/locations/{id}`
- **Body**: Same as Create Location (can update parent relationship)

### Delete Location
- **DELETE** `/api/locations/{id}`
- Note: Deleting a parent location will cascade delete all child locations

---

## 5. Car Management APIs (`/api/cars`)

### Create Car
- **POST** `/api/cars`
- **Body**:
```json
{
  "carPlatenum": "ABC-1234",
  "carBrand": "Toyota",
  "carModel": "Corolla",
  "carType": "SEDAN",
  "carYear": 2023,
  "carColor": "White",
  "carMileage": 15000,
  "carStatus": "AVAILABLE",
  "carPrice": 5000.00
}
```

### Get All Cars
- **GET** `/api/cars`

### Get Car by Plate Number
- **GET** `/api/cars/{plateNum}`
- **Example**: `/api/cars/ABC-1234`

### Get Cars by Status
- **GET** `/api/cars/status/{status}`
- **Example**: `/api/cars/status/AVAILABLE`
- **Valid statuses**: AVAILABLE, RENTED, MAINTENANCE, OUT_OF_SERVICE

### Get Cars by Type
- **GET** `/api/cars/type/{type}`
- **Example**: `/api/cars/type/SEDAN`
- **Valid types**: SEDAN, SUV, HATCHBACK, VAN, LUXURY

### Get Available Cars by Type
- **GET** `/api/cars/available/{type}`

### Get Cars by Price Range
- **GET** `/api/cars/price?minPrice=3000&maxPrice=7000`

### Get Cars with Pagination
- **GET** `/api/cars/paginated?page=0&size=10&sortBy=carPlatenum&sortDirection=ASC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: carPlatenum)
  - `sortDirection`: ASC or DESC (default: ASC)

### Update Car
- **PUT** `/api/cars/{plateNum}`
- **Body**: Same as Create Car

### Delete Car
- **DELETE** `/api/cars/{plateNum}`

---

## 6. Payment APIs (`/api/payments`)

### Create Payment
- **POST** `/api/payments`
- **Body**:
```json
{
  "paymentAmount": 15000.00,
  "paymentDate": "2026-03-15",
  "paymentMethod": "CREDIT_CARD",
  "paymentStatus": "COMPLETED",
  "booking": { "bookingId": 1 }
}
```

### Get All Payments
- **GET** `/api/payments`

### Get Payment by ID
- **GET** `/api/payments/{id}`

### Get Payments by Status
- **GET** `/api/payments/status/{status}`
- **Example**: `/api/payments/status/COMPLETED`
- **Valid statuses**: PENDING, COMPLETED, FAILED, REFUNDED

### Get Payments with Pagination
- **GET** `/api/payments/paginated?page=0&size=10&sortBy=paymentDate&sortDirection=DESC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: paymentDate)
  - `sortDirection`: ASC or DESC (default: DESC)

### Update Payment
- **PUT** `/api/payments/{id}`
- **Body**: Same as Create Payment

### Delete Payment
- **DELETE** `/api/payments/{id}`

---

## 7. Rent APIs (`/api/rents`)

### Create Rent
- **POST** `/api/rents`
- **Body**:
```json
{
  "rentDate": "2026-03-15",
  "returnDate": "2026-03-20",
  "totalAmount": 25000.00,
  "booking": { "bookingId": 1 }
}
```

### Get All Rents
- **GET** `/api/rents`

### Get Rent by ID
- **GET** `/api/rents/{id}`

### Get Active Rents
- **GET** `/api/rents/active`

### Get Rents with Pagination
- **GET** `/api/rents/paginated?page=0&size=10&sortBy=rentDate&sortDirection=DESC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: rentDate)
  - `sortDirection`: ASC or DESC (default: DESC)

### Update Rent
- **PUT** `/api/rents/{id}`
- **Body**: Same as Create Rent

### Delete Rent
- **DELETE** `/api/rents/{id}`

---

## 8. Return APIs (`/api/returns`)

### Create Return
- **POST** `/api/returns`
- **Body**:
```json
{
  "returnDate": "2026-03-20",
  "delayDays": 0,
  "additionalCharges": 0.00,
  "rent": { "rentId": 1 }
}
```

### Get All Returns
- **GET** `/api/returns`

### Get Return by ID
- **GET** `/api/returns/{id}`

### Get Returns with Delay
- **GET** `/api/returns/delayed`

### Get Returns with Pagination
- **GET** `/api/returns/paginated?page=0&size=10&sortBy=returnDate&sortDirection=DESC`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Items per page (default: 10)
  - `sortBy`: Field to sort by (default: returnDate)
  - `sortDirection`: ASC or DESC (default: DESC)

### Update Return
- **PUT** `/api/returns/{id}`
- **Body**: Same as Create Return

### Delete Return
- **DELETE** `/api/returns/{id}`

---

## Notes for Postman Testing

1. **Content-Type**: Set header `Content-Type: application/json` for all POST/PUT requests
2. **Date Format**: Use ISO format `YYYY-MM-DD` for dates
3. **Enums**: Use exact values as shown (case-sensitive)
4. **Relationships**: When creating entities with relationships, reference existing IDs
5. **Port**: Default Spring Boot port is 8080, adjust if configured differently

## Rwanda Location Enums

### Provinces
- KIGALI
- EASTERN
- WESTERN
- NORTHERN
- SOUTHERN

### Districts (by Province)

**Kigali City:**
- GASABO, KICUKIRO, NYARUGENGE

**Eastern Province:**
- BUGESERA, GATSIBO, KAYONZA, KIREHE, NGOMA, NYAGATARE, RWAMAGANA

**Northern Province:**
- BURERA, GAKENKE, GICUMBI, MUSANZE, RULINDO

**Southern Province:**
- GISAGARA, HUYE, KAMONYI, MUHANGA, NYAMAGABE, NYANZA, NYARUGURU, RUHANGO

**Western Province:**
- KARONGI, NGORORERO, NYABIHU, NYAMASHEKE, RUBAVU, RUSIZI, RUTSIRO
