package com.glasstowerstudios.rucksack.util;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public final class DataStub {
  private static final String BASE_PATH = resolveBasePath(); // e.g. "./mymodule/src/test/resources/";

  private static String resolveBasePath() {
    final String path = "./Rucksack/src/test/resources/";
    if (Arrays.asList(new File("./").list()).contains("Rucksack")) {
      return path; // version for call unit tests from Android Studio
    }
    return "../" + path; // version for call unit tests from terminal './gradlew test'
  }

  private DataStub() {
    //no instances
  }

  /**
   * Reads file content and returns string.
   * @throws IOException
   */
  public static String readFile(@NonNull final String aPath) throws IOException {
    String path = BASE_PATH + "/" + aPath;
    final StringBuilder sb = new StringBuilder();
    try {
      String strLine;
      final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
      while ((strLine = reader.readLine()) != null) {
        sb.append(strLine);
      }
    } catch (IOException e) {
      // Ignore this, since we're just testing anyway...
    }
    return sb.toString();
  }
}