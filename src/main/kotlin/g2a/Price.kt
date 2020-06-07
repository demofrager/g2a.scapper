package g2a

import java.math.BigDecimal

class Price : Comparable<Price> {
    private val price : BigDecimal
    private var seller : String

    constructor(price: String, seller: String)  {
        this.price = price.toBigDecimal()
        this.seller = seller
    }

    fun getPrice(): BigDecimal {
        return price
    }

    fun getSeller(): String {
        return seller
    }

    override fun toString(): String {
        return "Price=$price€ from: $seller"
    }

    override fun compareTo(other: Price): Int {
        if (this.price > other.getPrice()) {
            return 1
        }
        if (this.price < other.getPrice()) {
            return -1
        }
        return 0
    }


}
