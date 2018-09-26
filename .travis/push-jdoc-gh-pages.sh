#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "iotaledger/iota-java" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then

  echo -e "Publishing javadoc...\n"

  if [ -d "/home/travis/build/iotaledger/iota-java/jota/target/apidocs/" ]; then
  
      cp -R "/home/travis/build/iotaledger/iota-java/jota/target/apidocs/" $HOME/javadoc-latest

      cd $HOME
      git config --global user.email "travis@travis-ci.org"
      git config --global user.name "travis-ci"
      git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/iotaledger/iota-java.git gh-pages > /dev/null

      cd gh-pages
      git rm -rf ./javadoc
      cp -Rf $HOME/javadoc-latest ./javadoc
      git add -f .
      git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
      git push -fq origin gh-pages > /dev/null

      echo -e "Published Javadoc to gh-pages.\n"
  else
        echo -e "Javadoc build failed, not updating gh-pages.\n"
  fi
fi
