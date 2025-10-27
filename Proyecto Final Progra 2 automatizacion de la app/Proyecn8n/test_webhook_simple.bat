@echo off
REM Script simple para probar el webhook desde Windows
REM Uso: Doble click en este archivo

echo ========================================
echo Probando Webhook de n8n
echo ========================================
echo.
echo URL: https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
echo.

curl -X POST https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6 ^
  -H "Content-Type: application/json" ^
  -d "{\"action\":\"test\",\"userId\":\"test\",\"timestamp\":\"1234567890\",\"data\":\"test from windows\"}"

echo.
echo.
echo ========================================
echo Si ves "status":"success" = FUNCIONA
echo Si ves error 404 = Workflow no activo
echo ========================================
echo.
pause
