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
- 📧 Email PDF statement from your internal drive (C:\\Lab\\Mystatement.pdf) to registered customer email

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

### Clone the repo

```bash
git clone https://github.com/murphy0003/simplebank_app.git
cd simplebank_app
```
### For email service
Go to your google account.Turn on 2-step verification if not .Security tabs->How to sign to google(click app passwords)->select app(choose mail)->name your app->generate->copy password
Change it in the application.properties.

```
spring.mail.username =your-email or org email
spring.mail.password=copy-password
```
!Note.Don't do it in your production.Use secretmanager.

### For running 
You just have to do it build docker image or run dockerfile and run docker-compose.yml.First run dockerfile.It will show your error because it have not database next run docker-compose.yml and run again dockerimage.
Now you can test the api or whatever you want.

### JwtSecret
If you don't apply jwtsecret , it will auto create one.

## 🤝 Contributing

Contributions are welcome! 🎉

---

### 🐛 Issues

```markdown
## 🐛 Issues

Found a bug? Have a feature request?

1. Search existing [issues](https://github.com/murphy0003/simplebank_app/issues) to avoid duplicates
2. If not found, [open a new issue](https://github.com/murphy0003/simplebank_app/issues/new)

Please include:

- A clear description of the issue
- Steps to reproduce (if applicable)
- Logs or screenshots
```

## 📞 Contact
For questions or suggestions:
📧 alaxzandaaungmyintmyat@gmail.com
💬 Telegram-@murphy9x





