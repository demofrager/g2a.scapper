package g2a

import it.skrape.core.htmlDocument
import it.skrape.selects.DocElement
import it.skrape.selects.and
import it.skrape.selects.html5.span
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.Email
import org.apache.commons.mail.SimpleEmail
import java.math.BigDecimal


var map = mapOf(
    "Steam Gift Card 100EUR Global" to
            "https://www.g2a.com/steam-gift-card-100-eur-steam-key-global-i10000000258129",
    "Red Dead Redemption 2" to
            "https://www.g2a.com/red-dead-redemption-2-rockstar-key-global-i10000174280024"
)

fun main() {

    var products: HashSet<Product> = hashSetOf()
    for (e in map){
        val p = scrapeProduct(e.key, e.value)
        Thread.sleep(((Math.random() * 5000).toLong()))
        products.add(p)
    }

    // TODO: Apply notification process
    for (p in products){
        if (p.toNotify) {
            triggerNotification(p)
        } else
        {
            println("No notification for $p")
        }
    }

}

fun triggerNotification(p: Product): String =
    SimpleEmail().apply {
        hostName = "smtp.googlemail.com"
        setSmtpPort(465)
        setAuthenticator(
            DefaultAuthenticator("username", "password")
        )
        isSSLOnConnect = true
        setFrom("user@gmail.com")
        subject = "TestMail"
        setMsg("This is a test mail ... :-)")
        addTo("foo@bar.com")
    }.send()




fun scrapeProduct(name: String, url: String): Product {

    var prod = Product(name)
    prod.notificationThreshold = BigDecimal(10)
    // TODO: Maybe a db of entries?
    val process = ProcessBuilder("sh", "libs/g2a.sh", url).start()
    var sellers: List<DocElement> = mutableListOf()
    var prices: List<DocElement> = mutableListOf()

    process.inputStream.reader(Charsets.UTF_8).use {
        val html = it.readText()

        htmlDocument(html) {

            span {
                withClass = "seller-info__user"
                findAll {
                    sellers = this
                }
            }

            span {
                withClass = "offer__price" and "price"
                findAll {
                    prices = this
                }
            }
        }

        // Skipping the first seller element because it is the selected on the webpage to purchase.
        // And we only want the list
        sellers = sellers.subList(1, sellers.size)

        for (i in sellers.indices) {
            // Remove the nonsense from G2A
            val price = prices[i].text.removeSuffix("EUR").trim()
            val seller = sellers[i].text.trim()
            var p = Price(price.toBigDecimal(), seller)
            prod.addPrice(p)
        }
        println("Porduct: $prod has Best Price: ${prod.bestPrice}")
    }
    return prod
}

