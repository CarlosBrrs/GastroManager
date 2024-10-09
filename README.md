# GastroManager
The GastroManager is a restaurant management application that is designed to streamline and enhance the operational efficiency of restaurants. This web-based solution provides restaurant owners and staff with tools to manage inventory, place orders, and optimize kitchen operations seamlessly.

# Project Title:
Restaurant Management System GastroManager

# Problem Statement:
Managing restaurant operations like inventory control, menu updates, and sales reporting can be inefficient when handled manually. Inaccuracies in stock levels, difficulty tracking ingredient usage, and a lack of insightful reporting can lead to resource wastage and operational delays. This project aims to provide an integrated system to streamline these tasks, enhancing overall restaurant efficiency.

# Brief Overview of the Application’s Functionality:
The Restaurant Management System is a web-based application designed to manage restaurant operations such as inventory tracking, menu management, orders placing and sales reporting. It simplifies daily operations by automatically updating inventory levels when orders are placed and providing detailed performance reports. The system will also ensure secure access through role-based permissions with JWT authentication.

# Technology Stack:
- Frontend: Angular for a dynamic, user-friendly interface.
- Backend: Java and Spring Boot for server-side logic and REST API development.
- Database: PostgreSQL for efficient and scalable data management.
- Security: JWT (JSON Web Token) for user authentication and role-based access control.
- Containerization: Docker for creating consistent environments for development and deployment.
- Version Control: Git and GitHub for managing source code and version control.

# Features to Be Implemented:

- Inventory Management:
  - Automatically deduct ingredient quantities from inventory when a menu item is ordered.
  - Set alerts for low stock levels and generate inventory usage reports (not in first MVP).

- Menu Management:
  - Add, update, and remove menu items, including specifying ingredients, categories and prices.
  - Ensure that menu item availability is updated based on current inventory levels (not in first MVP).

- Orders Management:
  - Create order for the clients including the menu items and its quantities for each order, and automatically substract the quantities for each ingredient from the inventory.

- Sales and Reporting:

  - Generate sales reports based on daily, weekly, or monthly data.
  - Provide insights into popular dishes and track ingredient costs for better decision-making (not in first MVP).

- User Role Management:
  - Implement different user roles (e.g., owner, admin, waitress, etc).
  - Secure the application using JWT for authentication, with role-based permissions to control access to features.


# High level architecture

![High level architecture](./GastroManager.drawio.png)