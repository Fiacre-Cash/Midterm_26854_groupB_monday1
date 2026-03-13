# Postman Examples - Rwanda Car Rental System

## Quick Start Examples Using Rwanda Locations

### 1. Create Location (Kigali - Gasabo - Bibare - Intashyo - BCL)

**POST** `http://localhost:8080/api/locations`

```json
{
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL"
}
```

### 2. Create Customer in Gasabo

**POST** `http://localhost:8080/api/customers`

```json
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
```

### 3. Create Admin

**POST** `http://localhost:8080/api/admins`

```json
{
  "adminName": "Mugisha Jean",
  "email": "mugisha@carrental.rw",
  "password": "SecurePass123!",
  "role": "SUPER_ADMIN"
}
```

### 4. Create Car

**POST** `http://localhost:8080/api/cars`

```json
{
  "carPlatenum": "RAD-123A",
  "carBrand": "Toyota",
  "carModel": "RAV4",
  "carType": "SUV",
  "carYear": 2023,
  "carColor": "White",
  "carMileage": 15000,
  "carStatus": "AVAILABLE",
  "carPrice": 50000.00
}
```

### 5. Create Booking

**POST** `http://localhost:8080/api/bookings`

```json
{
  "bookingDate": "2026-03-15",
  "status": "PENDING",
  "customer": {
    "custName": "Uwase Marie"
  },
  "car": {
    "carPlatenum": "RAD-123A"
  }
}
```

### 6. Create Rent

**POST** `http://localhost:8080/api/rents`

```json
{
  "rentDate": "2026-03-15",
  "returnDate": "2026-03-20",
  "totalAmount": 250000.00,
  "booking": {
    "bookingId": 1
  }
}
```

### 7. Create Payment

**POST** `http://localhost:8080/api/payments`

```json
{
  "paymentAmount": 250000.00,
  "paymentDate": "2026-03-15",
  "paymentMethod": "CREDIT_CARD",
  "paymentStatus": "COMPLETED",
  "booking": {
    "bookingId": 1
  }
}
```

### 8. Create Return

**POST** `http://localhost:8080/api/returns`

```json
{
  "returnDate": "2026-03-20",
  "delayDays": 0,
  "additionalCharges": 0.00,
  "rent": {
    "rentId": 1
  }
}
```

## Location Hierarchy Examples

### Create Parent Location (Kigali City)

**POST** `http://localhost:8080/api/locations`

```json
{
  "province": "KIGALI",
  "district": "GASABO",
  "sector": "Bibare",
  "cell": "Intashyo",
  "village": "BCL"
}
```

### Create Child Location (Sub-location under BCL)

**POST** `http://localhost:8080/api/locations`

```json
{
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

### Get Root Locations

**GET** `http://localhost:8080/api/locations/roots`

### Get Children of a Location

**GET** `http://localhost:8080/api/locations/1/children`

## Query Examples

### Get Customers in Gasabo District

**GET** `http://localhost:8080/api/customers/district/GASABO`

### Get Customers in Kigali Province

**GET** `http://localhost:8080/api/customers/province/KIGALI`

### Get Locations in Gasabo

**GET** `http://localhost:8080/api/locations/district/GASABO`

### Get Available SUVs

**GET** `http://localhost:8080/api/cars/available/SUV`

### Get Cars by Price Range

**GET** `http://localhost:8080/api/cars/price?minPrice=30000&maxPrice=70000`

### Get Pending Bookings

**GET** `http://localhost:8080/api/bookings/status/PENDING`

### Get Active Rents

**GET** `http://localhost:8080/api/rents/active`

### Get Delayed Returns

**GET** `http://localhost:8080/api/returns/delayed`

## Pagination Examples

### Get Customers with Pagination (Page 1, 10 items, sorted by name)

**GET** `http://localhost:8080/api/customers/paginated?page=0&size=10&sortBy=custName&sortDirection=ASC`

### Get Cars with Pagination (Page 1, 20 items, sorted by price descending)

**GET** `http://localhost:8080/api/cars/paginated?page=0&size=20&sortBy=carPrice&sortDirection=DESC`

### Get Bookings with Pagination (Latest first)

**GET** `http://localhost:8080/api/bookings/paginated?page=0&size=10&sortBy=bookingId&sortDirection=DESC`

### Get Locations with Pagination

**GET** `http://localhost:8080/api/locations/paginated?page=0&size=10&sortBy=locationId&sortDirection=ASC`

## Update Examples

### Update Customer

**PUT** `http://localhost:8080/api/customers/Uwase Marie`

```json
{
  "custName": "Uwase Marie",
  "custAddress": "KG 20 Ave, Bibare, Gasabo",
  "custPhone": "0788123456",
  "custEmail": "uwase.marie@example.com",
  "custNic": "1199012345678901",
  "custDrivingLicense": "RW2024001234",
  "province": "KIGALI",
  "district": "KICUKIRO"
}
```

### Update Car Status

**PUT** `http://localhost:8080/api/cars/RAD-123A`

```json
{
  "carPlatenum": "RAD-123A",
  "carBrand": "Toyota",
  "carModel": "RAV4",
  "carType": "SUV",
  "carYear": 2023,
  "carColor": "White",
  "carMileage": 16000,
  "carStatus": "RENTED",
  "carPrice": 50000.00
}
```

### Update Booking Status

**PUT** `http://localhost:8080/api/bookings/1`

```json
{
  "bookingDate": "2026-03-15",
  "status": "CONFIRMED",
  "customer": {
    "custName": "Uwase Marie"
  },
  "car": {
    "carPlatenum": "RAD-123A"
  }
}
```

## Delete Examples

### Delete Customer

**DELETE** `http://localhost:8080/api/customers/Uwase Marie`

### Delete Car

**DELETE** `http://localhost:8080/api/cars/RAD-123A`

### Delete Location

**DELETE** `http://localhost:8080/api/locations/1`

### Delete Booking

**DELETE** `http://localhost:8080/api/bookings/1`

## Rwanda Provinces & Districts Reference

### KIGALI
- GASABO
- KICUKIRO
- NYARUGENGE

### EASTERN
- BUGESERA
- GATSIBO
- KAYONZA
- KIREHE
- NGOMA
- NYAGATARE
- RWAMAGANA

### NORTHERN
- BURERA
- GAKENKE
- GICUMBI
- MUSANZE
- RULINDO

### SOUTHERN
- GISAGARA
- HUYE
- KAMONYI
- MUHANGA
- NYAMAGABE
- NYANZA
- NYARUGURU
- RUHANGO

### WESTERN
- KARONGI
- NGORORERO
- NYABIHU
- NYAMASHEKE
- RUBAVU
- RUSIZI
- RUTSIRO

## Enum Values Reference

### AdminRole
- SUPER_ADMIN
- ADMIN
- MANAGER

### BookingStatus
- PENDING
- CONFIRMED
- CANCELLED
- COMPLETED

### CarStatus
- AVAILABLE
- RENTED
- MAINTENANCE
- OUT_OF_SERVICE

### CarType
- SEDAN
- SUV
- HATCHBACK
- VAN
- LUXURY

### PaymentMethod
- CASH
- CREDIT_CARD
- DEBIT_CARD
- MOBILE_MONEY
- BANK_TRANSFER

### PaymentStatus
- PENDING
- COMPLETED
- FAILED
- REFUNDED
