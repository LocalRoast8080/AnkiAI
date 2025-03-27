# AnkiAi

## Overview

This project is for me to test sending Anki study cards to an AI model (currently Grok v2) to see
if it can:

* spell check & clean up grammar on cards
* expand on a cards answer (back) if the LLM deems it lacking

My initial goal is to set up basic services for Anki operation and communicating with an AI LLM. The end goal
is to be able to process my personal 300 card coding study deck.

## Dependencies / Tech

- Anki Desktop Windows Version - 23.10.1
- Anki plug-in ~ AnkiConnect
- Java 21
- Spring Boot Starter Web
- Lombok - 1.18.30 ~ Might be able to upgrade. Do not want to risk breaking MapStruct
- MapStruct - 1.6.0
- Lombok-MapStruct-Binding - 0.2.0
- IDE plug-in ~ MapStruct Support
- IDE plug-in ~ Lombok

---


## Current Status

### MVP:

| Task Description                                            | Date Added | Status | Date Completed | Notes                                                                                 |
|:------------------------------------------------------------|:----------:|:------:|:---------------|:--------------------------------------------------------------------------------------|
| Spell check 10 note cards 2/28 done                         |    2/28    |  Done  | 3/5            |                                                                                       |
| Expand the back (answer) portion of 10 note cards 2/28 done |    2/28    |  Done  | 3/5            |                                                                                       |
| Update 10 note cards in Anki after AI operations            |    2/28    |  Done  | 3/13           |                                                                                       |
| Perform the above on a 300 card deck 2/28                   |    2/28    |  Done  | 3/20           | Grok times out with too large a pay load and this also makes it start to hallucinate. |

### Stretch Goals

| Task Description                 | Date Added | Status  | Date Completed | Notes                                                |
|:---------------------------------|:----------:|:-------:|:---------------|:-----------------------------------------------------|
| AnkiNote Controller Service      |    3/7     |  Done   | 3/7            | Complete AnkiNote Controller/Service CRUD logic      |
| NoteProcessor Controller Service |    3/7     | Pending | 3/10           | Complete NoteProcessor Controller/Service CRUD logic |
| AnkiNote Unit Testing            |    3/7     | Pending |                | Testing Last  :grin:                                 |
| NoteProcessor Unit Testing       |    3/7     | Pending |                |                                                      |

### Issues and Todos:

| Task Description                                                       | Target Date | Status   | Notes                                                                                                                                                               |
|------------------------------------------------------------------------|-------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Explore Swagger for API documentation                                  | 3/5         | Done 3/7 | Docs [here](https://springdoc.org/#getting-started)                                                                                                                 |
| Debug RestClient object parsing issue                                  | 2/28        | Pending  | Fix body serialization (.Body(Object)). Would always populate null.                                                                                                 |
| Add HTTP code catches in clients                                       | 2/28        | Pending  | If there is an http issue the code will break                                                                                                                       |
| Build interfaces for testing and mocking                               | 2/28        | Pending  | For unit tests                                                                                                                                                      |
| Swap all classes to use Lombok                                         | 2/28        | Done 3/7 | Reduced boilerplate                                                                                                                                                 |
| Look into OpenAI’s API contract                                        | 2/28        | Pending  | Compare with Grok, Claude                                                                                                                                           |
| Add constants (context unclear, revisit)                               | 2/28        | Pending  | Clarify purpose                                                                                                                                                     |
| Had issues using nested classes with mapStruct, broke them out instead | 3/7         | Pending  | This should be possible. Didn't want to waste more time for now.                                                                                                    |
| Input models vs Service Models?                                        | 3/9         | Pending  | Should I make entity for my service and move models to controller. Seems like unnecessary work atm                                                                  |
| Add Validator                                                          | 3/9         | Pending  | Don't need this now but [here](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html#validation-beanvalidation-spring) are the docs |
| Endpoint to Suggest ten new cards                                      | 3/12        | Pending  | Endpoint scans deck and suggests 10 cards                                                                                                                           |
| Ask AI generic thing have it generate Cards                            | 3/20        | Done     | Can now upload txt strings and txt files                                                                                                                            |
| Tracing?                                                               | 3/12        | Pending  | Should look into. I have never implemented tracing.                                                                                                                 |
| AWS Secrets Manager                                                    | 3/13        | Pending  | https://www.youtube.com/watch?v=ePTNs3pqVvg                                                                                                                         |
|                                                                        |             |          |                                                                                                                                                                     |

---

## Configuration

### Setup

#### Anki - AnkiConnect

#### API Keys & Secrets

In your `application.properties` file add the following.

```
xapi.apikey="you-api-key"
xapi.url =https://api.x.ai/v1/chat/completions
```

---

## Decision Log

3/11 I am going to skip testing for now. I looked into how to set up interface Mocks and basic testing. Its
basically the same as .NET . While testing needs to be done I have not settled on many things as I am mainly poking
around with spring.

3/20 While the code still needed intensive testing. I will start to transition to working on a UI for the app. I will still
set up an exception handler but the base prototype functions as intended and better than expected.

## Lesson Learned:

RestClient is a new way to send http request. Seems to replacing RestTemplate, which I have not used.

I can use Lombok to create getters and setters. It also allows the use of `val` and `var` similar to c#. I still need to
look into this since I do
note really understand it.

2/28 Lombok `@RequiredArgs` makes a constructor for fields marked final. where `@AllArgs` does what the name says

3/4 With jackson you can use @JsonIgnoreProperties(ignoreUnknown = true) to omit unwanted json fields being required in
the class. .net mapping
did this automatically.

3/4 MapStruct is like AutoMapper. Less clean, messier but seems to function the
same [Docs here](https://mapstruct.org/documentation/stable/reference/html/)

3/4 Sometimes after adding a maven dependency and building it still does not register with the IDE. With IntelliJ in the
pom.xml you can sync it to see if that fixes.

3/4 MapStruct and Lombok need special set
up [seen here](https://github.com/mapstruct/mapstruct-examples/blob/main/mapstruct-lombok/pom.xml) mapstruct
note [here](https://mapstruct.org/faq/#Can-I-use-MapStruct-together-with-Project-Lombok)
I just copied and pasted what they had on GitHub. I spent too many hours trying to just get them to work. I should go
back and read what it is actually doing.

3/5 To make large String text blocks / paragraphs align and not have an indent on the first line set Project Setting ->
Code Style -> Java -> Wrapping and Braces -> Binary Expression -> Align When Multiline

3/5 spring have two controller types `@Controller` and `@RestController` the first needs to return a view while rest
lets me return plain info

3/7 spring boot already includes JUnit 5 in the `spring-boot-starter-test`, it also contains mockito already... Noice

3/19 Java has text blocks which can be used with """ Text here """

3/19 Look into prompting [here](https://platform.openai.com/docs/guides/prompt-engineering)

3/24 In a class with multiple constructors you can use other constructors with each other to cut duplicate code.

3/24 Java/ Spring now have Problem Detail which is what I was trying to make to wrap executions based off an old article [here](https://www.toptal.com/java/spring-boot-rest-api-error-handling)