# ✅ Integración de Suscripciones con n8n - ACTUALIZADA

## 🎯 Resumen

Cuando guardes una suscripción en tu app, automáticamente se enviará la información a n8n en el formato solicitado.

---

## 🔗 URL del Webhook
```
https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
```

---

## 📤 Formato de Datos Enviados

Cuando el usuario guarda una suscripción, se envía este JSON a n8n:

```json
{
  "herramienta": "suscripciones",
  "cliente": "Juan Pérez",
  "email": "juan@example.com",
  "plan": "Netflix Premium",
  "fecha_inicio": "2025-01-15",
  "fecha_vencimiento": "2025-10-25",
  "precio": "Q99.99",
  "frecuencia": "MENSUAL"
}
```

### Descripción de Campos:

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| `herramienta` | Siempre será "suscripciones" | `"suscripciones"` |
| `cliente` | Nombre del usuario logueado | `"Juan Pérez"` |
| `email` | Email del usuario logueado | `"juan@example.com"` |
| `plan` | Nombre de la suscripción | `"Netflix Premium"` |
| `fecha_inicio` | Fecha cuando se guardó (formato ISO) | `"2025-01-15"` |
| `fecha_vencimiento` | Fecha de renovación | `"2025-10-25"` |
| `precio` | Precio con símbolo de moneda | `"Q99.99"` |
| `frecuencia` | Periodicidad (MENSUAL/TRIMESTRAL/ANUAL) | `"MENSUAL"` |

---

## 🚀 Flujo de la Aplicación

```
Usuario abre la app
    │
    └──► Herramientas Financieras
         │
         └──► Notificador de Suscripciones
              │
              └──► Presiona botón "+" (Agregar)
                   │
                   └──► Llena el formulario:
                        - Nombre: Netflix Premium
                        - Monto: 99.99
                        - Frecuencia: MENSUAL
                        - Fecha renovación: 25/10/2025
                        │
                        └──► Presiona "Guardar"
                             │
                             ├──► 💾 Se guarda en la base de datos local
                             │
                             └──► 📡 Se envía a n8n automáticamente
                                  │
                                  └──► ✅ Toast: "📊 Suscripción sincronizada con n8n"
```

---

## 📁 Archivos Modificados

### `AddSubscriptionActivity.java`
- **Línea 30:** URL del webhook actualizada
- **Línea 187-242:** Método `sendSubscriptionToN8n()` con nuevo formato

**Cambios principales:**
- ✅ URL actualizada al nuevo webhook
- ✅ Formato de datos según especificación
- ✅ Obtiene nombre y email del usuario desde SessionManager
- ✅ Fechas en formato ISO (yyyy-MM-dd)
- ✅ Precio con símbolo de moneda
- ✅ Agrega campo "frecuencia"

---

## 🧪 Cómo Probar

### Opción 1: Desde la App

1. **Compila y ejecuta la app**
2. **Navega a:** Inicio → Herramientas Financieras → Notificador de Suscripciones
3. **Presiona el botón "+"** para agregar suscripción
4. **Llena el formulario:**
   - Nombre: Netflix
   - Monto: 99.99
   - Frecuencia: MENSUAL
   - Fecha: Selecciona cualquier fecha futura
5. **Presiona "Guardar"**
6. **Verifica:**
   - ✅ Toast: "Suscripción guardada exitosamente"
   - ✅ Toast: "📊 Suscripción sincronizada con n8n"

### Opción 2: Script de Prueba Manual

Ejecuta el script `test_suscripcion_n8n.bat` que simula el envío:

```bash
test_suscripcion_n8n.bat
```

### Opción 3: cURL Manual

```bash
curl -X POST \
  "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6" \
  -H "Content-Type: application/json" \
  -d '{
    "action": "subscription_added",
    "userId": "123",
    "data": {
      "herramienta": "suscripciones",
      "cliente": "Juan Pérez",
      "email": "juan@example.com",
      "plan": "Netflix Premium",
      "fecha_inicio": "2025-01-15",
      "fecha_vencimiento": "2025-10-25",
      "precio": "Q99.99",
      "frecuencia": "MENSUAL"
    }
  }'
```

