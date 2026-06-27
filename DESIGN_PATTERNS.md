# Design Patterns & Principles — Implementation Guide

This document maps every **design pattern** and **clean-code / SOLID principle**
taught in class to the exact place it is used in this project. Use it as a checklist
when grading.

All pattern classes live under:
`src/main/java/com/ebookmanagement/pattern/`

---

## 1. Design Patterns

### 1.1 Singleton
**Where:** `pattern/singleton/AuditLogger.java`

A single shared audit log for the whole application. Enforced with a private
constructor, a private static instance, and a synchronized `getInstance()`.

**Used by:**
- `pattern/command/CollectionCommandInvoker.java` (logs every command)
- `pattern/facade/BookManagementFacade.java` (logs add/update/delete/import)

Every log line is also printed to the console prefixed with `[AUDIT]`, so you can
see the single instance working at runtime.

---

### 1.2 Simple Factory
**Where:** `pattern/factory/UserFactory.java`

Centralizes the creation of `User` objects (`createReader`, `createAdmin`) so the
construction logic is not duplicated.

**Used by:**
- `service/impl/UserServiceImpl.java` → `register(...)` calls `UserFactory.createReader`
- `config/DataInitializer.java` → seeds the admin via `UserFactory.createAdmin`

---

### 1.3 Prototype
**Where:**
- `pattern/prototype/BookTemplate.java` (implements `Cloneable`, has `copy()`)
- `pattern/prototype/BookTemplateRegistry.java` (holds master copies, hands back clones)

Lets an admin start a new book by **cloning** a ready-made master template instead
of retyping common fields.

**Used by:**
- `controller/AdminBookController.java` → `addFromTemplate(...)`
  (route `GET /admin/books/add-from-template/{key}`)
- UI buttons on `templates/admin/books.html` ("Quick add from a template")

---

### 1.4 Command
**Where:**
- `pattern/command/Command.java` (interface: `execute`, `undo`, `describe`)
- `pattern/command/AddToCollectionCommand.java`
- `pattern/command/RemoveFromCollectionCommand.java`
- `pattern/command/CollectionCommandInvoker.java` (runs commands, keeps undo history)

Adding/removing a book to a personal collection is wrapped as a command object.
The invoker keeps a history stack and supports **undo**.

- **Invoker:** `CollectionCommandInvoker`
- **Command:** `AddToCollectionCommand`, `RemoveFromCollectionCommand`
- **Receiver:** `CollectionService` (does the real work)

**Used by:**
- `controller/UserController.java` → add/remove/undo endpoints
- UI "Undo last change" button on `templates/user/collection.html`
  (route `POST /collection/undo`)

---

### 1.5 Adapter
**Where:**
- `pattern/adapter/LegacyBookRecord.java` (the incompatible "adaptee":
  uses `bookName`, `writer`, price in **cents**)
- `pattern/adapter/LegacyBookAdapter.java` (translates it into our `BookDto`)

Lets us import a book that arrives in an old external format without changing either
the legacy class or our `BookDto`.

**Used by:**
- `pattern/facade/BookManagementFacade.java` → `importLegacyBook(...)`
- `controller/AdminBookController.java` → `importLegacySample()`
  (route `POST /admin/books/import-legacy`)
- UI button on `templates/admin/books.html` ("Import legacy sample book")

---

### 1.6 Facade
**Where:** `pattern/facade/BookManagementFacade.java`

One simple entry point for admin book operations (add/update/delete/import + reads).
It hides the `BookService`, `CategoryService`, the `AuditLogger` singleton, and the
`LegacyBookAdapter` behind a single, easy interface.

**Used by:**
- `controller/AdminBookController.java` (the controller talks to the facade only,
  which keeps the controller thin)

---

## 2. SOLID Principles

- **S — Single Responsibility:** Each layer has one job. Controllers handle web
  requests, services hold business logic, repositories handle data, the
  `UserFactory` only builds objects (it does NOT encode passwords — the service
  does that). See `service/impl/UserServiceImpl.java` + `pattern/factory/UserFactory.java`.

- **O — Open/Closed:** New behavior is added by adding new classes, not editing old
  ones. New collection actions = new `Command` implementations; new book sources =
  new adapters. Nothing existing needs to change.

- **L — Liskov Substitution:** Every `Command` implementation
  (`AddToCollectionCommand`, `RemoveFromCollectionCommand`) is fully usable wherever
  a `Command` is expected; the invoker never checks the concrete type.

- **I — Interface Segregation:** Services are split into small, focused interfaces
  (`BookService`, `CategoryService`, `CollectionService`, `UserService`,
  `FileStorageService`) instead of one giant interface.

- **D — Dependency Inversion:** Controllers and services depend on **interfaces**,
  not concrete classes. Spring injects the implementations via constructors. See
  any `service/*.java` interface and its `service/impl/*.java` implementation.

---

## 3. Clean-Code Principles

- **DRY (Don't Repeat Yourself):** Object creation is centralized in `UserFactory`;
  book CRUD is centralized behind `BookManagementFacade`; shared book-building logic
  lives in one private method `applyDtoToBook(...)` in `BookServiceImpl`.

- **KISS / readable code:** Straightforward loops and methods, minimal cleverness.

- **YAGNI:** Only the required features are implemented; optional Cart/Order entities
  from the brief were intentionally left out because they are not needed.

- **No Magic Numbers:** Named constants instead of bare numbers, e.g.
  `MIN_PASSWORD_LENGTH` in `dto/UserRegistrationDto.java` and `CENTS_PER_DOLLAR`
  in `pattern/adapter/LegacyBookAdapter.java`.

- **Guard Clauses (avoid deep nesting):** Early returns instead of nested `if`s, e.g.
  `search(...)` in `service/impl/BookServiceImpl.java` and `getClone(...)` in
  `BookTemplateRegistry.java`.

- **Meaningful Names:** Classes and methods are named for what they do
  (`AddToCollectionCommand`, `importLegacyBook`, `BookManagementFacade`).

- **Self-documenting code + targeted comments:** Comments explain *why* (and label
  the pattern), not trivial *what*.

---

## 4. Quick Demo Script (to show patterns at runtime)

1. Run the app, log in as admin (`admin@ebook.com` / `admin123`).
2. Go to **Manage Books**:
   - Click a **template key** button → the add-book form opens pre-filled
     (**Prototype**).
   - Click **Import legacy sample book** → a book is imported via the
     **Adapter**, and `[AUDIT]` appears in the console (**Singleton** + **Facade**).
   - Add/update/delete any book → more `[AUDIT]` log lines (**Facade** + **Singleton**).
3. Register/log in as a normal user, open a book, **Add to Collection**, then go to
   **My Collection** and click **Undo last change** (**Command** with undo).
4. Watch the console: every action is logged by the single `AuditLogger` instance.
