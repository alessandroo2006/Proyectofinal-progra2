@echo off
echo ===============================================
echo    PRUEBA DE WEBHOOK N8N - NUEVA URL
echo ===============================================
echo.
echo URL: https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
echo.
echo Enviando peticion de prueba...
echo.

curl -X POST ^
  "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6" ^
  -H "Content-Type: application/json" ^
  -d "{\"action\":\"test_connection\",\"userId\":\"test_user\",\"timestamp\":\"%date% %time%\",\"data\":{\"message\":\"Prueba desde script\"}}"

echo.
echo ===============================================
echo    PRUEBA COMPLETADA
echo ===============================================
echo.
echo Si ves un mensaje de exito o JSON de respuesta,
echo el webhook esta funcionando correctamente.
echo.
pause

