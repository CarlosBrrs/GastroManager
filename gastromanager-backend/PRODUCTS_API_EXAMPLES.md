# Ejemplos de API - Productos (Products) - Modo Básico y Avanzado

## Concepto de Productos

Los **productos** son lo que se vende al cliente final. Pueden funcionar en dos modos:

### **Modo Básico**
- Solo requiere datos básicos y un precio de compra manual
- No usa recetas ni ingredientes
- El `purchasePrice` se establece manualmente

### **Modo Avanzado**
- Usa recetas y/o ingredientes para calcular automáticamente el `purchasePrice`
- Debe tener al menos una receta o un ingrediente
- El `purchasePrice` se calcula automáticamente sumando los costos

---

## 1. Crear Producto en Modo Básico

### Petición POST `/products`
```json
{
  "name": "Hamburguesa clásica",
  "description": "Pan, carne, queso y vegetales",
  "category": "Comida rápida",
  "purchasePrice": 3500,
  "salePrice": 7000
}
```

**Campos:**
- `name` *(requerido)*: Nombre del producto
- `description` *(opcional)*: Descripción del producto
- `category` *(opcional)*: Categoría del producto
- `purchasePrice` *(requerido en modo básico)*: Precio de compra/costo del producto
- `salePrice` *(requerido)*: Precio de venta al cliente
- `recipes`: null o lista vacía
- `ingredients`: null o lista vacía

### Respuesta 201 CREATED
```json
{
  "timestamp": "2025-10-05T14:30:00Z",
  "flag": true,
  "message": "Product created successfully",
  "data": "a1b2c3d4-e5f6-7890-ab12-cdef34567890"
}
```

---

## 2. Crear Producto en Modo Avanzado - Solo con Recetas

**Caso de uso:** Un producto que combina varias recetas preparadas previamente.

### Petición POST `/products`
```json
{
  "name": "Almuerzo ejecutivo carne molida",
  "description": "Carne molida con arroz y ensalada",
  "category": "Almuerzos",
  "salePrice": 12000,
  "recipes": [
    {
      "recipeUuid": "b94767a5-74f4-47d1-a13d-b5193ae7430d",
      "quantityMultiplier": 1.0
    },
    {
      "recipeUuid": "c1234567-89ab-4cde-f012-3456789abcde",
      "quantityMultiplier": 2.0
    }
  ]
}
```

**Explicación:**
- **NO se envía `purchasePrice`** - se calcula automáticamente
- `recipes`: Lista de recetas que componen el producto
  - `recipeUuid`: UUID de la receta (debe existir previamente)
  - `quantityMultiplier`: Multiplicador de porciones (1.0 = 1 porción, 2.0 = 2 porciones, 0.5 = media porción)

**Cálculo automático:**
Si las recetas tienen estos costos:
- Receta `b94767a5...` (Arroz blanco) = $1200.00 × 1.0 = $1200.00
- Receta `c1234567...` (Carne guisada) = $4500.00 × 2.0 = $9000.00
- **Total purchasePrice calculado = $10200.00**

### Respuesta 201 CREATED
```json
{
  "timestamp": "2025-10-05T14:35:00Z",
  "flag": true,
  "message": "Product created successfully",
  "data": "d2e3f4a5-b6c7-8901-de23-456789abcdef"
}
```

---

## 3. Crear Producto en Modo Avanzado - Solo con Ingredientes

**Caso de uso:** Un producto simple que usa ingredientes directos sin recetas intermedias.

### Petición POST `/products`
```json
{
  "name": "Carne molida con papas",
  "description": "Plato sencillo con carne molida y papas",
  "category": "Platos principales",
  "salePrice": 10000,
  "ingredients": [
    {
      "ingredientUuid": "92846fcc-d0e9-4a0b-8815-cf72050af84d",
      "quantity": 200
    },
    {
      "ingredientUuid": "8111b579-4e0f-4e5d-b05d-776b96d3e737",
      "quantity": 100
    }
  ]
}
```

