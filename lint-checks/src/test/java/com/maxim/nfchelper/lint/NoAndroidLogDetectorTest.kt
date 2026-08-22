package com.maxim.nfchelper.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class NoAndroidLogDetectorTest {

    private val suppressLintStub = java(
        """
        package android.annotation;

        public @interface SuppressLint {
            String[] value();
        }
        """
    ).indented()

    private val logStub = java(
        """
        package android.util;

        public class Log {
            public static int v(String tag, String msg) { return 0; }
            public static int d(String tag, String msg) { return 0; }
            public static int i(String tag, String msg) { return 0; }
            public static int w(String tag, String msg) { return 0; }
            public static int e(String tag, String msg) { return 0; }
            public static int wtf(String tag, String msg) { return 0; }
            public static int println(int priority, String tag, String msg) { return 0; }
        }
        """
    ).indented()

    @Test
    fun `log d call is reported`() {
        lint().files(
            logStub,
            kotlin(
                """
                package test.pkg

                import android.util.Log

                class Example {
                    fun doWork() {
                        Log.d("Example", "working")
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expect(
                """
                src/test/pkg/Example.kt:7: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.d("Example", "working")
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """
            )
    }

    @Test
    fun `fully qualified log call without import is reported`() {
        lint().files(
            logStub,
            kotlin(
                """
                package test.pkg

                class Example {
                    fun doWork() {
                        android.util.Log.e("Example", "failed")
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expect(
                """
                src/test/pkg/Example.kt:5: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        android.util.Log.e("Example", "failed")
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """
            )
    }

    @Test
    fun `all log methods are reported`() {
        lint().files(
            logStub,
            kotlin(
                """
                package test.pkg

                import android.util.Log

                class Example {
                    fun logEverything() {
                        Log.v("Example", "v")
                        Log.d("Example", "d")
                        Log.i("Example", "i")
                        Log.w("Example", "w")
                        Log.e("Example", "e")
                        Log.wtf("Example", "wtf")
                        Log.println(1, "Example", "println")
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expect(
                """
                src/test/pkg/Example.kt:7: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.v("Example", "v")
                        ~~~~~~~~~~~~~~~~~~~~~
                src/test/pkg/Example.kt:8: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.d("Example", "d")
                        ~~~~~~~~~~~~~~~~~~~~~
                src/test/pkg/Example.kt:9: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.i("Example", "i")
                        ~~~~~~~~~~~~~~~~~~~~~
                src/test/pkg/Example.kt:10: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.w("Example", "w")
                        ~~~~~~~~~~~~~~~~~~~~~
                src/test/pkg/Example.kt:11: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.e("Example", "e")
                        ~~~~~~~~~~~~~~~~~~~~~
                src/test/pkg/Example.kt:12: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.wtf("Example", "wtf")
                        ~~~~~~~~~~~~~~~~~~~~~~~~~
                src/test/pkg/Example.kt:13: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.println(1, "Example", "println")
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                7 errors, 0 warnings
                """
            )
    }

    @Test
    fun `own methods with log-like names are not reported`() {
        lint().files(
            kotlin(
                """
                package test.pkg

                class Logger {
                    fun d(message: String) = message
                    fun w(message: String) = message
                }

                class Example {
                    fun doWork() {
                        val logger = Logger()
                        logger.d("own method")
                        logger.w("own method")
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expectClean()
    }

    @Test
    fun `method level suppress lint suppresses the issue`() {
        lint().files(
            logStub,
            suppressLintStub,
            kotlin(
                """
                package test.pkg

                import android.annotation.SuppressLint
                import android.util.Log

                class Example {
                    @SuppressLint("DiscouragedAndroidLog")
                    fun doWork() {
                        Log.d("Example", "working")
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expectClean()
    }

    @Test
    fun `method level suppression does not hide other calls`() {
        lint().files(
            logStub,
            suppressLintStub,
            kotlin(
                """
                package test.pkg

                import android.annotation.SuppressLint
                import android.util.Log

                class Example {
                    @SuppressLint("DiscouragedAndroidLog")
                    fun suppressedWork() {
                        Log.d("Example", "suppressed")
                    }

                    fun doWork() {
                        Log.e("Example", "reported")
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expectErrorCount(1)
            .expectContains("src/test/pkg/Example.kt:13: Error: Do not use android.util.Log")
    }

    @Test
    fun `class level suppress lint suppresses the issue`() {
        lint().files(
            logStub,
            suppressLintStub,
            kotlin(
                """
                package test.pkg

                import android.annotation.SuppressLint
                import android.util.Log

                @SuppressLint("DiscouragedAndroidLog")
                class Example {
                    fun doWork() {
                        Log.d("Example", "working")
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expectClean()
    }

    @Test
    fun `java source with log call is reported`() {
        lint().files(
            logStub,
            java(
                """
                package test.pkg;

                import android.util.Log;

                public class Example {
                    public void doWork() {
                        Log.d("Example", "working");
                    }
                }
                """
            ).indented(),
        )
            .allowMissingSdk()
            .issues(NoAndroidLogDetector.ISSUE_DISCOURAGED_ANDROID_LOG)
            .run()
            .expect(
                """
                src/test/pkg/Example.java:7: Error: Do not use android.util.Log; remove this call or suppress with @SuppressLint("DiscouragedAndroidLog") if logging is really needed [DiscouragedAndroidLog]
                        Log.d("Example", "working");
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~
                1 errors, 0 warnings
                """
            )
    }
}
