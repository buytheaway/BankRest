package com.bankrest.security;
import jakarta.persistence.AttributeConverter; import jakarta.persistence.Converter;
import javax.crypto.Cipher; import javax.crypto.spec.GCMParameterSpec; import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets; import java.security.SecureRandom; import java.util.Base64;
@Converter
public class AttributeEncryptor implements AttributeConverter<String,String> {
  private static final String ALG="AES"; private static final String TRANS="AES/GCM/NoPadding";
  private static final int GCM_TAG_BITS=128; private static final int IV_BYTES=12;
  private final byte[] key;
  public AttributeEncryptor(){ String secret=System.getenv().getOrDefault("CARD_AES_SECRET","change-me-32-bytes-change-me-32-bytes");
    byte[] k=java.util.Arrays.copyOf(secret.getBytes(StandardCharsets.UTF_8), 32); this.key=k; }
  public String convertToDatabaseColumn(String attribute){
    if(attribute==null) return null;
    try{
      byte[] iv=new byte[IV_BYTES]; new SecureRandom().nextBytes(iv);
      Cipher c=Cipher.getInstance(TRANS); c.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key,ALG),new GCMParameterSpec(GCM_TAG_BITS,iv));
      byte[] enc=c.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(iv)+":"+Base64.getEncoder().encodeToString(enc);
    }catch(Exception e){ throw new IllegalStateException("encryption failed", e); }
  }
  public String convertToEntityAttribute(String dbData){
    if(dbData==null) return null;
    try{
      String[] p=dbData.split(":",2); byte[] iv=Base64.getDecoder().decode(p[0]); byte[] ct=Base64.getDecoder().decode(p[1]);
      Cipher c=Cipher.getInstance(TRANS); c.init(Cipher.DECRYPT_MODE,new SecretKeySpec(key,ALG),new GCMParameterSpec(GCM_TAG_BITS,iv));
      return new String(c.doFinal(ct), StandardCharsets.UTF_8);
    }catch(Exception e){ throw new IllegalStateException("decryption failed", e); }
  }
}
