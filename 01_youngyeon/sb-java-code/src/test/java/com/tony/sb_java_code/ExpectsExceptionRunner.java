package com.tony.sb_java_code;

import com.tony.sb_java_code.anno.ExpectsException;
import org.junit.AssumptionViolatedException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class ExpectsExceptionRunner extends BlockJUnit4ClassRunner {

    public ExpectsExceptionRunner(Class klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected org.junit.runners.model.Statement possiblyExpectingExceptions(FrameworkMethod method, Object test, org.junit.runners.model.Statement next) {
        ExpectsException annotation = method.getAnnotation(ExpectsException.class);
        if (annotation == null) {
            return next;
        }
        return new ExpectExceptionWithMessage(next, annotation.type(), annotation.message());
    }

    class ExpectExceptionWithMessage extends org.junit.runners.model.Statement {

        private final org.junit.runners.model.Statement next;
        private final Class expected;
        private final String expectedMessage;

        public ExpectExceptionWithMessage(org.junit.runners.model.Statement next, Class expected, String expectedMessage) {
            this.next = next;
            this.expected = expected;
            this.expectedMessage = expectedMessage;
        }

        @Override
        public void evaluate() throws Exception {
            boolean complete = false;
            try {
                next.evaluate();
                complete = true;
            } catch (AssumptionViolatedException e) {
                throw e;
            } catch (Throwable e) {
                if (!expected.isAssignableFrom(e.getClass())) {
                    String message = "Unexpected exception, expected<"
                            + expected.getName() + "> but was <"
                            + e.getClass().getName() + ">";
                    throw new Exception(message, e);
                }

                if (isNotNull(expectedMessage) && !expectedMessage.equals(e.getMessage())) {
                    String message = "Unexpected exception message, expected<"
                            + expectedMessage + "> but was<"
                            + e.getMessage() + ">";
                    throw new Exception(message, e);
                }
            }
            if (complete) {
                throw new AssertionError("Expected exception: "
                        + expected.getName());
            }
        }

        private boolean isNotNull(String s) {
            return s != null && !s.isEmpty();
        }
    }
}
