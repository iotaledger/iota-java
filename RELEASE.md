# Release process

This project uses a simplified release process, where you only need to push a tag starting with `release-`, followed by a version number. For instance, pushing a tag named `release-0.9.3` would tell Travis CI to release the version `0.9.3`.

## TL;DR

```bash
git checkout master
git fetch upstream
git rebase upstream/master
git tag release-0.9.X
git push upstream release-0.9.X
```

## Explanation

Once a new tag is pushed to the main repository, a Travis build will be started for it.
Once the build script detects that it's a release build, it will start a new Maven release process (`mvn release:prepare release:perform`) and upload the artifacts to Bintray, which is synchronized with Maven Central.