package org.arquillian.example.thucydides;

import org.jboss.arquillian.test.spi.LifecycleMethodExecutor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class ArquillianEnricher implements MethodRule
{

   @Override
   public Statement apply(final Statement base, final FrameworkMethod method, final Object target)
   {
      return new Statement()
      {
         public void evaluate() throws Throwable
         {

            TestRunnerAdaptor adaptor = ArquillianListener.adaptor.get();
            adaptor.before(target, method.getMethod(), LifecycleMethodExecutor.NO_OP);
            try
            {
               base.evaluate();
            }
            finally
            {
               adaptor.after(target, method.getMethod(), LifecycleMethodExecutor.NO_OP);
            }
         };
      };
   }

}
