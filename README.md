## TIW-HTML Project Overview

The TIW-HTML repository contains a web application developed using Java, HTML, CSS, and JavaScript, created for the "Tecnologie Informatiche per il Web" course at Politecnico di Milano. Below is a detailed description of its structure and features for inclusion in the README:

### Key Features

1. **Backend Development**:
   - The backend is developed using Java Servlets.
   - JDBC is used for database interactions, specifically with a MySQL database.

2. **Frontend Development**:
   - HTML, CSS, and JavaScript are used to create a responsive and interactive user interface.
   - The front end includes static pages and form handling to interact with the backend.

3. **Project Structure**:
   - The `src/main` directory contains the Java source code.
   - Web content including HTML, CSS, and JavaScript files are placed in the `WebContent` directory.

4. **Build and Dependency Management**:
   - Managed using Maven, with dependencies defined in the `pom.xml` file.
   - Ensures consistent project builds and simplifies dependency management.

5. **Development Tools**:
   - Configured for use with IntelliJ IDEA, as indicated by the `.idea` directory.
   - `.gitignore` is used to manage untracked files and directories.

### Setup and Usage

1. **Clone the Repository**:
   ```sh
   git clone https://github.com/mouadhltifi/TIW-HTML.git
   cd TIW-HTML
   ```

2. **Database Setup**:
   - Import the provided database dump (if available) into your MySQL database.

3. **Build and Run the Application**:
   - Build the project using Maven:
     ```sh
     mvn clean install
     ```
   - Deploy the application on a Tomcat server:
     ```sh
     mvn tomcat7:run
     ```
   - Access the application at `http://localhost:8080`.

### Interface Overview

- **Login Page**: Allows users to log in or register.
- **Home Page**: Displays available products or services.
- **Product Pages**: Provide detailed information about individual products.
- **Cart and Checkout**: Manage user purchases and process orders.
