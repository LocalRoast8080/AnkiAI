# AnkiAi
## Overview

This project is for me to test sending Anki study cards to an AI model (currently Grok v2) to see
if it can:
* spell check & clean up grammar on cards
* expand on a cards answer (back) if the LLM deems it lacking

My initial goal is to set up basic services for Anki operation and communicating with an AI LLM. 

### AnkiService
Is responsible for interfacing with Anki via the Anki Connect plug-in [docs here](https://git.sr.ht/~foosoft/anki-connect). This  service 
should offer basic CRUD operation for [Notes](https://docs.ankiweb.net/getting-started.html#notes--fields) and methods to
get deck names.

### AiService

Responsible for communication with an AI model, currently [Grok](https://docs.x.ai/docs/api-reference#chat-completions). 
This is my first time working with a LLM via API. Currently, I believe this service only needs to send messages via the client.

Whatever methods this service offers should be generic in nature so the client can be changed to say OpenAi's API without issue.

Operation will be performed manually in the main method to test POC.

## Things to do later... or never

### MVP Goals
- [x] Spell check 10 note cards 2/28 done 3/5
- [x] Elaborate the back (answer) portion of 10 note cards 2/28 done 3/5
- [ ] Update 10 note cards in Anki after AI operations 2/28
- [ ] Perform the above on a 300 card deck 2/28

### Random issues or todos
- [ ] Can I add swagger to this?
- [ ] RestClient does not want to parse my objects on the post body -> .Body(Object) posts nothing
- [ ] add http code catches in the clients 2/28
- [ ] interfaces need to be built out for testing and mocking 2/28
- [x] swap all classes to use lombok 2/28 done 3/5
- [ ] look into OpenAi's API contract. see if it differs much from Grok and Claude. Is the current generic class... generic enough. 2/28
- [ ] add constants (should have added more context forgot what this was for :) )

## Lesson Learned:

RestClient is a new way to send http request. Seems to replacing RestTemplate, which I have not used.

I can use Lombok to create getters and setters. It also allows the use of `val` and `var` similar to c#. I still need to look into this since I do
note really understand it.

2/28 Lombok `@RequiredArgs` makes a constructor for fields marked final. where `@AllArgs` does what the name says

3/4 With jackson you can use @JsonIgnoreProperties(ignoreUnknown = true) to omit unwanted json fields being required in the class. .net mapping
did this automatically.

3/4 MapStruct is like AutoMapper. Less clean, messier but seems to function the same [Docs here](https://mapstruct.org/documentation/stable/reference/html/)

3/4 Sometimes after adding a maven dependency and building it still does not register with the IDE. With IntelliJ in the pom.xml you can sync it to see if that fixes.

3/4 MapStruct and Lombok need special set up [seen here](https://github.com/mapstruct/mapstruct-examples/blob/main/mapstruct-lombok/pom.xml) mapstruct note [here](https://mapstruct.org/faq/#Can-I-use-MapStruct-together-with-Project-Lombok)
I just copied and pasted what they had on GitHub. I spent too many hours trying to just get them to work. I should go back and read what it is actually doing.

3/5 To make large String text blocks / paragraphs align and not have an indent on the first line set Project Setting -> Code Style -> Java -> Wrapping and Braces -> Binary Expression -> Align When Multiline

3/5 spring have two controller types `@Controller` and `@RestController` the first needs to return a view while rest lets me return plain info
