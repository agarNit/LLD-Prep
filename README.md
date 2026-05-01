# Most asked interview LLD problems. Language Used: Java

## How to approach an LLD Problem 

## Prep must:
- SOLID principles
- Knowledge of various design patterns

Interviewer presents you with a vague problem statement with least details. (Example: Design a Library Management System)

Now its your responsibility to derive out what and how you have to implement the given problem. Scope everything out and present a working solution.

Generally, candidates get lost on where and how to start. What to do and what not to. Follow the below process for clear understanding of the requirements and implementation. I have listed out the steps to follow along with an example of Library Management System.

---

## Requirements:
Clarify what all things you need to implement in the system. Focus only on the below aspects and you are through to the thinking process.

### Asking Questions:

1. **Primary Capabilities**
   - How many copies of each book? Configurable or not?
   - Fee on borrowing book, flat or per day basis, late fee calculation? Per day then 50/- late fee per day
   - Add books to store
   - Search book in store (by name, author, isbn)
   - Borrow book
   - Return book

2. **Error handling**
   - Borrowing an already borrowed book
   - Returning a book not belonging to library

3. **Scope boundaries**
   - Number of books in library
   - Single Library handling or multiple

---

## Entities:
These are the placeholders which will make up your system (Classes, Enum, Objects)

Scan through the requirements for nouns and consider whether each of those make sense being a class, variable or object in our ultimate design.

1. LibraryService (There will always be a service class/Orchestrator from where the main functionalities will happen)
2. Main (For writing tests)
3. Book (class)
4. BookCopy (class)
5. BookStatus (Enum)

---

## Class Design:
In this part, you define the state(model variables) and behaviour (functions) that should be present in a class.

Create the relevant classes along with the state(variables: Represented by '-') and behaviour(functions: Represented by '+') required in that class:

1. class Book:
   - id
   - title
   - author
   - isbn
   - List<BookCopy> copies

   + searchBookByTitle()
   + searchBookByAuthor()
   + addBookCopy(BookCopy copy)

---

## Implementation:
Now, since we have already done with our requirements and class design. We move on to the implementation part. Do note that its absolutely not necessayr you cannot add any new functonality once you are done with Class Design. Generally, we do keep changing models or adding new methods as we keep implementing requirements. The core idea is to just getting started with the most basic requirements so you do not feel stuck.

The area where most people get confused here is which class should they design first. Just follow a bottom-up approach, i.e., design those classes first which do not have any dependency on any other class, then classes with least dependency and so on. Use below two criteria to write implementation of any function.

- Define the core logic
- Consider edge cases

---

## Extensibility:
This part comes in handy and is asked to check whether you have written an extensible code or not, i.e., if any new functionality/feature comes up, does your code require a heavy refactor or can easily be extended with minimal changes. That is what LLD is all about. If you are just out of college or early in your career, this part won't be a pain point and would be less asked in interviews.

Extend your design with additional requirements with minimal changes in the design.

1. Adding different types of Payment methods if till now only cash payment was allowed.

---

**Note:** I'll keep adding more insights which would help in designing and acing LLD interviews easily.