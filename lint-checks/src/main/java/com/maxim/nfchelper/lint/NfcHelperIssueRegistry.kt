package com.maxim.nfchelper.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.CURRENT_API

class NfcHelperIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)

    override val api: Int = CURRENT_API
    override val minApi: Int = CURRENT_API
    override val vendor: Vendor = Vendor(
        vendorName = "NFC Helper",
        identifier = "com.maxim.nfchelper",
    )
}
