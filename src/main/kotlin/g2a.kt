import g2a.Price
import g2a.Product
import it.skrape.core.htmlDocument
import it.skrape.selects.DocElement
import it.skrape.selects.and
import it.skrape.selects.html5.span
import java.util.concurrent.TimeUnit


var map = mapOf(
    "Steam Gift Card 100EUR Global" to
            "https://www.g2a.com/steam-gift-card-100-eur-steam-key-global-i10000000258129",
    "Red Dead Redemption 2" to
            "https://www.g2a.com/red-dead-redemption-2-rockstar-key-global-i10000174280024"

)

fun main() {
    for (e in map){
        cmd(e.key, e.value)
        Thread.sleep(((Math.random() * 5000).toLong()))
    }
}

fun cmd(name: String, url: String) {

    var prod = Product(name)

    // TODO: Maybe a db of entries?
    val process = ProcessBuilder("sh", "libs/g2a.sh", url).start()
    var sellers: List<DocElement> = ArrayList()
    var prices: List<DocElement> = ArrayList()

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

        for (i in sellers.indices){
            // Remove the nonsense from G2A
            val price = prices[i].text.removeSuffix("EUR").trim()
            val seller = sellers[i].text.trim()
            var p = Price(price, seller)
            prod.addPrice(p)
        }

        println("Product[" + prod.getName() + "] Best Price: " + prod.getBestPrice())
    }
}

