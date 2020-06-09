package g2a

import java.math.BigDecimal

data class Product(val url: String) {

    val set = sortedSetOf<Price>()
    var priceToBuy : BigDecimal = BigDecimal.ZERO
    var name: String = ""

    fun addPrice(price: Price) {
        set.add(price)
    }

    override fun toString(): String {
        return "Product(name='$name', url='$url', set=$set, priceTobuy=$priceToBuy)"
    }

    val bestPrice: Price
        get() = set.first()

    val toNotify: Boolean
        get() = bestPrice.price <= priceToBuy

}