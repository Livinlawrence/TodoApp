package com.livin.androidmvvmtodoapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class LoginUtilTest {

    @Test
    fun `empty email returns false`() {
        val result = LoginUtil.isValidForm(
            "",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = LoginUtil.isValidForm(
            "test",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `invalid email returns false`() {
        val result = LoginUtil.isValidEmail(
            "test@m"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid email returns true`() {
        val result = LoginUtil.isValidEmail(
            "livinlawrence@gmail.com"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `valid email and password returns true`() {
        val result = LoginUtil.isValidForm(
            "livinlawrence@gmail.com",
            "123"
        )
        assertThat(result).isTrue()
    }

}