#!/usr/bin/env python3
"""
Script para probar todos los eventos de Herramientas Financieras con n8n

Uso:
    python test_herramientas_financieras.py

Requisitos:
    pip install requests
"""

import requests
import json
import time
from datetime import datetime

# URL del webhook de n8n
WEBHOOK_URL = "https://userfox.app.n8n.cloud/webhook/finanzas-webhook"

def print_separator():
    print("\n" + "="*70)

def print_header(text):
    print_separator()
    print(f"  {text}")
    print_separator()

def test_event(event_name, payload):
    """Prueba un evento individual"""
    print(f"\n📤 Enviando: {event_name}")
    print(f"📦 Payload: {json.dumps(payload, indent=2)}")
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        print(f"✅ Status: {response.status_code}")
        print(f"📥 Respuesta: {response.text[:200]}")
        
        if response.status_code == 200:
            return True
        else:
            print(f"⚠️ Error con código: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Error: {e}")
        return False

def test_subscription_tracker():
    """Test: Rastreador de Suscripciones"""
    print_header("🧪 TEST 1: Rastreador de Suscripciones")
    
    payload = {
        "action": "subscription_tracker_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al rastreador de suscripciones",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return test_event("subscription_tracker_accessed", payload)

def test_expense_classifier():
    """Test: Clasificador de Gastos"""
    print_header("🧪 TEST 2: Clasificador de Gastos")
    
    payload = {
        "action": "expense_classifier_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al clasificador de gastos",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return test_event("expense_classifier_accessed", payload)

def test_deal_hunter():
    """Test: Cazador de Ofertas"""
    print_header("🧪 TEST 3: Cazador de Ofertas")
    
    payload = {
        "action": "deal_hunter_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al cazador de ofertas",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return test_event("deal_hunter_accessed", payload)

def test_voice_assistant_access():
    """Test: Acceso al Asistente por Voz"""
    print_header("🧪 TEST 4: Asistente por Voz - Acceso")
    
    payload = {
        "action": "voice_assistant_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al asistente por voz",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return test_event("voice_assistant_accessed", payload)

def test_voice_expense_added():
    """Test: Gasto Agregado por Voz"""
    print_header("🧪 TEST 5: Gasto por Voz (CON DATOS)")
    
    payload = {
        "action": "voice_expense_added",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "amount": "150.50",
            "merchant": "Super Mercado Central",
            "category": "Alimentación",
            "input_method": "voice"
        }
    }
    
    print("💡 Este evento incluye datos estructurados del gasto")
    return test_event("voice_expense_added", payload)

def test_voice_input_cancelled():
    """Test: Cancelación de Entrada por Voz"""
    print_header("🧪 TEST 6: Cancelación de Voz")
    
    payload = {
        "action": "voice_input_cancelled",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario canceló la entrada de voz",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return test_event("voice_input_cancelled", payload)

def test_gamification():
    """Test: Gamificación y Retos"""
    print_header("🧪 TEST 7: Gamificación y Retos")
    
    payload = {
        "action": "gamification_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió a gamificación y retos",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return test_event("gamification_accessed", payload)

def run_all_tests():
    """Ejecuta todos los tests"""
    print("\n" + "🚀 PRUEBAS DE HERRAMIENTAS FINANCIERAS → n8n ".center(70, "="))
    print(f"URL: {WEBHOOK_URL}")
    print(f"Hora: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print_separator()
    
    results = []
    
    # Test 1: Suscripciones
    results.append(("Rastreador de Suscripciones", test_subscription_tracker()))
    time.sleep(1)
    
    # Test 2: Clasificador
    results.append(("Clasificador de Gastos", test_expense_classifier()))
    time.sleep(1)
    
    # Test 3: Ofertas
    results.append(("Cazador de Ofertas", test_deal_hunter()))
    time.sleep(1)
    
    # Test 4: Voz - Acceso
    results.append(("Asistente por Voz (Acceso)", test_voice_assistant_access()))
    time.sleep(1)
    
    # Test 5: Voz - Gasto (IMPORTANTE)
    results.append(("Gasto por Voz (CON DATOS)", test_voice_expense_added()))
    time.sleep(1)
    
    # Test 6: Voz - Cancelar
    results.append(("Cancelación de Voz", test_voice_input_cancelled()))
    time.sleep(1)
    
    # Test 7: Gamificación
    results.append(("Gamificación y Retos", test_gamification()))
    
    # Resumen
    print_separator()
    print("📊 RESUMEN DE RESULTADOS")
    print_separator()
    
    for test_name, result in results:
        status = "✅ PASÓ" if result else "❌ FALLÓ"
        print(f"{test_name:.<50} {status}")
    
    total_passed = sum(1 for _, result in results if result)
    total_tests = len(results)
    
    print_separator()
    print(f"Total: {total_passed}/{total_tests} tests exitosos")
    print_separator()
    
    if total_passed == total_tests:
        print("\n🎉 ¡Todos los tests pasaron exitosamente!")
        print("✅ Todos los eventos de Herramientas Financieras funcionan correctamente")
        print("\n📋 Próximos pasos:")
        print("   1. Ve a n8n → Executions para ver los 7 eventos")
        print("   2. Configura flujos personalizados para cada evento")
        print("   3. Recompila e instala la app para probar en vivo")
    else:
        print(f"\n⚠️ {total_tests - total_passed} test(s) fallaron")
        print("💡 Verifica que tu workflow en n8n esté activo")
    
    print("\n" + "="*70)

def test_single(test_number):
    """Ejecuta un solo test"""
    tests = {
        1: ("Suscripciones", test_subscription_tracker),
        2: ("Clasificador", test_expense_classifier),
        3: ("Ofertas", test_deal_hunter),
        4: ("Voz - Acceso", test_voice_assistant_access),
        5: ("Voz - Gasto", test_voice_expense_added),
        6: ("Voz - Cancelar", test_voice_input_cancelled),
        7: ("Gamificación", test_gamification)
    }
    
    if test_number in tests:
        name, func = tests[test_number]
        print(f"\n🚀 Ejecutando Test {test_number}: {name}")
        func()
    else:
        print(f"❌ Test {test_number} no existe. Opciones: 1-7")

if __name__ == "__main__":
    import sys
    
    print("""
╔══════════════════════════════════════════════════════════════════════╗
║     🧪 PRUEBAS DE HERRAMIENTAS FINANCIERAS - WEBHOOK N8N            ║
║                                                                      ║
║  Este script prueba los 7 eventos de Herramientas Financieras       ║
╚══════════════════════════════════════════════════════════════════════╝
    """)
    
    if len(sys.argv) > 1:
        try:
            test_num = int(sys.argv[1])
            test_single(test_num)
        except ValueError:
            print("❌ Argumento inválido. Usa: python test_herramientas_financieras.py [1-7]")
    else:
        # Ejecutar todos los tests
        run_all_tests()
    
    print("\n💡 Consejos:")
    print("   • Revisa las ejecuciones en n8n → Executions")
    print("   • El evento 'voice_expense_added' tiene datos estructurados")
    print("   • Puedes ejecutar un test específico: python test_herramientas_financieras.py [1-7]")
    print("\n✅ Script completado\n")

