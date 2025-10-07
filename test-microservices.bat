@echo off
echo ========================================
echo Testing EDA Microservices
echo ========================================

echo.
echo 1. Starting Docker services...
docker-compose up -d

echo.
echo 2. Waiting for services to start...
timeout /t 10 /nobreak

echo.
echo 3. Testing Customer Service (Port 8080)...
echo Creating a customer...
curl -X POST http://localhost:8080/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\":\"12345678\",\"name\":\"Juan Perez\",\"email\":\"juan@example.com\",\"phone\":\"3001234567\"}"

echo.
echo.
echo 4. Testing Login Service (Port 8081)...
echo Getting all logins...
curl -X GET http://localhost:8081/api/logins

echo.
echo.
echo 5. Testing Order Service (Port 8082)...
echo Creating an order...
curl -X POST http://localhost:8082/api/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"customerId\":1,\"orderNumber\":\"ORD-001\",\"totalAmount\":100.50,\"shippingAddress\":\"Calle 123\",\"billingAddress\":\"Calle 123\"}"

echo.
echo.
echo 6. Testing API Gateway (Port 8083)...
echo Testing Customer through Gateway...
curl -X GET http://localhost:8083/api/customers

echo.
echo Testing Login through Gateway...
curl -X GET http://localhost:8083/api/logins

echo.
echo Testing Order through Gateway...
curl -X GET http://localhost:8083/api/orders

echo.
echo ========================================
echo Test completed!
echo ========================================
pause
