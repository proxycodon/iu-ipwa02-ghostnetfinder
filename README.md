# Ghost Net Finder

**Projekt zum Modul IPWA02 an der IU. Die (fiktive) Non-Profit Organisation „Shea Sepherd“ hat eine Web-App zum Melden und Bergen sog. Geisternetze ins Leben gerufen.**

---

## 1. Requirements

The following software must be installed on the system:

- Java 21
- Git
- The Maven Wrapper included in the project (`mvnw` / `mvnw.cmd`)

Verify installation:

```bash
java -version
git --version
````

---

## 2. Cloning the Repository

```bash
git clone https://github.com/proxycodon/iu-ipwa02-ghostnetfinder.git
cd iu-ipwa02-ghostnetfinder
```

---

## 3. Running the Application

### Linux

Install Java:

```bash
sudo apt install openjdk-21-jdk
```

Make Maven wrapper executable:

```bash
chmod +x mvnw
```

Start the application:

```bash
./mvnw spring-boot:run
```

Open:

```
http://localhost:8080/
```

---

### Windows

Ensure Java 21 is installed.

Start the application:

```
mvnw.cmd spring-boot:run
```

Or using PowerShell:

```
.\mvnw spring-boot:run
```

Open:

```
http://localhost:8080/
```

---

### macOS

Install required tools:

```bash
brew install openjdk@21
brew install git
```

Ensure Java is available in PATH:

```bash
export PATH="/usr/local/opt/openjdk@21/bin:$PATH"
```

Start the application:

```bash
./mvnw spring-boot:run
```

Open:

```
http://localhost:8080/
```

---

## 4. Users and Authentication

- **No default login accounts are preconfigured.**
- Users can register themselves via the application UI.
- Passwords are securely hashed using BCrypt.
- Anonymous reporting of ghost nets is supported.

For demonstration and reference purposes, **non-login reference users** are seeded in the database and used only to populate example ghost-net data. These accounts are **disabled and cannot be used for authentication**.

---

## 5. Database Information

- The application uses an **H2 file-based database** in PostgreSQL compatibility mode.
- The database is stored locally under:

```
./.localdb/ghostnet
```

- The schema is managed via **Flyway migrations**.
- On a fresh setup, the database is initialized with:
- the full schema
- deterministic reference users
- example ghost-net data

To reset the local database during development, stop the application and delete:

```
./.localdb/ghostnet
```

Then restart the application.

---

## 6. Stopping the Application

Stop the running application with:

```
CTRL + C
```

inside the terminal running the Spring Boot process.
