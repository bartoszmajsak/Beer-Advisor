package org.arquillian.example.domain;

public enum Country
{
   UNKNOWN,
   POLAND,
   BELGIUM,
   SCOTLAND;


   public String getLabel()
   {
      return this.name().toLowerCase();
   }
}
