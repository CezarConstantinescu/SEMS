<!-- .github/copilot-instructions.md - guidance for AI coding agents working on SEMS -->
# SEMS — Copilot Instructions

Purpose: quick, actionable guidance for an AI agent to be immediately productive in this repository.

- **Entry point / demo:** `src/main/java/com/sems/demo/SEMSDemo.java` — run this to see end-to-end behavior and representative JPA queries.
- **Build / run:** use Maven in this repo root. Common commands:
  - `mvn compile`
  - `mvn test`
  - `mvn package`
  - `mvn exec:java -Dexec.mainClass="com.sems.demo.SEMSDemo"` (exec plugin configured in `pom.xml`)
  - Alternatively: `java -cp target/social-events-management-system-1.0-SNAPSHOT.jar com.sems.demo.SEMSDemo`

- **Persistence / DB:** `src/main/resources/META-INF/persistence.xml` defines persistence unit `SEMS-PU`.
  - Uses H2 in-memory DB with `hibernate.hbm2ddl.auto = create-drop` and `DB_CLOSE_DELAY=-1`.
  - Because entities are explicitly declared in `persistence.xml`, adding a new `@Entity` class requires adding its fully-qualified name to that file.

- **Entity manager utility:** `src/main/java/com/sems/persistence/EntityManagerUtil.java` exposes:
  - `createEntityManager()` — use in a try/finally and `close()` the manager.
  - `EntityManagerUtil.close()` — call at application shutdown to close the factory.

- **Domain model & important patterns:** all model classes are under `src/main/java/com/sems/model`.
  - `Event` is an abstract base class using `@Inheritance(strategy = InheritanceType.JOINED)`; concrete subclasses: `Concert`, `Wedding` (separate tables).
  - Bidirectional relationships use helper methods (e.g., `Venue.addAttendee`, `User.addTicket`, `Event.addTicket`) — prefer these helpers to keep both sides in sync.
  - Relations default to `FetchType.LAZY` (e.g., `@ManyToOne(fetch = FetchType.LAZY)`), and the demo uses `JOIN FETCH` in JPQL when eager data is required. Follow that pattern to avoid N+1 problems.
  - Cascades: many `@OneToMany` use `cascade = CascadeType.ALL` and `orphanRemoval = true` where appropriate; be mindful when persisting children vs parents.

- **Querying patterns:** the demo shows canonical queries to copy:
  - `SELECT e FROM Event e JOIN FETCH e.venue ORDER BY e.startDateTime` — fetch events and venues.
  - `SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.tickets t LEFT JOIN FETCH t.event` — fetch users with tickets and their events.
  - Use `TypedQuery<T>` with explicit types.

- **Templates / UI:** static Bootstrap templates live under `src/main/resources/templates` (`index.html`, `events.html`, `venues.html`, `tickets.html`). They are static HTML samples — there is no web framework wiring in this repo.

- **Tests:** unit/integration tests live under `test/java/com/sems/model` and use JUnit 5. Run with `mvn test`.

- **Logging / SQL visibility:** `persistence.xml` enables `hibernate.show_sql`, `format_sql`, and SQL comments. Expect verbose SQL in stdout during runs.

- **Common gotchas for changes:**
  - If you add entity classes, update `persistence.xml` to include them.
  - The DB is recreated on each run. To reproduce persistent state across runs you must change `hibernate.hbm2ddl.auto` and the JDBC URL.
  - Because IDs use `GenerationType.IDENTITY`, when writing tests that assert IDs, persist explicitly and flush/refresh as needed.

- **Where to look first when debugging:**
  - `SEMSDemo.java` — reproduces typical flows (persisting venues/events/users/tickets and reading them back).
  - `EntityManagerUtil.java` and `persistence.xml` — for any JPA/connection issues.
  - Model classes under `com.sems.model` — check relationships, helper methods, and annotations when queries misbehave.

Examples (copyable snippets):

1) Create an `EntityManager` and transaction pattern used throughout:
```java
EntityManager em = EntityManagerUtil.createEntityManager();
EntityTransaction tx = em.getTransaction();
try {
  tx.begin();
  // persist stuff
  tx.commit();
} finally {
  if (em.isOpen()) em.close();
}
```

2) Safe read query that avoids N+1:
```java
TypedQuery<Event> q = em.createQuery(
  "SELECT e FROM Event e JOIN FETCH e.venue ORDER BY e.startDateTime",
  Event.class
);
List<Event> events = q.getResultList();
```

If anything in these instructions is unclear or you want more detail on a particular area (tests, adding entities, query patterns), say which section and I will expand or adjust the file.
