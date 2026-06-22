# Backend — Zarządzanie Wdrożeniami (IPB)

REST API wspierające proces biznesowy **Zarządzanie Wdrożeniami** (Release / Deployment Management).
Realizuje prototypy usług wymagane na ocenę 4.5 (kolekcje obiektów w JSON, filtrowanie, sortowanie)
oraz operacje zapisu odwzorowujące przypadki użycia (baza pod działającą aplikację — ocena 5.0).

## Stack

- Java 21, Spring Boot 3.3.5 (Web, Data JPA, Validation)
- Baza H2 in-memory (zero konfiguracji; dane ładowane przy starcie przez `DataSeeder`)
- MapStruct 1.6.3 — mapowanie encja → DTO (`web/dto/DomainMapper`, implementacja generowana w czasie kompilacji)
- springdoc-openapi (Swagger UI)
- Maven

## Uruchomienie

```bash
cd backend
mvn spring-boot:run
```

Aplikacja startuje na `http://localhost:8080`.

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Konsola H2:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:deployment`, user `sa`, brak hasła)

## Postman

Zaimportuj `postman/ZarzadzanieWdrozeniami.postman_collection.json`.
Zmienna `{{baseUrl}}` domyślnie wskazuje `http://localhost:8080`.
Kolekcja zawiera gotowe przykłady: pobranie kolekcji, filtrowanie po statusie, sortowanie oraz operacje zapisu.

## Obiekty domenowe i endpointy

Każdy obiekt udostępnia: pobranie kolekcji, filtrowanie (po statusie/typie itp.) i sortowanie
(`?sort=pole` lub `?sort=pole,desc`).

| Obiekt | Bazowy URL | Przykładowe filtry | Zapis (przypadki użycia) |
|--------|-----------|--------------------|--------------------------|
| Cykl wydawniczy | `/api/release-cycles` | `?status=QA_TESTING` | POST (utworzenie), PATCH `/{id}/status` (zamknięcie) |
| Zadanie ITS | `/api/tickets` | `?status=&type=&cycleId=` | POST, PATCH `/{id}/status` (zarządzanie statusem zadań) |
| Błąd / rework | `/api/bugs` | `?priority=&source=&status=` | POST `/api/tickets/{ticketId}/bugs`, PATCH `/{id}/status` |
| Build | `/api/builds` | `?cycleId=&staticAnalysisPassed=` | POST (zapis kodu w repozytorium) |
| Release Notes | `/api/release-notes` | `?viewType=&buildId=` | POST, PUT `/{id}` |
| Wdrożenie | `/api/deployments` | `?deploymentStatus=&uatStatus=` | POST, PATCH `/{id}/uat-status`, PATCH `/{id}/deployment-status` |
| Weryfikacja UAT | `/api/uat-verifications` | `?deploymentId=` | POST `/api/deployments/{id}/verifications` (rejestracja UAT) |

### Logika procesu

- Rejestracja weryfikacji UAT ze wszystkimi kryteriami `true` ustawia `uatStatus=ACCEPTED`
  i datę `approvedProdDate`; w przeciwnym razie `uatStatus=REJECTED`.
- Walidacja żądań (400) oraz spójna obsługa błędów 404 (`GlobalExceptionHandler`).

## Przykłady (curl)

```bash
# Kolekcja + sortowanie
curl "http://localhost:8080/api/release-cycles?sort=targetVersionTag"

# Filtr po statusie
curl "http://localhost:8080/api/tickets?status=TO_DO"

# Filtr po priorytecie
curl "http://localhost:8080/api/bugs?priority=CRITICAL"

# Utworzenie cyklu
curl -X POST http://localhost:8080/api/release-cycles \
  -H "Content-Type: application/json" \
  -d '{"targetVersionTag":"v1.6.0"}'

# Zmiana statusu zadania
curl -X PATCH "http://localhost:8080/api/tickets/ITS-002/status?value=READY_FOR_QA"
```

## Model danych

Odwzorowuje `docs/erd.sql` (encje + słowniki statusów jako enumy Java). Schemat generowany
automatycznie przez Hibernate (`ddl-auto=create-drop`), dane startowe w
`config/DataSeeder.java` (2–3 zestawy na obiekt).

Mapowanie encji na DTO realizuje MapStruct (interfejs `web/dto/DomainMapper`); klucze obce
spłaszczane są do identyfikatorów (np. `cycle.cycleId` → `cycleId`) z null-checkiem
generowanym automatycznie. Annotation processing współpracuje z Lombokiem dzięki
`lombok-mapstruct-binding` (konfiguracja w `pom.xml`).
