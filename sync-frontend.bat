@echo off
xcopy frontend\*.* src\main\resources\static\ /Y /E
echo Frontend synced into static/