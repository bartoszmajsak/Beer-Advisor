package org.arquillian.example.domain;

public enum Country
{
   UNKNOWN,
   POLAND,
   BELGIUM,
   SCOTLAND,
   SWITZERLAND,
   NORWAY;


   public String getLabel()
   {
      return this.name().toLowerCase();
   }
}
