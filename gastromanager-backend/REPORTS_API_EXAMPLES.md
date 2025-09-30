# Ejemplos de uso de la API de Reportes de Ventas

## Endpoint
```
GET /reports/sales
```

## Ejemplos de Peticiones

### 1. Consulta básica (solo fechas obligatorias)
```http
GET /reports/sales?dateFrom=2024-01-01T00:00:00Z&dateTo=2024-01-31T23:59:59Z
```

### 2. Filtrar por múltiples cajas registradoras
```http
GET /reports/sales?dateFrom=2024-01-01T00:00:00Z&dateTo=2024-01-31T23:59:59Z&cashRegisterUuids=550e8400-e29b-41d4-a716-446655440001&cashRegisterUuids=550e8400-e29b-41d4-a716-446655440002
```

### 3. Filtrar por múltiples métodos de pago
```http
GET /reports/sales?dateFrom=2024-01-01T00:00:00Z&dateTo=2024-01-31T23:59:59Z&paymentMethods=cash&paymentMethods=credit_card
```

### 4. Filtrar por múltiples usuarios y sesiones cerradas
```http
GET /reports/sales?dateFrom=2024-01-01T00:00:00Z&dateTo=2024-01-31T23:59:59Z&assignedUserUuids=660e8400-e29b-41d4-a716-446655440001&assignedUserUuids=660e8400-e29b-41d4-a716-446655440002&sessionState=CLOSED
```

### 5. Consulta completa con todos los filtros
```http
GET /reports/sales?dateFrom=2024-01-01T00:00:00Z&dateTo=2024-01-31T23:59:59Z&cashRegisterUuids=550e8400-e29b-41d4-a716-446655440001&cashRegisterUuids=550e8400-e29b-41d4-a716-446655440002&sessionState=CLOSED&paymentMethods=cash&paymentMethods=credit_card&assignedUserUuids=660e8400-e29b-41d4-a716-446655440001

```

## Respuesta JSON Ejemplo

```json
{
  "success": true,
  "message": "Sales report generated successfully",
  "data": {
    "totalSales": 15750.50,
    "totalOrders": 125,
    "reportPeriodStart": "2024-01-01T00:00:00Z",
    "reportPeriodEnd": "2024-01-31T23:59:59Z",
    "summary": {
      "totalRevenue": 16200.25,
      "totalTips": 449.75,
      "completedOrders": 123,
      "cancelledOrders": 2,
      "averageOrderValue": 128.05,
      "paymentMethodBreakdown": [
        {
          "methodName": "CASH",
          "totalAmount": 7875.25,
          "transactionCount": 45,
          "percentage": 50.00
        },
        {
          "methodName": "CREDIT_CARD",
          "totalAmount": 5512.50,
          "transactionCount": 38,
          "percentage": 35.00
        },
        {
          "methodName": "DEBIT_CARD",
          "totalAmount": 2362.75,
          "transactionCount": 42,
          "percentage": 15.00
        }
      ],
      "cashRegisterBreakdown": [
        {
          "cashRegisterUuid": "550e8400-e29b-41d4-a716-446655440001",
          "cashRegisterName": "Caja Principal",
          "totalSales": 9450.30,
          "orderCount": 75,
          "transactionCount": 85,
          "sessionSummaries": [
            {
              "sessionUuid": "770e8400-e29b-41d4-a716-446655440001",
              "operatorUserUuid": "880e8400-e29b-41d4-a716-446655440001",
              "operatorUserName": "Juan Pérez",
              "sessionOpenTime": "2024-01-01T08:00:00Z",
              "sessionCloseTime": "2024-01-01T16:00:00Z",
              "sessionStatus": "CLOSED",
              "sessionSales": 4725.15,
              "sessionOrderCount": 38,
              "sessionTransactionCount": 42
            },
            {
              "sessionUuid": "770e8400-e29b-41d4-a716-446655440002",
              "operatorUserUuid": "880e8400-e29b-41d4-a716-446655440002",
              "operatorUserName": "Ana García",
              "sessionOpenTime": "2024-01-01T16:00:00Z",
              "sessionCloseTime": "2024-01-02T00:00:00Z",
              "sessionStatus": "CLOSED",
              "sessionSales": 4725.15,
              "sessionOrderCount": 37,
              "sessionTransactionCount": 43
            }
          ]
        },
        {
          "cashRegisterUuid": "550e8400-e29b-41d4-a716-446655440002",
          "cashRegisterName": "Caja Secundaria",
          "totalSales": 6300.20,
          "orderCount": 50,
          "transactionCount": 55,
          "sessionSummaries": [
            {
              "sessionUuid": "770e8400-e29b-41d4-a716-446655440003",
              "operatorUserUuid": "880e8400-e29b-41d4-a716-446655440003",
              "operatorUserName": "Carlos López",
              "sessionOpenTime": "2024-01-01T10:00:00Z",
              "sessionCloseTime": "2024-01-01T18:00:00Z",
              "sessionStatus": "CLOSED",
              "sessionSales": 3150.10,
              "sessionOrderCount": 25,
              "sessionTransactionCount": 28
            },
            {
              "sessionUuid": "770e8400-e29b-41d4-a716-446655440004",
              "operatorUserUuid": "880e8400-e29b-41d4-a716-446655440004",
              "operatorUserName": "María Rodríguez",
              "sessionOpenTime": "2024-01-01T18:00:00Z",
              "sessionCloseTime": null,
              "sessionStatus": "OPENED",
              "sessionSales": 3150.10,
              "sessionOrderCount": 25,
              "sessionTransactionCount": 27
            }
          ]
        }
      ]
    },
    "details": [
      {
        "paymentMethodName": "CASH",
        "totalAmount": 7875.25,
        "orderCount": 45,
        "transactionCount": 45,
        "averageTransactionAmount": 175.01
      },
      {
        "paymentMethodName": "CREDIT_CARD",
        "totalAmount": 5512.50,
        "orderCount": 30,
        "transactionCount": 38,
        "averageTransactionAmount": 145.07
      },
      {
        "paymentMethodName": "DEBIT_CARD",
        "totalAmount": 2362.75,
        "orderCount": 35,
        "transactionCount": 42,
        "averageTransactionAmount": 56.25
      }
    ]
  }
}
```

## Parámetros de Filtrado

### Obligatorios
- `dateFrom`: Fecha inicio en formato ISO 8601 (Instant)
- `dateTo`: Fecha fin en formato ISO 8601 (Instant)

### Opcionales (múltiples selecciones)
- `cashRegisterUuids`: Lista de UUIDs de cajas registradoras
  - No especificar = todas las cajas
  - Lista vacía = ninguna caja
  - UUIDs específicos = solo esas cajas

- `paymentMethods`: Lista de métodos de pago
  - Valores válidos: cash, credit_card, debit_card, transfer
  - No especificar = todos los métodos
  - Lista específica = solo esos métodos

- `assignedUserUuids`: Lista de UUIDs de usuarios/cajeros
  - No especificar = todos los usuarios
  - Lista vacía = ningún usuario
  - UUIDs específicos = solo esos usuarios

- `sessionState`: Estado de las sesiones
  - Valores: ALL (default), OPENED, CLOSED
  - Solo se puede especificar uno

## Notas sobre el manejo de fechas
- Las fechas se manejan como `Instant` en UTC
- Spring Boot automáticamente convierte las fechas ISO 8601 a Instant
- No hay problemas de zona horaria ya que Instant representa un momento específico en el tiempo
- Formato recomendado: "2024-01-01T00:00:00Z" (Z indica UTC)
