package com.bankrest.util;
public class Masking {
  public static String mask16(String plain){ if(plain==null || plain.length()<4) return "****"; String last=plain.substring(plain.length()-4); return "**** **** **** "+last; }
}
