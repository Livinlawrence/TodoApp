package com.livin.androidmvvmtodoapp.utils

import com.google.common.truth.Truth
import org.junit.Test

class TodoUtilTest {

    @Test
    fun `empty title returns false`() {
        val result = TodoUtil.isValidTitle(
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty date returns false`() {
        val result = TodoUtil.isValidDate(
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty time returns false`() {
        val result = TodoUtil.isValidTime(
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty frequency returns false`() {
        val result = TodoUtil.isValidFrequency(
            -1
        )
        Truth.assertThat(result).isFalse()
    }
}