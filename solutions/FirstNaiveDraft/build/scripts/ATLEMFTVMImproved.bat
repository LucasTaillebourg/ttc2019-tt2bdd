@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      http://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  ATLEMFTVMImproved startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and ATLEMFTVM_IMPROVED_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\ATLEMFTVMImproved.jar;%APP_HOME%\lib\org.eclipse.xtend.lib-2.17.0.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.emftvm.compiler-4.0.0-1.jar;%APP_HOME%\lib\org.eclipse.xtend.lib.macro-2.17.0.jar;%APP_HOME%\lib\org.eclipse.xtext.xbase.lib-2.17.0.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.engine-4.0.0-1.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.dsls-4.0.0-1.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.emftvm-4.0.0-1.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.core.emf-4.0.0-1.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.common-4.0.0-1.jar;%APP_HOME%\lib\guava-21.0.jar;%APP_HOME%\lib\org.eclipse.emf.ecore.xmi-2.16.0.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.emftvm.trace-4.0.0-1.jar;%APP_HOME%\lib\org.eclipse.emf.ecore-2.19.0.jar;%APP_HOME%\lib\org.eclipse.emf.common-2.16.0.jar;%APP_HOME%\lib\org.eclipse.equinox.common-3.6.200.v20130402-1505.jar;%APP_HOME%\lib\antlr-runtime-3.0.1.jar;%APP_HOME%\lib\asm-6.0_BETA.jar;%APP_HOME%\lib\org.eclipse.m2m.atl.core-4.0.0-1.jar;%APP_HOME%\lib\stringtemplate-3.1-b1.jar

@rem Execute ATLEMFTVMImproved
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %ATLEMFTVM_IMPROVED_OPTS%  -classpath "%CLASSPATH%" ttc2019.Driver %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable ATLEMFTVM_IMPROVED_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%ATLEMFTVM_IMPROVED_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
