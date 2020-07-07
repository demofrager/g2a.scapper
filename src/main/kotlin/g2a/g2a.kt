package g2a

import it.skrape.core.htmlDocument
import it.skrape.selects.DocElement
import it.skrape.selects.and
import it.skrape.selects.html5.span
import org.apache.commons.mail.DefaultAuthenticator

import org.apache.commons.mail.SimpleEmail
import com.natpryce.konfig.*
import javafx.beans.property.Property
import java.io.File
import java.io.IOException
import java.lang.StringBuilder
import kotlin.collections.HashSet
import kotlin.system.exitProcess

val email_enable = Key("email.enable", booleanType)
val email_hostname = Key("email.hostname", stringType)
val email_username = Key("email.username", stringType)
val email_password = Key("email.password", stringType)
val email_receiver = Key("email.receiver", stringType)
val programEnv = System.getenv("G2A")

fun main() {


    println("Environment var G2A: $programEnv")

    val propertiesFile: String = "$programEnv/config/config.properties"
    val productsFile: String = "$programEnv/products.db"

    val config = ConfigurationProperties.fromFile(File(propertiesFile))

    var products: HashSet<Product> = hashSetOf()

    File(productsFile).forEachLine {
        it.split(",").apply {
            val p = scrapeProduct(this[1], this[0])
            Thread.sleep(((Math.random() * 5000).toLong()))
            products.add(p)
        }
    }

    if (config[email_enable]) {
        checkNotifications(products, config[email_username], config[email_password], config[email_hostname], config[email_receiver])
    } else{
        println("No products to send notification")
    }
}

fun checkNotifications(products: HashSet<Product>, username: String, password: String, hostname: String, receiver: String) {

    var prodsToSend: HashSet<Product> = hashSetOf()
    for (p in products){
        if(p.toNotify) prodsToSend.add(p)
    }

    // Only prepare if we have products to notify
    if (prodsToSend.isNotEmpty()) {

        val body = prepareBody(prodsToSend)
        sendEmail(username, password, hostname, receiver, body)
        println("Sending email with body:\n$body")
    }
    else {
        println("No products triggered notifications")
    }
}

fun prepareBody(prodsToSend: HashSet<Product>): String =
    StringBuilder().apply {
        appendln("Hi there,")
        appendln()
        appendln("Just to let you know that products you mentioned are available for purchase: ")
        prodsToSend.forEach {
            appendln("-> ${it.name} at price: ${it.bestPrice.price}EUR from seller: ${it.bestPrice.seller} on URL: ${it.url}")
        }
        appendln()
        appendln("Best regards, and happy hunting")
    }.toString()


fun sendEmail(username: String, password: String, hostname: String, receiver: String, body: String) {
    SimpleEmail().apply {
        hostName = hostname
        setSmtpPort(465)
        setAuthenticator(
            DefaultAuthenticator(username, password)
        )
        isSSLOnConnect = true
        setFrom(username)
        subject = "G2A Products available for purchase!"
        setMsg(body)
        addTo(receiver)
    }.send()
}


fun scrapeProduct(url: String, priceToBuy: String): Product {

    var prod = Product(url)
    prod.priceToBuy = priceToBuy.toBigDecimal()
    var sellers: List<DocElement> = mutableListOf()
    var prices: List<DocElement> = mutableListOf()


    val text = try{
        ProcessBuilder("sh", "$programEnv/libs/g2a.sh", url)
            .start()
            .inputStream
            .reader(Charsets.UTF_8).use {
                it.readText()
        }
    } catch (e: IOException) {
        null
    }

    if (text != null) {
        htmlDocument(text) {

            span {
                withClass = "Card__clamp-container"
                findFirst {
                    prod.name = this.text
                }
            }

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
        println("Created product: ${prod.name} with BestPrice: ${prod.bestPrice} and your maximum price to purchase: ${prod.priceToBuy}")

    }
    else {
        println("Eoooooo :(")
        exitProcess(1)
    }

    return prod
}

