@echo off

rmdir /S /Q "src\main\resources\static"
mkdir "src\main\resources\static"

xcopy "frontend\src\main\resources\static\*" "src\main\resources\static\" /E /Y /I

echo Frontend synced successfully.
pause