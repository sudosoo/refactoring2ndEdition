package com.api.refactoring2ndedition.chapter01

import org.springframework.stereotype.Component

@Component
class Chapter01 {

    fun statement(invoice: Invoice, plays: Map<String, Play>): String {
        var totalAmount = 0
        var volumeCredits = 0
        var result = "청구 내역 (고객명: ${invoice.customer})\n"

        val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US)

        for (perf in invoice.performances) {
            val play = plays[perf.playID] ?: throw IllegalArgumentException("알 수 없는 플레이 ID: ${perf.playID}")
            var thisAmount = 0

            when (play.type) {
                "tragedy" -> {
                    thisAmount = 40_000
                    if (perf.audience > 30) {
                        thisAmount += 1_000 * (perf.audience - 30)
                    }
                }
                "comedy" -> {
                    thisAmount = 30_000
                    if (perf.audience > 20) {
                        thisAmount += 10_000 + 500 * (perf.audience - 20)
                    }
                    thisAmount += 300 * perf.audience
                }
                else -> throw IllegalArgumentException("알 수 없는 장르: ${play.type}")
            }

            volumeCredits += maxOf(perf.audience - 30, 0)

            if (play.type == "comedy") {
                volumeCredits += perf.audience / 5
            }

            result += "${play.name}: ${format.format(thisAmount / 100.0)} (${perf.audience}석)\n"
            totalAmount += thisAmount
        }

        result += "총액: ${format.format(totalAmount / 100.0)}\n"
        result += "적립 포인트: ${volumeCredits}점"

        return result
    }

    data class Invoice(val customer: String, val performances: List<Performance>)
    data class Performance(val playID: String, val audience: Int)
    data class Play(val name: String, val type: String)


}

