package g2a

import java.math.BigDecimal

data class Product(val name: String) {

    val set = sortedSetOf<Price>()
    var notificationThreshold : BigDecimal = BigDecimal.ZERO

    fun addPrice(price: Price) {
        set.add(price)
    }

    val bestPrice: Price
        get() = set.first()

    val toNotify: Boolean
        get() = bestPrice.price <= notificationThreshold

}