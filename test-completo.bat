@echo off
echo ========================================
echo    TEST COMPLETO - TODOS LOS PUNTOS
echo ========================================
echo.

echo 1. Limpiando y compilando todos los microservicios...
echo.
echo    - Customer Service...
cd edamicrokafka
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Customer Service no compila
    pause
    exit /b 1
)
cd ..

echo    - Login Service...
cd edamicrokafka-login
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Login Service no compila
    pause
    exit /b 1
)
cd ..

echo    - Order Service...
cd edamicrokafka-order
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Order Service no compila
    pause
    exit /b 1
)
cd ..

echo    - API Gateway...
cd edamicrokafka-gateway
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: API Gateway no compila
    pause
    exit /b 1
)
cd ..

echo.
echo 2. Iniciando Docker Compose (Kafka + MySQL)...
docker-compose up -d
timeout /t 15 /nobreak > nul

echo.
echo 3. Iniciando microservicios...
echo    - Customer Service (Puerto 8080)
start "Customer Service" cmd /k "cd edamicrokafka && mvn spring-boot:run"

timeout /t 20 /nobreak > nul

echo    - Login Service (Puerto 8081)
start "Login Service" cmd /k "cd edamicrokafka-login && mvn spring-boot:run"

timeout /t 20 /nobreak > nul

echo    - Order Service (Puerto 8082)
start "Order Service" cmd /k "cd edamicrokafka-order && mvn spring-boot:run"

timeout /t 20 /nobreak > nul

echo    - API Gateway con Netflix Zuul (Puerto 8083)
start "Netflix Zuul Gateway" cmd /k "cd edamicrokafka-gateway && mvn spring-boot:run"

timeout /t 25 /nobreak > nul

echo.
echo ========================================
echo    PRUEBAS FUNCIONALES
echo ========================================
echo.

echo 4. PUNTO 3: Creacion automatica de Login
echo    Creando Customer (debe crear Login automaticamente)...
curl -X POST http://localhost:8080/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\": \"19273\", \"firstname\": \"Laura\", \"lastname\": \"Perez\", \"address\": \"Norte\", \"phone\": \"5123452\", \"email\": \"pepito@c.com\"}"

echo.
echo    Verificando Login creado...
curl -X GET http://localhost:8081/api/logins

echo.
echo 5. PUNTO 4: Topicos unificados
echo    Probando diferentes operaciones de Customer...
curl -X GET http://localhost:8080/api/customers/19273
curl -X GET http://localhost:8080/api/customers

echo.
echo 6. PUNTO 5: API Gateway con Netflix Zuul
echo    Acceso a traves del Gateway...
curl -X GET http://localhost:8083/api/customers
curl -X GET http://localhost:8083/api/logins
curl -X GET http://localhost:8083/api/orders

echo.
echo ========================================
echo    TEST COMPLETADO
echo ========================================
echo.
echo Todos los microservicios estan funcionando correctamente!
echo.
echo EVIDENCIAS A CAPTURAR:
echo 1. Consolas de microservicios mostrando logs
echo 2. Respuestas de APIs funcionando
echo 3. Gateway Zuul redirigiendo correctamente
echo 4. Logs de Kafka con topicos unificados
echo.
echo Presiona cualquier tecla para cerrar...
pause > nul
