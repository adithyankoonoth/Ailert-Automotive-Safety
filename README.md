# Ailert: IoT-Powered Automotive Safety System 🚗💨

**Ailert** is an "Active Life-Safety" system designed to combat Carbon Monoxide (CO) poisoning in vehicles. Unlike traditional detectors that only sound an alarm, Ailert takes **physical action** to save lives by automatically ventilating the vehicle.

---

## 🌟 Key Features
* **Real-time Monitoring:** Live CO (PPM) tracking via a dedicated Android dashboard.
* **Active Mitigation:** Automated window actuation (SG90 Servo moves to 150°) and high-decibel buzzer alert.
* **Safety-First Logic:** Hardware-level overrides that prevent windows from being closed if dangerous gas levels are detected.
* **Cloud Connectivity:** Ultra-low latency synchronization using Firebase (Singapore Region).
* **Emergency Integration:** One-tap emergency dialing (112) and saved personal contacts.

---

## 📁 Project Structure
* **`/Android-App`**: The Kotlin/Android Studio project for the mobile dashboard.
* **`/ESP8266-Firmware`**: Arduino/C++ source code for the NodeMCU controller.
* **`/Documentation`**: System block diagrams, circuit schematics, and technical specifications.

---

## 🛠️ Technical Stack
* **Microcontroller:** NodeMCU ESP8266
* **Sensors:** MQ7 Carbon Monoxide Sensor
* **Actuators:** SG90 Servo Motor, 5V Active Buzzer
* **Cloud Backend:** Firebase Realtime Database (RTDB)
* **Mobile App:** Android Studio (Kotlin)

---

## 🔌 Hardware Connections
| Component | Pin (NodeMCU) | Voltage |
| :--- | :--- | :--- |
| MQ7 Sensor | A0 (Analog) | Vin (5V) |
| SG90 Servo | D6 (PWM) | Vin (5V) |
| Active Buzzer | D1 (Digital) | 3.3V / 5V |

---

## 🚀 How to Run
1.  **Hardware:** Wire the components as per the schematics in the `/Documentation` folder.
2.  **Firmware:** Update the `WIFI_SSID` and `FIREBASE_HOST` in the `.ino` file and flash it to the ESP8266.
3.  **App:** Open the `/Android-App` folder in Android Studio, sync Gradle, and install it on your device.
4.  **Database:** Ensure your Firebase rules are set to `public` for testing.

---

## 🤝 Acknowledgments
Developed as a life-safety prototype for automotive innovation.
