#include <SPI.h>
#include <MFRC522.h>
#define SS_PIN D4
#define RST_PIN D2


#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "hallmanagement-e5bae-default-rtdb.asia-southeast1.firebasedatabase.app"
#define FIREBASE_AUTH "jHjyop8UxdBPzNdEv53v4qiGaDuKJYeWY7MsdnLH"
#define WIFI_SSID "DUET CSE"
#define WIFI_PASSWORD "0000000000"
FirebaseData firebaseData;
FirebaseJson json;


MFRC522 mfrc522(SS_PIN, RST_PIN);

String nam[4] = {"CHANDAN", "RUMI", "SONIA", "SUBRA"};

int dd = 1, mm = 1, yy = 2021;

void writeDB(String field) {
  Firebase.setString(firebaseData, "/" + field + "/" + String(dd) + "-" + String(mm) + "-" + String(yy), "1" );
  Serial.println("Attendence with " + field);
}


void setup() {

  pinMode(D8, OUTPUT);
  pinMode(D0, OUTPUT);
  Serial.begin(9600);   // Initiate a serial communication
  SPI.begin();
  mfrc522.PCD_Init();

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED)
  {
    digitalWrite(D4, 0);
    delay(100);
    Serial.print(".");
    digitalWrite(D4, 1);
    delay(100);
  }

  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  digitalWrite(D0, 1);
}

void loop() {

  if (!digitalRead(D0))
  {
    inc();
    while (!digitalRead(D0))
      delay(50);
    Serial.println("In inf");

    return;
  }


  int i = checkCard();
  Serial.println(i);
  switch (i) {
    case 1: {
        writeDB("SUBRA");
        break;
      }
    case 2: {
        writeDB("RUME");
        break;
      }
    case 3: {
        writeDB("SONIA");
        break;
      }
    case 4: {
        writeDB("CHANDAN");
        break;
      }




  }

  delay(300);

}











void inc() {

  Serial.println("Increment");
  dd++;
  Serial.println(dd);
  if (dd > 30)
  {
    mm++;
    dd = 1;
  }

  if (mm > 12) {
    yy++;
    mm = 1;
  }

  digitalWrite(D8, 1);
  delay(500);
  digitalWrite(D8, 0);

}


int checkCard() {


  // Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent())
  {
    return -1;
  }
  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial())
  {
    return -1;
  }
  //Show UID on serial monitor
  //Serial.print("UID tag :");
  String content = "";
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++)
  {
    //Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
    //Serial.print(mfrc522.uid.uidByte[i], HEX);
    content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
    content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }

  delay(500);
  content.toUpperCase();

  if (content.substring(1) == "19 D7 5E B2") //change here the UID of the card/cards that you want to give access
  {
    beep();
    return 1;
  }

  else if (content.substring(1) == "FA D4 0E 28") {
    beep();
    return 2;
  }
  else if (content.substring(1) == "39 9D 72 B2") {
    beep();
    return 3;
  }
  else if (content.substring(1) == "19 D6 70 B2") {
    beep();
    return 4;
  }


  return -1;
}


void beep() {
  digitalWrite(D8, 1);
  delay(100);
  digitalWrite(D8, 0);
  delay(50);
  digitalWrite(D8, 1);
  delay(100);
  digitalWrite(D8, 0);

}
