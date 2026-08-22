package com.maxim.nfchelper.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class NfcHelperIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
}
