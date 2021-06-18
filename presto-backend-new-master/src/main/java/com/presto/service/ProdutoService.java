package com.presto.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

public interface ProdutoService {
    ResponseEntity<URI> salvarImagem(MultipartFile file);
}
