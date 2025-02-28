```md
# 📊 Trade Processing API

## 🚀 Project Setup  

To run the project locally, follow these steps:  

### 1️⃣ Clone the repository  
```sh
git clone https://github.com/ShovkunTimofejj/testTaskForFacieAI.git  
```

### 2️⃣ Build and start the Docker container for Redis  
```sh
docker-compose up --build
```

### 3️⃣ Run the application  
```sh
mvn spring-boot:run
```

---

## ⚙️ API Overview  

The project includes three controllers:  

- **🛒 ProductController** – retrieves product information by ID.  
- **🗄️ RedisController** – allows manual interaction with Redis (retrieve, save, delete).  
- **📈 TradeController** – the main controller for processing trades.  

📌 **Swagger API Documentation:**  
[👉 Open Swagger UI](http://localhost:8080/swagger-ui/index.html#/)

---

## 📤 File Processing  

The application supports **CSV, JSON, and XML** file uploads.  

### 📌 Processing CSV  
```sh
curl.exe -F "file=@YOUR_PROJECT_PATH/facieAIProjectTest/src/test/resources/trade.csv" \
  -H "Accept: text/csv" \
  http://localhost:8080/api/v1/enrich
```

### 📌 Processing JSON  
```sh
curl.exe -F "file=@YOUR_PROJECT_PATH/facieAIProjectTest/src/test/resources/trade.json" \
  -F "format=json" \
  -H "Accept: text/csv" \
  http://localhost:8080/api/v1/enrich
```

### 📌 Processing XML  
```sh
curl.exe -F "file=@YOUR_PROJECT_PATH/facieAIProjectTest/src/test/resources/trade.xml" \
  -F "format=xml" \
  -H "Accept: text/csv" \
  http://localhost:8080/api/v1/enrich
```

🔹 **Tip:** You can also use relative paths when running the request from the project root:  
```sh
curl.exe -F "file=@src/test/resources/trade.csv" \
  -H "Accept: text/csv" \
  http://localhost:8080/api/v1/enrich
```

---

## 📊 Response Format  

By default, responses are returned in **text/csv** format for better readability. JSON output is also available.  

### ✅ Example Response:  
```
date,productName,currency,price
20230101,Commodity Swaps 1,USD,100.25
20230101,Commodity Swaps,EUR,200.45
20230101,FX Forward,GBP,300.5
...
```

---

## ✅ Testing & CI/CD  

The project uses **GitHub Actions** to automatically run tests:  
- ✅ On every commit to the `dev` branch.  
- ✅ On every pull request to the `main` branch.  

Example output on a successful build:  
```
[INFO] Tests run: 51, Failures: 0, Errors: 0, Skipped: 0
```

---

## 🔥 Future Improvements  

✔ **Automatic Data Loading**  
A background task can be implemented to periodically check for updates in `trade.csv` and load new data automatically.  

✔ **Kafka Support (or another message broker)**  
Trade data can be sent and received via **Kafka**, making the system more flexible and scalable.  

---

## 📜 License  

This project is licensed under the **MIT License**.  

📌 **Author:** [Tymofii Shovkun](https://github.com/ShovkunTimofejj)  

