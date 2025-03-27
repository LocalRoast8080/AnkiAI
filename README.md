# AnkiAI

[![Java Version](https://img.shields.io/badge/Java-21-blue)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

## 📚 Overview

AnkiAI is a powerful bridge between Anki flashcards and AI language models. This application allows you to:

- **Spell check & improve grammar** on existing Anki cards
- **Expand card content** with AI-generated explanations when answers are lacking
- **Generate new flashcards** from text input, automatically creating question/answer pairs
- **Manage Anki decks and cards** through a RESTful API

Initially developed to process a personal 300-card coding study deck, AnkiAI provides a comprehensive set of services for Anki integration and AI-powered enhancements.

## 🛠️ Technology Stack

### Core Technologies
- **Java 21** - Latest LTS release with modern language features
- **Spring Boot 3.4.3** - Application framework with embedded web server
- **Lombok** - Reduces boilerplate code through annotations
- **MapStruct** - Type-safe bean mapping between DTOs and domain objects
- **Swagger/OpenAPI** - API documentation via SpringDoc

### Anki Integration
- **Anki Desktop** - Version 23.10.1 (Windows)
- **AnkiConnect** - Required Anki plugin for API communication

### AI Processing
- **X.AI (Grok v2)** - Default AI model for text processing operations
- Support for configurable AI providers

## 🚀 Features

### Anki Card Management
- Retrieve deck names and card information
- Create new cards programmatically
- Update existing cards
- Search for cards with specific content

### AI-Powered Enhancements
- **Spell Check & Grammar Correction** - Improve the quality of your flashcards
- **Content Expansion** - Add detailed explanations to card answers
- **Automatic Card Generation** - Create new cards from text input
- **Bulk Processing** - Handle multiple cards in a single request

### File Support
- Upload text files to generate cards from structured content
- Process raw text input with special formatting for question/answer pairs

## 🏗️ Architecture

AnkiAI follows a clean, layered architecture:

- **Controllers** - REST endpoints for API access
- **Services** - Business logic implementation
- **Clients** - External API integrations (Anki, AI providers)
- **Models** - Data structures and DTOs
- **Mappers** - Type conversion between different object models
- **Exception Handling** - Centralized error management

## 🔧 Setup & Configuration

### Prerequisites
1. Java 21 or higher
2. Anki Desktop (v23.10.1 or compatible)
3. AnkiConnect plugin installed in Anki
4. Maven for dependency management

### Configuration
1. Clone the repository
2. Rename `.env.example` to `.env` and add your API keys
3. Configure application settings in `application.properties`:
   ```properties
   xapi.apikey=your-api-key
   xapi.url=https://api.x.ai/v1/chat/completions
   ```

### Building the Project
```bash
mvn clean install
```

### Running the Application
```bash
mvn spring-boot:run
```

## 🔌 API Documentation

Once the application is running, access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

### Key Endpoints

#### Anki Note Management
- `GET /api/v1/note/deckNames` - Retrieve all available decks
- `GET /api/v1/note/noteIds` - Get note IDs from a specific deck
- `POST /api/v1/note/noteCards` - Retrieve note cards by IDs
- `PATCH /api/v1/note/updateNoteCard` - Update an existing note card
- `POST /api/v1/note/createNoteCard` - Create new note cards
- `POST /api/v1/note/search` - Search for note cards

#### AI Processing
- `POST /api/v1/aiProcessor/spellCheck` - Spell check and correct cards
- `POST /api/v1/aiProcessor/expandAnswers` - Expand card answers with AI
- `POST /api/v1/aiProcessor/generateCards` - Generate cards from text input
- `POST /api/v1/aiProcessor/generateCards/upload` - Generate cards from uploaded file

## 📝 Usage Examples

### Spell Check Cards
```bash
curl -X POST http://localhost:8080/api/v1/aiProcessor/spellCheck \
  -H "Content-Type: application/json" \
  -d '[{"noteId": 1234567890, "front": "What is a monad?", "back": "A monad is a monoid in the category of endofunctors."}]'
```

### Generate Cards from Text
```bash
curl -X POST http://localhost:8080/api/v1/aiProcessor/generateCards \
  -H "Content-Type: application/json" \
  -d "? What is Spring Boot?\nSpring Boot is an opinionated framework that simplifies Spring application development.\n\n? What is dependency injection?\nDependency injection is a design pattern used to implement IoC."
```

## 🔍 Current Status

### Completed Tasks
- ✅ Spell check and grammar correction for note cards
- ✅ Answer expansion for clarification and detail
- ✅ Card update functionality with Anki integration
- ✅ Processing of large card decks (300+ cards)
- ✅ Swagger/OpenAPI documentation
- ✅ Lombok integration for reduced boilerplate
- ✅ AI-based generation of new cards from text input
- ✅ File upload functionality for generating cards

### Ongoing Development
- 🔄 Exploring integration with additional AI providers
- 🔄 Enhanced exception handling and input validation
- 🔄 Unit and integration testing
- 🔄 UI development for improved user experience

## 🛣️ Roadmap

- Implement AWS Secrets Manager for better security
- Add distributed tracing capabilities
- Create a UI for easier interaction with the API
- Add support for additional AI models (OpenAI, Claude)
- Enhance card generation with specialized algorithms for different subjects

## 📜 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📚 Acknowledgements

- Anki for creating an excellent flashcard application
- AnkiConnect developers for enabling API integration
- X.AI for their language model API 