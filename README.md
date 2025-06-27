  # ![logo](https://github.com/user-attachments/assets/db3515d6-2c95-41cf-a123-0694cc06c0cf)

EasyShop version 2 is a simple e-commerce web application developed with Java, Spring Boot, and MySQL. It allows users to browse products, manage their shopping cart, 
and place orders. Admin users can manage products, categories, and user roles. The system provides secure authentication and authorization for all user actions. we were provided with starter code that was incomplete and contained a few bugs that we needed to address.
Throughout the development process, we leveraged Postman to run a series of tests to ensure that our controllers and DAOs were functioning correctly. 
These tests were essential in validating the integrity and behavior of our application endpoints.

---

## 📌 Table of Contents

- [Features by Phase](#features-by-phase)
- [API Endpoints](#api-endpoints)
- [Technologies Used](#technologies-used)
- [Screenshots](#screenshots)
- [Setup Instructions](#setup-instructions)

---

## ✅ Features by Phase

### Phase 1: 🗂️ Categories
- Built `CategoriesController` with full CRUD functionality.
- Admin-only restrictions for create, update, delete operations.
- Backend validation and exception handling.

### Phase 2: 🛍️ Products & Bug Fixes
- Fixed bugs in search/filter logic (category, min/max price, color).
- Resolved duplication issue on update (updating now modifies, not inserts).
- Verified through unit tests and Postman.

### Phase 3: 🛒 Shopping Cart
- Implemented add, update quantity, view, and clear cart functionality.
- Cart persists across login sessions using user-specific data.
- Calculated total and line-level prices per cart item.

### Phase 4: 👤 User Profile
- Enabled users to retrieve and update their profile (address, phone, etc.).
- Created `ProfileController` and connected with secure JWT user context.

### Phase 5: 💳 Checkout (Orders)
- Built `OrdersController` to convert cart to order.
- Saves order and item details in `orders` and `order_items` tables.
- Clears cart upon successful checkout.

---

## 🔗 API Endpoints

### Authentication
| VERB | Endpoint             | Description              |
|------|----------------------|--------------------------|
| POST | `/register`          | Register new user        |
| POST | `/login`             | User login & JWT token   |

### Categories
| VERB | Endpoint                 | Description             |
|------|--------------------------|-------------------------|
| GET  | `/categories`            | List all categories     |
| GET  | `/categories/{id}`       | Get category by ID      |
| POST | `/categories`            | Add new category (admin)|
| PUT  | `/categories/{id}`       | Update category (admin) |
|DELETE| `/categories/{id}`       | Delete category (admin) |

### Products
| VERB | Endpoint                 | Description                     |
|------|--------------------------|---------------------------------|
| GET  | `/products`              | List/search products            |
| GET  | `/products/{id}`         | Get product by ID               |
| POST | `/products`              | Add product (admin)             |
| PUT  | `/products/{id}`         | Update product (admin)          |
|DELETE| `/products/{id}`         | Delete product (admin)          |

**Search Filters Example:**  
`GET /products?cat=1&minPrice=100&maxPrice=500&color=red`

### Shopping Cart
| VERB | Endpoint                      | Description                    |
|------|-------------------------------|--------------------------------|
| GET  | `/cart`                       | View cart for current user     |
| POST | `/cart/products/{productId}`  | Add product to cart            |
| PUT  | `/cart/products/{productId}`  | Update quantity of product     |
|DELETE| `/cart`                       | Clear shopping cart            |

### Profile
| VERB | Endpoint         | Description            |
|------|------------------|------------------------|
| GET  | `/profile`       | View logged-in profile |
| PUT  | `/profile`       | Update profile         |

### Orders (Checkout)
| VERB | Endpoint         | Description            |
|------|------------------|------------------------|
| POST | `/orders`        | Checkout & create order|

---

## 💻 Technologies Used

- Java 17
- Spring Boot
- Spring Security (for authentication and authorization)
- MySQL (for the relational database)
- Postman (for API testing)
- JWT (for token-based authentication)

---

## 📸 Screenshots

- Frontend
  ![image](https://github.com/user-attachments/assets/d47a9e39-a15b-4885-b099-bf128c8b1a04)

- Profile
  ![image](https://github.com/user-attachments/assets/1f3dbabd-cacd-4adf-8424-44e528fbcd58)

- View Cart & Shopping cart in MySql
  ![image](https://github.com/user-attachments/assets/ead1b7fb-3a92-4820-be8a-e21bbeb872d2)

  ![image](https://github.com/user-attachments/assets/d691ab1b-99a6-4197-b5df-0f3c676058fa)

## ⚙️ Setup Instructions
Clone the Repository

```
git clone https://github.com/HeldanaG/EasyShop
```
## Developed By:
Heldana Gebremariam

6/26/2025
