# Delivery Project - README

## Overview
This project implements a delivery management system as outlined in the provided kata. The system enables clients to select delivery modes, reserve delivery time slots, and ensures data integrity using modern architectural principles and tools.

### Implemented Features
#### Functional Features
1. **Consult Delivery Modes and Time Slots**
   - Clients can view available delivery modes: DRIVE, DELIVERY, DELIVERY_TODAY, DELIVERY_ASAP.
   - Delivery time slots are displayed based on the selected mode.

2. **Reserve Delivery Time Slot**
   - Clients can reserve specific time slots.
   - Features include:
     - Marking a slot as reserved.
     - Setting order delivery status to "SCHEDULED."
     - Publishing a delivery event when a slot is reserved.

#### Technical Features
- **Spring Boot 3.4.1** and **Java 21** for robust and modern application development.
- **Kafka Producer** for event-driven architecture to publish delivery events.
- **MongoDB** for persistent data storage.
- **Test-Driven Development (TDD)** for unit testing.
- **Hexagonal Design** for clean and maintainable architecture.
- **Docker Compose** to simplify project environment setup.
- **Dockerfile** to facilitate containerization and future deployments.

## Architecture
The project follows a hexagonal architecture to ensure modularity and separation of concerns. Key layers include:

1. **Domain Layer**
   - Core business logic for delivery and slot management.

2. **Application Layer**
   - Services for orchestrating operations between domain and infrastructure layers.

3. **Infrastructure Layer**
   - Handles data persistence (MongoDB) and event streaming (Kafka).

## How to Run the Project
### Prerequisites
- Java 21
- Maven
- Docker & Docker Compose

### Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd delivery-project
   ```
2. Start the environment using Docker Compose:
   ```bash
   docker-compose up -d
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Testing
Unit tests are written following TDD principles. To execute tests:
```bash
mvn test
```

## API Endpoints
### Delivery Modes
- **GET** `/delivery-modes`
  - Returns available delivery modes.

### Delivery Time Slots
- **GET** `/time-slots/{mode}`
  - Returns available time slots for a given mode.

### Reserve Time Slot
- **POST** `/time-slots/reserve`
  - Reserves a delivery time slot.
  - Request body:
    ```json
    {
      "slotId": "<slot-id>",
      "orderId": "<order-id>"
    }
    ```

## Future Improvements
- Add security layers to the API.
- Introduce CI/CD pipelines.
- Create containerized deployment using Docker and Kubernetes.
- Extend testing to include End-to-End (E2E) tests.

## Notes
- The project uses Kafka for publishing events related to delivery slots. Ensure Kafka is running before starting the application.
- MongoDB is used as the primary database for storing time slots and reservations.
- Docker Compose is now available to streamline environment setup.
- A Dockerfile has been added for creating containers for deployment.

## Contact
For questions or suggestions, feel free to reach out via the repository's issue tracker.

