package g2a

import java.math.BigDecimal

class Product(s: String) {
    private val name : String = s
    private val set = sortedSetOf<Price>()

    fun addPrice(price: Price) {
        set.add(price)
    }

    override fun toString(): String {
        return "Product(name=$name, setOfPrices=$set)"
    }
    fun getName(): String {
        return name
    }
    fun getBestPrice():Price{
        return set.first()
    }
}