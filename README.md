**Project Title: E-Commerce Auto Parts Platform**

Overview:
This project is a fully functional, full-stack e-commerce web application designed to showcase technical proficiency in the Java/Spring ecosystem and MVC architecture. It provides users with a seamless shopping experience for auto parts, from browsing the catalog to placing an order.
<img width="1899" height="834" alt="image" src="https://github.com/user-attachments/assets/c8696235-67cb-470e-94da-d6cd96867967" />

**Key Features**
 - Product Catalog Management: Allows users to browse a comprehensive list of auto parts.
 - Robust Filtering: Implemented advanced server-side logic to filter products by two critical criteria:
 - Car Model (e.g., Audi A3, Ford F-150) and Part category
<img width="1899" height="855" alt="image" src="https://github.com/user-attachments/assets/63b8a751-9390-4101-95a3-d91913464cad" />
<img width="1891" height="785" alt="image" src="https://github.com/user-attachments/assets/25ab922f-b8f6-4e6d-97e4-5d045a3667e3" />

 - Auto Part Name (e.g., Brake Pad, Oil Filter)
  <img width="1892" height="467" alt="image" src="https://github.com/user-attachments/assets/5eb93afb-5ce8-4206-b488-9d260876bf63" />

 - Shopping Cart Module: A critical feature for order management, allowing users to add, update, and remove items before checkout.
 - <img width="1868" height="761" alt="image" src="https://github.com/user-attachments/assets/f397e3df-54d5-404d-a66f-2ff9437696b5" />

 - MVC Architecture: Adherence to the Model-View-Controller design pattern for clear separation of concerns, enhancing maintainability and scalability.

**Technology Stack**
Back-End / Application Logic	Java, Spring Framework (using Spring MVC Controllers)
Database / Data Persistence	MySQL
Front-End / User Interface	HTML, CSS, JavaScript (for responsive and dynamic user experience)
Design Pattern	Model-View-Controller (MVC)

Getting Started
**Prerequisites**
 - To run this project locally, you will need:
 - Java Development Kit (JDK) [Specify version, e.g., 17+]
 - Apache Maven [Specify version, e.g., 3.8+]
 - MySQL Server
 - An IDE (e.g., IntelliJ IDEA, Eclipse)

**Installation**
Clone the Repository:
   _ git clone [Your Repository URL]
     cd [your-project-name] __

Database Setup:
 _ spring.datasource.url=jdbc:mysql://localhost:3306/[your_database_name]
  spring.datasource.username=[your_username]
  spring.datasource.password=[your_password] _

Run the Application:
  _ mvn clean install
    mvn spring-boot:run _
    
Access the Application: 
    Open your web browser and navigate to http://localhost:8080/ (or the port specified in your configuration)

