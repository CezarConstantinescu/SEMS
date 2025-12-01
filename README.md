# SEMS - Social Events Management System

A Java 21 Maven project implementing a Social Events Management System using clean OOP principles, Jakarta Persistence (JPA), Hibernate ORM, and H2 in-memory database.

## Features

- **Clean OOP Design**: Proper inheritance hierarchy with abstract base class and concrete implementations
- **Jakarta Persistence (JPA)**: Entity management with Hibernate ORM
- **H2 In-Memory Database**: Fast, embedded database for development and testing
- **JOINED Inheritance Strategy**: Normalized database design for event hierarchy
- **Bootstrap Templates**: Responsive HTML templates for the user interface

## Project Structure

```
src/
├── main/
│   ├── java/com/sems/
│   │   ├── model/           # Domain model classes
│   │   │   ├── User.java
│   │   │   ├── Ticket.java
│   │   │   ├── TicketStatus.java
│   │   │   ├── Venue.java
│   │   │   ├── Event.java   # Abstract base class
│   │   │   ├── Concert.java
│   │   │   └── Wedding.java
│   │   ├── persistence/     # Persistence utilities
│   │   │   └── EntityManagerUtil.java
│   │   └── demo/            # Demo application
│   │       └── SEMSDemo.java
│   └── resources/
│       ├── META-INF/
│       │   └── persistence.xml
│       └── templates/       # Bootstrap HTML templates
│           ├── index.html
│           ├── events.html
│           ├── venues.html
│           └── tickets.html
└── test/
    └── java/com/sems/
```

## Domain Model

### Inheritance Hierarchy

```
Event (abstract, @Inheritance JOINED)
├── Concert (artist, genre, tourName)
└── Wedding (brideName, groomName, theme)
```

### Relationships

- **User** has many **Tickets** (One-to-Many)
- **Venue** has many **Events** (One-to-Many)
- **Venue** has many **Users** (Many-to-Many - attendees)
- **Event** has many **Tickets** (One-to-Many)
- **Ticket** belongs to a **User** and an **Event**

## Prerequisites

- Java 17 or later
- Maven 3.8+

## Building the Project

```bash
# Compile the project
mvn compile

# Package as JAR
mvn package

# Run tests
mvn test
```

## Running the Demo

```bash
# Using Maven exec plugin
mvn exec:java -Dexec.mainClass="com.sems.demo.SEMSDemo"

# Or run the packaged JAR
java -cp target/social-events-management-system-1.0-SNAPSHOT.jar com.sems.demo.SEMSDemo
```

## Example Usage

### Creating and Persisting Events

```java
EntityManager em = EntityManagerUtil.createEntityManager();
EntityTransaction tx = em.getTransaction();

try {
    tx.begin();
    
    // Create a venue
    Venue venue = new Venue("Grand Concert Hall", "123 Music Ave", 5000);
    em.persist(venue);
    
    // Create a concert
    Concert concert = new Concert(
        "Rock Night 2024",
        LocalDateTime.of(2024, 6, 15, 20, 0),
        LocalDateTime.of(2024, 6, 15, 23, 30),
        venue,
        "The Rolling Stones",
        "Rock"
    );
    em.persist(concert);
    
    // Create a wedding
    Wedding wedding = new Wedding(
        "Johnson-Smith Wedding",
        LocalDateTime.of(2024, 8, 10, 14, 0),
        LocalDateTime.of(2024, 8, 10, 23, 0),
        venue,
        "Emily Johnson",
        "Michael Smith"
    );
    em.persist(wedding);
    
    tx.commit();
} finally {
    em.close();
}
```

### Querying Events

```java
EntityManager em = EntityManagerUtil.createEntityManager();

// Query all events
TypedQuery<Event> query = em.createQuery(
    "SELECT e FROM Event e JOIN FETCH e.venue ORDER BY e.startDateTime",
    Event.class
);
List<Event> events = query.getResultList();

// Query specific event types
TypedQuery<Concert> concertQuery = em.createQuery(
    "SELECT c FROM Concert c WHERE c.genre = :genre",
    Concert.class
);
concertQuery.setParameter("genre", "Rock");
List<Concert> rockConcerts = concertQuery.getResultList();

em.close();
```

## Technologies Used

- **Java 17**: Modern Java LTS version
- **Maven**: Build and dependency management
- **Jakarta Persistence API 3.1**: Standard JPA specification
- **Hibernate ORM 6.4**: JPA implementation
- **H2 Database 2.2**: In-memory SQL database
- **Bootstrap 5.3**: CSS framework for templates
- **JUnit 5**: Testing framework

## Configuration

The `persistence.xml` file configures the JPA persistence unit:

```xml
<persistence-unit name="SEMS-PU" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <properties>
        <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:mem:semsdb"/>
        <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
        <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
    </properties>
</persistence-unit>
```

## License

This project is for educational purposes.
