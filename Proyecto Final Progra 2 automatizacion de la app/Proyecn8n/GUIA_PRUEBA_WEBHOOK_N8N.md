# 🧪 Guía de Prueba - Integración n8n Webhook

## ✅ Estado Actual
Tu aplicación **YA ESTÁ CONECTADA** con n8n usando el webhook:
```
https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
```

## 📱 Cómo Probar la Integración

### Método 1: Abrir la Aplicación
1. **Instala y abre la app** en tu dispositivo/emulador
2. **Inicia sesión** con tu cuenta
3. **Observa las notificaciones Toast**:
   - ✅ "Conexión exitosa con n8n" (aparece automáticamente)
   - ✅ "Datos sincronizados con n8n" (aparece al cargar datos financieros)

### Método 2: Presionar el Botón de Herramientas
1. En la pantalla principal (MainActivity)
2. **Desplázate hacia abajo** hasta ver la sección "Herramientas Financieras"
3. **Toca el botón** con el icono 🛠️ "Herramientas Financieras"
4. **Observa la notificación**: "Evento de herramientas registrado"
5. La app te llevará a la pantalla de herramientas

## 🔍 Ver los Logs (Android Studio)

### Opción A: Filtro por Tag
```bash
# En el Logcat de Android Studio
Tag: N8nWebhookClient
```

### Opción B: Búsqueda de Texto
Busca en Logcat:
- "Webhook test successful"
- "Webhook call successful"
- "N8n webhook service initialized"

### Logs Esperados
```
D/N8nWebhookClient: N8n webhook service initialized successfully
D/N8nWebhookClient: Webhook call to https://userfox.app.n8n.cloud/... successful. Response: ...
```

## 🎯 Eventos que se Envían Automáticamente

| Evento | Cuándo | Acción en n8n |
|--------|--------|---------------|
| `test_connection` | Al abrir la app | Verifica que el webhook funciona |
| `financial_data_update` | Al cargar datos | Envía balance, ingresos, gastos, ahorros |
| `financial_tools_accessed` | Al tocar botón herramientas | Registra que el usuario accedió a herramientas |

## 📊 Datos que Llegan a n8n

### Ejemplo de Payload Completo
```json
{
  "action": "financial_data_update",
  "userId": "123",
  "timestamp": "1729780000000",
  "data": {
    "total_balance": 12450.00,
    "monthly_income": "Q 5,500",
    "monthly_expenses": "Q 3,900",
    "monthly_savings": "Q 1,600"
  }
}
```

## 🛠️ Verificar en n8n

1. **Abre tu flujo de trabajo en n8n**
2. **Ve a la sección de Webhook**
3. **Revisa las ejecuciones recientes**
4. Deberías ver:
   - Peticiones POST entrantes
   - Datos JSON con la estructura mostrada arriba
   - Estado: Success (200)

## 🐛 Solución de Problemas

### ❌ "Error al conectar con n8n"
**Posibles causas:**
- ❗ No hay conexión a Internet
- ❗ El webhook de n8n está inactivo
- ❗ La URL del webhook cambió

**Solución:**
1. Verifica tu conexión a Internet
2. Verifica que el workflow en n8n esté activo
3. Revisa que la URL del webhook sea correcta en `MainActivity.java` (línea 31)

### ❌ Timeout o No Response
**Posibles causas:**
- ⏱️ n8n está procesando lento
- ⏱️ Hay muchas peticiones en cola

**Solución:**
- El timeout está configurado a 30 segundos
- Verifica los logs de n8n para ver si llegó la petición

### ❌ No Aparecen Notificaciones
**Posibles causas:**
- 📱 Las notificaciones Toast están siendo bloqueadas
- 📱 La app se ejecutó muy rápido

**Solución:**
- Revisa los logs en Android Studio
- Las peticiones se están enviando aunque no veas el Toast

## 🎨 Vista del Botón de Herramientas

El botón se ve así en la pantalla principal:

```
┌─────────────────────────────────────────┐
│  🛠️    Herramientas Financieras      ▶  │
│       Funciones avanzadas para          │
│       optimizar tus finanzas            │
└─────────────────────────────────────────┘
```

**Ubicación:** 
- Pantalla: MainActivity (pantalla principal)
- Posición: Abajo de las tarjetas de Ingresos, Gastos y Ahorros
- ID: `btn_goto_financial_tools`

## 📋 Checklist de Verificación

- [ ] La app se conecta a Internet
- [ ] Aparece notificación de "Conexión exitosa con n8n"
- [ ] Al tocar el botón de herramientas, se registra el evento
- [ ] En n8n se reciben las peticiones
- [ ] Los logs muestran "Webhook call successful"
- [ ] Los datos JSON tienen la estructura correcta

## 🚀 Próximos Pasos

Si todo funciona correctamente, puedes:

1. **Configurar tu flujo en n8n** para:
   - Guardar los datos en una base de datos
   - Enviar notificaciones personalizadas
   - Crear reportes automáticos
   - Integrar con otras herramientas

2. **Agregar más eventos** en la app:
   - Cuando se crea un nuevo gasto
   - Cuando se alcanza una meta de ahorro
   - Cuando hay alertas importantes
   - Cuando el usuario completa logros

3. **Personalizar las respuestas de n8n**:
   - Enviar consejos financieros personalizados
   - Calcular proyecciones
   - Sugerir optimizaciones

## 📞 Soporte

Si necesitas ayuda adicional:
- Revisa el archivo `INTEGRACION_N8N_WEBHOOK.md` para detalles técnicos
- Consulta los logs en Android Studio
- Verifica el estado del webhook en n8n

---
**Última actualización**: Octubre 2025
**Estado**: ✅ Totalmente Funcional

