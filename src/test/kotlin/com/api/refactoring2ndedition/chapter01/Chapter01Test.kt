package com.api.refactoring2ndedition.chapter01

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
class Chapter01Test @Autowired constructor(
    private val chapter01: Chapter01
) : BehaviorSpec() {

    init {
        val objectMapper = jacksonObjectMapper()



        Given("Test1") {
            val path = Paths.get("src/main/resources/chapter01/invoices.json")
            val playsPath = Paths.get("src/main/resources/chapter01/plays.json")

            val jsonContent = Files.readString(path)
            val playJsonContent = Files.readString(playsPath)

            val invoice: Chapter01.Invoice = objectMapper.readValue(jsonContent,Chapter01.Invoice::class.java)

            val plays: Map<String, Chapter01.Play> = objectMapper.readValue(playJsonContent, object : TypeReference<Map<String, Chapter01.Play>>() {})
            val result = chapter01.statement(invoice, plays)

            When("조회를 하면") {

                Then("조회된다") {
                    result shouldBe """
                        청구 내역 (고객명: BigCo)
                        Hamlet: $650.00 (55석)
                        As You Like It: $580.00 (35석)
                        Othello: $500.00 (40석)
                        총액: $1,730.00
                        적립 포인트: 47점
                    """.trimIndent()
                }
            }
        }


    }

}