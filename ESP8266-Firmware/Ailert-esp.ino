#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#include <Servo.h>

#define WIFI_SSID "Test"
#define WIFI_PASSWORD ""
#define FIREBASE_HOST "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
#define FIREBASE_AUTH "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"

#define MQ7_PIN A0
#define BUZZER_PIN 5  // D1
#define SERVO_PIN 12  // D6 (GPIO 12)

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
Servo windowServo;

void setup() {
  Serial.begin(115200); //
  pinMode(BUZZER_PIN, OUTPUT);
  windowServo.attach(SERVO_PIN);
  windowServo.write(0); // Start at 0 (Closed)
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) { delay(500); Serial.print("."); }
  
  config.host = FIREBASE_HOST;
  config.signer.tokens.legacy_token = FIREBASE_AUTH;
  Firebase.begin(&config, &auth);
}

void loop() {
  int ppm = map(analogRead(MQ7_PIN), 0, 1023, 0, 100);
  Firebase.setInt(fbdo, "/current_ppm", ppm);

  int threshold = 50; 
  if (Firebase.getInt(fbdo, "/trigger_threshold")) threshold = fbdo.intData();
  
  String cmd = "none";
  if (Firebase.getString(fbdo, "/manual_control")) cmd = fbdo.stringData();

  // Safety Logic with 150 Degree Angle
  if (ppm >= threshold) {
    digitalWrite(BUZZER_PIN, HIGH);
    windowServo.write(150); // UPDATED: 150 degrees for safety
    Serial.printf("ALERT! Moving to 150 deg. PPM: %d\n", ppm);
  } else {
    digitalWrite(BUZZER_PIN, LOW);
    if (cmd == "down") {
        windowServo.write(150); // UPDATED: 150 degrees for manual down
        Serial.println("Manual: 150 deg");
    }
    else if (cmd == "up") {
        windowServo.write(0);
        Serial.println("Manual: 0 deg");
    }
  }
  delay(2000);
}