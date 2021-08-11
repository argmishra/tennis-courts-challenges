package com.tenniscourts.exceptions;

/**
 * The type Already exists entity exception.
 */
public class UnauthorizedException extends RuntimeException {
  /**
   * Instantiates a new Already exists entity exception.
   *
   * @param msg the msg
   */
  public UnauthorizedException(String msg){
        super(msg);
    }

    private UnauthorizedException(){}
}
