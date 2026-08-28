@rem Lanzador de Gradle para ExploraVida (Windows).
@rem
@rem El binario gradle\wrapper\gradle-wrapper.jar no se incluye en este paquete
@rem porque no puede generarse sin conexion. Para crearlo, con Gradle instalado:
@rem
@rem     gradle wrapper --gradle-version 8.7
@rem
@if "%DEBUG%"=="" @echo off
setlocal

set DIRNAME=%~dp0
set WRAPPER_JAR=%DIRNAME%gradle\wrapper\gradle-wrapper.jar

if exist "%WRAPPER_JAR%" goto runWrapper

where gradle >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo gradle-wrapper.jar no encontrado. Generando el wrapper...
    call gradle wrapper --gradle-version 8.7
    if exist "%WRAPPER_JAR%" goto runWrapper
)

echo ERROR: falta gradle\wrapper\gradle-wrapper.jar y no hay Gradle instalado.
echo Instala Gradle 8.7 y ejecuta: gradle wrapper --gradle-version 8.7
exit /b 1

:runWrapper
if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
) else (
    set JAVA_EXE=java.exe
)
"%JAVA_EXE%" -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
