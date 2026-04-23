package com.rafael.rag_java.ingestion;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import dev.langchain4j.data.document.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class IngestionService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public IngestionService(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public void ingest(MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("rag-", file.getOriginalFilename());
            file.transferTo(tempFile);

            Document document = FileSystemDocumentLoader.loadDocument(tempFile);

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .documentSplitter(DocumentSplitters.recursive(500, 50))
                    .build();

            ingestor.ingest(document);

            Files.delete(tempFile);

            System.out.println("Documento ingerido: " + file.getOriginalFilename());

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo", e);
        }
    }
}
