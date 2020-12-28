package com.intellibet.model;

public enum EventCategory {
  FOOTBALL, BASKETBALL, VOLEYBALL, HANDBALL, TENNIS, CRICKET, BOX, K1, FORMULA1, DTM, WRC, MOTOGP, WSBK, POOL, CHESS, BOWLING, PINGPONG, GOLF, OTHERS;

  public static EventCategory getCategoryFromString(String category) {
    switch (category.toUpperCase()) {

      case "FOOTBALL":
        return FOOTBALL;

      case "BASKETBALL":
        return BASKETBALL;

      case "VOLEYBALL":
        return VOLEYBALL;

      case "HANDBALL":
        return HANDBALL;

      case "TENNIS":
        return TENNIS;

      case "CRICKET":
        return CRICKET;

      case "BOX":
        return BOX;

      case "K1":
        return K1;

      case "FORMULA1":
        return FORMULA1;

      case "DTM":
        return DTM;

      case "WRC":
        return WRC;

      case "MOTOGP":
        return MOTOGP;

      case "WSBK":
        return WSBK;

      case "POOL":
        return POOL;

      case "CHESS":
        return CHESS;

      case "BOWLING":
        return BOWLING;

      case "PINGPONG":
        return PINGPONG;

      case "GOLF":
        return GOLF;

      case "OTHERS":
        return OTHERS;

      default:
        throw new IllegalArgumentException("The option you chose is invalid!");
    }
  }

}