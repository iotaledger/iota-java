# Contributing

*Thank you for your interest in contributing to this project!*

We welcome all contributions and we'd like to help you send your first contribution to us.
For that, this is what we recommend:

1. [Look](#look) into the existing [issues][issues] (or [create a new one][new-issue])
1. [Discuss](#discuss) the possible solution
1. [Code!](#code)
1. [Test](#test)
1. [Send](#send) a PR

## Look

Take a look at the [**existing issues**][issues] and linked [pull requests][pull-requests]: perhaps there's one already related to the feature you want to contribute!
Otherwise, [**open a new one**][new-issue], describing the problem and your suggestion for the solution.
For bugs, it would be wonderful if you describe a way to _reproduce the issue_.

## Discuss

Usually, opening an issue is enough to get a feedback. If you prefer, join us on [**Slack**][iota-slack], under
the **#developers** channel.

## Code

Once you feel comfortable that the issue has been discussed and a general approach is agreed upon, it's time to start coding.

First, **fork** this repository into your own namespace, **clone** it and add this repository as `upstream`:

```bash
git clone git@github.com:YOUR_USERNAME/iota.lib.java.git
git remote add upstream git@github.com:iotaledger/iota.lib.java.git
```

Then, create a new **local branch** based off of `master`, like this:

```bash
git fetch upstream
git rebase upstream/master
git checkout -b MyTopicBranch
```

Run a sanity check build with `mvn clean install`. It should complete successfully without errors or test failures.

If everything worked, then do your changes as desired. Once you are done, run a local build with `mvn clean install` to make sure nothing is broken. Ideally, your code should include tests for the bugs you are fixing or for features you are adding. If you don't know how to test, it's OK to open the PR with a title saying "[wip]" (for Work in Progress). We can guide you on how to write tests.

## Test

Once your code is considered ready, run all unit tests, including your own tests.
If any test fails, fix the test issues and retry.

## Send

Once your code is ready for a review or if you want feedback, send your branch as a [pull request on GitHub][new-pull-request]. If your branch is not ready to be merged for some reason, add "[wip]" to the title. It is expected that all PRs are passing the continuous integration test.

During this process, someone will take a look at your contributions and make comments, ask questions and suggest different approaches. Even though the reviewer strives to be polite, don't take a possible criticism personally.
After all, we'd really love to get your contribution!

[issues]: https://github.com/iotaledger/iota.lib.java/issues
[new-issue]: https://github.com/iotaledger/iota.lib.java/issues/new
[pull-requests]: https://github.com/iotaledger/iota.lib.java/pulls
[new-pull-request]: https://github.com/iotaledger/iota.lib.java/compare
[iota-slack]: http://slack.iota.org/