**Explicación:**
- **NO se envía `purchasePrice`** - se calcula automáticamente
- `ingredients`: Lista de ingredientes directos
  - `ingredientUuid`: UUID del ingrediente (debe existir previamente)
  - `quantity`: Cantidad en gramos o unidad del ingrediente

**Cálculo automático:**
La fórmula es: `(precioUnitario × cantidad) / 1000`

Ejemplo si los ingredientes tienen estos precios por kilo:
- Ingrediente `92846fcc...` (Carne molida) = $15000/kg → (15000 × 200) / 1000 = $3000.00
- Ingrediente `8111b579...` (Papas) = $5000/kg → (5000 × 100) / 1000 = $500.00
- **Total purchasePrice calculado = $3500.00**

### Respuesta 201 CREATED
```json
{
  "timestamp": "2025-10-05T14:40:00Z",
  "flag": true,
  "message": "Product created successfully",
  "data": "e3f4a5b6-c7d8-9012-ef34-567890abcdef"
}
```

---

## 4. Crear Producto en Modo Avanzado - Recetas + Ingredientes

**Caso de uso:** Un producto que combina recetas preparadas y agrega ingredientes adicionales.

### Petición POST `/products`
```json
{
  "name": "Almuerzo ejecutivo pollo especial",
  "description": "Pollo con arroz, frijoles y queso extra",
  "category": "Almuerzos",
  "salePrice": 13500,
  "recipes": [
    {
      "recipeUuid": "f1e2d3c4-b5a6-7890-1234-567890abcdef",
      "quantityMultiplier": 1.0
    },
    {
      "recipeUuid": "a9b8c7d6-e5f4-3210-9876-543210fedcba",
      "quantityMultiplier": 1.0
    }
  ],
  "ingredients": [
    {
      "ingredientUuid": "8111b579-4e0f-4e5d-b05d-776b96d3e737",
      "quantity": 50
    }
  ]
}
```

**Explicación:**
- Combina recetas base (arroz + frijoles) con un ingrediente extra (queso)
- El `purchasePrice` se calcula sumando el costo de las recetas + ingredientes

**Cálculo automático:**
- Receta arroz = $1200.00 × 1.0 = $1200.00
- Receta frijoles = $800.00 × 1.0 = $800.00
- Ingrediente queso = (12000 × 50) / 1000 = $600.00
- **Total purchasePrice calculado = $2600.00**

### Respuesta 201 CREATED
```json
{
  "timestamp": "2025-10-05T14:45:00Z",
  "flag": true,
  "message": "Product created successfully",
  "data": "f4a5b6c7-d8e9-0123-f456-7890abcdef12"
}
```

---

## 5. Ejemplos de Errores y Validaciones

### Error: Modo básico sin purchasePrice
```json
// REQUEST
{
  "name": "Producto sin precio",
  "salePrice": 10000
}

// RESPONSE 400 BAD REQUEST
{
  "timestamp": "2025-10-05T15:00:00Z",
  "flag": false,
  "message": "Purchase price is required for basic mode products",
  "data": null
}
```

### Error: Modo avanzado sin recetas ni ingredientes
```json
// REQUEST
{
  "name": "Producto sin componentes",
  "salePrice": 10000,
  "recipes": [],
  "ingredients": []
}

// RESPONSE 400 BAD REQUEST
{
  "timestamp": "2025-10-05T15:05:00Z",
  "flag": false,
  "message": "Purchase price is required for basic mode products",
  "data": null
}
```
**Nota:** Si las listas están vacías, se considera modo básico y requiere `purchasePrice`.

