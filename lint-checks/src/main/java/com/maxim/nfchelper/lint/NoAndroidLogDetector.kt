package com.maxim.nfchelper.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

/**
 * Reports every call to `android.util.Log` methods (`Log.v/d/i/w/e/wtf/println`).
 *
 * Matching is done by method name and then confirmed by resolving the callee to
 * the `android.util.Log` class, so user-defined helpers named `d(...)`, `w(...)`
 * etc. do not trigger the check, and fully qualified calls without an import
 * are still caught.
 */
class NoAndroidLogDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> = LOG_METHOD_NAMES

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (!context.evaluator.isMemberInClass(method, "android.util.Log")) return

        context.report(
            issue = ISSUE_DISCOURAGED_ANDROID_LOG,
            scope = node,
            location = context.getLocation(node),
            message = "Do not use android.util.Log; remove this call or suppress with `@SuppressLint(\"DiscouragedAndroidLog\")` if logging is really needed",
        )
    }

    companion object {
        private val LOG_METHOD_NAMES = listOf("v", "d", "i", "w", "e", "wtf", "println")

        /** Calling android.util.Log directly is forbidden in this project. */
        @JvmField
        val ISSUE_DISCOURAGED_ANDROID_LOG: Issue = Issue.create(
            id = "DiscouragedAndroidLog",
            briefDescription = "Use of android.util.Log",
            explanation = """
                `android.util.Log` writes uncontrolled output to logcat and leaks \
                implementation details of the app. Do not call it directly; remove \
                the call, or use a project-approved logging approach instead.
                """,
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.ERROR,
            implementation = Implementation(
                NoAndroidLogDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
        )
    }
}
