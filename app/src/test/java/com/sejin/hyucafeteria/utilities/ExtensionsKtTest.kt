package com.sejin.hyucafeteria.utilities

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import java.time.LocalDate

internal class ExtensionsKtTest {

    @Test
    fun toUrlDate_isCorrect() {
        val localDate = LocalDate.of(2022, 8, 8)

        val urlDate = localDate.toUrlDate()

        assertThat(urlDate.year, `is`("2022"))
        assertThat(urlDate.monthMinusOne, `is`("7"))
        assertThat(urlDate.day, `is`("8"))
    }

    @Test
    fun urlDateToLocalDate_isCorrect() {
        val localDate = LocalDate.of(2022, 8, 8)
        val urlDate = localDate.toUrlDate()

        val convertedLocalDate = urlDate.toLocalDate()

        assertThat(localDate, `is`(convertedLocalDate))
    }
}