### Error: Receta no encontrada
```json
// REQUEST
{
  "name": "Producto con receta inválida",
  "salePrice": 10000,
  "recipes": [
    {
      "recipeUuid": "00000000-0000-0000-0000-000000000000",
      "quantityMultiplier": 1.0
    }
  ]
}

// RESPONSE 404 NOT FOUND
{
  "timestamp": "2025-10-05T15:10:00Z",
  "flag": false,
  "message": "Recipe with UUID 00000000-0000-0000-0000-000000000000 does not exist",
  "data": null
}
```

### Error: Ingrediente no encontrado
```json
// REQUEST
{
  "name": "Producto con ingrediente inválido",
  "salePrice": 10000,
  "ingredients": [
    {
      "ingredientUuid": "00000000-0000-0000-0000-000000000000",
      "quantity": 100
    }
  ]
}

// RESPONSE 404 NOT FOUND
{
  "timestamp": "2025-10-05T15:15:00Z",
  "flag": false,
  "message": "Ingredient with UUID 00000000-0000-0000-0000-000000000000 does not exist",
  "data": null
}
```

---

## Flujo Completo de Ejemplo

### 1. Crear Recetas Base
```json
// Crear receta de arroz blanco
POST /recipes
{
  "name": "Arroz Blanco",
  "description": "Arroz cocido tradicional",
  "yieldPortions": 1,
  "ingredients": [
    {
      "ingredientUuid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "quantity": 200
    },
    {
      "ingredientUuid": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "quantity": 400
    }
  ]
}
// Costo calculado automáticamente: $1200.00

// Crear receta de frijoles
POST /recipes
{
  "name": "Frijoles Rojos",
  "description": "Frijoles cocidos con especias",
  "yieldPortions": 1,
  "ingredients": [
    {
      "ingredientUuid": "e5f6a7b8-c9d0-1234-ef56-7890abcd1234",
      "quantity": 150
    },
    {
      "ingredientUuid": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "quantity": 300
    }
  ]
}
// Costo calculado automáticamente: $800.00
```

### 2. Crear Producto usando las Recetas
```json
POST /products
{
  "name": "Almuerzo Casero",
  "description": "Arroz con frijoles",
  "category": "Almuerzos",
  "salePrice": 8000,
  "recipes": [
    {
      "recipeUuid": "{uuid-arroz-blanco}",
      "quantityMultiplier": 1.0
    },
    {
      "recipeUuid": "{uuid-frijoles}",
      "quantityMultiplier": 1.0
    }
  ]
}
```

**Resultado:**
- Purchase Price calculado automáticamente: $2000.00 ($1200.00 + $800.00)
- Sale Price: $8000.00
- Margen de ganancia: $6000.00 (75%)

---

## Resumen de Reglas

| Modo | purchasePrice | recipes | ingredients | Validación |
|------|---------------|---------|-------------|------------|
| **Básico** | ✅ Requerido (manual) | ❌ Vacío o null | ❌ Vacío o null | OK |
| **Avanzado (recetas)** | 🔄 Calculado automáticamente | ✅ Al menos 1 | ❌ Vacío o null | OK |
| **Avanzado (ingredientes)** | 🔄 Calculado automáticamente | ❌ Vacío o null | ✅ Al menos 1 | OK |
| **Avanzado (mixto)** | 🔄 Calculado automáticamente | ✅ Al menos 1 | ✅ Al menos 1 | OK |
| **Inválido** | ❌ No enviado | ❌ Vacío | ❌ Vacío | ❌ ERROR |

**Notas importantes:**
- En modo avanzado, si envías `purchasePrice`, será ignorado y recalculado
- Puedes enviar `purchasePrice: 0` en modo avanzado, será recalculado de todas formas
- Las recetas e ingredientes deben existir previamente en la base de datos
- El `quantityMultiplier` acepta decimales (0.5, 1.5, 2.0, etc.)
- **Los precios están en formato colombiano (pesos)**: $1200.00 = mil doscientos pesos, $10000.00 = diez mil pesos
- El cálculo de ingredientes usa la fórmula: `(precioUnitario × cantidad) / 1000` donde precioUnitario es el precio por kilogramo
