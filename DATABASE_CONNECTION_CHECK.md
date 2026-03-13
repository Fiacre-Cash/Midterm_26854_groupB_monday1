# Database Connection Check

## Current Configuration

Your application is configured to connect to PostgreSQL:

### Database Details
- **Database Type**: PostgreSQL
- **Host**: localhost
- **Port**: 5432
- **Database Name**: car_rental_system
- **Username**: postgres
- **Password**: Fiacre.1

### Configuration File
Location: `backend/src/main/resources/application.properties`

```properties
spring.datasource.username=postgres
spring.datasource.password=Fiacre.1
spring.datasource.url=jdbc:postgresql://localhost:5432/car_rental_system
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## How to Verify Connection

### Option 1: Check if PostgreSQL is Running

Run this command in your terminal:

**Windows (PowerShell):**
```powershell
Get-Service -Name postgresql*
```

**Or check if port 5432 is listening:**
```powershell
netstat -an | findstr :5432
```

### Option 2: Test Database Connection with psql

```bash
psql -U postgres -h localhost -p 5432 -d car_rental_system
```

If the database doesn't exist, create it:
```bash
psql -U postgres -h localhost -p 5432
CREATE DATABASE car_rental_system;
\q
```

### Option 3: Start the Spring Boot Application

Navigate to the backend folder and run:

```bash
cd backend
./mvnw spring-boot:run
```

**Or on Windows:**
```bash
cd backend
mvnw.cmd spring-boot:run
```

### Option 4: Check Application Logs

When you start the application, look for these log messages:

✅ **Successful Connection:**
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
Hibernate: ...
Started CarRentalManagementSystemApplication in X.XXX seconds
```

❌ **Connection Failed:**
```
Failed to configure a DataSource
Connection refused
PSQLException: FATAL: database "car_rental_system" does not exist
```

## Common Issues and Solutions

### Issue 1: PostgreSQL Not Running
**Solution:**
- Start PostgreSQL service
- Windows: `net start postgresql-x64-XX` (check your version)
- Or use pgAdmin to start the service

### Issue 2: Database Doesn't Exist
**Solution:**
```sql
CREATE DATABASE car_rental_system;
```

### Issue 3: Wrong Password
**Solution:**
- Update password in `application.properties`
- Or reset PostgreSQL password

### Issue 4: Port Already in Use (8080)
**Solution:**
- Change port in `application.properties`:
```properties
server.port=8081
```

### Issue 5: PostgreSQL Not on localhost
**Solution:**
- Update the URL in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://YOUR_HOST:5432/car_rental_system
```

## Testing the Connection

### Step 1: Verify PostgreSQL Service
```powershell
# Check if PostgreSQL is running
Get-Service -Name postgresql*
```

### Step 2: Create Database (if needed)
```bash
psql -U postgres
CREATE DATABASE car_rental_system;
\l  # List all databases to verify
\q  # Quit
```

### Step 3: Start Spring Boot Application
```bash
cd backend
./mvnw spring-boot:run
```

### Step 4: Test API Endpoint
Once the application starts, test with:
```bash
curl http://localhost:8080/api/locations
```

Or open in browser:
```
http://localhost:8080/api/locations
```

## Expected Database Tables

When the application starts successfully with `spring.jpa.hibernate.ddl-auto=update`, it will automatically create these tables:

1. **admins** - Admin users
2. **bookings** - Car bookings
3. **customers** - Customer information
4. **locations** - Location hierarchy (with parent_location_id for relationships)
5. **manage_car** - Car inventory
6. **payments** - Payment records
7. **rents** - Rental records
8. **returns** - Return records

## Verify Tables Created

Connect to PostgreSQL and check:
```sql
psql -U postgres -d car_rental_system

\dt  -- List all tables

-- Check location table structure (should have parent_location_id column)
\d locations
```

## Application Health Check

Once running, check these endpoints:

1. **Health Check**: `http://localhost:8080/actuator/health` (if actuator is enabled)
2. **Get All Locations**: `http://localhost:8080/api/locations`
3. **Get All Cars**: `http://localhost:8080/api/cars`

## Next Steps After Successful Connection

1. Use Postman to test the APIs (see `POSTMAN_EXAMPLES_RWANDA.md`)
2. Create sample data using POST requests
3. Test the location hierarchy feature
4. Verify pagination and sorting work correctly

## Troubleshooting Commands

```bash
# Check Java version (should be 17+)
java -version

# Check Maven
./mvnw -version

# Clean and rebuild
./mvnw clean install

# Run with debug logging
./mvnw spring-boot:run -Dspring-boot.run.arguments=--logging.level.org.springframework=DEBUG
```
