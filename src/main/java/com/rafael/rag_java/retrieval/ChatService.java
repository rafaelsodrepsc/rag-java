package com.rafael.rag_java.retrieval;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final ChatLanguageModel chatLanguageModel;

    public ChatService(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, ChatLanguageModel chatLanguageModel) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.chatLanguageModel = chatLanguageModel;
    }

    public String retrieval(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Pergunta não pode estar vazia");
        }

        Embedding questionEmbedding = embeddingModel.embed(question).content();

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(questionEmbedding)
                .maxResults(5)
                .minScore(0.7)
                .build();

        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(searchRequest).matches();

        String context = matches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n\n"));

        String prompt = """
        Responda a pergunta baseado apenas no contexto abaixo.
        Se não encontrar a resposta no contexto, diga que não sabe.
        
        Contexto:
        %s
        
        Pergunta: %s
        """.formatted(context, question);

        return chatLanguageModel.generate(prompt);
    }
}
