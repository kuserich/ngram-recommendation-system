## ASE’18 Project Description
### Goal
The ASE18 project is about replicating a “Recommendation System for Software Engineering” (RSSE). Teams of 2-3 students will select a scientific paper and use the techniques and technologies from the lecture to recreate an existing system. It is the ultimate goal of the project to build a working recommender system in the end, but following a proper development process and creating a tested, maintainable, and reusable piece of software is an equally important part of the project.

### Registration
Please register with the organizers by sending an email. You can register in teams of up to three students, just send an email that includes your Top-5(!) picks from the following list of recommender systems. You can also register individually and will then be automatically assigned to a team.

### Requirements
To succeed in the project, your RSSE should pass the following criteria:

* ✅ Use Java as the programming language.
* You have successfully re-implemented the assigned tool.
* You provide a set of learned models for the tool (or raw input data, depending on the approach).
* You use Git to coordinate team development.
* You have a significant test suite of unit tests that show the correctness of your code.
* Your tool is build with Maven.
* A build server is used for continuous integration.
* The result is deployed to a public Maven repository. The easiest way is to just publish your artifacts in a Github repository (you will find a deployment tutorial on StackOverflow).
* You provide an example client that uses the tool to answer a set of predefined queries
(i.e., that implements a predefined interface).
* ✅ Your code has a license (to make sure that you get the appropriate credit when others reuse your library)! We encourage you to use the Apache Public License v2, in accordance with the KaVE project, but you are free to pick any other compatible license.

### Process
To work on the project, you should follow some simple steps. Make sure that you agree in your team team, how you plan to work on these steps and make sure that all of you are on the same page.

#### Study the Available Data
* To make the development and the evaluation easy for you, we want you to reuse the framework provided in the KaVE Project. The Project offers two datasets: a dataset of repository data (contexts) and a dataset of recorded developer interactions that can be used for the validation.
* The KaVE project also features a couple of examples that illustrate how to use the datasets.
* In your own project, just add a Maven dependency to our KaVE libraries to get all you need to make the example code working. Import cc.kave.commons and cc.kave.rsse.calls. At the time of writing, the newest version is 0.0.3, but it will be updated frequently.
* Please also note the mailing list of the KaVE project, which is a good source for information.You can ask questions about the KaVE libraries or the data. Just bear in mind that this is a public mailing list, so please do not send questions that are specific to the course, e.g., grading!
* It is crucial that you understand what kind of information is contained in the datasets and how to access it. It might be a good practice for everybody to do some simple exercises with the data to make sure you “know your tools”, before you actually start with your project. You should be able to write simple analyses:
    * How many method invocations are typically called in a method body?
    * What fraction of classes of the dataset overrides the Equals method?
    * How often do developers abort a code completion invocation on string variables?
* You should understand the Visitor Pattern. We will include a refresher in the lecture, but you will need to understand it to traverse the syntax trees.

#### Create a cook-book
* Before you start implementing(!), read the paper carefully and lay out a precise plan of the required steps you need to do for your RSSE.
* Make a meeting with Sebastian Proksch to discuss your laid out plan. The meeting should take place as soon as possible, but at the latest ~2 weeks before the midterm presentation. You are responsible to get in contact and schedule a meeting.
* You might also want to try to contact the original authors of the paper when you have questions, just bear in mind that they are busy people, so don’t ask for too much and be polite!

#### Implement your Recommender
Once you have a clear idea of the required steps to replicate the recommender system, you can start implementing it. The core task of the implementation is to create a working recommender, but you will have to define a process in your group to work together as a team. Several of the requirements listed above affect this process and will affect the grading. Think about these requirements as best practices in team development. To fully benefit from them, you should integrate them early on in your process. If you would only include them in your workflow right before the deadline -just to fulfil the requirements-, they do not help you anymore and just add overhead!

#### Evaluation
Of course, you should also provide an evaluation of your recommender. You should try to replicate the original evaluation as presented in the paper (as long as it does not involve human subjects). If you cannot replicate the original evaluation, use the provided dataset to design your own evaluation. In this case, please get in contact with me, first.

#### Create a developer documentation
You component should include a meaningful developer documentation that illustrates how others can extend your code, should they decide to clone your repo, as well as how others could reuse your implementation as a dependency if they want to use your recommender. It is sufficient if your repository contains a README file that provides enough information to build and run your recommender, some technical details about how you have solved possible unclarities in the original publications (or how you deviated on purpose) is a plus. In addition, you should provide a (runnable) example class that illustrates how somebody else would use your recommender, i.e., preprocess the data, build the model, use completion events to query the model.
As a rule of thumb, it should be possible to point any other student of this course to your repository and they should be able to get your recommender to run, just given the information they find. In case your approach includes an offline model building, you should provide a set of models that other can use “out of the box”. It is not required to publish “everything”, just select a meaningful subset that enables others to play around with your recommender, e.g., include all models for the .Net core library.

#### Give two presentations
**Please note that this section is still work in progress! I will fill in the requirements for the presentations before the event, once I have a better impression about your progress and the pressing questions that many of you have.**

#### Report Efforts
It would be interesting for me to learn how long it took you to replicate the recommender. You don’t need to report on every minute you have worked, for me a “rough” estimate in hours would be super interesting for me to see how much time you spend with the following activities:
How much time did you spent...
* … learning how to load and use the KaVE dataset, e.g., to solve the mentioned basic exercises?
* … understanding the algorithm of the paper?
* … replicating the static analysis of the paper that extracts information from the source code?
* … reimplementing the mining algorithm?
* … replicating the original evaluation?

If other categories make sense in your project, feel free to break the individual categories down into subcategories that are more meaningful for you.

Please note that it is clear for me that you have spent way more time on the project than reported here, because the project also includes a lot of learning activities. You don’t need to report these additional time. Be also assured that I will not use your reported efforts in the grading. I would highly appreciate this feedback out of personal curiosity, but you do not have to share this information. Please just make sure that it is accurate, in case you are willing to share it.

If in doubt, just give me the data after the grading (or not at all) -- it will not affect your course result!