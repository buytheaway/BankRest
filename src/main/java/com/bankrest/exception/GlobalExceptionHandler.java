package com.bankrest.exception;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.util.*;
@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NoSuchElementException.class) public ResponseEntity<?> nse(NoSuchElementException e){ return ResponseEntity.status(404).body(Map.of("error",e.getMessage())); }
  @ExceptionHandler(IllegalStateException.class)  public ResponseEntity<?> ise(IllegalStateException e){ return ResponseEntity.status(400).body(Map.of("error",e.getMessage())); }
  @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<?> iae(IllegalArgumentException e){ return ResponseEntity.status(400).body(Map.of("error",e.getMessage())); }
  @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> manve(MethodArgumentNotValidException e){
    var errs=new ArrayList<>(); e.getBindingResult().getFieldErrors().forEach(fe->errs.add(fe.getField()+": "+fe.getDefaultMessage())); return ResponseEntity.badRequest().body(Map.of("errors",errs));
  }
}
