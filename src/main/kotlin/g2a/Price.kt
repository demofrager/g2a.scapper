package g2a

import java.math.BigDecimal

data class Price(val price: BigDecimal, val seller :String) : Comparable<Price> {

    override fun compareTo(other: Price): Int {

        if (this.price > other.price) {
            return 1
        }
        if (this.price < other.price) {
            return -1
        }
        return 0
    }
}
