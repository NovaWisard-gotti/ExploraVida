#!/bin/sh
#
# Lanzador de Gradle para ExploraVida.
#
# NOTA IMPORTANTE
# El archivo binario gradle/wrapper/gradle-wrapper.jar no se incluye en este
# paquete porque no puede generarse sin conexion a los repositorios de Gradle.
# Para crearlo basta con ejecutar una vez, con Gradle instalado:
#
#     gradle wrapper --gradle-version 8.7
#
# El flujo de GitHub Actions incluido en .github/workflows/android-build.yml
# ya realiza ese paso automaticamente antes de compilar.

APP_HOME=$(cd "$(dirname "$0")" && pwd)
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ -f "$WRAPPER_JAR" ]; then
    exec "${JAVA_HOME:+$JAVA_HOME/bin/}java" \
        -classpath "$WRAPPER_JAR" \
        org.gradle.wrapper.GradleWrapperMain "$@"
fi

if command -v gradle >/dev/null 2>&1; then
    echo "gradle-wrapper.jar no encontrado. Generando el wrapper con Gradle local..."
    gradle wrapper --gradle-version 8.7 || exit 1
    exec "${JAVA_HOME:+$JAVA_HOME/bin/}java" \
        -classpath "$WRAPPER_JAR" \
        org.gradle.wrapper.GradleWrapperMain "$@"
fi

echo "ERROR: falta gradle/wrapper/gradle-wrapper.jar y no hay Gradle instalado."
echo "Instala Gradle 8.7 y ejecuta: gradle wrapper --gradle-version 8.7"
exit 1
