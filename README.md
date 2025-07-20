# 🏪 E-commerce Backend - Spring Boot

Backend completo para e-commerce con integración de **Stripe**, **Twilio**, **PostgreSQL** y notificaciones.

## 🏗️ Arquitectura

```
┌─────────────┐    ┌──────────────┐    ┌─────────────┐
│   Frontend  │───▶│  Spring Boot │───▶│ PostgreSQL  │
│   (React)   │    │   Backend    │    │ Database    │
└─────────────┘    └──────────────┘    └─────────────┘
                           │
                           ▼
                   ┌──────────────┐
                   │   Stripe     │
                   │   Webhooks   │
                   └──────────────┘
                           │
                           ▼
                   ┌──────────────┐
                   │  Twilio SMS  │
                   │  Notifications│
                   └──────────────┘
```

## 🚀 Tecnologías

- **Spring Boot 3.5.3** - Framework principal
- **Spring Data JPA** - ORM y persistencia
- **PostgreSQL** - Base de datos principal
- **Stripe API** - Procesamiento de pagos
- **Twilio API** - SMS y WhatsApp
- **JavaMail** - Envío de emails
- **Docker** - Containerización

## 📦 Estructura del Proyecto

```
src/main/java/com/techstore/ecommerce/
├── model/          # Entidades JPA
├── repository/     # Repositorios de datos
├── service/        # Lógica de negocio
├── controller/     # REST Controllers
├── dto/           # Data Transfer Objects
├── config/        # Configuraciones
└── exception/     # Manejo de excepciones
```

## 🛠️ Instalación y Configuración

### 1. Prerrequisitos
- Java 21+
- Maven 3.6+
- PostgreSQL 17+
- Docker (opcional)

### 2. Configurar Variables de Entorno
```bash
cp .env.example .env
# Editar .env con tus credenciales
```

### 3. Ejecutar con Docker
```bash
# Levantar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f app
```

### 4. Ejecutar manualmente
```bash
# Instalar dependencias
mvn clean install

# Ejecutar aplicación
mvn spring-boot:run
```

## 📡 API Endpoints

### Productos
- `GET /api/v1/products` - Listar productos
- `GET /api/v1/products/{id}` - Obtener producto
- `POST /api/v1/products` - Crear producto
- `PUT /api/v1/products/{id}` - Actualizar producto
- `DELETE /api/v1/products/{id}` - Eliminar producto

### Checkout
- `POST /api/v1/checkout/create-session` - Crear sesión de pago

### Órdenes
- `GET /api/v1/orders` - Listar órdenes
- `GET /api/v1/orders?email={email}` - Órdenes por email
- `PUT /api/v1/orders/{id}/status` - Actualizar estado

### Webhooks
- `POST /api/v1/webhook/stripe` - Webhook de Stripe

### Health Check
- `GET /health` - Estado del sistema
- `GET /actuator/health` - Métricas detalladas

## 🔗 Integración con Stripe

### 1. Configurar Webhooks en Stripe Dashboard
```
URL: https://tu-dominio.com/api/v1/webhook/stripe
Eventos: checkout.session.completed, payment_intent.succeeded
```

### 2. Ejemplo de Checkout
```json
POST /api/v1/checkout/create-session
{
  "items": [
    {
      "productId": "laptop_001",
      "quantity": 1
    }
  ],
  "customerEmail": "cliente@example.com",
  "successUrl": "https://tu-frontend.com/success",
  "cancelUrl": "https://tu-frontend.com/cancel"
}
```

## 📱 Integración con Twilio

### Configuración
```yaml
twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  phone-number: ${TWILIO_PHONE_NUMBER}
```

### Funcionalidades
- ✅ SMS de confirmación de pedido
- ✅ WhatsApp notifications
- ✅ Notificaciones de cambio de estado

## 📧 Configuración de Email

### Gmail SMTP
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${SMTP_USERNAME}
    password: ${SMTP_PASSWORD}
```

## 🔒 Seguridad

- ✅ Validación de webhooks de Stripe
- ✅ CORS configurado
- ✅ Rate limiting en endpoints
- ✅ Validación de entrada con Bean Validation
- ✅ Manejo centralizado de excepciones

## 🧪 Testing

```bash
# Ejecutar tests
mvn test

# Tests con cobertura
mvn test jacoco:report
```

## 📊 Monitoreo

### Actuator Endpoints
- `/actuator/health` - Estado de la aplicación
- `/actuator/metrics` - Métricas de performance
- `/actuator/info` - Información de la aplicación

### Logs
```bash
# Ver logs en tiempo real
docker-compose logs -f app

# Logs de Stripe webhooks
grep "Stripe webhook" logs/app.log
```

## 🚀 Despliegue en Producción

### 1. Variables de Entorno Requeridas
```bash
DB_HOST=your-postgres-host
DB_USERNAME=your-db-user
DB_PASSWORD=your-secure-password
STRIPE_SECRET_KEY=sk_live_your_live_key
TWILIO_ACCOUNT_SID=your_live_account_sid
SMTP_USERNAME=your-email@domain.com
```

### 2. Docker Build
```bash
# Build de la imagen
docker build -t ecommerce-backend .

# Run en producción
docker run -d \
  --name ecommerce-backend \
  -p 8080:8080 \
  --env-file .env.prod \
  ecommerce-backend
```

## 🤝 Contribuir

1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/nueva-funcionalidad`)
5. Abrir Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia Apache. Ver `LICENSE` para más detalles.

## 🆘 Soporte

- 📧 Email: me@geovannycode.com