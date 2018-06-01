# Stepwise API usage assistance using n-gram language models [![Build Status](https://travis-ci.org/kuserich/ngram-recommendation-system.svg?branch=master)](https://travis-ci.org/kuserich/ngram-recommendation-system) [![Coverage Status](https://coveralls.io/repos/github/kuserich/ngram-recommendation-system/badge.svg?branch=master)](https://coveralls.io/github/kuserich/ngram-recommendation-system?branch=master)

Implementation of a Recommendation System for Software Engineering using n-gram language models as described by [Santos et al. (2016)](https://www.sciencedirect.com/science/article/pii/S0164121216300917?via%3Dihub).
The recommender has been implemented using the [KaVE Project](http://www.kave.cc/).

---

## Disclaimer

This project is part of the course [Advanced Software Engineering - FS 18](http://www.ifi.uzh.ch/en/seal/teaching/courses/ase.html) and follows a given [project description](/Project%20Description.md).

---
## Table of Contents
1. [Getting Started](#getting-started)
    1. [Maven Configuration](#getting-started-maven)
    2. [Clone Project](#getting-started-clone)
2. [Usage of Recommender](#usage-of-recommender)
    1. [Train/Prepare Recommender](#usage-of-recommender-train)
    2. [Call Recommender](#continouos-development-test-coverage)
3. [Continuous Deployment](#continouos-deployment)
    1. [Building](#continouos-deployment-building)
    2. [Artifacts](#continouos-deployment-artifacts)
    3. [Test Coverage](#continouos-deployment-test-coverage)
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
Run the following commands to clone the project to your local file system:
```bash
git clone https://github.com/kuserich/ngram-recommendation-system.git

cd ngram-recommendation-system

mvn clean install
```

After you have run the commands the .jar file should be available and can be added to your project. 

---

## Usage of Recommender <a name="usage-of-recommender"></a>

The n-gram sentences recommender has been trained with the static repository data set provided by the [KaVE Project](http://www.kave.cc/datasets).
All repositories are processed using the SentenceExtractor.
 
Note that the amount of data that is extracted might easily exceed i) space on commodity hard disk and ii) memory requirements on commodity hardware.

Using the static repository data and the current configuration of the *SentenceExtractor* the output of the extraction process will be around 332GB, which will give a total of 1730 language models.

### Train/Prepare Recommender <a name="usage-of-recommender-train"></a>

A recommender, which is represented in *NgramRecommenderClient*, can either be trained using new data or a language model from the models directory can be loaded.

**Own Training Set**

The user has the possibility to add its own API sentences, this is particularly of interest if the user has his own projects which are not open for the public.

```java
public void train(String trainFile) throws IOException {
    ...
}
```

**Trained Set**

The user has the possibility to use a model file, which is mostly used by widely used libraries.
```java
public void setModelNameFromFileName(String modelFile) {
    ...
}
```


### Call Recommender <a name="usage-of-recommende-callr"></a>

After the NgramRecommenderClient has been trained, the recommender can accept inputs and predict the next word. 
Notice that usually a prediction includes an entire API sentence. That is, rather than the single subsequent token that follows given the input, the recommender would predict the most likely API sentence which can be of any length between 1 and n-1 such that n is the n in "n-grams" which the model created during training.
Nevertheless, as we're using the predictions with the Event data from the [KaVE Project](http://www.kave.cc/datasets) and comparing to the selection that was made, we reduced the number of API tokens that are predicted to 1.


```java
public Set<Tuple<IMethodNAme, Double>> query(StringList stringToCompare) {
    Set<Tuple<IMethodName, Double>> recommendation = new HashSet<>();
    ...
    return recommendation;
}
```



**Example file**

```java
NgramRecommenderClient nrc = new NgramRecommenderClient("models/NUnit.Framework.xml");
System.out.println(nrc.query(new StringList("NUnit.Framework.Assert,AreEqual")));
```

All the models can be found in the "models" directory.

---
## Continuous Deployment <a name="continouos-deployment"></a>
We chose to use Travis CI to have automated tests and automated deployment of the artifacts. Sharing is caring. ;)

### Building <a name="continouos-deployment-building"></a>
Thanks to the commitment to open-source the Travis-CI takes care of the building, testing, deployment, and test coverage sharing of the project.
The current build processes can be found [here](https://travis-ci.org/kuserich/ngram-recommendation-system).

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
(1) - This step installs all the dependencies, if they are not locally avaialable.

(2) - This step ensures a clean setup.

(3) - This step runs the test and prepares a test report.

(4) - This is not really a build step, it just saves dependencies and other required fragments for the maven build in cache.

(5) - The deployment step pushes the artifact to the [Maven Repository](https://packagecloud.io/sebastian/ngram-recommender) at packetcloud.io.

(6) - The last step of the build script pushes the test report to [Coveralls](https://coveralls.io/github/kuserich/ngram-recommendation-system).
Important to note is that the Travis CI job id is added to the coverall report. That is, each artifact is assigned an ID which is used accross in both, Coveralls and packagecloud.

### Artifacts <a name="continouos-development-artifacts"></a>
The artifacts are deployed to [packagecloud.io](https://packagecloud.io/sebastian/ngram-recommender) and versioned as follows:

```
0.1.${env.TRAVIS_BUILD_NUMBER}-${env.TRAVIS_BRANCH}
```

**env.TRAVIS_BUILD_NUMBER:** Every build in Travis CI gets a number, starting with 1.

**env.TRAVIS_BRANCH:** As we build every commit, the branch name is added to the artifacts name.

### Test Coverage <a name="continouos-development-test-coverage"></a>

As outlined in the sixth build step in Section [Building](#continouos-development-building), the test report is pushed to [Coveralls](https://coveralls.io/github/kuserich/ngram-recommendation-system).
The build number as well as the branch of the build are the same as of the Travis CI task.

---

## Evaluation
We extracted and trained language models on the entire Context data set for a total of 1730 models.
Using the Event data set with a total of 11'546'219 events registered, we evaluated the accuracy of our n-gram language model for a set of 2-gram, 3-gram, 4-gram, and 5-gram language models by comparing the predicted token from the language model with the actual selection.
We were able to find models for a total of 1792 out of 11'546'219 events. Out of these, only 78 were predicted accurately (1:1). The overall accuracy of our recommender thus is ~4.35%.

We were unable to reproduce the results outlined in [Santos et al. (2016)](https://www.sciencedirect.com/science/article/pii/S0164121216300917?via%3Dihub). We believe that this may be due to the following reasons:

i) We ommitted evaluating the accuracy for non-selected but proposed tokens (i.e. rank position)

ii) A large number of language models is relatively small (small number of sentences with small number of tokens)

iii) The overlap between Event data and Context data is unclear

iv) A large portion of accuracy might be lost due to namespace specific bucketizing

v) Due to branching statements, some API sentences get astronomically large, which skews the distribution (relative probability)


## Contributing <a name="continouos-development"></a>
For contribution please get in contact with the representatives of the [Software Evolution and Architecture Lab (s.e.a.l) of the University of Zurich](https://www.ifi.uzh.ch/en/seal.html).

---

## Credits <a name="credits"></a>
Markus Göckeritz

Julian Iff

Sebastian Pinegger

---

## License <a name="license"></a>
This project is licensed under the Apache License 2.0.