---

## ⚙️ Configuración en n8n

Para recibir los datos, configura en n8n:

### 1. Crea o Abre un Workflow

### 2. Agrega un Nodo "Webhook"
- **HTTP Method:** `POST`
- **Path:** `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`
- **Response Mode:** `Last Node` o `Response Node`
- **Authentication:** `None`

### 3. (Opcional) Agrega Nodos de Procesamiento

Ejemplo de workflow básico:

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Webhook   │─────►│ Filtrar datos│─────►│ Guardar en  │
│   (POST)    │      │ suscripciones│      │   Database  │
└─────────────┘      └──────────────┘      └─────────────┘
                            │
                            ├────────────────┐
                            │                │
                      ┌─────▼──────┐   ┌────▼──────┐
                      │ Enviar     │   │ Notificar │
                      │ Email      │   │ Slack     │
                      └────────────┘   └───────────┘
```

### 4. Activa el Workflow
⚠️ **MUY IMPORTANTE:** El toggle debe estar en VERDE (Active)

### 5. Verifica la URL
Debería mostrar:
```
Production URL:
https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
```

---

## 📊 Datos Disponibles en n8n

Dentro de n8n, puedes acceder a los datos con estas expresiones:

```javascript
// Acción realizada
{{ $json.action }}  // "subscription_added"

// ID del usuario
{{ $json.userId }}  // "123"

// Datos de la suscripción
{{ $json.data.herramienta }}         // "suscripciones"
{{ $json.data.cliente }}             // "Juan Pérez"
{{ $json.data.email }}               // "juan@example.com"
{{ $json.data.plan }}                // "Netflix Premium"
{{ $json.data.fecha_inicio }}        // "2025-01-15"
{{ $json.data.fecha_vencimiento }}   // "2025-10-25"
{{ $json.data.precio }}              // "Q99.99"
{{ $json.data.frecuencia }}          // "MENSUAL"
```

---

## 🔧 Solución de Problemas

### ❌ Error: "Network error"
**Causa:** No hay conexión a Internet
**Solución:** Verifica la conexión del dispositivo

### ❌ Error: "HTTP Error: 404"
**Causa:** El workflow no está activo o la URL es incorrecta
**Solución:**
1. Abre n8n
2. Verifica que el workflow esté ACTIVO (toggle verde)
3. Verifica que el path sea exactamente: `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`

### ❌ Error: "HTTP Error: 500"
**Causa:** Error en el workflow de n8n
**Solución:**
1. Abre n8n
2. Ve a "Executions" → "Error"
3. Revisa el error específico
4. Corrige la configuración del workflow

### ⚠️ No se muestra el Toast de sincronización
**Causa:** El callback no se está ejecutando
**Solución:**
1. Revisa los logs con: `adb logcat -s AddSubscriptionActivity`
2. Busca mensajes que empiecen con "✅" o "❌"

---

## 📝 Notas Importantes

1. **Fechas en formato ISO:** Las fechas se envían como `yyyy-MM-dd` (ej: 2025-01-15)
2. **Precio con símbolo:** El precio incluye el símbolo de moneda (ej: Q99.99)
3. **Datos del usuario:** Se obtienen automáticamente de la sesión actual
4. **Frecuencia:** Puede ser MENSUAL, TRIMESTRAL o ANUAL
5. **Sin errores de compilación:** El código está listo para usarse ✅

---

## 🎉 ¡Listo!

Tu app ahora envía automáticamente los datos de suscripciones a n8n cuando el usuario las guarda.

**Fecha de actualización:** ${new Date().toLocaleDateString('es-ES')}
**Estado:** ✅ Configurado y Funcionando

