@echo off
echo ===============================================
echo    PRUEBA DE WEBHOOK - SUSCRIPCIONES
echo ===============================================
echo.
echo URL: https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
echo.
echo Enviando datos de suscripcion de prueba...
echo.

curl -X POST ^
  "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6" ^
  -H "Content-Type: application/json" ^
  -d "{\"action\":\"subscription_added\",\"userId\":\"123\",\"data\":{\"herramienta\":\"suscripciones\",\"cliente\":\"Juan Perez\",\"email\":\"juan@example.com\",\"plan\":\"Netflix Premium\",\"fecha_inicio\":\"2025-01-15\",\"fecha_vencimiento\":\"2025-10-25\",\"precio\":\"Q99.99\",\"frecuencia\":\"MENSUAL\"}}"

echo.
echo ===============================================
echo    PRUEBA COMPLETADA
echo ===============================================
echo.
echo Si ves una respuesta JSON o mensaje de exito,
echo el webhook esta funcionando correctamente.
echo.
echo Datos enviados:
echo   - Herramienta: suscripciones
echo   - Cliente: Juan Perez
echo   - Email: juan@example.com
echo   - Plan: Netflix Premium
echo   - Fecha inicio: 2025-01-15
echo   - Fecha vencimiento: 2025-10-25
echo   - Precio: Q99.99
echo   - Frecuencia: MENSUAL
echo.
pause

