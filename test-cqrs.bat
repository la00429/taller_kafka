@echo off
echo ========================================
echo    TESTING CQRS ARCHITECTURE
echo ========================================
echo.

echo [1/6] Starting Docker containers...
docker-compose -f docker-compose-cqrs.yml up -d
timeout /t 10 /nobreak > nul

echo [2/6] Compiling CQRS Controller (Command Side)...
cd cqrscontroller
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile CQRS Controller
    pause
    exit /b 1
)
cd ..

echo [3/6] Compiling CQRS Query (Query Side)...
cd cqrsquery
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to compile CQRS Query
    pause
    exit /b 1
)
cd ..

echo [4/6] Starting CQRS Controller (Command Side) on port 11080...
start "CQRS Controller" cmd /k "cd cqrscontroller && mvn spring-boot:run"
timeout /t 15 /nobreak > nul

echo [5/6] Starting CQRS Query (Query Side) on port 12080...
start "CQRS Query" cmd /k "cd cqrsquery && mvn spring-boot:run"
timeout /t 15 /nobreak > nul

echo [6/6] Testing CQRS Architecture...
echo.
echo ========================================
echo    CQRS TEST RESULTS
echo ========================================
echo.

echo Testing Customer Creation (Command Side)...
curl -X POST http://localhost:11080/api/customers/add ^
  -H "Content-Type: application/json" ^
  -d "{\"document\":\"19273\",\"firstname\":\"Laura\",\"lastname\":\"Perez\",\"address\":\"Norte\",\"phone\":\"5123452\",\"email\":\"pepito@c.com\"}" ^
  --silent --show-error

echo.
echo.
echo Testing Customer Query (Query Side)...
curl -X GET http://localhost:12080/api/customers/all ^
  --silent --show-error

echo.
echo.
echo Testing Login Query (Query Side)...
curl -X GET http://localhost:12080/api/logins/all ^
  --silent --show-error

echo.
echo.
echo ========================================
echo    CQRS TEST COMPLETED
echo ========================================
echo.
echo Command Side (MySQL): http://localhost:11080
echo Query Side (MongoDB): http://localhost:12080
echo.
echo Press any key to continue...
pause > nul
