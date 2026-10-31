# Fuel Management System Backend

Backend for Turatsinze Enock's Web Technology and Internet mid-term project.

- **Student:** Turatsinze Enock
- **Student ID:** 26909
- **Project:** Fuel Management System
- **Repository name:** `fuel-management-system-backend`

The API records drivers, pump attendants, fuel types, pumps, fuel issues, and fuel receipts. It uses the entity names, endpoint paths, query styles, and business rules in the examination document.

## Technologies

- Java 21
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven

## Database setup

Install and start PostgreSQL, then create an empty database:

```sql
CREATE DATABASE fuel_management_system;
```

Copy `.env.example` to `.env` as a reference, then set the values in your terminal. Spring Boot reads environment variables; it does not load a `.env` file automatically.

PowerShell example:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/fuel_management_system"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "your-local-postgres-password"
```

The application uses `spring.jpa.hibernate.ddl-auto=update` to create and update the tables. SQL statements are shown in the console.

## Run the backend

Use Java 21 and Maven:

```powershell
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

The application creates the examination fixture once when all six tables are empty. It contains four fuel types (Petrol, Diesel, Kerosene, LPG), four pumps, three attendants, five drivers, ten issues dated in October and November 2026, and four receipts. Pump 4 is free, LPG is unused, two drivers are named Habimana, and one driver has three issues. The fixture includes the required two issues for the same attendant on October 20, one of which is declined.

The required historical October 20 fixture cannot be created through the safe-save endpoint on a later date because that endpoint must reject past issues. The startup fixture preserves the required historical demo data; new API submissions still follow the past-date validation.

## Test with Postman

Import `fuel-management-system-midterm.postman_collection.json` into Postman. Set `baseUrl` to `http://localhost:8080`, start the application, and run the collection from top to bottom. Its initial requests read the fixture IDs and save them as collection variables.

The collection has a success and relevant error request for the business rules. In particular, it checks 400 responses for past issues, illegal status changes, and receipts for non-dispensed issues; 404 responses for missing records; and 409 responses for an occupied attendant date, duplicate attendant fuel type, occupied pump, and issue deletion with receipts.

## Implemented examination requirements

### Part A — domain model and CRUD

- **A1:** Five required entities, UUID keys, `FuelIssueStatus`, required relationships, and JSON loop prevention.
- **A2:** Save, list, find-by-ID, update, and delete endpoints for the required entities.

### Part B — derived queries

- **B1:** Case-insensitive driver last-name search, ordered by first name.
- **B2:** Fuel issues by status, ordered by issue date.
- **B3:** Inclusive issue date range, ordered by issue date.

### Part C — JPQL queries

- **C1:** Pump attendants by fuel type using a collection join and case-insensitive match.
- **C2:** Free pumps using a `NOT EXISTS` subquery.
- **C3:** Unused fuel types using `IS EMPTY`.
- **C4:** Distinct drivers who have issues with a selected attendant.

### Part D — business rules

- **D1:** Rejects past dates, prevents an attendant from getting another non-declined issue on the same date, and starts every new issue as `REQUESTED`.
- **D2:** Allows only the required status transitions; the general `PUT` update preserves status.
- **D3:** Adds a fuel type without removing the attendant's existing types.
- **D4:** Reassigns an attendant to a pump and rejects an occupied pump.

### Part E — fuel receipts

- **E1:** Creates a receipt only for a dispensed issue and sets the receipt date.
- **E2:** Lists a driver's receipt history newest first.
- **E3:** Deletes receipts and protects issues that still have receipts.

### Part F — reporting and bulk update

- **F1:** Groups fuel issues by status and returns their counts.
- **F2:** Declines issues for an attendant and date in one JPQL bulk update, except dispensed issues.

### Bonus

- **Dashboard:** Returns driver, attendant, free-pump, today's issue, requested issue, and receipt counts.

## Endpoint overview

All required and demonstration endpoints are in the Postman collection. Main paths include:

| Feature | Endpoint |
|---|---|
| Standard entity save/list/find | `POST /api/{resource}/save`, `GET /api/{resource}/all`, `GET /api/{resource}/{id}` |
| Drivers by last name | `GET /api/drivers/by-last-name?lastName=Habimana` |
| Issues by status | `GET /api/fuel-issues/by-status?status=REQUESTED` |
| Issues by date range | `GET /api/fuel-issues/between?start=2026-10-01&end=2026-10-31` |
| Attendants by fuel type | `GET /api/pump-attendants/by-fuel-type?name=Diesel` |
| Free pumps / unused fuel | `GET /api/pumps/free`, `GET /api/fuel-types/unused` |
| Drivers of an attendant | `GET /api/drivers/of-pump-attendant/{pumpAttendantId}` |
| Status change / cancel day | `PATCH /api/fuel-issues/{id}/status?status=APPROVED`, `PATCH /api/fuel-issues/cancel-day?pumpAttendantId={id}&date=2026-10-20` |
| Attendant changes | `PUT /api/pump-attendants/{pumpAttendantId}/fuel-types/{fuelTypeId}`, `PATCH /api/pump-attendants/{pumpAttendantId}/pump?pumpNumber=4` |
| Receipts | `POST /api/fuel-receipts/save?fuelIssueId={id}`, `GET /api/fuel-receipts/driver/{licenseNumber}`, `DELETE /api/fuel-receipts/delete/{id}` |
| Reports and dashboard | `GET /api/fuel-issues/stats/by-status`, `GET /api/dashboard/summary` |

Entity relationships are supplied as nested JSON objects that include the related entity ID, for example `"driver": {"id": "..."}` and `"attendant": {"id": "..."}`. A pump attendant's fuel types use `"fuelTypes": [{"id": "..."}]`.

## Main classes in simple words

- **Entities:** `Driver`, `PumpAttendant`, `FuelType`, `Pump`, `FuelIssue`, and `FuelReceipt` describe the saved database records.
- **Repositories:** Each repository reads and writes its entity. Repository method names make the derived queries; `@Query` contains the required JPQL queries.
- **Services:** Services apply the rules, call repositories, and return a list, optional record, or message.
- **Controllers:** Controllers provide the HTTP paths and turn service results into the required response status.
- **`SeedDataLoader`:** Adds the sample records when the database has no records, so the examination queries have data to demonstrate.
- **`DashboardService`:** Builds the bonus count summary.

## GitHub

The project is prepared for a repository named `fuel-management-system-backend`. No repository URL is included because the project has not been pushed to GitHub.
