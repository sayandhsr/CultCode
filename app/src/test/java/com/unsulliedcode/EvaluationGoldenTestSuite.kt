package com.unsulliedcode

import android.content.Context
import com.unsulliedcode.engine.CodeExecutionEngine
import com.unsulliedcode.engine.EvaluationEngine
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import androidx.test.core.app.ApplicationProvider

@RunWith(RobolectricTestRunner::class)
class EvaluationGoldenTestSuite {

    private lateinit var context: Context
    private lateinit var execEngine: CodeExecutionEngine
    private lateinit var evalEngine: EvaluationEngine

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        execEngine = CodeExecutionEngine(context)
        evalEngine = EvaluationEngine()
    }

    @Test
    fun testPythonRouting_IsActuallyRouted() {
        // Simulating the Python routing fix (passing 'python' instead of 'PY')
        // Using a non-hardcoded print so astAntiHardcodingCheck doesn't block it
        val result = execEngine.execute("python", "a=1\nb=2\nprint(a+b)", "3", listOf("3"))
        assertTrue("Python execution should succeed", result.isSuccess)
    }

    @Test
    fun testEvaluationEngine_StreakCalculation() {
        val r1 = evalEngine.evaluateAnswer(1, 1, 10, "")
        assertTrue(r1.isCorrect)
        assertEquals(1, evalEngine.getStreak())
        
        val r2 = evalEngine.evaluateAnswer(1, 1, 10, "")
        assertEquals(2, evalEngine.getStreak())
        
        val r3 = evalEngine.evaluateAnswer(2, 1, 10, "") // wrong
        assertFalse(r3.isCorrect)
        assertEquals(0, evalEngine.getStreak())
    }
    
    @Test
    fun testAstAntiHardcoding_GoldenTest_ShouldFailHardcodedPrints() {
        // v3.0 §8 Phase 2 AST Anti-hardcoding test
        val hardcodedCode = "print('10')"
        val expectedOutput = "10"
        val result = execEngine.execute("python", hardcodedCode, expectedOutput, listOf(expectedOutput))
        
        // The AST pipeline must reject this
        assertFalse("AST Anti-Hardcoding must reject literal print statements of the expected output", result.isSuccess)
        assertEquals("Error: Hardcoded literal detected. Write the actual logic.", result.stdout)
    }
}
