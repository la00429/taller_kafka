@echo off
echo ========================================
echo    CQRS 6 MICROSERVICES TEST SCRIPT
echo ========================================
echo.

echo [1/8] Starting Docker infrastructure...
docker-compose up -d
timeout /t 10 /nobreak > nul

echo [2/8] Compiling Command Side microservices...
cd command-side\customer-command
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile customer-command
    pause
    exit /b 1
)
cd ..\..

cd command-side\login-command
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile login-command
    pause
    exit /b 1
)
cd ..\..

cd command-side\order-command
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile order-command
    pause
    exit /b 1
)
cd ..\..

echo [3/8] Compiling Query Side microservices...
cd query-side\customer-query
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile customer-query
    pause
    exit /b 1
)
cd ..\..

cd query-side\login-query
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile login-query
    pause
    exit /b 1
)
cd ..\..

cd query-side\order-query
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile order-query
    pause
    exit /b 1
)
cd ..\..

echo [4/8] Compiling API Gateway...
cd cqrs-gateway
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile API Gateway
    pause
    exit /b 1
)
cd ..

echo [5/8] Starting Command Side microservices...
echo Starting Customer Command (Port 8080)...
start "Customer Command" cmd /k "cd command-side\customer-command && mvn spring-boot:run"

echo Starting Login Command (Port 8081)...
start "Login Command" cmd /k "cd command-side\login-command && mvn spring-boot:run"

echo Starting Order Command (Port 8082)...
start "Order Command" cmd /k "cd command-side\order-command && mvn spring-boot:run"

echo [6/8] Starting Query Side microservices...
echo Starting Customer Query (Port 8083)...
start "Customer Query" cmd /k "cd query-side\customer-query && mvn spring-boot:run"

echo Starting Login Query (Port 8084)...
start "Login Query" cmd /k "cd query-side\login-query && mvn spring-boot:run"

echo Starting Order Query (Port 8085)...
start "Order Query" cmd /k "cd query-side\order-query && mvn spring-boot:run"

echo [7/8] Starting API Gateway (Port 8086)...
start "API Gateway" cmd /k "cd cqrs-gateway && mvn spring-boot:run"

echo [8/8] Waiting for services to start...
timeout /t 45 /nobreak > nul

echo.
echo ========================================
echo    TESTING CQRS 6 MICROSERVICES
echo ========================================
echo.

echo Testing Command Side (Write Operations)...
echo.

echo [TEST 1] Creating Customer (should auto-create Login)...
curl -X POST http://localhost:8086/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\":\"12345\",\"firstname\":\"Laura\",\"lastname\":\"Perez\",\"address\":\"Norte\",\"phone\":\"5123452\",\"email\":\"laura@test.com\"}"
echo.
echo.

echo [TEST 2] Creating Order...
curl -X POST http://localhost:8086/api/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"customerId\":\"12345\",\"productName\":\"Laptop\",\"quantity\":1,\"price\":1500.0,\"orderDate\":\"2024-01-15T10:00:00\",\"status\":\"PENDING\"}"
echo.
echo.

echo Testing Query Side (Read Operations)...
echo.

echo [TEST 3] Reading Customers from MongoDB...
curl -X GET http://localhost:8086/api/customers
echo.
echo.

echo [TEST 4] Reading Logins from MongoDB (auto-created)...
curl -X GET http://localhost:8086/api/logins
echo.
echo.

echo [TEST 5] Reading Orders from MongoDB...
curl -X GET http://localhost:8086/api/orders
echo.
echo.

echo ========================================
echo    CQRS 6 MICROSERVICES TEST COMPLETED
echo ========================================
echo.
echo Check the console windows for:
echo - Command Side: MySQL operations and Kafka events
echo   * Customer Command (Port 8080)
echo   * Login Command (Port 8081)  
echo   * Order Command (Port 8082)
echo - Query Side: MongoDB operations and Kafka consumption
echo   * Customer Query (Port 8083)
echo   * Login Query (Port 8084)
echo   * Order Query (Port 8085)
echo - API Gateway: Request routing logs (Port 8086)
echo.
echo Press any key to exit...
pause > nul
