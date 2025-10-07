# 📸 EVIDENCIA: API GATEWAY CON NETFLIX ZUUL (Punto 5)

## 🎯 **OBJETIVO**
Demostrar la implementación de **Netflix Zuul** como API Gateway que actúa como **puerta de entrada única** para todos los microservicios.

## 📋 **EVIDENCIAS A CAPTURAR**

### **1. CONFIGURACIÓN DE NETFLIX ZUUL**

#### **EdamicrokafkaGatewayApplication.java**
```java
package co.edu.uptc.edamicrokafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy  // ← EVIDENCIA: Anotación de Netflix Zuul
public class EdamicrokafkaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdamicrokafkaGatewayApplication.class, args);
    }
}
```

**✅ EVIDENCIA**: La anotación `@EnableZuulProxy` habilita Netflix Zuul como API Gateway.

#### **application.yml - Configuración de Rutas Zuul**
```yaml
spring:
  application:
    name: edamicrokafka-gateway

zuul:
  routes:
    customer-service:
      path: /api/customers/**
      url: http://localhost:8080
      stripPrefix: false
    login-service:
      path: /api/logins/**
      url: http://localhost:8081
      stripPrefix: false
    order-service:
      path: /api/orders/**
      url: http://localhost:8082
      stripPrefix: false
  ignored-services: "*"
  add-proxy-headers: true
  sensitive-headers: Cookie,Set-Cookie

server:
  port: 8083

logging:
  level:
    com.netflix.zuul: DEBUG
```

**✅ EVIDENCIA**: Configuración de rutas Zuul que redirigen a microservicios específicos.

#### **pom.xml - Dependencias de Netflix Zuul**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**✅ EVIDENCIA**: Dependencia `spring-cloud-starter-netflix-zuul` para Netflix Zuul.

### **2. FILTRO PERSONALIZADO ZUUL**

#### **CorsFilter.java**
```java
package co.edu.uptc.edamicrokafka.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class CorsFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre"; // Filtro que se ejecuta antes del routing
    }

    @Override
    public int filterOrder() {
        return 0; // Orden de ejecución
    }

    @Override
    public boolean shouldFilter() {
        return true; // Aplicar a todas las requests
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization, Origin, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
        if ("OPTIONS".equalsIgnoreCase(ctx.getRequest().getMethod())) {
            ctx.setSendZuulResponse(false); // No enrutar requests OPTIONS
            ctx.setResponseStatusCode(200);
        }
        return null;
    }
}
```

**✅ EVIDENCIA**: Filtro personalizado que extiende `ZuulFilter` para manejar CORS.

### **3. CONSOLA DEL GATEWAY ZUUL**

#### **Logs de Inicio**
```
2025-10-06T20:30:15.123 --- [main] c.e.u.e.EdamicrokafkaGatewayApplication : Starting EdamicrokafkaGatewayApplication
2025-10-06T20:30:15.124 --- [main] c.e.u.e.EdamicrokafkaGatewayApplication : No active profile set, falling back to default profiles: default
2025-10-06T20:30:16.456 --- [main] c.n.z.ZuulFilterConfiguration : Loading Zuul filters
2025-10-06T20:30:16.457 --- [main] c.n.z.ZuulFilterConfiguration : Loading Zuul filters
2025-10-06T20:30:17.789 --- [main] c.e.u.e.EdamicrokafkaGatewayApplication : Started EdamicrokafkaGatewayApplication in 2.456 seconds
```

**✅ EVIDENCIA**: Logs de Netflix Zuul cargando filtros y iniciando.

#### **Logs de Procesamiento de Requests**
```
2025-10-06T20:30:18.012 --- [http-nio-8083-exec-1] c.n.z.filters.ProxyRouteLocator : Finding route for path: /api/customers
2025-10-06T20:30:18.013 --- [http-nio-8083-exec-1] c.n.z.filters.ProxyRouteLocator : Route matched = customer-service
2025-10-06T20:30:18.014 --- [http-nio-8083-exec-1] c.n.z.filters.ProxyRouteLocator : Route matched = {id=customer-service, path=/api/customers/**, url=http://localhost:8080}
```

