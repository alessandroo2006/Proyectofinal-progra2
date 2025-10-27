#!/usr/bin/env python3
"""
Script de prueba para verificar el webhook de n8n
Este script simula las peticiones que hace la app Android

Uso:
    python test_webhook.py

Requisitos:
    pip install requests
"""

import requests
import json
import time

# URL del webhook de n8n
WEBHOOK_URL = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6"

def print_separator():
    print("\n" + "="*60)

def test_connection():
    """Prueba básica de conexión (Evento 1)"""
    print_separator()
    print("🧪 TEST 1: Conexión Básica")
    print_separator()
    
    payload = {
        "action": "test_connection",
        "userId": "test_user",
        "timestamp": str(int(time.time() * 1000)),
        "data": "Testing connection from Python script"
    }
    
    print(f"📤 Enviando a: {WEBHOOK_URL}")
    print(f"📦 Payload:\n{json.dumps(payload, indent=2)}")
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        print(f"\n✅ Status Code: {response.status_code}")
        print(f"📥 Respuesta: {response.text}")
        
        if response.status_code == 200:
            print("✅ Test de conexión exitoso!")
            return True
        else:
            print(f"⚠️ Test falló con código: {response.status_code}")
            return False
    except requests.exceptions.Timeout:
        print("❌ Error: Timeout después de 30 segundos")
        return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Error de conexión: {e}")
        return False

def test_financial_data():
    """Simula el envío de datos financieros (Evento 2)"""
    print_separator()
    print("🧪 TEST 2: Datos Financieros")
    print_separator()
    
    payload = {
        "action": "financial_data_update",
        "userId": "123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "total_balance": 12450.00,
            "monthly_income": "Q 5,500",
            "monthly_expenses": "Q 3,900",
            "monthly_savings": "Q 1,600"
        }
    }
    
    print(f"📤 Enviando a: {WEBHOOK_URL}")
    print(f"📦 Payload:\n{json.dumps(payload, indent=2)}")
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        print(f"\n✅ Status Code: {response.status_code}")
        print(f"📥 Respuesta: {response.text}")
        
        if response.status_code == 200:
            print("✅ Datos financieros enviados correctamente!")
            return True
        else:
            print(f"⚠️ Test falló con código: {response.status_code}")
            return False
    except requests.exceptions.Timeout:
        print("❌ Error: Timeout después de 30 segundos")
        return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Error de conexión: {e}")
        return False

def test_financial_tools_access():
    """Simula el clic en el botón de herramientas financieras (Evento 3)"""
    print_separator()
    print("🧪 TEST 3: Acceso a Herramientas Financieras")
    print_separator()
    
    payload = {
        "action": "financial_tools_accessed",
        "userId": "123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "User accessed financial tools."
        }
    }
    
    print(f"📤 Enviando a: {WEBHOOK_URL}")
    print(f"📦 Payload:\n{json.dumps(payload, indent=2)}")
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        print(f"\n✅ Status Code: {response.status_code}")
        print(f"📥 Respuesta: {response.text}")
        
        if response.status_code == 200:
            print("✅ Evento de herramientas registrado correctamente!")
            return True
        else:
            print(f"⚠️ Test falló con código: {response.status_code}")
            return False
    except requests.exceptions.Timeout:
        print("❌ Error: Timeout después de 30 segundos")
        return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Error de conexión: {e}")
        return False

def test_custom_event():
    """Prueba con un evento personalizado"""
    print_separator()
    print("🧪 TEST 4: Evento Personalizado")
    print_separator()
    
    payload = {
        "action": "custom_test_event",
        "userId": "python_test",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "test_type": "custom",
            "message": "Este es un evento de prueba personalizado",
            "value": 9999,
            "extra_data": {
                "key1": "valor1",
                "key2": "valor2"
            }
        }
    }
    
    print(f"📤 Enviando a: {WEBHOOK_URL}")
    print(f"📦 Payload:\n{json.dumps(payload, indent=2)}")
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        print(f"\n✅ Status Code: {response.status_code}")
        print(f"📥 Respuesta: {response.text}")
        
        if response.status_code == 200:
            print("✅ Evento personalizado enviado correctamente!")
            return True
        else:
            print(f"⚠️ Test falló con código: {response.status_code}")
            return False
    except requests.exceptions.Timeout:
        print("❌ Error: Timeout después de 30 segundos")
        return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Error de conexión: {e}")
        return False

def run_all_tests():
    """Ejecuta todos los tests"""
    print("\n" + "🚀 INICIANDO PRUEBAS DEL WEBHOOK N8N ".center(60, "="))
    print(f"URL: {WEBHOOK_URL}")
    print("="*60)
    
    results = []
    
    # Test 1: Conexión básica
    results.append(("Conexión Básica", test_connection()))
    time.sleep(1)  # Pausa entre tests
    
    # Test 2: Datos financieros
    results.append(("Datos Financieros", test_financial_data()))
    time.sleep(1)
    
    # Test 3: Acceso a herramientas
    results.append(("Acceso a Herramientas", test_financial_tools_access()))
    time.sleep(1)
    
    # Test 4: Evento personalizado
    results.append(("Evento Personalizado", test_custom_event()))
    
    # Resumen
    print_separator()
    print("📊 RESUMEN DE RESULTADOS")
    print_separator()
    
    for test_name, result in results:
        status = "✅ PASÓ" if result else "❌ FALLÓ"
        print(f"{test_name:.<40} {status}")
    
    total_passed = sum(1 for _, result in results if result)
    total_tests = len(results)
    
    print_separator()
    print(f"Total: {total_passed}/{total_tests} tests exitosos")
    print_separator()
    
    if total_passed == total_tests:
        print("\n🎉 ¡Todos los tests pasaron exitosamente!")
        print("✅ Tu webhook de n8n está funcionando correctamente")
    else:
        print(f"\n⚠️ {total_tests - total_passed} test(s) fallaron")
        print("💡 Verifica que tu workflow en n8n esté activo y la URL sea correcta")
    
    print("\n" + "="*60)

def test_single(test_number):
    """Ejecuta un solo test"""
    tests = {
        1: ("Conexión Básica", test_connection),
        2: ("Datos Financieros", test_financial_data),
        3: ("Acceso a Herramientas", test_financial_tools_access),
        4: ("Evento Personalizado", test_custom_event)
    }
    
    if test_number in tests:
        name, func = tests[test_number]
        print(f"\n🚀 Ejecutando: {name}")
        func()
    else:
        print(f"❌ Test {test_number} no existe. Opciones: 1-4")

if __name__ == "__main__":
    import sys
    
    print("""
╔══════════════════════════════════════════════════════════════╗
║         🧪 SCRIPT DE PRUEBA - WEBHOOK N8N                   ║
║                                                              ║
║  Este script simula las peticiones que hace la app Android  ║
╚══════════════════════════════════════════════════════════════╝
    """)
    
    if len(sys.argv) > 1:
        try:
            test_num = int(sys.argv[1])
            test_single(test_num)
        except ValueError:
            print("❌ Argumento inválido. Usa: python test_webhook.py [1-4]")
    else:
        # Ejecutar todos los tests
        run_all_tests()
    
    print("\n💡 Consejos:")
    print("   • Revisa las ejecuciones en tu workflow de n8n")
    print("   • Los logs deben mostrar los eventos recibidos")
    print("   • Puedes ejecutar un test específico: python test_webhook.py [1-4]")
    print("\n✅ Script completado\n")
