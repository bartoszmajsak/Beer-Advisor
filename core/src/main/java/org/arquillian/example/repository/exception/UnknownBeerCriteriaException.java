package org.arquillian.example.repository.exception;

public class UnknownBeerCriteriaException extends RuntimeException
{

   private static final long serialVersionUID = 4192615774516733111L;

   public UnknownBeerCriteriaException()
   {
   }

   public UnknownBeerCriteriaException(String message)
   {
      super(message);
   }

   public UnknownBeerCriteriaException(Throwable cause)
   {
      super(cause);
   }

   public UnknownBeerCriteriaException(String message, Throwable cause)
   {
      super(message, cause);
   }

}
