package com.edutech.comentarios.controllers;

import com.edutech.comentarios.dto.ComentarioDTO;
import com.edutech.comentarios.services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService service;

    @PostMapping
    public ResponseEntity<ComentarioDTO> crear(@RequestBody ComentarioDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ComentarioDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComentarioDTO> actualizar(@PathVariable Integer id, @RequestBody ComentarioDTO dto) {
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    
    // Endpoints personalizados para buscar comentarios tanto por id de curso como por id de persona

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentariosPorCurso(@PathVariable Integer cursoId) {
        List<ComentarioDTO> comentarios = service.buscarPorCurso(cursoId);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentariosPorUsuario(@PathVariable Integer usuarioId) {
        List<ComentarioDTO> comentarios = service.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(comentarios);
    }
}
