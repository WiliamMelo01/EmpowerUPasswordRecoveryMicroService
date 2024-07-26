# EmpowerU Recovery Password Microservice

## Overview

The EmpowerU Password Recovery Microservice provides endpoints for managing user password recovery. This service handles sending recovery codes to users and verifying these codes to update passwords. It is implemented as an Azure Function using Java with Spring Boot and integrates with a database to validate user information and store recovery codes.

## Features

- Updates user passwords securely in the database
- Sends a confirmation email upon successful password update

## Technologies

- `Azure Functions`
- `Java`
- `Spring Boot`
- `PostgreSQL`

## Setup

- Java 17 or higher
- Maven 3.6.0 or higher
- Azure Functions Core Tools
- Azure account for deployment
- SMTP server configuration (if using email notifications)
- Database setup (e.g., PostgreSQL)

### Installation
1. Clone the repository:

    ```bash
    git clone https://github.com/WiliamMelo01/EmpowerUPasswordRecoveryMicroService
    cd EmpowerUPasswordRecoveryMicroService
    ```

2. Set up the profiles in the `application.properties` file to use the `dev` profile:

    ```properties
    # application.properties
    # Define the active profile as 'dev'
    spring.profiles.active=dev
    ```

3. Update the `application-dev.properties` file with your specific configurations:

   ```properties
    # application-dev.properties
    # MAIL CONFIGS
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your-email@example.com
    spring.mail.password=your-email-password
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true

    # DATABASE CONFIGS
    spring.datasource.username=your-db-username
    spring.datasource.password=your-db-password
    spring.datasource.host=your-db-host
    spring.datasource.port=your-db-port
    spring.datasource.database-name=your-database-name
    spring.datasource.url=jdbc:postgresql://${spring.datasource.host}:${spring.datasource.port}/${spring.datasource.database-name}
    spring.datasource.driver-class-name=org.postgresql.Driver

    # HIBERNATE CONFIGS
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=none
    ```

4. Build the project:

    ```bash
    ./mvnw clean package
    ```

5. Run the Azure Function locally:

    ```bash
    func start
    ```
## Usage

To use the microservice:

1. **Send Recovery Code**

    Send a POST request to the `/sendRecoveryCode` endpoint with a JSON body containing the user's email:

    ```json
    {
      "email": "user@example.com"
    }
    ```

    The service will send a recovery code to the provided email if the email exists in the system.

2. **Verify Recovery Code**

    Send a POST request to the `/verifyRecoveryCode` endpoint with a JSON body containing the email, recovery code, and new password:

    ```json
    {
      "email": "user@example.com",
      "code": "recovery_code",
      "password": "new_password"
    }
    ```

    The service will verify the recovery code and update the password if the code is valid.

## TODO

### Planned Features

- [ ] **Rate Limiting**: Add rate limiting to prevent abuse of the password update endpoint.
- [ ] **Logging and Monitoring**: Implement logging and monitoring to track the usage and performance of the microservice.

## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/WiliamMelo01/EmpowerUPasswordRecoveryMicroService/blob/master/LICENSE) file for details.
