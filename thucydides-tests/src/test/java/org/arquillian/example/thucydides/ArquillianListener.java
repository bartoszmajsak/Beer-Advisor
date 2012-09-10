package org.arquillian.example.thucydides;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;

import org.jboss.arquillian.test.spi.LifecycleMethodExecutor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptorBuilder;

public class ArquillianListener implements StepListener
{

   private static final Logger logger = Logger.getLogger(ArquillianListener.class.getName());

   /*
    * Note about the ThreadLocals.
    *
    * We have an issue here - the testSuiteFinished() method can be invoked from a different thread,
    * in contrast to the testSuiteStarted() method when the Surefire/Failsafe testrunner runs tests in parallel.
    *
    * Therefore, a new instance of the ArquillianListener is created because the Thucydides StepEventBus is initalized
    * once again for the new thread.
    * Eventually, the testSuiteFinished() method does not have access to the Arquillian TestRunnerAdaptor
    * and the currentClass instance, i.e. the Test class. This also means that the archive is not undeployed by Arquillian.
    *
    */
   static final ThreadLocal<TestRunnerAdaptor> adaptor = new ThreadLocal<TestRunnerAdaptor>();

   private static final ThreadLocal<Class<?>> currentClass = new ThreadLocal<Class<?>>();

   public void testSuiteStarted(Class<?> paramClass)
   {
      logger.log(Level.INFO, "Arquillian Test Suite started for test class {0}", paramClass);

      try
      {
         if (paramClass.equals(currentClass.get()))
         {
            /*
             * Suite started for the provided test class. Therefore, return immediately.
             * This looks like a bug in Thucydides core - a suite should have been initialized once per class.
             */
            return;
         }
         else
         {
            currentClass.set(paramClass);
         }
         if (adaptor.get() == null)
         {
            adaptor.set(TestRunnerAdaptorBuilder.build());
         }
         TestRunnerAdaptor testRunner = adaptor.get();
         testRunner.beforeSuite();
         testRunner.beforeClass(paramClass, LifecycleMethodExecutor.NO_OP);
      }
      catch (Exception ex)
      {
         throw new RuntimeException(ex);
      }
   }

   public void testSuiteFinished()
   {
      logger.log(Level.INFO, "Arquillian Test Suite finished.");
      try
      {
         TestRunnerAdaptor testRunner = adaptor.get();
         Class<?> klass = currentClass.get();
         if (testRunner != null && klass != null)
         {
            testRunner.afterClass(klass, LifecycleMethodExecutor.NO_OP);
            testRunner.afterSuite();
         }
      }
      catch (Exception ex)
      {
         throw new RuntimeException(ex);
      }
   }

   public void testStarted(String description) {}

   public void testFinished(TestOutcome result) {}

   public void stepStarted(ExecutedStepDescription description) {}

   public void skippedStepStarted(ExecutedStepDescription description) {}

   public void stepFailed(StepFailure failure) {}

   public void lastStepFailed(StepFailure failure) {}

   public void stepIgnored() {}

   public void stepIgnored(String message) {}

   public void stepPending() {}

   public void stepPending(String message) {}

   public void stepFinished() {}

   public void testFailed(TestOutcome testOutcome, Throwable cause) {}

   public void testIgnored() {}

   public void notifyScreenChange() {}

   public void testSuiteStarted(Story story) {}



}
