# Misti - Advanced Pisti Game

## 1. Introduction

### 1.1 Purpose and Objectives of the Project

The **Misti** project is an advanced version of the traditional card game **Pisti**. The objective is to enhance gameplay with the following features:

- **New Card Points**: Custom point values for each card.
- **Difficulty Levels**: Various levels to adjust game difficulty.
- **Game Modes**: Different modes to offer varied gameplay experiences.

## 2. Design

### 2.1 Programming Language and Tools Used

This project is developed using **Java** and **Eclipse IDE**.

### 2.2 Data Structures, Algorithms, and Design Patterns

The design incorporates:

- **Object-Oriented Programming (OOP)**: Utilizing abstraction, encapsulation, and modularity.
- **Classes**: Java Collections Framework.
- **Inheritance**: Extending classes for specialized behavior.
- **Polymorphism**: Interfaces and abstract classes for flexible code design.
- **Error Handling**: Robust error handling to manage unexpected conditions.

## 3. Implementation

### 3.1 Detailed Explanation of Each Functional Requirement

- **Deck Creation**: `Cards` class for card representation. The `Deck` class generates a standard 52-card deck. Cards are assigned point values from a file, defaulting to `Integer.MAX_VALUE` if not specified.
- **Deck Shuffling and Cutting**: The `cutDeck` method splits and reorders the deck. The `shuffleDeck` method uses `Collections. shuffle` for randomization.
- **Command Line Input**: Handles game parameters, including player details and verbosity settings—Validates input with exceptions and prompts for corrections.
- **Card Dealing**: The `StartGame` method deals cards to players. The `LoopGame` method manages card dealing per round.
- **Verbose and Succinct Modes**: Methods `checkBoardStatus`, `checkPlayerStatus`, and `getBotHand` manage output based on verbosity settings.
- **Score Calculation**: The `scoreCalculator` method calculates and displays player scores.
- **High Score List**: Maintains a top 10 score list with player details in a file.
- **Bot Strategies**:
  - **Novice Bot**: Random card selection.
  - **Regular Bot**: Strategy based on card rank and point value.
  - **Expert Bot**: Advanced strategy considering played cards and potential moves.

## 4. Challenges and Problems Encountered

- **File Implementation**: Initially misinterpreted file requirements; resolved by creating the correct point file.
- **Hand Assignment**: Developed methods to assign hands and card lists to players correctly.
- **Unique Bot Names**: Implemented unique naming for bots based on difficulty.
- **Compare Methods**: Refined methods to compare player references accurately.
- **Card Selection Logic**: Improved bot card selection to avoid suboptimal plays.
- **Error Handling**: Enhanced error handling to prevent null pointer exceptions and invalid moves.
- **Expert Bot Strategy**: Enhanced strategy for optimal card plays and tie handling.
