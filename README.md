# g2a.scrapper
This is a G2A product scrapper. Uses gmail to send a notification to a desired account.

## Input
config/config.properties
```
email.enable= true
email.hostname= smtp.googlemail.com
email.username= [your gmail account]
email.password= [your gmail account password] 
email.receiver= [email that where you desire to be notified]
```

products.db
```
[price you want to purchase],[g2a url to product]
10,https://www.g2a.com/star-wars-battlefront-2-2017-origin-key-global-english-only-i10000068865010
```

## How to Run
Use `./gradlew run` (no arguments)

## Insights
G2A uses an anti-bot protection so I used curl to mimic a browser request. Need to check if skrape{it} framework lets us make a request without default settings, so we can override them with browser headers.

## PLEASE SUPPORT GAMES BY BUYING DIRECTLY FROM THE DEVELOPER. I USED THIS PROJECT TO LEARN ABOUT KOTLIN & JAVA & IDEA
