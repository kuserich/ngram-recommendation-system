# Stepwise API usage assistance using n-gram language models [![Build Status](https://travis-ci.org/kuserich/ngram-recommendation-system.svg?branch=master)](https://travis-ci.org/kuserich/ngram-recommendation-system) [![Coverage Status](https://coveralls.io/repos/github/kuserich/ngram-recommendation-system/badge.svg?branch=master)](https://coveralls.io/github/kuserich/ngram-recommendation-system?branch=master)

Implementation of a Recommendation System for Software Engineering using n-gram language models as described by [Santos et al. (2016)](https://www.sciencedirect.com/science/article/pii/S0164121216300917?via%3Dihub). The recommender has been implemented with the help of the [KaVE Project](http://www.kave.cc/).

---

## Disclaimer

This project is part of the course [Advanced Software Engineering - FS 18](http://www.ifi.uzh.ch/en/seal/teaching/courses/ase.html) and follows a given [project description](/Project%20Description.md). For the final report of the project, please refer to the corresponing [repository](https://github.com/kuserich/ngram-recommendation-system-docs).

---
## Table of Content
1. [Getting Started](#getting-started)
    1. [Maven Configuration](#getting-started-maven)
    2. [Clone Project](#getting-started-clone)
2. [Usage of Recommender](#usage-of-recommender)
    1. [Train/Prepare Recommender](#usage-of-recommender-train)
    2. [Call Recommender](#continouos-development-test-coverage)
3. [Continuous Development](#continouos-development)
    1. [Building](#continouos-development-building)
    2. [Artifacts](#continouos-development-artifacts)
    3. [Test Coverage](#continouos-development-test-coverage)
4. [Contributing](#contributing)
5. [Credits](#credits)
6. [License](#license)

---

## Getting Started <a name="getting-started"></a>
For the installation of the recommender you can either use the maven repository dependency, or compile this project. The steps required are outlined in the corresponding subchapters.

### Maven Configuration <a name="getting-started-maven"></a>
To use it in your Maven build add:

```xml
<repositories>
  <repository>
    <id>sebastian-ngram-recommender</id>
    <url>https://packagecloud.io/sebastian/ngram-recommender/maven2</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

and the dependency:

```xml
<dependency>
  <groupId>ch.uzh.ase.ngram</groupId>
  <artifactId>ngram-recommender</artifactId>
  <version>LATEST</version>
</dependency>
```

### Clone Project <a name="getting-started-clone"></a>
Please run the following commands to clone the project to your local file system:
```bash
git clone https://github.com/kuserich/ngram-recommendation-system.git

cd ngram-recommendation-system

mvn clean install
```

After you have run the commands the .jar file should be available and can be added to your project. 

---

## Usage of Recommender <a name="usage-of-recommender"></a>

The n-gram sentences recommender has been trained with the context and event dataset provided by the [KaVE Project](http://www.kave.cc/datasets). Any other repository can be extracted and therefore the SentenceExtractor needs to be usee. The current training set covers 1770 namespaces.

### SentenceExtractor
Julian

### Train/Prepare Recommender <a name="usage-of-recommender-train"></a>

Teh training/preparation of the recommender can be done through 2 ways. The first option is to train the recommender with own scratched data (done trough the APISentenceExtractor), or with the data already available.

**Own Training Set**

The user has the possible to add its own API sentences, this is specially from interest if the user has his own projects which are not open for the public.

```java
public void train(String trainFile) throws IOException {
    ...
}
```

**Trained Set**

The user has the possibility to use a model file, which is mostly used by widely used libraries. Please refer to the chapter [Mining](#mining) for more details on how the the models have been atracted from the dataset.
```java
public void setModelNameFromFileName(String modelFile) {
    ...
}
```


### Call Recommender <a name="usage-of-recommende-callr"></a>

After the NgramReocmmenderClient has been trained the recommender can accept inputs and predict the next word. Normally this is based on 3-grams.
```java
public Set<Tuple<IMethodNAme, Double>> query(StringList stringToCompare) {
    Set<Tuple<IMethodName, Double>> recommendation = new HashSet<>();
    ...
    return recommendation;
}
```

The returned set is ordered in an ascending order, based on the prediction of the model.

**Example file**

```java
    NgramRecommenderClient nrc = new NgramRecommenderClient("models/NUnit.Framework.xml");
    System.out.println(nrc.query(new StringList("NUnit.Framework.Assert,AreEqual")));
```

All the models can be found in the folder "models"

---
## Continuous Development <a name="continouos-development"></a>
In order for the development process, the team chose to use Travis CI to have automated tests and publishing of the artifacts, to be used by other parties. Sharing is caring. ;)

### Building <a name="continouos-development-building"></a>
Thanks to the commitment to open-source the Travis-CI takes care of the building, testing, deployment, and test coverage sharing of the project. The currenty build processes can be found [here](https://travis-ci.org/kuserich/ngram-recommendation-system).

```yml
language: java
jdk: oraclejdk8

(1)install:
  - "mvn -N io.takari:maven:wrapper -Dmaven=3.3.9"
  - "./mvnw --show-version --errors --batch-mode test-compile dependency:go-offline"

(2)script: "./mvnw --show-version --errors --batch-mode -Prun-its clean verify"

(3)test:
  - mvn test -B -q

(4)cache:
  directories:
  - $HOME/.m2

(5)deploy:
  provider: script
  script: "cp .travis.settings.xml $HOME/.m2/settings.xml && mvn deploy"
  skip_cleanup: true
  on:
    all_branches: true

(6)after_success:
  - mvn clean -DTRAVIS_JOB_ID=$TRAVIS_JOB_ID cobertura:cobertura coveralls:report
```
(1) - This steps installs all the dependencies, if they are not locally avaialable.

(2) - This step ensures a clean setup.

(3) - This steps runs the test and adds a report.

(4) - This is not really a build step, it just saves dependencies and other needed fragments for the maven build in cache.

(5) - The deployment step pushes the artifact to the [Maven Repository](https://packagecloud.io/sebastian/ngram-recommender) at packeclou.io.

(6) - The last step of the build script deploy the [Coveralls](https://coveralls.io/github/kuserich/ngram-recommendation-system). Important to note is that the Travis CI job id is added to the coverall report.

### Artifacts <a name="continouos-development-artifacts"></a>
The artifacts are deployed to [packagecloud.io](https://packagecloud.io/sebastian/ngram-recommender) and versioned in the following manner:

```
0.1.${env.TRAVIS_BUILD_NUMBER}-${env.TRAVIS_BRANCH}
```

**env.TRAVIS_BUILD_NUMBER:** Every build in Travis CI gets a number, starting with 1.

**env.TRAVIS_BRANCH:** As we build every commit, the branch name is added to the artifacts name.

### Test Coverage <a name="continouos-development-test-coverage"></a>

As outline in the sixth build step at the sub chapter [Building](#continouos-development-building) the test report is pushed to [Coveralls](https://coveralls.io/github/kuserich/ngram-recommendation-system). The build number as well the branch of the build are the same as of the Travis CI task.

---

## Contributing <a name="continouos-development"></a>
For contribution please get in contact with the representatives of the [Software Evolution and Architecture Lab (s.e.a.l) of the University of Zurich](https://www.ifi.uzh.ch/en/seal.html). Your direct contact should be Sebastian Proksch, Dr.-Ing..

---

## Credits <a name="credits"></a>
Markus Göckeritz

Julian Iff

Sebastian Pinegger

---

## License <a name="license"></a>
This project is licensed under the Apache License 2.0.

