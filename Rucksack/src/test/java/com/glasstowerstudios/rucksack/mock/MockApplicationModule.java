package com.glasstowerstudios.rucksack.mock;

import com.glasstowerstudios.rucksack.RucksackApplication;
import com.glasstowerstudios.rucksack.di.ApplicationModule;

public class MockApplicationModule extends ApplicationModule {

  public MockApplicationModule(RucksackApplication app) {
    super(app);
  }

//  @Override
//
//  GitHubClient provideClient() {
//    GitHubClient client = mock(GitHubClient.class);
//    // mock behaviour
//    return client;
//  }
//
//  public void setResult(List<Repo> result) {
//    this.result = result;
//  }
}
