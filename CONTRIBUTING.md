Contributing
================

First and foremost, thank you for your interest in contributing to Rucksack! We welcome your contributions and hope that you are an avid supporter of Rucksack for a long time to come.

General Development Process
----------------

In general, our process uses a branch-based approach to fixing issues. If you're interested in fixing a particular issue (or adding a new feature), it's best to follow this procedure:

1. File an issue (if one isn't already filed) and tag @jwir3 on the issue so it can get prioritized into a release (if it's a new feature).
2. Fork the repository to your gitlab account.
3. Create a branch for your development work: `git checkout -b feature/#xx-add-wombat-support`
4. Fix/add whatever you need.
  1. When you make commits, use the present-tense, imperative style for commit messages (indicate what you did, and why you did it), prefixed by the issue number (e.g. "(#XX): Fix garbage collection so it doesn't collect as much garbage in order to prevent out of memory issues.").
  2. Try to make the first line of the commit message as short as possible, but feel free to add additional lines for more description.
  3. Make small commits frequently as you develop code that is self-contained and cohesive (this is the power of the branching model), rather than one large commit at the end.
  4. If this is your first patch, add one more commit that adds your name and location (optional) to the AUTHORS.md file.
5. Push your code to your repository fork.
6. Submit a merge request from your branch on your fork to origin/master on jwir3/rucksack.
  a. Generally, there might be a couple of review iterations, but almost all merge requests are accepted.

Contributor Agreement
----------------

By submitting a merge request, you agree to the following:

1. The code you submit is either your own code, which you developed and own, or every author of the code that you've compiled must agree to their code being distributed under the license of Rucksack.
2. The code contained in the pull request will be distributed under the license specified in LICENSE.md. You unconditionally transfer all rights to this code to the Rucksack administrators, without exception, under this license, and agree that at some future date, if the license should change to another open source license (not to a commercial or proprietary license), you will agree to your code being included under this new license.
3. As a contributor, you should be willing to answer questions and facilitate discussion brought up by both users of the software and other contributors, within a reasonable timeframe. It's not expected that you never leave the project or that you answer questions that aren't related to the work that you performed, but it is expected that you will be responsive and helpful, to the best of your ability, to new contributors and users of the software.

Questions/Concerns/Issues
----------------

gitlab is a collaborative environment. That's what makes it powerful. If you have a question about Rucksack, you can get it answered in any of the following ways:

1. Submit an issue and tag it with the 'question' label.
2. If you know the developer (via blame or some other method) who developed the code you have a question on, you can email them directly.
3. You can email the project administrator, Scott, at jaywir3{at}gmail.com.
