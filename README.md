# 🏦 SimpleBank (Backend)

A simple banking backend built with Java 21,Rest-api and Spring Boot 3.5.3. It supports basic banking operations such as account creation, login with JWT, credit/debit transactions, transfers, and bank statement generation as a PDF sent to customer email.

---

## ✨ Features

- ✅ New account creation with email confirmation
- 🔐 JWT-based user authentication
- 📊 View account balance and customer name
- ➕ Credit account
- ➖ Debit account
- 🔄 Transfer between accounts
- 🧾 Generate bank statement as PDF
- 📧 Email PDF statement from your internal drive to registered customer

---

## 🚀 Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Security + JWT
- Spring Data JPA
- Hibernate
- Mysql / H2 (dev)
- JavaMailSender
- iText / OpenPDF (PDF generation)
- Lombok
- Docker

---

## 📦 Requirements

- Java 21+
- Maven 3.9+
- Mysql (or use embedded H2 for testing)
- Docker

---

## 🛠️ Setup Instructions

### 1. Clone the repo

```bash
git clone https://github.com/murphy0003/simplebank_app.git
cd simplebank_app
```
