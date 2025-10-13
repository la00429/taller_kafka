@echo off
echo ========================================
echo    CQRS ARCHITECTURE TEST SCRIPT
echo ========================================
echo.

echo [1/6] Starting Docker infrastructure...
docker-compose up -d
timeout /t 10 /nobreak > nul

echo [2/6] Compiling Command Side (cqrscontroller)...
cd cqrscontroller
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile Command Side
    pause
    exit /b 1
)
cd ..

echo [3/6] Compiling Query Side (cqrsquery)...
cd cqrsquery
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile Query Side
    pause
    exit /b 1
)
cd ..

echo [4/6] Compiling API Gateway...
cd cqrs-gateway
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile API Gateway
    pause
    exit /b 1
)
cd ..

echo [5/6] Starting microservices...
echo Starting Command Side (Port 8080)...
start "Command Side" cmd /k "cd cqrscontroller && mvn spring-boot:run"

echo Starting Query Side (Port 8081)...
start "Query Side" cmd /k "cd cqrsquery && mvn spring-boot:run"

echo Starting API Gateway (Port 8082)...
start "API Gateway" cmd /k "cd cqrs-gateway && mvn spring-boot:run"

echo [6/6] Waiting for services to start...
timeout /t 30 /nobreak > nul

echo.
echo ========================================
echo    TESTING CQRS ARCHITECTURE
echo ========================================
echo.

echo Testing Command Side (Write Operations)...
echo.

echo [TEST 1] Creating Customer (should auto-create Login)...
curl -X POST http://localhost:8082/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\":\"12345\",\"firstname\":\"Laura\",\"lastname\":\"Perez\",\"address\":\"Norte\",\"phone\":\"5123452\",\"email\":\"laura@test.com\"}"
echo.
echo.

echo [TEST 2] Creating Order...
curl -X POST http://localhost:8082/api/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"customerId\":\"12345\",\"productName\":\"Laptop\",\"quantity\":1,\"price\":1500.0,\"orderDate\":\"2024-01-15T10:00:00\",\"status\":\"PENDING\"}"
echo.
echo.

echo Testing Query Side (Read Operations)...
echo.

echo [TEST 3] Reading Customers from MongoDB...
curl -X GET http://localhost:8082/api/customers
echo.
echo.

echo [TEST 4] Reading Logins from MongoDB (auto-created)...
curl -X GET http://localhost:8082/api/logins
echo.
echo.

echo [TEST 5] Reading Orders from MongoDB...
curl -X GET http://localhost:8082/api/orders
echo.
echo.

echo ========================================
echo    CQRS TEST COMPLETED
echo ========================================
echo.
echo Check the console windows for:
echo - Command Side: MySQL operations and Kafka events
echo - Query Side: MongoDB operations and Kafka consumption
echo - API Gateway: Request routing logs
echo.
echo Press any key to exit...
pause > nul
