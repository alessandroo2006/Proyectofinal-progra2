# 🔧 Cómo Configurar el Workflow en n8n

## 📋 Descripción
Esta guía te explica cómo configurar n8n para recibir y procesar los eventos que envía tu app Android.

## 📥 Importar el Workflow

### Opción 1: Importar el JSON (Recomendado)
1. **Abre n8n** en tu navegador
2. Ve a **Workflows** → **Add workflow** (o un workflow existente)
3. Haz clic en los **tres puntos** (menú) → **Import from File**
4. Selecciona el archivo: `EJEMPLO_WORKFLOW_N8N.json`
5. Haz clic en **Import**

### Opción 2: Crear Manualmente
Si prefieres crear el workflow desde cero, sigue la guía de construcción manual al final.

## 🎯 Estructura del Workflow

```
Webhook Entrada (POST)
    │
    ├──► Es Test? ────────────► Procesar Test ──────────┐
    │                                                    │
    ├──► Son Datos Financieros? ──► Procesar Datos ────►├──► Responder a la App
    │                                    │              │
    │                                    ├──► Guardar DB│
    │                                    └──► Enviar Email
    │
    └──► Acceso a Herramientas? ──► Procesar Acceso ───►├
                                             │           │
                                             └──► Slack  │
```

## 🔧 Configuración Paso a Paso

### 1. Webhook Node (Nodo de Entrada)

Este es el punto de entrada para todas las peticiones de la app.

**Configuración:**
- **HTTP Method:** POST
- **Path:** `webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93`
- **Response Mode:** Response Node (para enviar respuesta personalizada)

**Resultado:**
Tu webhook estará disponible en:
```
https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
```

### 2. Nodos de Filtrado (IF)

Estos nodos separan los eventos por tipo.

#### Nodo "Es Test?"
**Condición:**
- `{{$json.body.action}}` **equals** `test_connection`

#### Nodo "Son Datos Financieros?"
**Condición:**
- `{{$json.body.action}}` **equals** `financial_data_update`

#### Nodo "Acceso a Herramientas?"
**Condición:**
- `{{$json.body.action}}` **equals** `financial_tools_accessed`

### 3. Nodos de Procesamiento (SET)

Estos nodos extraen y formatean los datos recibidos.

#### Procesar Test
Extrae:
- `message`: "Test de conexión recibido correctamente"
- `userId`: `{{$json.body.userId}}`
- `timestamp`: `{{$json.body.timestamp}}`

#### Procesar Datos Financieros
Extrae:
- `userId`: `{{$json.body.userId}}`
- `total_balance`: `{{$json.body.data.total_balance}}`
- `monthly_income`: `{{$json.body.data.monthly_income}}`
- `monthly_expenses`: `{{$json.body.data.monthly_expenses}}`
- `monthly_savings`: `{{$json.body.data.monthly_savings}}`

#### Procesar Acceso Herramientas
Extrae:
- `userId`: `{{$json.body.userId}}`
- `event`: "Usuario accedió a herramientas financieras"
- `timestamp`: `{{$json.body.timestamp}}`

### 4. Nodo de Respuesta

**Tipo:** Respond to Webhook

**Configuración:**
- **Response Mode:** JSON
- **Status Code:** 200
- **Body:**
```json
{
  "status": "success",
  "message": "Data received successfully",
  "data": {
    "received_at": "{{new Date().toISOString()}}",
    "action": "{{$json.body.action}}",
    "userId": "{{$json.body.userId}}"
  }
}
```

## 🚀 Activar el Workflow

1. **Guarda el workflow** (Ctrl+S o botón Save)
2. **Activa el workflow** usando el toggle en la esquina superior derecha
3. El webhook estará **activo** y listo para recibir peticiones

**Importante:** El workflow debe estar **activo** para recibir peticiones. Si está inactivo, la app recibirá errores 404.

## 🎨 Extensiones Opcionales

### A. Guardar en Base de Datos (PostgreSQL)

**Nodo:** Postgres
**Operación:** Insert
**Tabla:** `financial_events`
**Columnas:**
- `userId`
- `event_type`
- `event_data` (JSON)
- `timestamp`

**Conexión:**
Necesitas configurar una credencial de PostgreSQL en n8n.

**SQL para crear la tabla:**
```sql
CREATE TABLE financial_events (
    id SERIAL PRIMARY KEY,
    userId VARCHAR(50),
    event_type VARCHAR(100),
    event_data JSONB,
    timestamp BIGINT,
    created_at TIMESTAMP DEFAULT NOW()
);
```

### B. Notificar en Slack

**Nodo:** Slack
**Operación:** Post Message
**Channel:** `#finanzas-app`
**Message:**
```
🔔 Nuevo evento de la app: {{$json.body.action}}
Usuario: {{$json.body.userId}}
Timestamp: {{$json.body.timestamp}}
```

