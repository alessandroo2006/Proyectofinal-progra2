@echo off
REM Script de prueba del webhook de n8n (foxyti) - Windows Batch

echo ================================================
echo   PRUEBA DE WEBHOOK N8N - FOXYTI
echo ================================================
echo.

set WEBHOOK_URL=https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6

echo URL del Webhook:
echo    %WEBHOOK_URL%
echo.

echo Enviando peticion...
echo.

curl -X POST %WEBHOOK_URL% ^
  -H "Content-Type: application/json" ^
  -d "{\"action\":\"test_connection\",\"userId\":\"test_user\",\"timestamp\":\"1234567890\",\"data\":\"test from batch\"}" ^
  -w "\n\nCodigo de Estado: %%{http_code}\n" ^
  -s -S

echo.
echo.
echo ================================================
echo   FIN DE LA PRUEBA
echo ================================================
echo.
echo Si ves "status":"success" y "Codigo de Estado: 200", el webhook funciona.
echo Si ves error 404, el workflow en n8n NO esta activo.
echo.
echo Para mas informacion, consulta: DIAGNOSTICO_ERROR_404.md
echo.

pause

