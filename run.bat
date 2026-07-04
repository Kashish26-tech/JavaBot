
@echo off
cd /d E:\ChatbotApp

javac --module-path "C:\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" ^
      --add-modules javafx.controls ^
      -cp "lib\json-20240303.jar" ^
      ChatbotGUI.java ResponseEngine.java UserProfile.java ^
      ChatHistory.java GroqAI.java ^
      -d out

java --module-path "C:\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib" ^
     --add-modules javafx.controls ^
     -cp "out;lib\json-20240303.jar" ^
     ChatbotGUI

pause