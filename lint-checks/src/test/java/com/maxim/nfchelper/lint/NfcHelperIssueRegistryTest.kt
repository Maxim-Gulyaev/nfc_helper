package com.maxim.nfchelper.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.LintClient
import com.android.tools.lint.detector.api.CURRENT_API
import java.util.ServiceLoader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class NfcHelperIssueRegistryTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun initializeLintClient() {
            LintClient.clientName = LintClient.CLIENT_UNIT_TESTS
        }

        @JvmStatic
        @AfterClass
        fun resetLintClient() {
            LintClient.Companion.resetClientName()
        }
    }

    @Test
    fun `registry exposes issue and compatibility metadata`() {
        val registry = NfcHelperIssueRegistry()

        assertEquals(
            listOf(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG),
            registry.issues,
        )
        assertEquals(CURRENT_API, registry.api)
        assertEquals(CURRENT_API, registry.minApi)
        assertEquals("NFC Helper", registry.vendor.vendorName)
        assertEquals("com.maxim.nfchelper", registry.vendor.identifier)
    }

    @Test
    fun `registry is discoverable through service loader`() {
        val registries = ServiceLoader.load(IssueRegistry::class.java).toList()

        assertTrue(registries.any { it is NfcHelperIssueRegistry })
    }
}
