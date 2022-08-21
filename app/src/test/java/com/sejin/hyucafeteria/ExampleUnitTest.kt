package com.sejin.hyucafeteria

import com.sejin.hyucafeteria.utilities.isEngMenuName
import com.sejin.hyucafeteria.utilities.reduceEngMenu
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val sangGwaDaeMenu = "[Dam-A]뚝배기설렁탕*소면, 제육김치볶음"
    val hanyangPlazaMenuName = "떡 or 만두 치즈라면 (14:00~15:00)\n Rice Cake or Dumpling or Cheese Ramen"


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun isEngMenuName_isCorrect(){
        assertEquals(hanyangPlazaMenuName.isEngMenuName(), true)
    }
    @Test
    fun menuNameRefine_isCorrect() {
        val name1 = "[Dam-A]뚝배기설렁탕*소면, 제육김치볶음"
        val name2 = "떡 or 만두 치즈라면 (14:00~15:00)\n Rice Cake or Dumpling or Cheese Ramen"
        var refined = hanyangPlazaMenuName.reduceEngMenu()
        assertEquals("떡 or 만두 치즈라면 (14:00~15:00)", refined)
    }
}