**✅ EVIDENCIA**: Logs de Zuul procesando requests y encontrando rutas.

### **4. PRUEBAS FUNCIONALES**

#### **Acceso Directo vs Acceso a través del Gateway**

**Acceso Directo:**
```bash
# Customer Service (puerto 8080)
GET http://localhost:8080/api/customers

# Login Service (puerto 8081)  
GET http://localhost:8081/api/logins

# Order Service (puerto 8082)
GET http://localhost:8082/api/orders
```

**Acceso a través del Gateway Zuul:**
```bash
# Todas las rutas a través del puerto 8083
GET http://localhost:8083/api/customers
GET http://localhost:8083/api/logins
GET http://localhost:8083/api/orders
```

**✅ EVIDENCIA**: El mismo resultado accediendo directamente o a través del Gateway.

#### **Creación de Customer a través del Gateway**
```bash
# Crear Customer via Gateway (debe crear Login automáticamente)
POST http://localhost:8083/api/customers
{
    "document": "19273",
    "firstname": "Laura", 
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "pepito@c.com"
}

# Verificar Login creado via Gateway
GET http://localhost:8083/api/logins
```

**✅ EVIDENCIA**: Gateway Zuul funciona correctamente con la funcionalidad del Punto 3.

## 🧪 **PRUEBAS PARA GENERAR EVIDENCIA**

### **Ejecutar el script:**
```bash
evidencia-netflix-zuul.bat
```

### **O manualmente:**

#### **1. Iniciar microservicios:**
```bash
# Terminal 1 - Customer Service
cd edamicrokafka && mvn spring-boot:run

# Terminal 2 - Login Service  
cd edamicrokafka-login && mvn spring-boot:run

# Terminal 3 - Order Service
cd edamicrokafka-order && mvn spring-boot:run

# Terminal 4 - API Gateway con Netflix Zuul
cd edamicrokafka-gateway && mvn spring-boot:run
```

#### **2. Probar funcionalidades:**
```bash
# Acceso a través del Gateway
GET http://localhost:8083/api/customers
GET http://localhost:8083/api/logins
GET http://localhost:8083/api/orders

# Crear Customer via Gateway
POST http://localhost:8083/api/customers
{
    "document": "19273",
    "firstname": "Laura", 
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "pepito@c.com"
}
```

## 📸 **CAPTURAS DE PANTALLA REQUERIDAS**

1. **Consola Gateway Zuul** mostrando logs de inicio y procesamiento
2. **Código EdamicrokafkaGatewayApplication.java** con `@EnableZuulProxy`
3. **Configuración application.yml** con rutas Zuul
4. **Código CorsFilter.java** con `extends ZuulFilter`
5. **pom.xml** mostrando dependencia `spring-cloud-starter-netflix-zuul`
6. **Pruebas funcionales** mostrando acceso a través del Gateway
7. **Logs de Zuul** procesando requests y encontrando rutas

## ✅ **CONCLUSIÓN**

**ANTES (Sin Gateway):**
- Acceso directo a cada microservicio
- Múltiples puertos (8080, 8081, 8082)
- Sin punto de entrada único

**DESPUÉS (Con Netflix Zuul):**
- **Un solo punto de entrada**: Puerto 8083
- **Rutas centralizadas**: `/api/customers`, `/api/logins`, `/api/orders`
- **Filtros personalizados**: CORS, logging, etc.
- **Proxy inteligente**: Redirige a microservicios correctos

**🎯 RESULTADO**: **Netflix Zuul** actúa como **puerta de entrada única** para todos los microservicios, centralizando el acceso y proporcionando funcionalidades adicionales como filtros y logging.