**Conexión:**
Necesitas conectar tu workspace de Slack a n8n.

### C. Enviar Email

**Nodo:** Send Email
**To:** `admin@tudominio.com`
**Subject:** `Nuevos datos financieros recibidos`
**Body (HTML):**
```html
<h2>Datos Financieros Actualizados</h2>
<p><strong>Usuario:</strong> {{$json.body.userId}}</p>
<p><strong>Balance Total:</strong> {{$json.body.data.total_balance}}</p>
<p><strong>Ingresos:</strong> {{$json.body.data.monthly_income}}</p>
<p><strong>Gastos:</strong> {{$json.body.data.monthly_expenses}}</p>
<p><strong>Ahorros:</strong> {{$json.body.data.monthly_savings}}</p>
```

**Conexión:**
Configura las credenciales SMTP en n8n.

### D. Integrar con Google Sheets

**Nodo:** Google Sheets
**Operación:** Append
**Spreadsheet ID:** Tu ID de spreadsheet
**Sheet:** Hoja1
**Columns:**
- Fecha
- Usuario
- Acción
- Balance
- Ingresos
- Gastos
- Ahorros

## 🧪 Probar el Workflow

### Opción 1: Desde la App Android
1. Abre la app
2. Inicia sesión
3. Toca el botón de herramientas financieras
4. Ve a n8n y revisa las ejecuciones

### Opción 2: Desde el Test Webhook Button
1. En el nodo Webhook, haz clic en **Listen for test event**
2. Abre la app y genera un evento
3. n8n capturará la petición
4. Haz clic en **Execute Workflow** para procesarla

### Opción 3: Usando el Script Python
1. Abre una terminal
2. Ejecuta: `python test_webhook.py`
3. El script enviará 4 eventos de prueba
4. Revisa las ejecuciones en n8n

### Opción 4: Usando curl
```bash
curl -X POST https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93 \
  -H "Content-Type: application/json" \
  -d '{
    "action": "test_connection",
    "userId": "test_user",
    "timestamp": "1729780000000",
    "data": "Testing from curl"
  }'
```

## 📊 Ver las Ejecuciones

1. En n8n, ve a **Executions** (panel izquierdo)
2. Verás todas las ejecuciones del workflow
3. Haz clic en cualquiera para ver:
   - Datos de entrada
   - Flujo de ejecución
   - Datos de salida de cada nodo
   - Errores (si los hay)

## 🐛 Solución de Problemas

### El webhook no recibe peticiones

**Solución:**
- ✅ Verifica que el workflow esté **activo**
- ✅ Comprueba que la URL del webhook sea correcta
- ✅ Revisa que no haya errores en los logs de n8n

### Error 404 en la app

**Causa:** El workflow está inactivo o la URL es incorrecta

**Solución:**
- Activa el workflow en n8n
- Verifica la URL en `MainActivity.java` línea 31

### Error 500 en n8n

**Causa:** Error en el procesamiento del workflow

**Solución:**
- Revisa los logs de ejecución en n8n
- Verifica que las expresiones `{{$json.body.xxx}}` sean correctas
- Comprueba que todos los nodos obligatorios estén conectados

### Los datos no se guardan en la base de datos

**Causa:** Credenciales incorrectas o tabla no existe

**Solución:**
- Verifica las credenciales de la base de datos
- Crea la tabla usando el SQL proporcionado
- Habilita el nodo (por defecto está deshabilitado)

## 💡 Tips y Mejores Prácticas

1. **Mantén el workflow activo:** Configura alertas si se desactiva
2. **Usa error workflows:** Crea un workflow para manejar errores
3. **Logging:** Agrega nodos Function para log personalizado
4. **Versiones:** Guarda versiones del workflow regularmente
5. **Testing:** Prueba siempre después de modificar el workflow
6. **Documentación:** Usa las notas en los nodos para documentar
7. **Credenciales:** Usa variables de entorno para datos sensibles

## 📚 Recursos Adicionales

- [Documentación oficial de n8n](https://docs.n8n.io/)
- [n8n Community](https://community.n8n.io/)
- [Webhook Node Documentation](https://docs.n8n.io/integrations/builtin/core-nodes/n8n-nodes-base.webhook/)

## 🎯 Próximos Pasos

Una vez que tu workflow básico funcione:

1. **Personaliza las respuestas** según tus necesidades
2. **Agrega analytics** para rastrear el uso
3. **Implementa notificaciones** personalizadas
4. **Integra con más servicios** (CRM, Analytics, etc.)
5. **Crea dashboards** con los datos recopilados

---
**Estado:** Listo para usar  
**Última actualización:** Octubre 2025

